package BitaWalletCard;

import javacard.framework.*;
import com.es.specialmethod.ESUtil;
// import com.es.specialmethod.ESComm;
// import com.es.specialmethod.method;

public class Display {

	private static final byte NEWLINE = (byte) 0x0A;
	private static final byte SPACE = (byte) ' ';
	private static final short HEADER_SIZE = 8;

	private static final short MAX_IMG_DATA_EACH_PKT = (short) 240;

	private static final byte MSG_WELCOME[] = { ' ', ' ', ' ', 'B', 'i', 't', 'a', 'W', 'a', 'l', 'l', 'e', 't' };
	private static final byte MSG_SUCCESSFUL[] = { ' ', ' ', ' ', 'S', 'u', 'c', 'c', 'e', 's', 's', 'f', 'u', 'l' };
	private static final byte MSG_FAILED[] = { ' ', ' ', ' ', ' ', ' ', 'F', 'a', 'i', 'l', 'e', 'd' };
	private static final byte MSG_WIPE[] = { ' ', ' ', ' ', ' ', 'S', 'u', 'r', 'e', ' ', 't', 'o', NEWLINE, ' ', ' ',
			' ', ' ', ' ', 'w', 'i', 'p', 'e', '?' };
	private static final byte BTN_WIPE[] = { 'W', 'I', 'P', 'E', ':' };
	private static final byte MSG_BACKUP[] = { ' ', ' ', ' ', ' ', 'S', 'u', 'r', 'e', ' ', 't', 'o', NEWLINE, ' ', ' ',
			' ', ' ', 'b', 'a', 'c', 'k', 'u', 'p', '?' };
	private static final byte BTN_BACKUP[] = { 'B', 'A', 'C', 'K', 'U', 'P', ':' };
	private static final byte MSG_BACKUPKEY[] = { ' ', ' ', ' ', 'B', 'a', 'c', 'k', 'u', 'p', ' ', 'k', 'e', 'y' };
	private static final byte MSG_VCODE[] = { ' ', ' ', ' ', 'v', 'c', 'o', 'd', 'e', ':' };
	private static final byte BTN_SEND[] = { 'S', 'E', 'N', 'D', ':' };
	private static final byte MSG_TO[] = { 'T', 'o' };
	private static final byte MSG_BTC[] = { 'B', 'T', 'C' };
	private static final byte MSG_FEE[] = { 'f', 'e', 'e' };
	private static final byte MSG_MBTC[] = { 'm', 'B', 'T', 'C' };

	private static ESUtil esUtil = null;
	// public method esMethod;
	// public static ESComm esComm;

	public Display() {
		esUtil = new ESUtil();
		// esComm = new ESComm();
		// esMethod = new method();
		// esMethod.CommBuff = JCSystem.makeTransientByteArray((short) 300,
		// JCSystem.CLEAR_ON_DESELECT);
	}

	public boolean welcomeScreen(byte[] scratch, short scratchOffset) {
		short offset = (short) (scratchOffset + HEADER_SIZE);

		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;

		offset = Util.arrayCopyNonAtomic(MSG_WELCOME, (short) 0, scratch, offset, (short) MSG_WELCOME.length);

		return displayText(scratch, scratchOffset, (short) (offset - scratchOffset));
	}

	public boolean successfulScreen(byte[] scratch, short scratchOffset) {
		short offset = (short) (scratchOffset + HEADER_SIZE);

		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;

		offset = Util.arrayCopyNonAtomic(MSG_SUCCESSFUL, (short) 0, scratch, offset, (short) MSG_SUCCESSFUL.length);

		return displayText(scratch, scratchOffset, (short) (offset - scratchOffset));
	}

	public boolean failedScreen(byte[] scratch, short scratchOffset) {
		short offset = (short) (scratchOffset + HEADER_SIZE);

		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;

		offset = Util.arrayCopyNonAtomic(MSG_FAILED, (short) 0, scratch, offset, (short) MSG_FAILED.length);

		return displayText(scratch, scratchOffset, (short) (offset - scratchOffset));
	}

	public boolean wipeScreen(byte[] yescode, short yescodeOffset, short yescodeLength, byte[] scratch,
			short scratchOffset) {
		short offset = (short) (scratchOffset + HEADER_SIZE);

		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;

		offset = Util.arrayCopyNonAtomic(MSG_WIPE, (short) 0, scratch, offset, (short) MSG_WIPE.length);

		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;

		offset = Util.arrayCopyNonAtomic(BTN_WIPE, (short) 0, scratch, offset, (short) BTN_WIPE.length);

		offset = Util.arrayCopyNonAtomic(yescode, yescodeOffset, scratch, offset, yescodeLength);

		return displayText(scratch, scratchOffset, (short) (offset - scratchOffset));
	}

	public boolean backup1Screen(byte[] kcv, short kcvOffset, short kcvLength, byte[] scratch, short scratchOffset) {
		short offset = (short) (scratchOffset + HEADER_SIZE);

		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;

		offset = Util.arrayCopyNonAtomic(MSG_BACKUPKEY, (short) 0, scratch, offset, (short) MSG_BACKUPKEY.length);
		scratch[offset++] = NEWLINE;

		offset = Util.arrayCopyNonAtomic(MSG_VCODE, (short) 0, scratch, offset, (short) MSG_VCODE.length);

		short vcodeOffset = (short) (kcvOffset + kcvLength - 4);

		offset = Util.arrayCopyNonAtomic(kcv, vcodeOffset, scratch, offset, (short) 4);

		return displayText(scratch, scratchOffset, (short) (offset - scratchOffset));
	}

	public boolean backup2Screen(byte[] yescode, short yescodeOffset, short yescodeLength, byte[] kcv, short kcvOffset,
			short kcvLength, byte[] scratch, short scratchOffset) {
		short offset = (short) (scratchOffset + HEADER_SIZE);

		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;

		offset = Util.arrayCopyNonAtomic(MSG_BACKUP, (short) 0, scratch, offset, (short) MSG_BACKUP.length);
		scratch[offset++] = NEWLINE;

		offset = Util.arrayCopyNonAtomic(MSG_VCODE, (short) 0, scratch, offset, (short) MSG_VCODE.length);

		short vcodeOffset = (short) (kcvOffset + kcvLength - 4);

		offset = Util.arrayCopyNonAtomic(kcv, vcodeOffset, scratch, offset, (short) 4);

		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;

		offset = Util.arrayCopyNonAtomic(BTN_BACKUP, (short) 0, scratch, offset, (short) BTN_BACKUP.length);

		offset = Util.arrayCopyNonAtomic(yescode, yescodeOffset, scratch, offset, yescodeLength);

		return displayText(scratch, scratchOffset, (short) (offset - scratchOffset));
	}

	public boolean sendScreen(byte[] yescode, short yescodeOffset, short yescodeLength, byte[] amount,
			short amountOffset, short amountLength, byte[] fee, short feeOffset, short feeLength, byte[] destAddress,
			short destAddressOffset, short destAddressLength, byte[] scratch, short scratchOffset) {
		short offset = (short) (scratchOffset + HEADER_SIZE);

		toDecimalString(amount, amountOffset, amountLength, scratch, (short) (offset + 10));// temp use of scratch
		offset += satoshi2BTC(scratch, (short) (offset + 10), scratch, offset);
		scratch[offset++] = SPACE;
		offset = Util.arrayCopyNonAtomic(MSG_BTC, (short) 0, scratch, offset, (short) MSG_BTC.length);
		scratch[offset++] = NEWLINE;

		offset = Util.arrayCopyNonAtomic(MSG_TO, (short) 0, scratch, offset, (short) MSG_TO.length);
		scratch[offset++] = NEWLINE;

		offset += Base58.encode(destAddress, destAddressOffset, destAddressLength, scratch, offset, scratch,
				(short) (offset + 50));
		scratch[offset++] = NEWLINE;

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

		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;

		offset = Util.arrayCopyNonAtomic(BTN_SEND, (short) 0, scratch, offset, (short) BTN_SEND.length);

		offset = Util.arrayCopyNonAtomic(yescode, yescodeOffset, scratch, offset, yescodeLength);

		return displayText(scratch, scratchOffset, (short) (offset - scratchOffset));
	}

	private static final byte[] BYTES_TO_DECIMAL_SIZE = { 0, 3, 5, 8, 10, 13, 15, 17, 20, 22, 25, 27, 29, 32, 34, 37,
			39 };

	private short toDecimalString(byte[] uBigBuf, short uBigOff, short uBigLen, byte[] decBuf, short decOff) {
		short dividend, division, remainder;
		final short uBigEnd = (short) (uBigOff + uBigLen);
		final short decDigits = BYTES_TO_DECIMAL_SIZE[uBigLen];
		for (short decIndex = (short) (decOff + decDigits - 1); decIndex >= decOff; decIndex--) {
			remainder = 0;
			for (short uBigIndex = uBigOff; uBigIndex < uBigEnd; uBigIndex++) {
				dividend = (short) ((remainder << 8) + (uBigBuf[uBigIndex] & 0xFF));
				division = (short) (dividend / 10);
				remainder = (short) (dividend - division * 10);
				uBigBuf[uBigIndex] = (byte) division;
			}
			decBuf[decIndex] = (byte) (remainder + '0');
		}
		return decDigits;
	}

	private short satoshi2BTC(byte[] satoshi, short satoshiOffset, byte[] btc, short btcOffset) {
		// original hex number length : 8B => satoshiLength : 20B
		// 99.99999999 BTC

		short offset = btcOffset;
		offset = Util.arrayCopyNonAtomic(satoshi, (short) (satoshiOffset + 10), btc, offset, (short) 2);
		btc[offset++] = '.';
		offset = Util.arrayCopyNonAtomic(satoshi, (short) (satoshiOffset + 12), btc, offset, (short) 8);

		// remove right zeros
		short length = (short) (offset - btcOffset);
		for (short i = (short) (length - 1); i >= 0; i--) {
			if (btc[(short) (btcOffset + i)] != 0x30) {
				length = (short) (i + 1);
				break;
			}
		}
		return length;
	}

	private short satoshi2mBTC(byte[] satoshi, short satoshiOffset, byte[] mbtc, short mbtcOffset) {
		// original hex number length : 8B => satoshiLength : 20B
		// 9.9999 mBTC

		short offset = mbtcOffset;
		offset = Util.arrayCopyNonAtomic(satoshi, (short) (satoshiOffset + 14), mbtc, offset, (short) 1);
		mbtc[offset++] = '.';
		offset = Util.arrayCopyNonAtomic(satoshi, (short) (satoshiOffset + 15), mbtc, offset, (short) 4);

		// remove right zeros
		short length = (short) (offset - mbtcOffset);
		for (short i = (short) (length - 1); i >= 0; i--) {
			if (mbtc[(short) (mbtcOffset + i)] != 0x30) {
				length = (short) (i + 1);
				break;
			}
		}
		return length;
	}

	public boolean displayText(byte[] inBuff, short inOffset, short inLength) {

		esUtil.clearScreen();

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
	}

	private void displayImage(byte[] inBuff, short inOffset, short inLength, byte[] scratch300, short scratchOffset) {

		// esMethod = new method();
		// esMethod.CommBuff = JCSystem.makeTransientByteArray((short) 300,
		// JCSystem.CLEAR_ON_DESELECT);

		byte result;
		short ImgDataReadIndex, ImgCntCurrPkt, ImgCntLeft, PktPayloadSize;
		byte[] resp = new byte[20];
		byte LastPkt;

		ImgCntLeft = (short) (inLength - 10);
		ImgDataReadIndex = (short) (inOffset + 10);

		while (true) {
			LastPkt = (ImgCntLeft <= MAX_IMG_DATA_EACH_PKT) ? (byte) 1 : (byte) 0;
			ImgCntCurrPkt = (LastPkt == (byte) 1) ? ImgCntLeft : MAX_IMG_DATA_EACH_PKT;

			if (ImgDataReadIndex == (short) (inOffset + 10)) // first packet
			{
				scratch300[scratchOffset] = 0x21;
				scratch300[(short) (scratchOffset + 1)] = 0x02;
				scratch300[(short) (scratchOffset + 2)] = (byte) ((LastPkt << 1) | 1); // link type (last packet | first
																						// packet)

				PktPayloadSize = (short) (ImgCntCurrPkt + 13);
				Util.arrayCopyNonAtomic(inBuff, inOffset, scratch300, (short) (scratchOffset + 3),
						(short) (ImgCntCurrPkt + 10));
			} else {
				scratch300[scratchOffset] = 0x21;
				scratch300[(short) (scratchOffset + 1)] = 0x02;
				scratch300[(short) (scratchOffset + 2)] = (byte) ((LastPkt << 1) | 0); // link type
				PktPayloadSize = (short) (ImgCntCurrPkt + 3);
				Util.arrayCopyNonAtomic(inBuff, ImgDataReadIndex, scratch300, (short) (scratchOffset + 3),
						ImgCntCurrPkt);
			}

			// result = esComm.sendAndReceiveData(PktBuf, (short) 0, PktPayloadSize, (short)
			// 0xFFFF, resp, (short) 0,
			// (short) 0xFFFF);

			// result = esMethod.tcSendAndRecv(scratch300, scratchOffset, PktPayloadSize,
			// resp, (short) 0);
			// if (result != 0x00 && resp[0] != 0x00 && resp[1] != 0x02) {
			// continue;
			// }

			ImgCntLeft -= ImgCntCurrPkt;
			ImgDataReadIndex += ImgCntCurrPkt;

			if (ImgCntLeft == 0) // get response for DisplayImage cmd
			{
				break;
			}
		}
	}
}
