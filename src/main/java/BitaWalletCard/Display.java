package BitaWalletCard;

import javacard.framework.*;
import javacard.security.*;

import com.es.specialmethod.ESUtil;

public class Display {

	private boolean isHomeScreen;

	private static final byte NEWLINE = (byte) 0x0A;
	private static final byte SPACE = (byte) ' ';
	private static final short HEADER_SIZE = 8;

	public static final byte MSG_HOME[] = { ' ', ' ', ' ', 'X', 'e', 'b', 'a', 'W', 'a', 'l', 'l', 'e', 't' };
	public static final byte MSG_SUCCESSFUL[] = { ' ', ' ', ' ', 'S', 'u', 'c', 'c', 'e', 's', 's', 'f', 'u', 'l' };
	public static final byte MSG_FAILED[] = { ' ', ' ', ' ', ' ', ' ', 'F', 'a', 'i', 'l', 'e', 'd' };
	public static final byte MSG_WIPE[] = { 'W', 'i', 'p', 'e', '?', ' ', 'E', 'n', 't', 'e', 'r', ' ', '1', '2', '3',
			'4' };
	public static final byte MSG_CURRENT_PIN[] = { 'E', 'n', 't', 'e', 'r', ' ', 'C', 'u', 'r', 'r', 'u', 'n', 't', ' ',
			'P', 'I', 'N' };
	public static final byte MSG_NEW_PIN[] = { 'E', 'n', 't', 'e', 'r', ' ', 'N', 'e', 'w', ' ', 'P', 'I', 'N' };
	public static final byte MSG_CONFIRM_PIN[] = { 'C', 'o', 'n', 'f', 'i', 'r', 'm', ' ', 'N', 'e', 'w', ' ', 'P', 'I',
			'N' };
	public static final byte MSG_PIN[] = { 'E', 'n', 't', 'e', 'r', ' ', 'P', 'I', 'N' };
	public static final byte MSG_IMPORT[] = { 'I', 'm', 'p', 'o', 'r', 't', ' ', 'v', 'c', 'o', 'd', 'e', '2', ':',
			' ' };
	public static final byte MSG_EXPORT[] = { 'E', 'x', 'p', 'o', 'r', 't', ' ', 'v', 'c', 'o', 'd', 'e', '1', ':',
			' ' };
	public static final byte MSG_VCODE1[] = { 'v', 'c', 'o', 'd', 'e', '1', ':' };
	public static final byte MSG_VCODE2[] = { 'v', 'c', 'o', 'd', 'e', '2', ':' };
	private static final byte MSG_TO[] = { 'T', 'o', ':' };
	private static final byte MSG_BTC[] = { 'B', 'T', 'C' };
	private static final byte MSG_FEE[] = { 'f', 'e', 'e' };
	private static final byte MSG_MBTC[] = { 'm', 'B', 'T', 'C' };

	private static ESUtil esUtil = null;

	private static final byte PIN_SIZE = 4;
	public static final byte VCODE_SIZE = 4;
	private static RandomData randomData;
	public byte[] keypad;

	private MessageDigest sha1;

	public Display() {
		esUtil = new ESUtil();
		randomData = RandomData.getInstance(RandomData.ALG_SECURE_RANDOM);
		keypad = JCSystem.makeTransientByteArray((short) 10, JCSystem.CLEAR_ON_DESELECT);
		sha1 = MessageDigest.getInstance(MessageDigest.ALG_SHA, false);
	}

	public void initialize() {
		isHomeScreen = false;
	}

	public boolean homeScreen(byte[] scratch, short scratchOffset) {
		if (isHomeScreen) {
			return true;
		}
		isHomeScreen = true;

		short offset = (short) (scratchOffset + HEADER_SIZE);

		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;

		offset = Util.arrayCopyNonAtomic(MSG_HOME, (short) 0, scratch, offset, (short) MSG_HOME.length);

		return displayText(scratch, scratchOffset, (short) (offset - scratchOffset));
	}

	public boolean message(byte[] message, short messageOffset, short messageLength, byte[] scratch,
			short scratchOffset) {
		isHomeScreen = false;
		short offset = (short) (scratchOffset + HEADER_SIZE);
		offset = Util.arrayCopyNonAtomic(message, messageOffset, scratch, offset, messageLength);
		return displayText(scratch, scratchOffset, (short) (offset - scratchOffset));
	}

	public boolean messageWithKeypad(byte[] message, short messageOffset, short messageLength, byte[] scratch,
			short scratchOffset) {
		isHomeScreen = false;
		short offset = (short) (scratchOffset + HEADER_SIZE);
		offset = Util.arrayCopyNonAtomic(message, messageOffset, scratch, offset, messageLength);
		scratch[offset++] = NEWLINE;
		generateKeypad();
		offset = fillKeypad(keypad, (short) 0, (short) keypad.length, scratch, offset);
		return displayText(scratch, scratchOffset, (short) (offset - scratchOffset));
	}

	public short generateSignMessage(byte[] amount, short amountOffset, short amountLength, byte[] fee, short feeOffset,
			short feeLength, byte[] destAddress, short destAddressOffset, short destAddressLength, byte[] output,
			short outputOffset, byte[] scratch40, short scratchOffset) {

		short decLength = toDecimalString(amount, amountOffset, amountLength, scratch40, scratchOffset, scratch40,
				(short) (scratchOffset + 20));
		short offset = satoshi2BTC(scratch40, scratchOffset, decLength, output, outputOffset);
		output[offset++] = SPACE;
		offset = Util.arrayCopyNonAtomic(MSG_BTC, (short) 0, output, offset, (short) MSG_BTC.length);
		output[offset++] = NEWLINE;

		offset = Util.arrayCopyNonAtomic(MSG_TO, (short) 0, output, offset, (short) MSG_TO.length);

		offset += Base58.encode(destAddress, destAddressOffset, destAddressLength, output, offset, scratch40,
				scratchOffset);
		output[offset++] = NEWLINE;

		// toDecimalString(fee, feeOffset, feeLength, scratch, (short) (offset + 6));//
		// temp use of scratch
		// offset += satoshi2mBTC(scratch, (short) (offset + 6), scratch, offset);
		// scratch[offset++] = SPACE;
		// offset = Util.arrayCopyNonAtomic(MSG_MBTC, (short) 0, scratch, offset,
		// (short) MSG_MBTC.length);
		// scratch[offset++] = SPACE;
		// offset = Util.arrayCopyNonAtomic(MSG_FEE, (short) 0, scratch, offset, (short)
		// MSG_FEE.length);
		// scratch[offset++] = NEWLINE;

		return offset;
	}

	private void generateKeypad() {

		for (short i = 0; i < (short) 10; i++) {
			keypad[i] = (byte) (i + (short) 0x30);
		}

		byte[] temp = new byte[1];

		for (short i = 0; i < (short) 7; i++) {
			do {
				randomData.generateData(temp, (short) 0, (short) 1);
			} while (temp[(short) 0] < 0);
			short index1 = (short) (temp[(short) 0] % (short) 10);

			do {
				randomData.generateData(temp, (short) 0, (short) 1);
			} while (temp[(short) 0] < 0);
			short index2 = (short) (temp[(short) 0] % (short) 10);

			byte soap = keypad[index1];
			keypad[index1] = keypad[index2];
			keypad[index2] = soap;
		}
	}

	public void decodeKeypad(byte[] enteredButtons, short enteredButtonsOffset, byte[] decodedPin,
			short decodedPinOffset) {

		for (short i = 0; i < PIN_SIZE; i++) {
			byte index = (byte) (enteredButtons[(short) (enteredButtonsOffset + i)] - (short) 0x30);
			decodedPin[(short) (decodedPinOffset + i)] = keypad[index];
		}
	}

	private static final byte MSG_KEYPAD[] = { ' ', ' ', ' ', '(', '1', ')', ' ', '(', '2', ')', ' ', '(', '3', ')',
			(byte) 0x0A, ' ', ' ', ' ', '(', '4', ')', ' ', '(', '5', ')', ' ', '(', '6', ')', (byte) 0x0A, ' ', ' ',
			' ', '(', '7', ')', ' ', '(', '8', ')', ' ', '(', '9', ')', (byte) 0x0A, ' ', ' ', ' ', ' ', ' ', ' ', ' ',
			'(', '0', ')', ' ', ' ', ' ', ' ' };// len=59

	private short fillKeypad(byte[] keypad, short keypadOffset, short keypadLength, byte[] scratch59,
			short scratchOffset) {
		short offset = scratchOffset;
		short otpBegin = offset;
		offset = Util.arrayCopyNonAtomic(MSG_KEYPAD, (short) 0, scratch59, offset, (short) MSG_KEYPAD.length);
		scratch59[(short) (otpBegin + 4)] = keypad[(short) (keypadOffset + 1)];
		scratch59[(short) (otpBegin + 8)] = keypad[(short) (keypadOffset + 2)];
		scratch59[(short) (otpBegin + 12)] = keypad[(short) (keypadOffset + 3)];
		scratch59[(short) (otpBegin + 19)] = keypad[(short) (keypadOffset + 4)];
		scratch59[(short) (otpBegin + 23)] = keypad[(short) (keypadOffset + 5)];
		scratch59[(short) (otpBegin + 27)] = keypad[(short) (keypadOffset + 6)];
		scratch59[(short) (otpBegin + 34)] = keypad[(short) (keypadOffset + 7)];
		scratch59[(short) (otpBegin + 38)] = keypad[(short) (keypadOffset + 8)];
		scratch59[(short) (otpBegin + 42)] = keypad[(short) (keypadOffset + 9)];
		scratch59[(short) (otpBegin + 53)] = keypad[(short) (keypadOffset + 0)];
		return offset;
	}

	public short generateVCode(byte[] data, short dataOffset, short dataLength, byte[] vcode, short vcodeOffset,
			byte[] scratch70, short scratchOffset) {
		sha1.reset();
		short offset = scratchOffset;
		short sha1Len = sha1.doFinal(data, dataOffset, dataLength, scratch70, offset);
		offset += sha1Len;

		offset += Base58.encode(scratch70, scratchOffset, sha1Len, scratch70, offset, scratch70, (short) (offset + 50));

		// copy last 4 chars
		Util.arrayCopyNonAtomic(scratch70, (short) (offset - VCODE_SIZE), vcode, vcodeOffset, VCODE_SIZE);
		return (short) 4;
	}

	private static final byte[] BYTES_TO_DECIMAL_SIZE = { 0, 3, 5, 8, 10, 13, 15, 17, 20, 22, 25, 27, 29, 32, 34, 37,
			39 };

	private short toDecimalString(byte[] uBigBuf, short uBigOff, short uBigLen, byte[] decBuf, short decOff,
			byte[] scratch20, short scratchOffset) {

		Util.arrayCopyNonAtomic(uBigBuf, uBigOff, scratch20, scratchOffset, uBigLen);

		short dividend, division, remainder;
		final short uBigEnd = (short) (scratchOffset + uBigLen);
		final short decDigits = BYTES_TO_DECIMAL_SIZE[uBigLen];
		for (short decIndex = (short) (decOff + decDigits - 1); decIndex >= decOff; decIndex--) {
			remainder = 0;
			for (short uBigIndex = scratchOffset; uBigIndex < uBigEnd; uBigIndex++) {
				dividend = (short) ((remainder << 8) + (scratch20[uBigIndex] & 0xFF));
				division = (short) (dividend / 10);
				remainder = (short) (dividend - division * 10);
				scratch20[uBigIndex] = (byte) division;
			}
			decBuf[decIndex] = (byte) (remainder + '0');
		}
		return decDigits;
	}

	private short satoshi2BTC(byte[] satoshi, short satoshiOffset, short satoshiLength, byte[] btc, short btcOffset) {
		// [5B:before point][8B: after point]
		// 00000.00000000

		// decimal number length must be 13
		if (satoshiLength != 13)
			return 0;

		// remove left zeros
		// short startOffset = satoshiOffset;
		short beforePointLength = 5;
		for (short i = 0; i < 5 && (beforePointLength > 1); i++) {
			if (satoshi[(short) (satoshiOffset + i)] == 0x30) {
				beforePointLength--;
			} else
				break;
		}

		// remove right zeros
		short afterPointLength = 8;
		for (short i = (short) (satoshiLength - 1); i > 0 && afterPointLength > 1; i--) {
			if (satoshi[(short) (satoshiOffset + i)] == 0x30) {
				afterPointLength--;
			} else
				break;
		}

		short offset = btcOffset;
		offset = Util.arrayCopyNonAtomic(satoshi, (short) (5 - beforePointLength + satoshiOffset), btc, offset,
				beforePointLength);
		btc[offset++] = '.';
		offset = Util.arrayCopyNonAtomic(satoshi, (short) (satoshiOffset + 5), btc, offset, afterPointLength);
		return offset;
	}

	private boolean displayText(byte[] inBuff, short inOffset, short inLength) {

		// String message = new String(inBuff, inOffset + 8, inLength - 8);
		// java.lang.System.out.println(message);

		esUtil.clearScreen();

		if (inLength > 78)
			inLength = 78;

		short dataLength = (short) (inLength - 8);

		short offset = inOffset;

		inBuff[offset++] = (byte) 0; // Reserved
		inBuff[offset++] = (byte) 0; // Coding: 00:UFT-8
		inBuff[offset++] = (byte) 0;
		inBuff[offset++] = (byte) 0; // Start column pixel
		inBuff[offset++] = (byte) 0;
		inBuff[offset++] = (byte) 0; // Start row pixel
		inBuff[offset++] = (byte) ((dataLength & (short) 0xFF00) >> 8);
		inBuff[offset++] = (byte) (dataLength & (short) 0x00FF); // Text length

		return esUtil.displayText(inBuff, inOffset, inLength);
		// return true;
	}
}
