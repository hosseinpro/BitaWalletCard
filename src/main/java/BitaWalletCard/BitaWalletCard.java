package BitaWalletCard;

import javacard.framework.*;
import javacard.security.*;
import javacardx.apdu.ExtendedLength;
import javacardx.crypto.Cipher;

public class BitaWalletCard extends Applet implements ISO7816, ExtendedLength {
    public static final byte[] AID = { (byte) 0xFF, (byte) 0xBC, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01 };

    private static final byte PIN_SIZE = 4;
    private static final byte PIN_TRY_LIMIT = 15;
    private static final byte SERIALNUMBER_SIZE = 8;
    private static final short SW_PIN_INCORRECT_TRIES_LEFT = (short) 0x63C0;
    private static final short LABEL_SIZE_MAX = 16;
    private static final short MSEED_SIZE = 64;

    public static final byte ALG_EC_SVDP_DH_PLAIN_XY = (byte) 6;// Not defined until JC 3.0.5

    private static final short CL_NONE = 0;
    private static final short CL_WIPE = 1;
    private static final short CL_SET_PIN = 2;
    private static final short CL_CHANGE_PIN = 3;
    private static final short CL_EXPORT_MSEED = 4;
    private static final short CL_IMPORT_MSEED = 5;
    private static final short CL_GET_XPUBS = 6;
    private static final short CL_SIGN_TX = 7;

    private static OwnerPIN pin;
    private static RandomData randomData;
    private boolean noPin = false;
    private OwnerPIN tempPin;

    private static byte[] serialNumber;
    private static final byte version[] = { 'B', ' ', '1', '.', '0' };
    private static final byte defaultLabel[] = { 'B', 'i', 't', 'a', 'W', 'a', 'l', 'l', 'e', 't' };
    private static byte[] label;
    private static short labelLength;
    private static final byte defaultPIN[] = { '1', '2', '3', '4' };
    private byte P2PKH[] = { 0x19, 0x76, (byte) 0xa9, 0x14, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0x88, (byte) 0xac };
    private static final byte zero[] = { 0x00 };

    private static byte[] mseed;
    private static boolean mseedInitialized;
    private ECPrivateKey signKey;
    private MessageDigest sha256;
    private Signature ecdsaSignature;
    private KeyAgreement ecdh;
    private AESKey transportKeySecret;
    private static KeyPair transportKey;
    private Cipher aesCBCCipher;

    private byte[] commandBuffer129;
    private short commandLock;
    private byte[] vcode1;

    private byte[] main500;
    private byte[] scratch515;

    private short responseRemainingLength;
    private short responseOffset;

    private Display display;
    private BIP bip;

    private short lc;
    private short offData;

    public static void install(byte[] bArray, short bOffset, byte bLength) {
        new BitaWalletCard().register();
    }

    public BitaWalletCard() {

        display = new Display();
        bip = new BIP();

        pin = new OwnerPIN(PIN_TRY_LIMIT, PIN_SIZE);
        pin.update(defaultPIN, (short) 0, PIN_SIZE);
        pin.resetAndUnblock();

        tempPin = new OwnerPIN(PIN_TRY_LIMIT, PIN_SIZE);

        label = new byte[LABEL_SIZE_MAX];
        labelLength = Util.arrayCopyNonAtomic(defaultLabel, (short) 0, label, (short) 0, (short) defaultLabel.length);

        randomData = RandomData.getInstance(RandomData.ALG_SECURE_RANDOM);

        serialNumber = new byte[SERIALNUMBER_SIZE];
        randomData.generateData(serialNumber, (short) 0, SERIALNUMBER_SIZE);

        mseed = new byte[64];
        mseedInitialized = false;
        signKey = (ECPrivateKey) KeyBuilder.buildKey(KeyBuilder.TYPE_EC_FP_PRIVATE, KeyBuilder.LENGTH_EC_FP_256, false);

        transportKey = new KeyPair(KeyPair.ALG_EC_FP, KeyBuilder.LENGTH_EC_FP_256);

        main500 = JCSystem.makeTransientByteArray((short) 500, JCSystem.CLEAR_ON_DESELECT);
        scratch515 = JCSystem.makeTransientByteArray((short) 500, JCSystem.CLEAR_ON_DESELECT);

        sha256 = MessageDigest.getInstance(MessageDigest.ALG_SHA_256, false);
        ecdsaSignature = Signature.getInstance(Signature.ALG_ECDSA_SHA_256, false);
        ecdh = KeyAgreement.getInstance(ALG_EC_SVDP_DH_PLAIN_XY, false);
        transportKeySecret = (AESKey) KeyBuilder.buildKey(KeyBuilder.TYPE_AES_TRANSIENT_DESELECT,
                KeyBuilder.LENGTH_AES_256, false);
        aesCBCCipher = Cipher.getInstance(Cipher.ALG_AES_BLOCK_128_CBC_NOPAD, false);
        commandBuffer129 = JCSystem.makeTransientByteArray((short) 129, JCSystem.CLEAR_ON_DESELECT);
        vcode1 = JCSystem.makeTransientByteArray(Display.VCODE_SIZE, JCSystem.CLEAR_ON_DESELECT);

        commandLock = CL_NONE;
    }

    public void process(APDU apdu) {
        if (selectingApplet()) {
            display.initialize();
            display.homeScreen(scratch515, (short) 0);
            return;
        }

        apdu.setIncomingAndReceive();
        byte[] buf = apdu.getBuffer();
        offData = apdu.getOffsetCdata();
        lc = apdu.getIncomingLength();
        byte cla = buf[OFFSET_CLA];
        byte ins = buf[OFFSET_INS];

        if (cla != (byte) 0x00) {
            ISOException.throwIt(SW_CLA_NOT_SUPPORTED);
        }

        switch (ins) {
        case (byte) 0x11:
            processGetInfo(apdu);
            break;
        case (byte) 0xE0:
            processWipe(apdu);
            break;
        case (byte) 0x20:
            processVerifyPIN(apdu);
            break;
        case (byte) 0x21:
            processSetPIN(apdu);
            break;
        case (byte) 0x24:
            processChangePIN(apdu);
            break;
        case (byte) 0xB1:
            processExportMasterSeed(apdu);
            break;
        case (byte) 0xB2:
            processImportMasterSeed(apdu);
            break;
        case (byte) 0x50:
            processGetXPubs(apdu);
            break;
        case (byte) 0x30:
            processSignTransaction(apdu);
            break;
        case (byte) 0xC0:
            processGetResponse(apdu);
            break;
        case (byte) 0xAA:
            test(apdu);
            break;
        default:
            ISOException.throwIt(SW_INS_NOT_SUPPORTED);
        }
    }

    private void test(APDU apdu) {
    }

    private void processGetInfo(APDU apdu) {
        commandLock = CL_NONE;
        display.homeScreen(scratch515, (short) 0);

        if (serialNumber == null) {
            ISOException.throwIt(SW_COMMAND_NOT_ALLOWED);
            return;
        }

        short offset = Util.arrayCopyNonAtomic(serialNumber, (short) 0, main500, (short) 0, SERIALNUMBER_SIZE);
        offset = Util.arrayCopyNonAtomic(version, (short) 0, main500, offset, (short) version.length);
        offset = Util.arrayCopyNonAtomic(label, (short) 0, main500, offset, labelLength);

        sendLongResponse(apdu, (short) 0, offset);
    }

    private void processVerifyPIN(APDU apdu) {
        if (commandLock == CL_NONE) {
            ISOException.throwIt(SW_COMMAND_NOT_ALLOWED);
        }

        if (lc != PIN_SIZE) {
            ISOException.throwIt(SW_WRONG_LENGTH);
        }

        byte[] buf = apdu.getBuffer();
        display.decodeKeypad(buf, OFFSET_CDATA, scratch515, (short) 0);

        if (commandLock == CL_WIPE) {
            if (tempPin.check(scratch515, (short) 0, PIN_SIZE) == false) {
                commandLock = CL_NONE;
                display.message(Display.MSG_FAILED, (short) 0, (short) Display.MSG_FAILED.length, scratch515,
                        (short) 0);
                ISOException.throwIt(SW_PIN_INCORRECT_TRIES_LEFT);
            } else {
                commitWipe(apdu);
                commandLock = CL_NONE;
            }
            return;
        }

        if (pin.check(scratch515, (short) 0, PIN_SIZE) == false) {
            commandLock = CL_NONE;
            display.message(Display.MSG_FAILED, (short) 0, (short) Display.MSG_FAILED.length, scratch515, (short) 0);
            ISOException.throwIt((short) (SW_PIN_INCORRECT_TRIES_LEFT | pin.getTriesRemaining()));
        }

        if (commandLock == CL_CHANGE_PIN)
            commitChangePin(apdu);
        else if (commandLock == CL_EXPORT_MSEED)
            commitExportMasterSeed(apdu);
        else if (commandLock == CL_IMPORT_MSEED)
            commitImportMasterSeed(apdu);
        else if (commandLock == CL_GET_XPUBS)
            commitGetXPubs(apdu);
        else if (commandLock == CL_SIGN_TX)
            commitSignTransaction(apdu);

        commandLock = CL_NONE;
    }

    private void processWipe(APDU apdu) {
        // [0]: 0=> main, 1=> backup, 2=> main with imported master seed
        // [1]: labelLen
        // [2..]: label
        // [labelLen..]: master seed (optional)
        byte[] buf = apdu.getBuffer();
        short labelLen = buf[(short) (offData + 1)];
        if (labelLen > LABEL_SIZE_MAX) {
            display.message(Display.MSG_FAILED, (short) 0, (short) Display.MSG_FAILED.length, scratch515, (short) 0);
            ISOException.throwIt(SW_WRONG_LENGTH);
        }

        if (buf[offData] == 2) {
            short mseedLen = (short) (lc - labelLen - 2);
            if (mseedLen != MSEED_SIZE) {
                ISOException.throwIt(SW_WRONG_LENGTH);
            }
        }

        Util.arrayCopyNonAtomic(buf, offData, commandBuffer129, (short) 0, buf[OFFSET_LC]);

        // set tempPin to 1234
        tempPin.update(defaultPIN, (short) 0, PIN_SIZE);
        tempPin.resetAndUnblock();

        display.messageWithKeypad(Display.MSG_WIPE, (short) 0, (short) Display.MSG_WIPE.length, scratch515, (short) 0);

        commandLock = CL_WIPE;
    }

    private void commitWipe(APDU apdu) {
        // unset mseed
        Util.arrayFillNonAtomic(mseed, (short) 0, MSEED_SIZE, (byte) 0);
        mseedInitialized = false;

        // set label
        labelLength = Util.arrayCopyNonAtomic(commandBuffer129, (short) 2, label, (short) 0, commandBuffer129[1]);

        // main wallet
        if (commandBuffer129[0] == 0) {
            do {
                // entropy => mseed
                randomData.generateData(mseed, (short) 0, MSEED_SIZE);
            } while (!bip.bip32GenerateMasterKey(mseed, (short) 0, MSEED_SIZE, main500, (short) 0));
            mseedInitialized = true;
        }
        // backup wallet
        else if (commandBuffer129[0] == 1) {
            // generate tk
            transportKey.getPrivate().clearKey();
            Secp256k1.setCommonCurveParameters(((ECPrivateKey) transportKey.getPrivate()));
            Secp256k1.setCommonCurveParameters(((ECPublicKey) transportKey.getPublic()));
            transportKey.genKeyPair();

            // calculate vcode1
            short publicKeyLength = ((ECPublicKey) transportKey.getPublic()).getW(main500, (short) 0);
            display.generateVCode(main500, (short) 0, publicKeyLength, vcode1, (short) 0, scratch515, (short) 0);

            sendLongResponse(apdu, (short) 0, publicKeyLength);
        }
        // main wallet with imported master seed
        else if (commandBuffer129[0] == 2) {
            // check master seed
            short masterSeedOffset = (short) (commandBuffer129[1] + 2);
            if (!bip.bip32GenerateMasterKey(commandBuffer129, masterSeedOffset, MSEED_SIZE, main500, MSEED_SIZE)) {
                ISOException.throwIt(SW_DATA_INVALID);
            }

            // set master seed
            Util.arrayCopyNonAtomic(commandBuffer129, masterSeedOffset, mseed, (short) 0, MSEED_SIZE);
            mseedInitialized = true;
        } else {
            display.message(Display.MSG_FAILED, (short) 0, (short) Display.MSG_FAILED.length, scratch515, (short) 0);
            ISOException.throwIt(SW_DATA_INVALID);
        }

        // unset PIN
        randomData.generateData(scratch515, (short) 0, PIN_SIZE);
        pin.update(scratch515, (short) 0, PIN_SIZE);
        pin.resetAndUnblock();
        noPin = true;

        // display random buttons
        display.messageWithKeypad(Display.MSG_NEW_PIN, (short) 0, (short) Display.MSG_NEW_PIN.length, scratch515,
                (short) 0);
    }

    private void processSetPIN(APDU apdu) {
        if (noPin == false) {
            display.message(Display.MSG_FAILED, (short) 0, (short) Display.MSG_FAILED.length, scratch515, (short) 0);
            ISOException.throwIt(SW_COMMAND_NOT_ALLOWED);
        }

        if (lc != PIN_SIZE) {
            display.message(Display.MSG_FAILED, (short) 0, (short) Display.MSG_FAILED.length, scratch515, (short) 0);
            ISOException.throwIt(SW_WRONG_LENGTH);
        }

        byte[] buf = apdu.getBuffer();
        display.decodeKeypad(buf, OFFSET_CDATA, scratch515, (short) 0);

        // first call, set PIN
        if (commandLock != CL_SET_PIN) {
            tempPin.update(scratch515, (short) 0, PIN_SIZE);
            tempPin.resetAndUnblock();

            display.messageWithKeypad(Display.MSG_CONFIRM_PIN, (short) 0, (short) Display.MSG_CONFIRM_PIN.length,
                    scratch515, (short) 0);
            commandLock = CL_SET_PIN;
        }
        // second call, confirm PIN
        else {
            commandLock = CL_NONE;
            if (tempPin.check(scratch515, (short) 0, PIN_SIZE) == false) {
                display.message(Display.MSG_FAILED, (short) 0, (short) Display.MSG_FAILED.length, scratch515,
                        (short) 0);
                ISOException.throwIt(SW_DATA_INVALID);
            } else {
                pin.update(scratch515, (short) 0, PIN_SIZE);
                pin.resetAndUnblock();
                noPin = false;
                display.message(Display.MSG_SUCCESSFUL, (short) 0, (short) Display.MSG_SUCCESSFUL.length, scratch515,
                        (short) 0);
                if (vcode1[0] != 0x00) {
                    short offset = Util.arrayCopyNonAtomic(Display.MSG_VCODE1, (short) 0, scratch515, (short) 0,
                            (short) Display.MSG_VCODE1.length);
                    offset = Util.arrayCopyNonAtomic(vcode1, (short) 0, scratch515, offset, Display.VCODE_SIZE);
                    display.message(scratch515, (short) 0, offset, scratch515, offset);
                    vcode1[0] = 0x00;
                } else {
                    display.homeScreen(scratch515, (short) 0);
                }
            }
        }
    }

    private void processChangePIN(APDU apdu) {
        display.messageWithKeypad(Display.MSG_CURRENT_PIN, (short) 0, (short) Display.MSG_CURRENT_PIN.length,
                scratch515, (short) 0);

        commandLock = CL_CHANGE_PIN;
    }

    private void commitChangePin(APDU apdu) {
        // open PIN
        noPin = true;

        // display random buttons
        display.messageWithKeypad(Display.MSG_NEW_PIN, (short) 0, (short) Display.MSG_NEW_PIN.length, scratch515,
                (short) 0);
    }

    private void processExportMasterSeed(APDU apdu) {
        if (mseedInitialized == false) {
            ISOException.throwIt(SW_COMMAND_NOT_ALLOWED);
        }

        if (lc != (short) 65) {
            ISOException.throwIt(SW_WRONG_LENGTH);
        }

        short publicKeyLength = lc;

        // [0..64]: transport key public
        byte[] buf = apdu.getBuffer();
        Util.arrayCopyNonAtomic(buf, OFFSET_CDATA, commandBuffer129, (short) 0, (publicKeyLength));

        // display random buttons
        short offset = Util.arrayCopyNonAtomic(Display.MSG_EXPORT, (short) 0, scratch515, (short) 0,
                (short) Display.MSG_EXPORT.length);
        offset += display.generateVCode(commandBuffer129, (short) 0, (short) 65, scratch515, offset, scratch515,
                (short) (offset + Display.VCODE_SIZE));
        display.messageWithKeypad(scratch515, (short) 0, offset, scratch515, offset);

        commandLock = CL_EXPORT_MSEED;
    }

    private short createExportPacket(byte[] data, short dataOffset, short dataLen, byte[] pack, short packOffset,
            byte[] scratch64, short scratchOffset) {

        // Generate main wallet transport key
        Secp256k1.setCommonCurveParameters(((ECPrivateKey) transportKey.getPrivate()));
        Secp256k1.setCommonCurveParameters(((ECPublicKey) transportKey.getPublic()));
        transportKey.genKeyPair();

        ecdh.init(transportKey.getPrivate());
        short resultLen = ecdh.generateSecret(commandBuffer129, (short) 0, (short) 65, scratch64, scratchOffset);
        sha256.reset();
        sha256.doFinal(scratch64, scratchOffset, resultLen, scratch64, (short) (scratchOffset + resultLen));
        transportKeySecret.setKey(scratch64, (short) (scratchOffset + resultLen));

        aesCBCCipher.init(transportKeySecret, Cipher.MODE_ENCRYPT);

        short offset = packOffset;
        offset += ((ECPublicKey) transportKey.getPublic()).getW(pack, offset);
        offset += aesCBCCipher.doFinal(data, dataOffset, dataLen, pack, offset);// offset=65
        transportKey.getPrivate().clearKey();
        transportKeySecret.clearKey();

        return offset;
    }

    private void commitExportMasterSeed(APDU apdu) {
        if (mseedInitialized == false) {
            ISOException.throwIt(SW_COMMAND_NOT_ALLOWED);
        }

        short packLen = createExportPacket(mseed, (short) 0, MSEED_SIZE, main500, (short) 0, scratch515, (short) 0);

        short offset = Util.arrayCopyNonAtomic(Display.MSG_VCODE2, (short) 0, scratch515, (short) 0,
                (short) Display.MSG_VCODE2.length);
        offset += display.generateVCode(main500, (short) 0, (short) 65, scratch515, offset, scratch515,
                (short) (offset + Display.VCODE_SIZE));
        display.message(scratch515, (short) 0, offset, scratch515, offset);

        sendLongResponse(apdu, (short) 0, packLen);
    }

    private void processImportMasterSeed(APDU apdu) {
        display.homeScreen(scratch515, (short) 0);

        if (mseedInitialized == true) {
            ISOException.throwIt(SW_COMMAND_NOT_ALLOWED);
        }

        if (transportKey.getPrivate().isInitialized() == false) {
            ISOException.throwIt(SW_COMMAND_NOT_ALLOWED);
        }

        if (lc != (short) 129) {
            ISOException.throwIt(SW_WRONG_LENGTH);
        }

        // [0..64] : transport key public
        // [65..129] : encrypted master key
        byte[] buf = apdu.getBuffer();
        Util.arrayCopyNonAtomic(buf, OFFSET_CDATA, commandBuffer129, (short) 0, lc);

        // display random buttons
        short offset = Util.arrayCopyNonAtomic(Display.MSG_IMPORT, (short) 0, scratch515, (short) 0,
                (short) Display.MSG_IMPORT.length);
        offset += display.generateVCode(commandBuffer129, (short) 0, (short) 65, scratch515, offset, scratch515,
                (short) (offset + Display.VCODE_SIZE));
        display.messageWithKeypad(scratch515, (short) 0, offset, scratch515, offset);

        commandLock = CL_IMPORT_MSEED;
    }

    private void commitImportMasterSeed(APDU apdu) {
        // calculate AES key
        ecdh.init(transportKey.getPrivate());
        short resultLen = ecdh.generateSecret(commandBuffer129, (short) 0, (short) 65, scratch515, (short) 0);
        sha256.reset();
        sha256.doFinal(scratch515, (short) 0, resultLen, scratch515, (short) (65 + resultLen));
        transportKeySecret.setKey(scratch515, (short) (65 + resultLen));

        // decrypt master seed
        aesCBCCipher.init(transportKeySecret, Cipher.MODE_DECRYPT);
        aesCBCCipher.doFinal(commandBuffer129, (short) (65), MSEED_SIZE, mseed, (short) 0);
        mseedInitialized = true;

        transportKey.getPrivate().clearKey();
        transportKeySecret.clearKey();

        display.message(Display.MSG_SUCCESSFUL, (short) 0, (short) Display.MSG_SUCCESSFUL.length, scratch515,
                (short) 0);
        display.homeScreen(scratch515, (short) 0);
    }

    private void processGetXPubs(APDU apdu) {
        if (mseedInitialized == false) {
            ISOException.throwIt(SW_COMMAND_NOT_ALLOWED);
        }

        if ((short) (lc % 5) != (short) 1) {
            ISOException.throwIt(SW_WRONG_LENGTH);
        }

        // [0]: count
        // [x5]: keyPath
        byte[] buf = apdu.getBuffer();
        Util.arrayCopyNonAtomic(buf, OFFSET_CDATA, commandBuffer129, (short) 0, lc);

        // display random buttons
        display.messageWithKeypad(Display.MSG_PIN, (short) 0, (short) Display.MSG_PIN.length, scratch515, (short) 0);

        commandLock = CL_GET_XPUBS;
    }

    private void commitGetXPubs(APDU apdu) {
        short offset = 0;
        short commandBufferOffset = 1;
        short count = commandBuffer129[0];
        for (short i = 0; i < count; i++) {
            offset += bip.bip44GetXPub(mseed, (short) 0, MSEED_SIZE, commandBuffer129, commandBufferOffset, main500,
                    offset, scratch515, (short) 0);
            commandBufferOffset += 5;
        }

        sendLongResponse(apdu, (short) 0, offset);

        display.homeScreen(scratch515, (short) 0);
    }

    // private short toBigEndian(byte[] leNumber, short leNumberOffset, byte[]
    // beNumber, short beNumberOffset,
    // short length) {
    // for (short i = 0; i < length; i++) {
    // beNumber[(short) (beNumberOffset + length - 1 - i)] = leNumber[(short)
    // (leNumberOffset + i)];
    // }
    // return (short) (beNumberOffset + length);
    // }

    private short toBigEndian8(byte[] leNumber, short leNumberOffset, byte[] beNumber, short beNumberOffset,
            short length) {
        for (short i = 0; i < length; i++) {
            beNumber[(short) (beNumberOffset + length - 1 - i)] = leNumber[(short) (leNumberOffset + i)];
        }
        for (short i = length; i < 8; i++) {
            beNumber[(short) (beNumberOffset + i)] = 0x00;
        }
        return (short) (beNumberOffset + 8);
    }

    private short signTransaction(byte[] inputSection, short inputSectionOffset, byte[] signerKeyPaths,
            short signerKeyPathsOffset, byte[] outputSection, short outputSectionOffset, short outputSectionLength,
            byte[] signedTx, short signedTxOffset, byte[] scratch322, short scratchOffset) {
        // version 01000000
        short signedTxIndex = signedTxOffset;
        short headerIndex = signedTxIndex;
        signedTx[signedTxIndex++] = 0x01;
        signedTx[signedTxIndex++] = 0x00;
        signedTx[signedTxIndex++] = 0x00;
        signedTx[signedTxIndex++] = 0x00;
        // inputs
        signedTx[signedTxIndex++] = inputSection[inputSectionOffset]; // input count

        // footer
        short footerIndex = (short) (signedTx.length - 8);
        signedTxIndex = footerIndex;
        // locktime 00000000
        signedTx[signedTxIndex++] = 0x00;
        signedTx[signedTxIndex++] = 0x00;
        signedTx[signedTxIndex++] = 0x00;
        signedTx[signedTxIndex++] = 0x00;
        // hashtype 01000000
        signedTx[signedTxIndex++] = 0x01;
        signedTx[signedTxIndex++] = 0x00;
        signedTx[signedTxIndex++] = 0x00;
        signedTx[signedTxIndex++] = 0x00;

        short offset = scratchOffset;

        signedTxIndex = (short) (headerIndex + 5);// begin of outputs
        short inputCount = inputSection[inputSectionOffset];
        inputSectionOffset++;
        for (short i = 0; i < inputCount; i++) {
            short inputSectionOffsetInputX = (short) (inputSectionOffset + (short) (i * 66));
            // 32B hash + 4B UTXO
            signedTxIndex = Util.arrayCopyNonAtomic(inputSection, inputSectionOffsetInputX, signedTx, signedTxIndex,
                    (short) 36);

            sha256.reset();
            sha256.update(signedTx, headerIndex, (short) 5);// header
            for (short j = 0; j < inputCount; j++) {// inputs
                if (i == j) {// complete
                    sha256.update(inputSection, (short) (inputSectionOffset + (short) (j * 66)), (short) 66);
                } else {// cut script
                    sha256.update(inputSection, (short) (inputSectionOffset + (short) (j * 66)), (short) 36);
                    sha256.update(zero, (short) 0, (short) 1);
                    sha256.update(inputSection, (short) ((short) (inputSectionOffset + (short) (j * 66)) + 62),
                            (short) 4);
                }
            }
            sha256.update(outputSection, outputSectionOffset, outputSectionLength);// outputs
            sha256.doFinal(signedTx, footerIndex, (short) 8, scratch322, offset);// footer
            // hold tx hash in scratch 0..31

            bip.bip44DerivePath(mseed, (short) 0, MSEED_SIZE, signerKeyPaths, signerKeyPathsOffset, scratch322,
                    (short) (offset + 32), (short) 1, scratch322, (short) (offset + 64), scratch322,
                    (short) (offset + 90));
            // hold prikey in scratch 32..63 [32]

            Secp256k1.setCommonCurveParameters(signKey);
            signKey.setS(scratch322, (short) (offset + 32), (short) 32);
            ecdsaSignature.init(signKey, Signature.MODE_SIGN);
            short scriptLenIndex = signedTxIndex;
            signedTxIndex++;// 1B script Len
            short signatureLen = 0;
            short sIndex = 0;
            do {
                signatureLen = ecdsaSignature.sign(scratch322, offset, (short) 32, scratch322, (short) (offset + 64));
                // 30 45 02 20 XXXX 02 20 XXXX
                sIndex = (short) (offset + 64 + 4 - 1);
                sIndex += scratch322[sIndex];
                sIndex += 3;
                if (scratch322[sIndex] == 0x00) {
                    sIndex++;
                }
                // if s > N/2
            } while (MathMod256.ucmp(scratch322, sIndex, Secp256k1.SECP256K1_Rdiv2, (short) 0,
                    (short) Secp256k1.SECP256K1_Rdiv2.length) > 0);

            Util.arrayCopyNonAtomic(scratch322, (short) (offset + 64), signedTx, (short) (signedTxIndex + 1),
                    signatureLen);

            signedTx[signedTxIndex] = (byte) (signatureLen + 1);// sig len + 1
            signedTxIndex += signatureLen + 1;
            signedTx[signedTxIndex++] = 0x01;// hash type
            short pubKeyLen = bip.ec256PrivateKeyToPublicKey(scratch322, (short) (offset + 32), signedTx,
                    (short) (signedTxIndex + 1), true);
            signedTx[signedTxIndex++] = (byte) pubKeyLen;
            signedTxIndex += pubKeyLen;
            // x = 1B [sig len byte] + sigLen + 1B [hash type] + 1B [pubkey Len] + pubKeyLen
            signedTx[scriptLenIndex] = (byte) (3 + signatureLen + pubKeyLen);
            // 63 = 1B len + 32B hash + 4B UTXO + 26B script
            signedTxIndex = Util.arrayCopyNonAtomic(inputSection, (short) (inputSectionOffsetInputX + 62), signedTx,
                    signedTxIndex, (short) 4);// sequence
        }

        if ((short) (signedTxIndex + outputSectionLength + 4) >= footerIndex) {
            ISOException.throwIt(SW_FILE_FULL);
        }

        signedTxIndex = Util.arrayCopyNonAtomic(outputSection, outputSectionOffset, signedTx, signedTxIndex,
                outputSectionLength);

        signedTxIndex = Util.arrayCopyNonAtomic(signedTx, footerIndex, signedTx, signedTxIndex, (short) 4);

        short signedTxLength = (short) (signedTxIndex - signedTxOffset);
        return signedTxLength;
    }

    private void processSignTransaction(APDU apdu) {
        if (mseedInitialized == false) {
            ISOException.throwIt(SW_COMMAND_NOT_ALLOWED);
        }

        byte[] buf = apdu.getBuffer();
        Util.arrayCopyNonAtomic(buf, OFFSET_CDATA, commandBuffer129, (short) 0, lc);

        // commandBuffer
        short spendIndex = 0; // 5B
        short feeIndex = 5; // 5B
        short destAddressIndex = 10; // 25B
        // short fundIndex = 35; // 5B
        // short changeKeyPathIndex = 40; // 7B
        // short inputSectionIndex = 47; // 1B + n*66B
        // short inputCount = commandBuffer129[inputSectionIndex];
        // short signerKeyPathsIndex = (short) (inputSectionIndex + 1 + (short)
        // (inputCount * 66)); // n*7B

        // display random buttons
        short length = display.generateSignMessage(commandBuffer129, spendIndex, (short) 5, commandBuffer129, feeIndex,
                (short) 5, commandBuffer129, destAddressIndex, (short) 25, scratch515, (short) 0, scratch515,
                (short) 100);// 100?
        display.messageWithKeypad(scratch515, (short) 0, length, scratch515, length);

        commandLock = CL_SIGN_TX;
    }

    private void commitSignTransaction(APDU apdu) {
        // commandBuffer
        short spendIndex = 0; // 5B
        short feeIndex = 5; // 5B
        short destAddressIndex = 10; // 25B
        short fundIndex = 35; // 5B
        short changeKeyPathIndex = 40; // 7B
        short inputSectionIndex = 47; // 1B + n*66B
        short inputCount = commandBuffer129[inputSectionIndex];
        short signerKeyPathsIndex = (short) (inputSectionIndex + 1 + (short) (inputCount * 66)); // n*7B

        // build output section
        short moffset = 0;
        main500[moffset++] = 0x02;
        // spend value (big endian)
        moffset = toBigEndian8(commandBuffer129, spendIndex, main500, moffset, (short) 5);
        // P2SH dest pub key hash
        Util.arrayCopyNonAtomic(commandBuffer129, (short) (destAddressIndex + 1), P2PKH, (short) 4, (short) 20);
        moffset = Util.arrayCopyNonAtomic(P2PKH, (short) 0, main500, moffset, (short) (P2PKH.length));
        // change value (big endian) : change = fund - spend - fee;
        MathMod256.sub(scratch515, (short) 0, commandBuffer129, fundIndex, commandBuffer129, spendIndex, (short) 5);
        MathMod256.sub(scratch515, (short) 0, scratch515, (short) 0, commandBuffer129, feeIndex, (short) 5);
        moffset = toBigEndian8(scratch515, (short) 0, main500, moffset, (short) 5);
        // P2SH change pub key hash
        bip.bip44DerivePath(mseed, (short) 0, MSEED_SIZE, commandBuffer129, changeKeyPathIndex, scratch515, (short) 0,
                (short) 1, scratch515, (short) 32, scratch515, (short) 60);
        // addressList: 1B len + 25B address
        Util.arrayCopyNonAtomic(scratch515, (short) (32 + 1 + 1), P2PKH, (short) 4, (short) 20);
        moffset = Util.arrayCopyNonAtomic(P2PKH, (short) 0, main500, moffset, (short) (P2PKH.length));

        Util.arrayCopyNonAtomic(main500, (short) 0, scratch515, (short) 0, moffset);// moffset=69

        // build final Tx
        short signedTxLength = signTransaction(commandBuffer129, inputSectionIndex, commandBuffer129,
                signerKeyPathsIndex, scratch515, (short) 0, moffset, main500, (short) 0, scratch515, moffset);

        display.message(Display.MSG_SUCCESSFUL, (short) 0, (short) Display.MSG_SUCCESSFUL.length, scratch515,
                (short) 0);
        display.homeScreen(scratch515, (short) 0);

        sendLongResponse(apdu, (short) 0, signedTxLength);
    }

    private void sendLongResponse(APDU apdu, short responseOffset, short responseLength) {
        if (responseLength > (short) 250) {
            this.responseOffset = responseOffset;
            this.responseRemainingLength = responseLength;
            ISOException.throwIt(SW_BYTES_REMAINING_00);
        } else {
            apdu.setOutgoing();
            apdu.setOutgoingLength(responseLength);
            apdu.sendBytesLong(main500, responseOffset, responseLength);
        }
    }

    private void processGetResponse(APDU apdu) {
        if (pin.isValidated() == false) {
            ISOException.throwIt(SW_SECURITY_STATUS_NOT_SATISFIED);
        }

        if (responseRemainingLength == (short) 0) {
            ISOException.throwIt(SW_COMMAND_NOT_ALLOWED);
        }

        short offset = responseOffset;
        short length = 0;
        if (responseRemainingLength >= (short) 250) {
            length = (short) 250;
        } else {
            length = responseRemainingLength;
        }

        responseOffset += length;
        responseRemainingLength -= length;

        short sw = 0;
        if (responseRemainingLength >= (short) 250) {
            sw = SW_BYTES_REMAINING_00;
        } else if (responseRemainingLength > (short) 0) {
            sw = (short) (SW_BYTES_REMAINING_00 | responseRemainingLength);
        } else {
            sw = SW_NO_ERROR;
        }

        apdu.setOutgoing();
        apdu.setOutgoingLength(length);
        apdu.sendBytesLong(main500, offset, length);
        ISOException.throwIt(sw);
    }
}
