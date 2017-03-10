package com.blanks.test.demo.swingcard;

import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by Administrator on 2016/6/19.
 */
public class Utils {
    private final static String TAG = Utils.class.getName();

    static final char[] HEX = "0123456789ABCDEF".toCharArray();

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        if (sz == 0) {
            return false;
        }
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i)) && (str.charAt(i) != '.')) {
                return false;
            }
        }
        return true;
    }

    public static String hexEncode(byte[] buffer, int start, int length) {
        if (buffer.length == 0) {
            return "";
        }
        int holder = 0;
        char[] chars = new char[length * 2];
        int pos = -1;
        for (int i = start; i < start + length; i++) {
            holder = (buffer[i] & 0xF0) >> 4;
            chars[++pos * 2] = HEX[holder];
            holder = buffer[i] & 0x0F;
            chars[(pos * 2) + 1] = HEX[holder];
        }
        return new String(chars);
    }

    public static byte[] hexDecode(String hex) {
        // A null string returns an empty array
        if (hex == null || hex.length() == 0) {
            return new byte[0];
        } else if (hex.length() < 3) {
            return new byte[]{(byte) (Integer.parseInt(hex, 16) & 0xFF)};
        }
        // Adjust accordingly for odd-length strings
        int count = hex.length();
        int nibble = 0;
        if (count % 2 != 0) {
            count++;
            nibble = 1;
        }
        byte[] buf = new byte[count / 2];
        char c;
        int holder = 0;
        int pos = 0;
        for (int i = 0; i < buf.length; i++) {
            for (int z = 0; z < 2 && pos < hex.length(); z++) {
                c = hex.charAt(pos++);
                if (c >= 'A' && c <= 'F') {
                    c -= 55;
                } else if (c >= '0' && c <= '9') {
                    c -= 48;
                } else if (c >= 'a' && c <= 'f') {
                    c -= 87;
                } else if (c == '=') {
                    c -= 48;
                }
                if (nibble == 0) {
                    holder = c << 4;
                } else {
                    holder |= c;
                    buf[i] = (byte) holder;
                }
                nibble = 1 - nibble;
            }
        }
        return buf;
    }

    public static void BCDEncode(String value, byte[] buf, int align, int fillChar) {
        int charpos = 0; //char where we start
        int bufpos = 0;
//        if (value.length() % 2 == 1) {
//            //for odd lengths we encode just the first digit in the first byte
//            buf[0] = (byte)(value.charAt(0) - 48);
//            charpos = 1;
//            bufpos = 1;
//        }
        //encode the rest of the string
        if (value.length() % 2 > 0) {
            if (align == FieldConfig.SDK_8583_ALIGN_L) {
                value = value + (char) fillChar;
            } else {
                value = (char) fillChar + value;
            }
        }

        while (charpos < value.length()) {
            buf[bufpos] = (byte) ((((value.charAt(charpos) > '?') ? (value.charAt(charpos) - 55) : (value.charAt(charpos) - 48)) << 4)
                    | ((value.charAt(charpos + 1) > '?') ? (value.charAt(charpos + 1) - 55) : (value.charAt(charpos + 1) - 48)));
            charpos += 2;
            bufpos++;
        }
    }

    public static void BCDEncode(String value, byte[] buf) {
        int charpos = 0; //char where we start
        int bufpos = 0;
//        if (value.length() % 2 == 1) {
//            //for odd lengths we encode just the first digit in the first byte
//            buf[0] = (byte)(value.charAt(0) - 48);
//            charpos = 1;
//            bufpos = 1;
//        }
        //encode the rest of the string
        if (value.length() % 2 > 0) {
            value = value + "0";
        }

        while (charpos < value.length()) {
            buf[bufpos] = (byte) ((((value.charAt(charpos) > '?') ? (value.charAt(charpos) - 55) : (value.charAt(charpos) - 48)) << 4)
                    | ((value.charAt(charpos + 1) > '?') ? (value.charAt(charpos + 1) - 55) : (value.charAt(charpos + 1) - 48)));
            charpos += 2;
            bufpos++;
        }
    }

    /**
     * Decodes a TYPE_BCD-encoded number as a String.
     *
     * @param buf The byte buffer containing the TYPE_BCD data.
     */
    public static String Bcd2String(byte[] buf, int align, int fillChar) throws IndexOutOfBoundsException {
        int length = buf.length;
        char[] digits = new char[length * 2];
        int start = 0;

        for (int i = 0; i < length; i++) {
            digits[start++] = (char) ((((buf[i] & 0x00f0) >> 4) > 9) ?
                    ((buf[i] & 0x00f0) >> 4) + 55 : ((buf[i] & 0x00f0) >> 4) + 48);
            digits[start++] = (char) (((buf[i] & 0x000f) > 9) ?
                    (buf[i] & 0x000f) + 55 : (buf[i] & 0x000f) + 48);
        }
        String val = new String(digits);
        if (align == FieldConfig.SDK_8583_ALIGN_L) {
            if (digits[length * 2 - 2] != fillChar) {
                return val.substring(0, val.length() - 1) + (char) fillChar;
            }
        } else {
            if (digits[length * 2 - 2] != fillChar) {
                return (char) fillChar + val.substring(1);
            }
        }
        return val;
    }

    /**
     * Decodes a TYPE_BCD-encoded number as a String.
     *
     * @param buf The byte buffer containing the TYPE_BCD data.
     */
    public static String Bcd2String(byte[] buf) throws IndexOutOfBoundsException {
        int length = buf.length;
        char[] digits = new char[length * 2];
        int start = 0;

        for (int i = 0; i < length; i++) {
            digits[start++] = (char) ((((buf[i] & 0x00f0) >> 4) > 9) ?
                    ((buf[i] & 0x00f0) >> 4) + 55 : ((buf[i] & 0x00f0) >> 4) + 48);
            digits[start++] = (char) (((buf[i] & 0x000f) > 9) ?
                    (buf[i] & 0x000f) + 55 : (buf[i] & 0x000f) + 48);
        }

        return new String(digits);
    }

    /**
     * BASE64 编码
     *
     * @param buff
     * @return
     */
    public static String encodeBufferBase64(byte[] buff) {
        return buff == null ? null : Base64.encodeToString(buff, Base64.NO_WRAP);
    }

    /**
     * BASE64解码
     *
     * @param s
     * @return
     */
    public static byte[] decodeBufferBase64(String s) {
        try {
            return s == null ? null : Base64.decode(s, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * BASE64 字节数组编码
     *
     * @param s
     * @return String
     */
    public static String encodeBase64(byte[] s) {
        if (s == null)
            return null;

        String res = Base64.encodeToString(s, Base64.NO_WRAP);
        res = res.replace("\n", "");
        res = res.replace("\r", "");

        return res;
    }

    /**
     * BASE64解码
     *
     * @param buff
     * @return
     */
    public static byte[] decodeBase64(byte[] buff) {
        if (buff == null)
            return null;

        try {
            byte[] key = Base64.decode(buff, Base64.DEFAULT);

            return key;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getEigthBitsStringFromByte(int b) {
        // if this is a positive number its bits number will be less
        // than 8
        // so we have to fill it to be a 8 digit binary string
        // b=b+100000000(2^8=256) then only get the lower 8 digit
        b |= 256; // mark the 9th digit as 1 to make sure the string
        // has at
        // least 8 digits
        String str = Integer.toBinaryString(b);
        int len = str.length();
        return str.substring(len - 8, len);
    }

    public static byte getByteFromEigthBitsString(String str) {
        // if(str.length()!=8)
        // throw new Exception("It's not a 8 length string");
        byte b;
        // check if it's a minus number
        if (str.substring(0, 1).equals("1")) {
            // get lower 7 digits original code
            str = "0" + str.substring(1);
            b = Byte.valueOf(str, 2);
            // then recover the 8th digit as 1 equal to plus
            // 1000000
            b |= 128;
        } else {
            b = Byte.valueOf(str, 2);
        }
        return b;
    }

    /**
     * 将一个8/16字节数组转成128二进制数组
     *
     * @param b
     * @return
     */
    public static boolean[] getBinaryFromByte(byte[] b) {
        boolean[] binary = new boolean[b.length * 8 + 1];
        String strsum = "";
        for (int i = 0; i < b.length; i++) {
            strsum += getEigthBitsStringFromByte(b[i]);
        }
        for (int i = 0; i < strsum.length(); i++) {
            if (strsum.substring(i, i + 1).equalsIgnoreCase("1")) {
                binary[i + 1] = true;
            } else {
                binary[i + 1] = false;
            }
        }
        return binary;
    }

    /**
     * 将一个128二进制数组转成16字节数组
     *
     * @param binary
     * @return
     */
    public static byte[] getByteFromBinary(boolean[] binary) {
        int num = (binary.length - 1) / 8;
        if (((binary.length - 1) % 8) != 0) {
            num = num + 1;
        }

        byte[] b = new byte[num];
        String s = "";
        for (int i = 1; i < binary.length; i++) {
            if (binary[i]) {
                s += "1";
            } else {
                s += "0";
            }
        }

        String tmpstr;
        int j = 0;
        for (int i = 0; i < s.length(); i = i + 8) {
            tmpstr = s.substring(i, i + 8);
            b[j] = getByteFromEigthBitsString(tmpstr);
            j = j + 1;
        }

        return b;
    }

    /**
     * 将一个byte位图转成字符串
     *
     * @param b
     * @return
     */
    public static String getStrFromBitMap(byte[] b) {
        String strsum = "";
        for (int i = 0; i < b.length; i++) {
            strsum += getEigthBitsStringFromByte(b[i]);
        }
        return strsum;
    }

    /**
     * bytes转换成十六进制字符串
     *
     * @param b
     * @return
     */
    public static String byte2HexStr(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0xFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase();
    }

    private static byte uniteBytes(String src0, String src1) {
        byte b0 = Byte.decode("0x" + src0).byteValue();
        b0 = (byte) (b0 << 4);
        byte b1 = Byte.decode("0x" + src1).byteValue();
        byte ret = (byte) (b0 | b1);

        return ret;
    }

    /**
     * 十六进制字符串转换成bytes
     *
     * @param src
     * @return
     */
    public static byte[] hexStr2Bytes(String src) {
        int m = 0, n = 0;
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            m = i * 2 + 1;
            n = m + 1;
            ret[i] = uniteBytes(src.substring(i * 2, m),
                    src.substring(m, n));
        }
        return ret;
    }

    /**
     * 将String转成BCD码
     *
     * @param s
     * @return
     */
    public static byte[] StrToBCDBytes(String s) {
        if (s.length() % 2 != 0) {
            s = "0" + s;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        char[] cs = s.toCharArray();
        for (int i = 0; i < cs.length; i += 2) {
            int high = cs[i] - 48;
            int low = cs[i + 1] - 48;
            baos.write(high << 4 | low);
        }

        return baos.toByteArray();
    }

    /**
     * @功能: 10进制串转为BCD码
     * @参数: 10进制串
     * @结果: BCD码
     */
    public static byte[] str2Bcd(String asc) {
        int len = asc.length();
        int mod = len % 2;
        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }
        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }
        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;
        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }
            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }
            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }

    /**
     * 将BCD码转成int
     *
     * @param b
     * @return
     */
    public static int bcdToint(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            int h = ((b[i] & 0xF0) >> 4) + 48;
            sb.append((char) h);
            int l = (b[i] & 0x0F) + 48;
            sb.append((char) l);
        }
        return Integer.parseInt(sb.toString());
    }
}
