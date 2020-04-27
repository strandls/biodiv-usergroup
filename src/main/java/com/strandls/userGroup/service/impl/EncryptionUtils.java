package com.strandls.userGroup.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Abhishek Rudra
 *
 */

public class EncryptionUtils {

	private final Logger logger = LoggerFactory.getLogger(EncryptionUtils.class);

	private String key;

	public EncryptionUtils() {
		try {
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			Properties properties = new Properties();
			try {
				properties.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			}

			key = properties.getProperty("encryptKey");

			in.close();

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	public String encrypt(String plainText) {
		String strData = "";

		try {
			SecretKeySpec skeyspec = new SecretKeySpec(key.getBytes(), "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
			byte[] encrypted = cipher.doFinal(plainText.getBytes());
			strData = new String(encrypted);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return strData;
	}

	public String decrypt(String encryptedText) {
		String strData = "";

		try {
			SecretKeySpec skeyspec = new SecretKeySpec(key.getBytes(), "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.DECRYPT_MODE, skeyspec);
			byte[] decrypted = cipher.doFinal(encryptedText.getBytes());
			strData = new String(decrypted);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return strData;
	}

}
