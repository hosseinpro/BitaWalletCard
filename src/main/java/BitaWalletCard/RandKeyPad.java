package BitaWalletCard;

import javacard.framework.*;
import javacard.security.*;

public class RandKeyPad {

    private static final byte PIN_SIZE = 4;

    private static RandomData randomData;
    public byte[] buttons;

    public RandKeyPad() {
        randomData = RandomData.getInstance(RandomData.ALG_SECURE_RANDOM);
        buttons = JCSystem.makeTransientByteArray((short) 10, JCSystem.CLEAR_ON_DESELECT);
    }

    public void generate() {

        for (short i = 0; i < (short) 10; i++) {
            buttons[i] = (byte) (i + (short) 0x30);
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

            byte soap = buttons[index1];
            buttons[index1] = buttons[index2];
            buttons[index2] = soap;
        }
    }

    public void decode(byte[] enteredButtons, short enteredButtonsOffset, byte[] decodedPin, short decodedPinOffset) {

        for (short i = 0; i < PIN_SIZE; i++) {
            byte index = (byte) (enteredButtons[(short) (enteredButtonsOffset + i)] - (short) 0x30);
            decodedPin[(short) (decodedPinOffset + i)] = buttons[index];
        }
    }
}
