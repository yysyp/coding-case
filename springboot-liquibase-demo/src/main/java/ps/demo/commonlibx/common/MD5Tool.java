/**Copyright CopyLeft it's Fri Feb 28 23:46:35 CST 2020 Good luck~~~*/
package ps.demo.commonlibx.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

public class MD5Tool {

    /**
     * hex digits
     */
    private final static String[] hexDigitsStrings = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
            "e", "f"};

    /**
     * hex digits
     */
    private final static char[] hexDigitsChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * MD5 encryption string
     *
     * @param source source string
     * @return encrypted string
     */
    public static String getMD5(String source) {
        String mdString = null;
        if (source != null) {
            try {
                mdString = getMD5(source.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return mdString;
    }

    /**
     * MD5 encrypt byte array
     *
     * @param source byte array
     * @return encrypted string
     */
    public static String getMD5(byte[] source) {
        String s = null;

        final int temp = 0xf;
        final int arraySize = 32;
        final int strLen = 16;
        final int offset = 4;
        try {
            MessageDigest md = MessageDigest
                    .getInstance("MD5");
            md.update(source);
            byte[] tmp = md.digest();
            char[] str = new char[arraySize];
            int k = 0;
            for (int i = 0; i < strLen; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigitsChar[byte0 >>> offset & temp];
                str[k++] = hexDigitsChar[byte0 & temp];
            }
            s = new String(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * * get the md5 of a file
     *
     * @param file source file
     * @return MD5 string
     * @throws Exception
     */
    public static String getFileMD5String(File file) throws Exception {
        String ret = "";
        FileInputStream in = null;
        FileChannel ch = null;
        try {
            in = new FileInputStream(file);
            ch = in.getChannel();
            ByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,
                    file.length());
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(byteBuffer);
            ret = byteArrayToHexString(messageDigest.digest());
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ch != null) {
                try {
                    ch.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    /**
     * * get md5 for a file
     *
     * @param fileName file full path and name
     * @return MD5 string
     * @throws Exception
     */
    public static String getFileMD5String(String fileName) throws Exception {
        return getFileMD5String(new File(fileName));
    }

    /**
     * Encryption
     *
     * @param source    plain string
     * @param encoding  encoding type
     * @param uppercase Convert to uppercase?
     * @return
     */
    public static String MD5Encode(String source, String encoding, boolean uppercase) {
        String result = null;
        try {
            result = source;
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(result.getBytes(encoding));
            result = byteArrayToHexString(messageDigest.digest());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return uppercase ? result.toUpperCase() : result;
    }


    /**
     * Convert to hex digits string
     *
     * @param bytes byte array
     * @return
     */
    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte tem : bytes) {
            stringBuilder.append(byteToHexString(tem));
        }
        return stringBuilder.toString();
    }

    /**
     * * Convert specified sub array to hex string
     *
     * @param bytes byte array
     * @param start start index (included)
     * @param end   end index (excluded)
     * @return convert result
     */
    public static String bytesToHex(byte bytes[], int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < start + end; i++) {
            sb.append(byteToHexString(bytes[i]));
        }
        return sb.toString();
    }

    /**
     * convert type to hex
     *
     * @param b byte
     * @return hex string
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigitsStrings[d1] + hexDigitsStrings[d2];
    }


    /**
     * * verify password against its MD5
     *
     * @param pwd password string
     * @param md5 md5
     * @return is match?
     */
    public static boolean checkPassword(String pwd, String md5) {
        return getMD5(pwd).equalsIgnoreCase(md5);
    }

    /**
     * * verify password against md5
     *
     * @param pwd password string
     * @param md5 md5
     * @return is match?
     */
    public static boolean checkPassword(char[] pwd, String md5) {
        return checkPassword(new String(pwd), md5);
    }


    /**
     * * check md5 of a file
     *
     * @param file target file
     * @param md5  md5
     * @return is match?
     * @throws Exception
     */
    public static boolean checkFileMD5(File file, String md5) throws Exception {
        return getFileMD5String(file).equalsIgnoreCase(md5);
    }

    /**
     * * check file md5
     *
     * @param fileName file path
     * @param md5      md5
     * @return result
     * @throws Exception
     */
    public static boolean checkFileMD5(String fileName, String md5) throws Exception {
        return checkFileMD5(new File(fileName), md5);
    }
}
