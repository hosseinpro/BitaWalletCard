package BitaWalletCard;

import javacard.security.ECKey;

public class Secp256k1 {

        // Nice SECp256k1 constants, only available during NIST opening hours

        protected static final byte SECP256K1_FP[] = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                        (byte) 0xFF, (byte) 0xFE, (byte) 0xFF, (byte) 0xFF, (byte) 0xFC, (byte) 0x2F };
        protected static final byte SECP256K1_A[] = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
        protected static final byte SECP256K1_B[] = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x07 };
        protected static final byte SECP256K1_G[] = { (byte) 0x04, (byte) 0x79, (byte) 0xBE, (byte) 0x66, (byte) 0x7E,
                        (byte) 0xF9, (byte) 0xDC, (byte) 0xBB, (byte) 0xAC, (byte) 0x55, (byte) 0xA0, (byte) 0x62,
                        (byte) 0x95, (byte) 0xCE, (byte) 0x87, (byte) 0x0B, (byte) 0x07, (byte) 0x02, (byte) 0x9B,
                        (byte) 0xFC, (byte) 0xDB, (byte) 0x2D, (byte) 0xCE, (byte) 0x28, (byte) 0xD9, (byte) 0x59,
                        (byte) 0xF2, (byte) 0x81, (byte) 0x5B, (byte) 0x16, (byte) 0xF8, (byte) 0x17, (byte) 0x98,
                        (byte) 0x48, (byte) 0x3A, (byte) 0xDA, (byte) 0x77, (byte) 0x26, (byte) 0xA3, (byte) 0xC4,
                        (byte) 0x65, (byte) 0x5D, (byte) 0xA4, (byte) 0xFB, (byte) 0xFC, (byte) 0x0E, (byte) 0x11,
                        (byte) 0x08, (byte) 0xA8, (byte) 0xFD, (byte) 0x17, (byte) 0xB4, (byte) 0x48, (byte) 0xA6,
                        (byte) 0x85, (byte) 0x54, (byte) 0x19, (byte) 0x9C, (byte) 0x47, (byte) 0xD0, (byte) 0x8F,
                        (byte) 0xFB, (byte) 0x10, (byte) 0xD4, (byte) 0xB8 };
        protected static final byte SECP256K1_R[] = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFE, (byte) 0xBA, (byte) 0xAE, (byte) 0xDC,
                        (byte) 0xE6, (byte) 0xAF, (byte) 0x48, (byte) 0xA0, (byte) 0x3B, (byte) 0xBF, (byte) 0xD2,
                        (byte) 0x5E, (byte) 0x8C, (byte) 0xD0, (byte) 0x36, (byte) 0x41, (byte) 0x41 };
        protected static final byte SECP256K1_K = (byte) 0x01;

        protected static final byte SECP256K1_Rdiv2[] = { (byte) 0x7F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x5D, (byte) 0x57,
                        (byte) 0x6E, (byte) 0x73, (byte) 0x57, (byte) 0xA4, (byte) 0x50, (byte) 0x1D, (byte) 0xDF,
                        (byte) 0xE9, (byte) 0x2F, (byte) 0x46, (byte) 0x68, (byte) 0x1B, (byte) 0x20, (byte) 0xA0 };

        protected static boolean setCommonCurveParameters(ECKey key) {
                try {
                        key.setA(SECP256K1_A, (short) 0, (short) SECP256K1_A.length);
                        key.setB(SECP256K1_B, (short) 0, (short) SECP256K1_B.length);
                        key.setFieldFP(SECP256K1_FP, (short) 0, (short) SECP256K1_FP.length);
                        key.setG(SECP256K1_G, (short) 0, (short) SECP256K1_G.length);
                        key.setR(SECP256K1_R, (short) 0, (short) SECP256K1_R.length);
                        key.setK(SECP256K1_K);
                        return true;
                } catch (Exception e) {
                        return false;
                }

        }
}
