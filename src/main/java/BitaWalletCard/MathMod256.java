package BitaWalletCard;

class MathMod256 {

    /**
     * c = a+b mod n
     *
     * a,b,n 256 unsigned bits value precondition: 0 <= a < n 0 <= b < n n > 0 c
     * shall either NOT partially overlap a or b, or shall be a or b
     *
     */
    public static void addm(byte[] c, short c_off, byte[] a, short a_off, byte[] b, short b_off, byte[] n,
            short n_off) {
        if ((add(c, c_off, a, a_off, b, b_off, (short) 32) != 0) || (ucmp(c, c_off, n, n_off, (short) 32) > 0)) {
            sub(c, c_off, c, c_off, n, n_off, (short) 32);
        }
    }

    /**
     * unsigned compare a and b return 0 if a == b return < 0 if a < b return > 0 if
     * a > b
     */
    public static short ucmp(byte[] a, short a_off, byte[] b, short b_off, short length) {
        short ai, bi;
        for (short i = 0; i < length; i++) {
            ai = (short) (a[(short) (a_off + i)] & 0x00ff);
            bi = (short) (b[(short) (b_off + i)] & 0x00ff);
            if (ai != bi) {
                return (short) (ai - bi);
            }
        }
        return 0;
    }

    protected static short add(byte[] c, short c_off, byte[] a, short a_off, byte[] b, short b_off, short length) {

        short ci = 0;
        for (short i = (short) (length - 1); i >= 0; i--) {
            ci = (short) ((short) (a[(short) (a_off + i)] & 0x00FF) + (short) (b[(short) (b_off + i)] & 0xFF) + ci);
            c[(short) (c_off + i)] = (byte) ci;
            ci = (short) (ci >> 8);
        }
        return ci;
    }

    protected static short sub(byte[] c, short c_off, byte[] a, short a_off, byte[] b, short b_off, short length) {
        short ci = 0;
        for (short i = (short) (length - 1); i >= 0; i--) {
            ci = (short) ((short) (a[(short) (a_off + i)] & 0xFF) - (short) (b[(short) (b_off + i)] & 0xFF) - ci);
            c[(short) (c_off + i)] = (byte) ci;
            ci = (short) (((ci >> 8) != 0) ? 1 : 0);
        }
        return ci;
    }

    // c = a[nB] / b[1B]
    protected static void div(byte[] c, short c_off, byte[] a, short a_off, byte b, short length) {
        short r = 0;
        short t = 0;
        short ci = 0;
        for (short i = 0; i < length; i++) {
            t = (short) (r << 4);
            t = (short) (t | (short) ((short) (a[(short) (i + a_off)] & 0xF0) >> 4));
            r = (short) (t % b);
            ci = (short) ((short) (t / b) << 4);

            t = (short) (r << 4);
            t = (short) (t | (short) (a[(short) (i + a_off)] & 0x0F));
            r = (short) (t % b);
            ci = (short) (ci | (t / b));

            c[(short) (i + c_off)] = (byte) ci;
        }
    }

}