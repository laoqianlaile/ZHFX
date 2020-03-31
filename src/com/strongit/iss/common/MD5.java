package com.strongit.iss.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.Random;

/**
 * MD5加密
 *
 */
public class MD5 {
	private static Logger logger = Logger.getLogger(MD5.class);

	/**
	 * MD5加密算法
	 *
	 * @param text
	 *            String 加密文本
	 * @return
	 */
	public static String encode(String text){
		return encode(text,32);
	}
	/**
	 * MD5加密算法
	 *
	 * @param text
	 *            String 加密文本
	 * @param encodeType
	 *            int 加密位数（16：16位加密，32：32位加密）
	 * @return
	 */
	public static String encode(String text, int encodeType) {
		if (StringUtils.isBlank(text)) {
			return null;
		}

		String result = null;

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(text.getBytes());
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

			switch (encodeType) {
				case 16: // md5 16bit
					result = buf.toString().substring(10, 26);
					break;
				case 32: // md5 32bit
					result = buf.toString();
					break;
				default: // md5 16bit
					result = buf.toString().substring(10, 26);
					break;
			}

		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(),e);
		}

		return result;
	}

	public static String genRandomNum(int pwd_len) {
		// 35是因为数组是从0开始的，26个字母+10个数字
		final int maxNum = 36;
		int i; // 生成的随机数
		int count = 0; // 生成的密码的长度
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < pwd_len) {
			// 生成随机数，取绝对值，防止生成负数，

			i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1

			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}

		return pwd.toString();
	}
}
