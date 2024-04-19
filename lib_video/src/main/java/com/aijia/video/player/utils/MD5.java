
package com.aijia.video.player.utils;


import java.math.BigInteger;
import java.security.MessageDigest;


public class MD5 {

    public static String getMD5(String s) {
        if (s == null) return "";
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(s.getBytes());
            return (new BigInteger(1, md.digest())).toString(16);
        } catch (Exception e) {
            return result;
        }
    }
}
