package com.ccx.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * MD5 32λ����
 * @classname MD5
 * @author lilin
 * @date 2016��11��10�� ����11:36:15
 * @version
 */
public class MD5 {
	
    /**
    *
    * @param plainText
    *            ����
    * @return 32λ����
    */
   public static String encryption(String plainText) {
       String re_md5 = new String();
       try {
           MessageDigest md = MessageDigest.getInstance("MD5");
			try {
				md.update(plainText.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
           byte b[] = md.digest();
           int i;
           StringBuffer buf = new StringBuffer("");
           for (int offset = 0; offset < b.length; offset++) {
               i = b[offset];
               if (i < 0)
                   i += 256;
               if (i < 16)
                   buf.append("0");
               buf.append(Integer.toHexString(i));
           }
           re_md5 = buf.toString();
       } catch (NoSuchAlgorithmException e) {
           e.printStackTrace();
       }
       return re_md5;
   }
  public static void main(String[] args) {
	System.out.println(encryption("zhengpingABC"));
}
}
