package BitaWalletCard;

import javacard.framework.*;
import com.es.specialmethod.ESUtil;
// import com.es.specialmethod.ESComm;
// import com.es.specialmethod.method;

public class Display {

	private static final byte NEWLINE = (byte) 0x0A;
	private static final short HEADER_SIZE = 8;

	private static final short MAX_IMG_DATA_EACH_PKT = (short) 240;

	private static final byte MSG_WELCOME[] = { ' ', ' ', ' ', 'B', 'i', 't', 'a', 'W', 'a', 'l', 'l', 'e', 't' };
	private static final byte MSG_SUCCESSFUL[] = { ' ', ' ', ' ', 'S', 'u', 'c', 'c', 'e', 's', 's', 'f', 'u', 'l' };
	private static final byte MSG_FAILED[] = { ' ', ' ', ' ', ' ', ' ', 'F', 'a', 'i', 'l', 'e', 'd' };
	private static final byte MSG_YESCODE[] = { 'Y', 'E', 'S', ':' };
	private static final byte MSG_WIPE[] = { ' ', ' ', ' ', ' ', ' ', 'W', 'i', 'p', 'e', '?' };
	private static final byte MSG_BACKUP[] = { 'B', 'a', 'c', 'k', 'u', 'p', '?' };
	private static final byte MSG_BACKUPKEY[] = { ' ', ' ', ' ', 'b', 'a', 'c', 'k', 'u', 'p', ' ', 'k', 'e', 'y' };
	private static final byte MSG_VCODE_DATA[] = { ' ', ' ', ' ', ' ', '[', ' ', ' ', ' ', ' ', ' ', ' ', ']' };
	private static final byte MSG_VCODE_DATA_INDEX = (byte) 6;
	private static final byte MSG_SEND[] = { 'S', 'e', 'n', 'd', '?' };
	private static final byte MSG_AMOUNT[] = { 'A', 'm', 'o', 'u', 'n', 't', ':' };
	private static final byte MSG_FEE[] = { 'F', 'e', 'e', ':' };
	private static final byte MSG_TO[] = { 'T', 'o', ':' };

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

	public boolean screenWelcome(byte[] scratch, short scratchOffset) {
		short offset = (short) (scratchOffset + HEADER_SIZE);

		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;

		offset = Util.arrayCopyNonAtomic(MSG_WELCOME, (short) 0, scratch, offset, (short) MSG_WELCOME.length);

		return displayText(scratch, scratchOffset, (short) (offset - scratchOffset));
	}

	public boolean screenSuccessful(byte[] scratch, short scratchOffset) {
		short offset = (short) (scratchOffset + HEADER_SIZE);

		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;

		offset = Util.arrayCopyNonAtomic(MSG_SUCCESSFUL, (short) 0, scratch, offset, (short) MSG_SUCCESSFUL.length);

		return displayText(scratch, scratchOffset, (short) (offset - scratchOffset));
	}

	public boolean screenFailed(byte[] scratch, short scratchOffset) {
		short offset = (short) (scratchOffset + HEADER_SIZE);

		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;

		offset = Util.arrayCopyNonAtomic(MSG_FAILED, (short) 0, scratch, offset, (short) MSG_FAILED.length);

		return displayText(scratch, scratchOffset, (short) (offset - scratchOffset));
	}

	public boolean screenWipe(byte[] yescode, short yescodeOffset, short yescodeLength, byte[] scratch,
			short scratchOffset) {
		short offset = (short) (scratchOffset + HEADER_SIZE);

		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;

		offset = Util.arrayCopyNonAtomic(MSG_WIPE, (short) 0, scratch, offset, (short) MSG_WIPE.length);

		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;

		offset = Util.arrayCopyNonAtomic(MSG_YESCODE, (short) 0, scratch, offset, (short) MSG_YESCODE.length);

		offset = Util.arrayCopyNonAtomic(yescode, yescodeOffset, scratch, offset, yescodeLength);

		return displayText(scratch, scratchOffset, (short) (offset - scratchOffset));
	}

	public boolean screenBackup1(byte[] kcv, short kcvOffset, short kcvLength, byte[] scratch, short scratchOffset) {
		short offset = (short) (scratchOffset + HEADER_SIZE);

		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;

		offset = Util.arrayCopyNonAtomic(MSG_BACKUPKEY, (short) 0, scratch, offset, (short) MSG_BACKUPKEY.length);
		scratch[offset++] = NEWLINE;

		Util.arrayCopyNonAtomic(MSG_VCODE_DATA, (short) 0, scratch, offset, (short) MSG_VCODE_DATA.length);

		offset += MSG_VCODE_DATA_INDEX;

		short vcodeOffset = (short) (kcvOffset + kcvLength - 4);

		offset = Util.arrayCopyNonAtomic(kcv, vcodeOffset, scratch, offset, (short) 4);
		offset += 2;

		return displayText(scratch, scratchOffset, (short) (offset - scratchOffset));
	}

	public boolean screenBackup2(byte[] yescode, short yescodeOffset, short yescodeLength, byte[] kcv, short kcvOffset,
			short kcvLength, byte[] scratch, short scratchOffset) {
		short offset = (short) (scratchOffset + HEADER_SIZE);

		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;

		offset = Util.arrayCopyNonAtomic(MSG_BACKUPKEY, (short) 0, scratch, offset, (short) MSG_BACKUPKEY.length);
		scratch[offset++] = NEWLINE;

		Util.arrayCopyNonAtomic(MSG_VCODE_DATA, (short) 0, scratch, offset, (short) MSG_VCODE_DATA.length);

		offset += MSG_VCODE_DATA_INDEX;

		short vcodeOffset = (short) (kcvOffset + kcvLength - 4);

		offset = Util.arrayCopyNonAtomic(kcv, vcodeOffset, scratch, offset, (short) 4);
		offset += 2;

		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;
		scratch[offset++] = NEWLINE;

		offset = Util.arrayCopyNonAtomic(MSG_BACKUP, (short) 0, scratch, offset, (short) MSG_BACKUP.length);
		scratch[offset++] = NEWLINE;

		offset = Util.arrayCopyNonAtomic(MSG_YESCODE, (short) 0, scratch, offset, (short) MSG_YESCODE.length);

		offset = Util.arrayCopyNonAtomic(yescode, yescodeOffset, scratch, offset, yescodeLength);

		return displayText(scratch, scratchOffset, (short) (offset - scratchOffset));
	}

	public boolean screenSend(byte[] yescode, short yescodeOffset, short yescodeLength, byte[] amount,
			short amountOffset, short amountLength, byte[] fee, short feeOffset, short feeLength, byte[] destAddress,
			short destAddressOffset, short destAddressLength, byte[] scratch, short scratchOffset) {
		short offset = (short) (scratchOffset + HEADER_SIZE);

		offset = Util.arrayCopyNonAtomic(MSG_SEND, (short) 0, scratch, offset, (short) MSG_SEND.length);

		scratch[offset++] = NEWLINE;

		offset = Util.arrayCopyNonAtomic(MSG_AMOUNT, (short) 0, scratch, offset, (short) MSG_AMOUNT.length);

		scratch[offset++] = NEWLINE;

		offset = Util.arrayCopyNonAtomic(MSG_FEE, (short) 0, scratch, offset, (short) MSG_FEE.length);

		scratch[offset++] = NEWLINE;

		offset = Util.arrayCopyNonAtomic(MSG_TO, (short) 0, scratch, offset, (short) MSG_TO.length);

		scratch[offset++] = NEWLINE;

		offset = Util.arrayCopyNonAtomic(MSG_YESCODE, (short) 0, scratch, offset, (short) MSG_YESCODE.length);

		offset = Util.arrayCopyNonAtomic(yescode, yescodeOffset, scratch, offset, yescodeLength);

		return displayText(scratch, scratchOffset, (short) (offset - scratchOffset));
	}

	private boolean displayText(byte[] inBuff, short inOffset, short inLength) {

		if (esUtil.clearScreen() == false) {
			return false;
		}

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
