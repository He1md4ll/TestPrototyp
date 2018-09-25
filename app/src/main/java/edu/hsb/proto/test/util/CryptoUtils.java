package edu.hsb.proto.test.util;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtils {

    private static final char HEX_DIGITS[] =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String encryptSHA512ToString(final String data) {
        if (data == null || data.length() == 0) return "";
        return encryptSHA512ToString(data.getBytes());
    }

    public static String encryptSHA512ToString(final byte[] data) {
        return bytes2HexString(encryptSHA512(data));
    }

    public static byte[] encryptSHA512(final byte[] data) {
        return hashTemplate(data, "SHA-512");
    }

    private static byte[] hashTemplate(final byte[] data, final String algorithm) {
        if (data == null || data.length <= 0) return null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encryptHmacSHA512ToString(final String data, final String key) {
        if (data == null || data.length() == 0 || key == null || key.length() == 0) return "";
        return encryptHmacSHA512ToString(data.getBytes(), key.getBytes());
    }

    public static String encryptHmacSHA512ToString(final byte[] data, final byte[] key) {
        return bytes2HexString(encryptHmacSHA512(data, key));
    }

    public static byte[] encryptHmacSHA512(final byte[] data, final byte[] key) {
        return hmacTemplate(data, key, "HmacSHA512");
    }

    private static byte[] hmacTemplate(final byte[] data,
                                       final byte[] key,
                                       final String algorithm) {
        if (data == null || data.length == 0 || key == null || key.length == 0) return null;
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key, algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(secretKey);
            return mac.doFinal(data);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String bytes2HexString(final byte[] bytes) {
        if (bytes == null) return "";
        int len = bytes.length;
        if (len <= 0) return "";
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = HEX_DIGITS[bytes[i] >> 4 & 0x0f];
            ret[j++] = HEX_DIGITS[bytes[i] & 0x0f];
        }
        return new String(ret);
    }
}