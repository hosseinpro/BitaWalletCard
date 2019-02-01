package BitaWalletCard;

import javacard.framework.*;
import com.es.specialmethod.ESUtil;

public class Display {

	public static final byte NEWLINE = (byte) 0x0A;
	private static final short DISPLAY_COL = 16;
	private static final short DISPLAY_ROW = 8;

	private static final byte[] BlackCard = new byte[] { (byte) 'B', (byte) 'l', (byte) 'a', (byte) 'c', (byte) 'k',
			(byte) 'C', (byte) 'a', (byte) 'r', (byte) 'd' };
	private static final byte[] BTCTestNet = new byte[] { (byte) 'B', (byte) 'T', (byte) 'T' };
	private static final byte[] BTCMainNet = new byte[] { (byte) 'B', (byte) 'T', (byte) 'C' };

	private static ESUtil esUtil = null;

	public Display() {
		esUtil = new ESUtil();
		// clearScreen();
	}

	public boolean clearScreen() {
		// return true;
		return esUtil.clearScreen();
	}

	public boolean displayText(byte[] inBuff, short inOffset, short inLength, byte[] scratch, short scratchOffset) {
		if (clearScreen() == false) {
			return false;
		}

		scratch[(short) (scratchOffset + 0)] = (byte) 0; // Reserved
		scratch[(short) (scratchOffset + 1)] = (byte) 0; // Coding: 00:UFT-8
		scratch[(short) (scratchOffset + 2)] = (byte) 0;
		scratch[(short) (scratchOffset + 3)] = (byte) 0; // Start column pixel
		scratch[(short) (scratchOffset + 4)] = (byte) 0;
		scratch[(short) (scratchOffset + 5)] = (byte) 0; // Start row pixel
		scratch[(short) (scratchOffset + 6)] = (byte) ((inLength & (short) 0xFF00) >> 8);
		scratch[(short) (scratchOffset + 7)] = (byte) (inLength & (short) 0x00FF); // Text length
		Util.arrayCopyNonAtomic(inBuff, inOffset, scratch, (short) (scratchOffset + 8), inLength);

		short offset = (short) 8;

		// word wrapping
		// short wordBegin = offset;
		// short wordEnd = (short)0;
		// while(wordBegin <= (short)(inLength + offset)){
		// short i = wordBegin;
		// while((scratch[(short)(scratchOffset + i)] != (byte)0x20) && (i <
		// (short)(inLength + offset))){
		// i++;}
		// wordEnd = (short)(i - 1);
		// if(((short)(wordEnd - wordBegin) < DISPLAY_COL) &&
		// ((short)((short)(wordBegin - offset) / DISPLAY_COL) !=
		// (short)((short)(wordEnd - offset) / DISPLAY_COL))){
		// scratch[(short)(scratchOffset + (short)(wordBegin - 1))] = NEWLINE;}
		// wordBegin = (short)(wordEnd + 2);
		// }

		// return true;
		return esUtil.displayText(scratch, scratchOffset, (short) (inLength + offset));
	}

	public boolean displayMessage(byte[] message, short messageLength, byte[] scratch, short scratchOffset) {
		// scratch must be min 136 =
		// 16 * 8 : char * line
		// + 8 : header
		short displayBufferSize = 136;

		if (clearScreen() == false) {
			return false;
		}

		scratch[(short) (scratchOffset + 0)] = (byte) 0; // Reserved
		scratch[(short) (scratchOffset + 1)] = (byte) 0; // Coding: 00:UFT-8
		scratch[(short) (scratchOffset + 2)] = (byte) 0;
		scratch[(short) (scratchOffset + 3)] = (byte) 0; // Start column pixel
		scratch[(short) (scratchOffset + 4)] = (byte) 0;
		scratch[(short) (scratchOffset + 5)] = (byte) 0; // Start row pixel
		scratch[(short) (scratchOffset + 6)] = (byte) ((displayBufferSize & (short) 0xFF00) >> 8);
		scratch[(short) (scratchOffset + 7)] = (byte) (displayBufferSize & (short) 0x00FF); // Text length

		short offset = (short) (scratchOffset + 8);
		Util.arrayFillNonAtomic(scratch, offset, displayBufferSize, (byte) ' ');

		short lineCount = 1;
		for (short i = 0; i < messageLength; i++) {
			if (message[i] == NEWLINE) {
				lineCount++;
			}
		}
		short currentLine = (short) ((short) (DISPLAY_ROW / 2) - (short) (lineCount / 2));

		short textBegin = 0;
		short textEnd = 0;

		for (short j = textBegin; j <= messageLength; j++) {
			if ((message[j] == NEWLINE) || (j == messageLength)) {
				textEnd = (short) (j - 1);
				short textLength = (short) ((short) (textEnd - textBegin) + 1);
				short index = offset;
				index += (short) ((short) (currentLine - 1) * DISPLAY_COL);
				index += (short) ((short) (DISPLAY_COL / 2) - (short) (textLength / 2));
				Util.arrayCopyNonAtomic(message, textBegin, scratch, index, textLength);
				currentLine++;
				textBegin = (short) (j + 1);
			}
		}

		// return true;
		return esUtil.displayText(scratch, scratchOffset, (short) (displayBufferSize + 8));
	}

	public boolean displayWelcome(byte[] version, byte[] label, short labelLength, byte[] scratch) {
		short offset = 0;
		Util.arrayCopyNonAtomic(BlackCard, (short) 0, scratch, offset, (short) BlackCard.length);
		offset += BlackCard.length;
		scratch[offset++] = NEWLINE;
		scratch[offset++] = (byte) 'v';
		Util.arrayCopyNonAtomic(version, (short) 0, scratch, offset, (short) version.length);
		offset += (short) version.length;
		scratch[offset++] = NEWLINE;
		Util.arrayCopyNonAtomic(label, (short) 0, scratch, offset, labelLength);
		offset += labelLength;

		return displayMessage(scratch, offset, scratch, offset);
	}

	public boolean displayAddress(short coin, byte[] address, short addressOffset, short addressLength,
			byte[] scratch) {
		switch (coin) {
		case BIP.TST:
			Util.arrayCopyNonAtomic(BTCTestNet, (short) 0, scratch, (short) 0, (short) 3);
			break;
		case BIP.BTC:
			Util.arrayCopyNonAtomic(BTCMainNet, (short) 0, scratch, (short) 0, (short) 3);
			break;
		default:
			return false;
		}
		scratch[3] = (byte) ':';
		scratch[4] = NEWLINE;
		Util.arrayCopyNonAtomic(address, addressOffset, scratch, (short) 5, addressLength);

		return displayText(scratch, (short) 0, (short) (5 + addressLength), scratch, (short) 50);
	}
}
