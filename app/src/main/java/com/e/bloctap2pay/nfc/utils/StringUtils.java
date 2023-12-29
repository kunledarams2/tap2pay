package com.e.bloctap2pay.nfc.utils;

import androidx.annotation.Keep;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

@Keep
public class StringUtils {
    public static String processPan(String pan) {
        String acqCode = pan.substring(0, 6);
        String endCode = pan.substring(pan.length() - 4, pan.length());

        int elen = pan.length() - acqCode.length() - endCode.length();

        String etext = padLeft("*", elen, '*');

        String newPan = acqCode + etext + endCode;

        return newPan;
    }

    public static String padLeft(String data, int length, char padChar) {
        int remaining = length - data.length();

        String newData = data;
        for (int i = 0; i < remaining; i++)
            newData = padChar + newData;
        return newData;
    }

    public static String hexStringToUTF8String(String data) {
        ByteBuffer buff = ByteBuffer.allocate(data.length() / 2);
        for (int i = 0; i < data.length(); i += 2) {
            buff.put((byte) Integer.parseInt(data.substring(i, i + 2), 16));
        }
        buff.rewind();
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = cs.decode(buff);
        return cb.toString();
    }

}
