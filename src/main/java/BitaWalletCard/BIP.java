package BitaWalletCard;

import javacard.framework.*;
import javacard.security.*;

public class BIP {

    private static final byte BITCOIN_SEED[] = { 'B', 'i', 't', 'c', 'o', 'i', 'n', ' ', 's', 'e', 'e', 'd' };
    private static final byte SUBWALLET_SEED[] = { 'S', 'u', 'b', 'w', 'a', 'l', 'l', 'e', 't', ' ', '0', '0', '0',
            '0' };

    public static final short BTC = 0;
    public static final short TST = 1;
    public static final short ETH = 60;

    public static final byte ALG_EC_SVDP_DH_PLAIN_XY = (byte) 6;// Not defined until JC 3.0.5

    private MessageDigest sha256;
    private Signature hmacSignature;
    private HMACKey hmacMasterKey;
    private HMACKey hmacDerivedKey;
    private HMACKey hmacSubWallet;
    private KeyAgreement ecMultiplyHelper;
    private ECPrivateKey ecPrivateKeyTemp;

    public BIP() {
        sha256 = MessageDigest.getInstance(MessageDigest.ALG_SHA_256, false);
        hmacSignature = Signature.getInstance(Signature.ALG_HMAC_SHA_512, false);
        hmacMasterKey = (HMACKey) KeyBuilder.buildKey(KeyBuilder.TYPE_HMAC_TRANSIENT_DESELECT,
                KeyBuilder.LENGTH_HMAC_SHA_512_BLOCK_128, false);
        hmacDerivedKey = (HMACKey) KeyBuilder.buildKey(KeyBuilder.TYPE_HMAC_TRANSIENT_DESELECT,
                KeyBuilder.LENGTH_HMAC_SHA_512_BLOCK_128, false);
        hmacSubWallet = (HMACKey) KeyBuilder.buildKey(KeyBuilder.TYPE_HMAC_TRANSIENT_DESELECT,
                KeyBuilder.LENGTH_HMAC_SHA_512_BLOCK_128, false);
        ecMultiplyHelper = KeyAgreement.getInstance(ALG_EC_SVDP_DH_PLAIN_XY, false);
        ecPrivateKeyTemp = (ECPrivateKey) KeyBuilder.buildKey(KeyBuilder.TYPE_EC_FP_PRIVATE,
                KeyBuilder.LENGTH_EC_FP_256, false);
        Secp256k1.setCommonCurveParameters(ecPrivateKeyTemp);
    }

    public short publicKeyToAddress(short coin, byte[] publicKey, short publicKeyOffset, short publicKeyLength,
            byte[] address, short addressOffset, boolean base58, byte[] scratch92, short scratchOffset) {

        // resultLen <= 42 becuase of scratch92
        short resultLen = 0;
        switch (coin) {
        case BTC:
        case TST:
            // https://en.bitcoin.it/w/images/en/9/9b/PubKeyToAddr.png
            sha256.reset();
            sha256.doFinal(publicKey, publicKeyOffset, publicKeyLength, scratch92, (short) (scratchOffset + 25));
            Ripemd160.hash32(scratch92, (short) (scratchOffset + 25), scratch92, (short) (scratchOffset + 1), scratch92,
                    (short) (scratchOffset + 60));

            if (coin == BTC) {
                scratch92[scratchOffset] = (byte) 0x00;// MainNet
            } else {
                scratch92[scratchOffset] = (byte) 0x6f;// TestNet
            }

            sha256.reset();
            sha256.doFinal(scratch92, scratchOffset, (short) 21, scratch92, (short) (scratchOffset + 25));
            sha256.reset();
            sha256.doFinal(scratch92, (short) (scratchOffset + 25), (short) 32, scratch92,
                    (short) (scratchOffset + 60));

            Util.arrayCopyNonAtomic(scratch92, (short) (scratchOffset + 60), scratch92, (short) (scratchOffset + 21),
                    (short) 4);
            resultLen = (short) 25;
            break;
        default:
            return 0;
        }
        if (!base58) {// Hex
            Util.arrayCopyNonAtomic(scratch92, scratchOffset, address, addressOffset, resultLen);
        } else {// b58
            resultLen = Base58.encode(scratch92, scratchOffset, resultLen, address, addressOffset, scratch92,
                    (short) (scratchOffset + resultLen));
        }
        return resultLen;
    }

    private boolean checkAddress(short coin, byte[] addressHex, short addressOffset, short addressLength,
            byte[] scratch64, short scratchOffset) {
        switch (coin) {
        case BTC:
        case TST:
            if ((addressLength != 25) || (addressHex[addressOffset] != 0x00)) {
                return false;
            }

            sha256.reset();
            sha256.doFinal(addressHex, addressOffset, (short) 21, scratch64, scratchOffset);
            sha256.reset();
            sha256.doFinal(scratch64, scratchOffset, (short) 32, scratch64, (short) (scratchOffset + 32));

            if (Util.arrayCompare(addressHex, addressOffset, scratch64, (short) (scratchOffset + 32),
                    (short) 32) != 0) {
                return false;
            }
            return true;
        default:
            return false;
        }
    }

    public boolean bip32GenerateMasterKey(byte[] seed, short seedOffset, short seedLength, byte[] kcPar,
            short kcParOffset) {
        // HMAC-SHA512(key="Bitcoin seed", data=mseed) => iL|iR
        hmacMasterKey.setKey(BITCOIN_SEED, (short) 0, (short) BITCOIN_SEED.length);
        hmacSignature.init(hmacMasterKey, Signature.MODE_SIGN);
        hmacSignature.sign(seed, seedOffset, seedLength, kcPar, kcParOffset);
        // if iL=0 or >=n => invalid
        if ((kcPar[kcParOffset] == (byte) 0)
                || MathMod256.ucmp(kcPar, kcParOffset, Secp256k1.SECP256K1_R, (short) 0, (short) 32) >= (short) 0) {
            return false;
        }
        return true;
    }

    public short ec256PrivateKeyToPublicKey(byte[] privateKey, short privateKeyOffset, byte[] publicKey65,
            short publicKeyOffset, boolean compressed) {
        // Generate Public Key from given Private Key using the Key Agreement API
        ecPrivateKeyTemp.setS(privateKey, privateKeyOffset, (short) 32);
        ecMultiplyHelper.init(ecPrivateKeyTemp);
        short publicKeyLength = ecMultiplyHelper.generateSecret(Secp256k1.SECP256K1_G, (short) 0, (short) 65,
                publicKey65, publicKeyOffset);
        if (compressed) {
            if ((publicKey65[(short) (publicKeyOffset + publicKeyLength - 1)] & 0x01) == 0x01) {// Y last digit is odd
                publicKey65[publicKeyOffset] = 0x03;
            } else {// Y last digit is even
                publicKey65[publicKeyOffset] = 0x02;
            }
            publicKeyLength = 33;
        }
        return publicKeyLength;
    }

    public boolean bip32DerivePrivateKey(byte[] kcPar64, short kcParOffset, short i, boolean hardenedKey,
            byte[] kcChi64, short kcChiOffset, byte[] scratch104, short scratchOffset) {
        // CKDpriv
        if (hardenedKey) {
            // 00|kPar|i : [37B]
            scratch104[scratchOffset] = (byte) 0x00;
            Util.arrayCopyNonAtomic(kcPar64, kcParOffset, scratch104, (short) (scratchOffset + 1), (short) 32);
            // Util.arrayCopyNonAtomic(i, iOffset, scratchBuffer, (short) 33, (short) 4);
            scratch104[(short) (scratchOffset + 33)] = (byte) 0x80;// i = i + 2^31
            scratch104[(short) (scratchOffset + 34)] = (byte) 0x00;
            Util.setShort(scratch104, (short) (scratchOffset + 35), i);
        } else {// normal key
            // pointX(kPar)|i : [37B]
            ec256PrivateKeyToPublicKey(kcPar64, kcParOffset, scratch104, scratchOffset, true);
            // Util.arrayCopyNonAtomic(i, iOffset, scratchBuffer, (short) 65, (short) 4);
            scratch104[(short) (scratchOffset + 33)] = (byte) 0x00;
            scratch104[(short) (scratchOffset + 34)] = (byte) 0x00;
            Util.setShort(scratch104, (short) (scratchOffset + 35), i);
        }

        // HMAC-SHA512(key=cPar, data=input) => iL|iR
        short iL_index = (short) (scratchOffset + 40);
        hmacDerivedKey.setKey(kcPar64, (short) (kcParOffset + 32), (short) 32);
        hmacSignature.init(hmacDerivedKey, Signature.MODE_SIGN);
        hmacSignature.sign(scratch104, (short) (scratchOffset + 0), (short) 37, scratch104, iL_index);

        // if iL>=n => invalid
        if (MathMod256.ucmp(scratch104, iL_index, Secp256k1.SECP256K1_R, (short) 0, (short) 32) >= (short) 0) {
            return false;
        }

        // ki = iL + kPar mod n
        MathMod256.addm(scratch104, iL_index, scratch104, iL_index, kcPar64, kcParOffset, Secp256k1.SECP256K1_R,
                (short) 0);

        // if ki=0 => invalid
        if (scratch104[iL_index] == 0x00) {
            return false;
        }

        Util.arrayCopyNonAtomic(scratch104, iL_index, kcChi64, kcChiOffset, (short) 64);
        return true;
    }

    public short bip44DerivePath(byte[] masterSeed, short masterSeedOffset, short masterSeedLength, byte[] keyPath,
            short keyPathOffset, byte[] privateKey32, short privateKeyOffset, short addressCount, byte[] addressList,
            short addressOffset, byte[] scratch232, short scratchOffset) {

        // m[1]/44'[1]/coin'[1]/account'[1]/change[1]/address_index[2]
        short coin = keyPath[(short) (keyPathOffset + 2)];

        // m
        if ((keyPath[(short) (keyPathOffset + 0)] != 'm')
                || !bip32GenerateMasterKey(masterSeed, masterSeedOffset, masterSeedLength, scratch232, scratchOffset)) {
            return 0;
        }
        // purpose'
        if ((keyPath[(short) (keyPathOffset + 1)] != 44)
                || !bip32DerivePrivateKey(scratch232, scratchOffset, keyPath[(short) (keyPathOffset + 1)], true,
                        scratch232, scratchOffset, scratch232, (short) (scratchOffset + 64))) {
            return 0;
        }
        // coin'
        if (!bip32DerivePrivateKey(scratch232, scratchOffset, keyPath[(short) (keyPathOffset + 2)], true, scratch232,
                scratchOffset, scratch232, (short) (scratchOffset + 64))) {
            return 0;
        }
        // account'
        if ((keyPath[(short) (keyPathOffset + 3)] > 255)
                || !bip32DerivePrivateKey(scratch232, scratchOffset, keyPath[(short) (keyPathOffset + 3)], true,
                        scratch232, scratchOffset, scratch232, (short) (scratchOffset + 64))) {
            return 0;
        }
        // change
        if ((keyPath[(short) (keyPathOffset + 4)] > 1)
                || !bip32DerivePrivateKey(scratch232, scratchOffset, keyPath[(short) (keyPathOffset + 4)], false,
                        scratch232, scratchOffset, scratch232, (short) (scratchOffset + 64))) {
            return 0;
        }
        // address_index
        short address_index = Util.makeShort(keyPath[(short) (keyPathOffset + 5)],
                keyPath[(short) (keyPathOffset + 6)]);
        if ((addressCount < 1) || (addressCount > 99) || ((short) (address_index + addressCount) > (short) (32000))) {
            return 0;
        }
        short publicKeyLen = 0;
        short addressLen = 0;
        short resultLen = 1;
        if ((coin == BTC) || coin == TST) {
            addressList[addressOffset] = 25;
        } else {
            return 0;
        }
        for (short i = 0; i < addressCount; i++) {
            if (!bip32DerivePrivateKey(scratch232, scratchOffset, (short) (address_index + i), false, scratch232,
                    (short) (scratchOffset + 64), scratch232, (short) (scratchOffset + 128))) {
                return 0;
            }
            Util.arrayCopyNonAtomic(scratch232, (short) (scratchOffset + 64), privateKey32, privateKeyOffset,
                    (short) 32);
            publicKeyLen = ec256PrivateKeyToPublicKey(privateKey32, privateKeyOffset, scratch232,
                    (short) (scratchOffset + 64), true);
            addressLen = publicKeyToAddress(coin, scratch232, (short) (scratchOffset + 64), publicKeyLen, addressList,
                    (short) (addressOffset + resultLen), false, scratch232, (short) (scratchOffset + 100));

            resultLen += addressLen;
        }
        return resultLen;
    }

    public short bip44GetXPub(byte[] masterSeed, short masterSeedOffset, short masterSeedLength, byte[] keyPath,
            short keyPathOffset, byte[] xpub, short xpubOffset, byte[] scratch232, short scratchOffset) {

        // m[1]/44'[1]/coin'[1]/account'[1]/change[1]
        short coin = keyPath[(short) (keyPathOffset + 2)];

        // m
        if ((keyPath[(short) (keyPathOffset + 0)] != 'm')
                || !bip32GenerateMasterKey(masterSeed, masterSeedOffset, masterSeedLength, scratch232, scratchOffset)) {
            return 0;
        }
        // purpose'
        if ((keyPath[(short) (keyPathOffset + 1)] != 44)
                || !bip32DerivePrivateKey(scratch232, scratchOffset, keyPath[(short) (keyPathOffset + 1)], true,
                        scratch232, scratchOffset, scratch232, (short) (scratchOffset + 64))) {
            return 0;
        }
        // coin'
        if (!bip32DerivePrivateKey(scratch232, scratchOffset, keyPath[(short) (keyPathOffset + 2)], true, scratch232,
                scratchOffset, scratch232, (short) (scratchOffset + 64))) {
            return 0;
        }
        // account'
        if ((keyPath[(short) (keyPathOffset + 3)] > 255)
                || !bip32DerivePrivateKey(scratch232, scratchOffset, keyPath[(short) (keyPathOffset + 3)], true,
                        scratch232, scratchOffset, scratch232, (short) (scratchOffset + 64))) {
            return 0;
        }
        // change
        if ((keyPath[(short) (keyPathOffset + 4)] > 1)
                || !bip32DerivePrivateKey(scratch232, scratchOffset, keyPath[(short) (keyPathOffset + 4)], false,
                        scratch232, scratchOffset, scratch232, (short) (scratchOffset + 64))) {
            return 0;
        }

        // xPri to xPub : N((k, c)) -> (K, c)
        short offset = xpubOffset;
        offset += ec256PrivateKeyToPublicKey(scratch232, scratchOffset, xpub, xpubOffset, false);

        offset = Util.arrayCopyNonAtomic(scratch232, (short) (scratchOffset + 32), xpub, offset, (short) 32);

        return (short) (offset - xpubOffset);
    }

    private void decToHexString4(short decimal, byte[] hexString, short hexStringOffset) {
        short t = decimal;
        for (short i = 0; i < 4; i++) {
            hexString[(short) (hexStringOffset + i)] = (byte) ((t % 10) + 0x30);
            t = (short) (t / 10);
        }
    }

    public short generateSubwalletSeed(byte[] masterSeed, short masterSeedOffset, short masterSeedLength,
            short subwalletNumber, byte[] subwalletSeed, short subwalletSeedOffset, byte[] scratch14,
            short scratchOffset) {
        // HMAC-SHA512(key="Subwallet XXXX", data=mseed) => subwalletSeed
        Util.arrayCopyNonAtomic(SUBWALLET_SEED, (short) 0, scratch14, scratchOffset, (short) SUBWALLET_SEED.length);
        decToHexString4(subwalletNumber, scratch14, (short) (scratchOffset + 10));

        hmacSubWallet.setKey(scratch14, scratchOffset, (short) SUBWALLET_SEED.length);
        hmacSignature.init(hmacSubWallet, Signature.MODE_SIGN);
        short subwalletSeedLength = hmacSignature.sign(masterSeed, masterSeedOffset, masterSeedLength, subwalletSeed,
                subwalletSeedOffset);
        return subwalletSeedLength;
    }
}