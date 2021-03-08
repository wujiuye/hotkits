package com.wujiuye.hotkit.util.encrypt;

/**
 * 十六进制工具类
 *
 * @author wujiuye 2020/06/15
 */
public class HexUtils {

    private static final char[] DIGITS_LOWER;
    private static final char[] DIGITS_UPPER;

    static {
        DIGITS_LOWER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        DIGITS_UPPER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    }

    /**
     * 字符数组转16进制字节数组
     *
     * @param data 字符数组
     * @return
     * @throws DecoderException
     */
    public static byte[] decodeHex(char[] data) throws DecoderException {
        int len = data.length;
        if ((len & 1) != 0) {
            throw new DecoderException("Odd number of characters.");
        } else {
            byte[] out = new byte[len >> 1];
            int i = 0;
            for (int j = 0; j < len; ++i) {
                int f = toDigit(data[j], j) << 4;
                ++j;
                f |= toDigit(data[j], j);
                ++j;
                out[i] = (byte) (f & 255);
            }
            return out;
        }
    }

    /**
     * 十六进制字节数组转字符数组
     *
     * @param data 十六进制字节数组
     * @return
     */
    public static char[] encodeHex(byte[] data) {
        return encodeHex(data, true);
    }

    /**
     * 十六进制字节数组转字符数组
     *
     * @param data        十六进制字节数组
     * @param toLowerCase 十六进制字符的字母大小还是小写
     * @return
     */
    public static char[] encodeHex(byte[] data, boolean toLowerCase) {
        int l = data.length;
        char[] out = new char[l << 1];
        int i = 0;
        char[] toDigits = toLowerCase ? DIGITS_LOWER : DIGITS_UPPER;
        for (int var5 = 0; i < l; ++i) {
            out[var5++] = toDigits[(240 & data[i]) >>> 4];
            out[var5++] = toDigits[15 & data[i]];
        }
        return out;
    }

    private static int toDigit(char ch, int index) throws DecoderException {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new DecoderException("Illegal hexadecimal character " + ch + " at index " + index);
        } else {
            return digit;
        }
    }

}
