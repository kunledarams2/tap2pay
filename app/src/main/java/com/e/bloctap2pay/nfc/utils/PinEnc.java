package com.e.bloctap2pay.nfc.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class PinEnc {


    private static byte[] encHexStringToByteArray(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];

        for(int i = 0; i < len; i += 2) {
            data[i / 2] = (byte)((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        }

        return data;
    }

    public static String encryptData(String desKey, String clrPinBlockStr) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] keyBytes = encHexStringToByteArray(desKey);
        byte[] pinBlockBytes = encHexStringToByteArray(clrPinBlockStr);
        Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
        cipher.init(1, new SecretKeySpec(keyBytes, "DESede"));
        byte[] encBytes = cipher.doFinal(pinBlockBytes);
        return encBytesToHexString(encBytes);
    }

    private static int fromHex(char c) {
        if (c >= '0' && c <= '9') {
            return c - 48;
        } else if (c >= 'A' && c <= 'F') {
            return c - 65 + 10;
        } else if (c >= 'a' && c <= 'f') {
            return c - 97 + 10;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static char toHex(int nybble) {
        if (nybble >= 0 && nybble <= 15) {
            return "0123456789ABCDEF".charAt(nybble);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static String encBytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        byte[] var2 = bytes;
        int var3 = bytes.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            byte aByte = var2[var4];
            sb.append(String.format("%02X", aByte));
        }

        return sb.toString();
    }

    public static String decryptData(String pinKeyStr, String encData) throws Exception {
        byte[] encryptedData = encHexStringToByteArray(encData);
        byte[] keyBytes = encHexStringToByteArray(pinKeyStr);
        Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
        cipher.init(2, new SecretKeySpec(keyBytes, "DESede"));
        String restoredPinBlock = encBytesToHexString(cipher.doFinal(encryptedData));
        return restoredPinBlock;
    }

    private static String xorHex(String a, String b) {
        char[] chars = new char[a.length()];

        for(int i = 0; i < chars.length; ++i) {
            chars[i] = toHex(fromHex(a.charAt(i)) ^ fromHex(b.charAt(i)));
        }

        return (new String(chars)).toUpperCase();
    }

    public static String encryptPinBlock(String pin, String cardNumber, String pinKeyStr) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if (pin.length() >= 4 && pin.length() <= 6) {
            String pinBlock;
            for(pinBlock = String.format("%s%d%s", "0", pin.length(), pin); pinBlock.length() != 16; pinBlock = pinBlock + "F") {
            }

            int cardLen = cardNumber.length();
            String pan = "0000" + cardNumber.substring(cardLen - 13, cardLen - 1);
            String clrPinBlock = xorHex(pinBlock, pan);
            String encPinBlock = encryptData(pinKeyStr, clrPinBlock);
            return encPinBlock;
        } else {
            return "";
        }
    }

}
