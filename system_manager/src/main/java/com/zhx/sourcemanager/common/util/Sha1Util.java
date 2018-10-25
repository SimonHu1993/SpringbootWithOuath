package com.zhx.sourcemanager.common.util;

import java.security.MessageDigest;

/**
 * Created by mulder on 2015/9/23.
 */
public class Sha1Util {

    private static final String ALGORITHM_SHA1 = "SHA1";
    private static final String ALGORITHM_SHA256 = "SHA-256";
    private static final String ALGORITHM_SHA512 = "SHA-512";

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    /**
     * sha-512
     * @param str   加密字符串
     * @param salt  key
     * @return
     */
    public static String encodeSha512(String str, String salt) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM_SHA512);
            messageDigest.update((str + salt).getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * sha-256
     * @param str   加密字符串
     * @param salt  key
     * @return
     */
    public static String encodeSha256(String str, String salt) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM_SHA256);
            messageDigest.update((str + salt).getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * sha-1
     * @param str   加密字符串
     * @param salt  key
     * @return
     */
    public static String encodeSha1(String str, String salt) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM_SHA1);
            messageDigest.update((str + salt).getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }


    public static void main(String[] args) {
        System.out.println("111111 SHA1 :" + Sha1Util.encodeSha1("ip%3D10.5.17.221%26os%3D3%26mac%3DAC-FD-FS-DF-EQ-UI-OI%26area%3D110100%26imei%3D2343243254345758%26phone%3D13716949754%26userId%3D1000133%26phoneNo%3D13716949754%26queryid%3D92f3a950-b8c7-4059-a%26deviceId%3D4654sadsadasfsdfafds%26operType%3D300%26timespan%3D20151027203522%26androidId%3D2314324324234%26phoneModel%3Dsmartisan+T1123456", ""));

        System.out.println("111111 SHA1 :" + Sha1Util.encodeSha256("123456", "#$%RFVtgb^&*("));
    }


}
