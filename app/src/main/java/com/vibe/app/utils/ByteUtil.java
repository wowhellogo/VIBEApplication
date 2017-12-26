package com.vibe.app.utils;

import java.nio.charset.Charset;

/**
 * Created by Ike on 2017/4/1 0001.
 */

public class ByteUtil {
    //Hton big port,Ntoh small port

    public static byte[] getBytesNtoh(short data) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        return bytes;
    }

    public static byte[] getBytesHton(short data) {
        byte[] bytes = new byte[2];
        bytes[1] = (byte) (data & 0xff);
        bytes[0] = (byte) ((data & 0xff00) >> 8);
        return bytes;
    }

    public static byte[] getBytesNtoh(char data) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data);
        bytes[1] = (byte) (data >> 8);
        return bytes;
    }

    public static byte[] getBytesHton(char data) {
        byte[] bytes = new byte[2];
        bytes[1] = (byte) (data);
        bytes[0] = (byte) (data >> 8);
        return bytes;
    }

    public static byte[] getBytesNtoh(int data) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        bytes[2] = (byte) ((data & 0xff0000) >> 16);
        bytes[3] = (byte) ((data & 0xff000000) >> 24);
        return bytes;
    }

    public static byte[] getBytesHton(int data) {
        byte[] bytes = new byte[4];
        bytes[3] = (byte) (data & 0xff);
        bytes[2] = (byte) ((data & 0xff00) >> 8);
        bytes[1] = (byte) ((data & 0xff0000) >> 16);
        bytes[0] = (byte) ((data & 0xff000000) >> 24);
        return bytes;
    }

    public static byte[] getBytesNtoh(long data) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data >> 8) & 0xff);
        bytes[2] = (byte) ((data >> 16) & 0xff);
        bytes[3] = (byte) ((data >> 24) & 0xff);
        bytes[4] = (byte) ((data >> 32) & 0xff);
        bytes[5] = (byte) ((data >> 40) & 0xff);
        bytes[6] = (byte) ((data >> 48) & 0xff);
        bytes[7] = (byte) ((data >> 56) & 0xff);
        return bytes;
    }

    public static byte[] getBytesHton(long data) {
        byte[] bytes = new byte[8];
        bytes[7] = (byte) (data & 0xff);
        bytes[6] = (byte) ((data >> 8) & 0xff);
        bytes[5] = (byte) ((data >> 16) & 0xff);
        bytes[4] = (byte) ((data >> 24) & 0xff);
        bytes[3] = (byte) ((data >> 32) & 0xff);
        bytes[2] = (byte) ((data >> 40) & 0xff);
        bytes[1] = (byte) ((data >> 48) & 0xff);
        bytes[0] = (byte) ((data >> 56) & 0xff);
        return bytes;
    }

    public static byte[] getBytesNtoh(float data) {
        int intBits = Float.floatToIntBits(data);
        return getBytesNtoh(intBits);
    }

    public static byte[] getBytesNtoh(double data) {
        long intBits = Double.doubleToLongBits(data);
        return getBytesNtoh(intBits);
    }

    public static byte[] getStringBytes(String data, String charsetName) {
        Charset charset = Charset.forName(charsetName);
        return data.getBytes(charset);
    }

    /**
     * get GBK string bytes
     *
     * @param data
     * @return
     */
    public static byte[] getStringBytes(String data) {
        return getStringBytes(data, "GBK");
    }


    public static short getShortNtoj(byte[] bytes) {
        return (short) ((0xff & bytes[1]) | (0xff00 & (bytes[0] << 8)));
    }

    public static short getShortHtoj(byte[] bytes) {
        return (short) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }

    public static short getShortNtoj(byte[] bytes, int offset) {
        return (short) ((0xff & bytes[offset + 1]) | (0xff00 & (bytes[offset] << 8)));
    }

    public static short getShortHtoj(byte[] bytes, int offset) {
        return (short) ((0xff & bytes[offset]) | (0xff00 & (bytes[offset + 1] << 8)));
    }

    public static char getCharNtoj(byte[] bytes) {
        return (char) ((0xff & bytes[1]) | (0xff00 & (bytes[0] << 8)));
    }

    public static char getCharHtoj(byte[] bytes) {
        return (char) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }

    public static int getIntNtoj(byte[] bytes) {
        return (0xff & bytes[3]) | (0xff00 & (bytes[2] << 8)) | (0xff0000 & (bytes[1] << 16)) | (0xff000000 & (bytes[0] << 24));
    }

    public static int getIntHtoj(byte[] bytes) {
        return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 & (bytes[2] << 16)) | (0xff000000 & (bytes[3] << 24));
    }

    public static int getIntNtoj(byte[] bytes, int offset) {
        return (0xff & bytes[offset + 3]) | (0xff00 & (bytes[offset + 2] << 8)) | (0xff0000 & (bytes[offset + 1] << 16)) | (0xff000000 & (bytes[offset] << 24));
    }

    public static int getIntHtoj(byte[] bytes, int offset) {
        return (0xff & bytes[offset]) | (0xff00 & (bytes[offset + 1] << 8)) | (0xff0000 & (bytes[offset + 2] << 16)) | (0xff000000 & (bytes[offset + 3] << 24));
    }

    public static long getLongNtoj(byte[] bytes) {
        return (0xffL & (long) bytes[7]) | (0xff00L & ((long) bytes[6] << 8)) | (0xff0000L & ((long) bytes[5] << 16)) | (0xff000000L & ((long) bytes[4] << 24))
                | (0xff00000000L & ((long) bytes[3] << 32)) | (0xff0000000000L & ((long) bytes[2] << 40)) | (0xff000000000000L & ((long) bytes[1] << 48)) | (0xff00000000000000L & ((long) bytes[0] << 56));
    }

    public static long getLongHtoj(byte[] bytes) {
        return (0xffL & (long) bytes[0]) | (0xff00L & ((long) bytes[1] << 8)) | (0xff0000L & ((long) bytes[2] << 16)) | (0xff000000L & ((long) bytes[3] << 24))
                | (0xff00000000L & ((long) bytes[4] << 32)) | (0xff0000000000L & ((long) bytes[5] << 40)) | (0xff000000000000L & ((long) bytes[6] << 48)) | (0xff00000000000000L & ((long) bytes[7] << 56));
    }

    public static float getFloatNtoj(byte[] bytes) {
        return Float.intBitsToFloat(getIntNtoj(bytes));
    }

    public static float getFloatHtoj(byte[] bytes) {
        return Float.intBitsToFloat(getIntHtoj(bytes));
    }

    public static double getDoubleNtoj(byte[] bytes) {
        long l = getLongNtoj(bytes);
        System.out.println(l);
        return Double.longBitsToDouble(l);
    }

    public static double getDoubleHtoj(byte[] bytes) {
        long l = getLongHtoj(bytes);
        System.out.println(l);
        return Double.longBitsToDouble(l);
    }

    public static String getString(byte[] bytes, String charsetName) {
        return new String(bytes, Charset.forName(charsetName));
    }

    public static String getString(byte[] bytes) {
        return getString(bytes, "GBK");
    }

    /**
     * float to small byte[]
     *
     * @param f
     * @return
     */
    public static byte[] float2byteNtoh(float f) {

        //float to byte[]
        int fbit = Float.floatToIntBits(f);

        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (fbit >> (24 - i * 8));
        }

        //change byte[] position i and len - i
        int len = b.length;
        //for copy
        byte[] dest = new byte[len];
        //
        System.arraycopy(b, 0, dest, 0, len);
        byte temp;
        //change i and len-i position
        for (int i = 0; i < len / 2; ++i) {
            temp = dest[i];
            dest[i] = dest[len - i - 1];
            dest[len - i - 1] = temp;
        }

        return dest;

    }

    /**
     * float to big byte[]
     *
     * @param f
     * @return
     */
    public static byte[] float2byteHton(float f) {

        // 把float转换为byte[]
        int fbit = Float.floatToIntBits(f);

        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (fbit >> (24 - i * 8));
        }
        return b;
    }

    /**
     * small byte[]to java float
     *
     * @param b     at lease 4 byte
     * @param index start index
     * @return
     */
    public static float byte2floatHtoj(byte[] b, int index) {
        int l;
        l = b[index + 0];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }

    /**
     * big byte[]to java float
     *
     * @param b
     * @param index
     * @return
     */
    public static float byte2floatNtoj(byte[] b, int index) {
        int l;
        l = b[index + 3];
        l &= 0xff;
        l |= ((long) b[index + 2] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 1] << 16);
        l &= 0xffffff;
        l |= ((long) b[index] << 24);
        return Float.intBitsToFloat(l);
    }

    public static String bytesToHexString(byte[] b) {
        String returnstr = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            returnstr += " " + hex;
        }
        return returnstr.toUpperCase();
    }

    public static byte[] getBytes(int b, int b1) {
        byte[] bs = new byte[2];
        bs[0] = (byte) b;
        bs[1] = (byte) b1;
        return bs;
    }

}
