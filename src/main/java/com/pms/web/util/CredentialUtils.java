package com.pms.web.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class CredentialUtils {


	public static String encrypt(final String message) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(message.getBytes());

		byte byteData[] = md.digest();

		//convert the byte to hex format method 1
		StringBuffer sb = new StringBuffer();
		for (byte element : byteData) {
			sb.append(Integer.toString((element & 0xff) + 0x100, 16).substring(1));
		}

		System.out.println("Hex format : " + sb.toString());

		//convert the byte to hex format method 2
		StringBuffer hexString = new StringBuffer();
		for (byte element : byteData) {
			String hex=Integer.toHexString(0xff & element);
			if(hex.length()==1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		System.out.println("Hex format : " + hexString.toString());
		return hexString.toString();
	}

	public static String decrypt(final byte[] message) throws Exception {
		final MessageDigest md = MessageDigest.getInstance("md5");
		final byte[] digestOfPassword = md.digest("HG58YZ3CR9"
				.getBytes("utf-8"));
		final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
		for (int j = 0, k = 16; j < 8;) {
			keyBytes[k++] = keyBytes[j++];
		}

		final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
		final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
		final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		decipher.init(Cipher.DECRYPT_MODE, key, iv);

		// final byte[] encData = new
		// sun.misc.BASE64Decoder().decodeBuffer(message);
		final byte[] plainText = decipher.doFinal(message);

		return new String(plainText, "UTF-8");
	}

	public static String encryptPassword(final String userCredentials) throws NoSuchAlgorithmException, InvalidKeySpecException  {
		String generatedSecuredPasswordHash = generateStorngPasswordHash(userCredentials);
		System.out.println(generatedSecuredPasswordHash);

		boolean matched = validatePassword(userCredentials, generatedSecuredPasswordHash);
		System.out.println(matched);

		//matched = validatePassword("password1", generatedSecuredPasswordHash);
		//System.out.println(matched);
		return generatedSecuredPasswordHash;
	}

	public static boolean validatePassword(final String originalPassword, final String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		String[] parts = storedPassword.split(":");
		int iterations = Integer.parseInt(parts[0]);
		byte[] salt = fromHex(parts[1]);
		byte[] hash = fromHex(parts[2]);

		PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);

		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

		byte[] testHash = skf.generateSecret(spec).getEncoded();

		int diff = hash.length ^ testHash.length;

		for(int i = 0; i < hash.length && i < testHash.length; i++)
		{
			diff |= hash[i] ^ testHash[i];
		}

		return diff == 0;
	}



	public static String generateStorngPasswordHash(final String password) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		int iterations = 1000;
		char[] chars = password.toCharArray();
		byte[] salt = getSalt().getBytes();

		PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] hash = skf.generateSecret(spec).getEncoded();
		return iterations + ":" + toHex(salt) + ":" + toHex(hash);

	}

	private static String getSalt() throws NoSuchAlgorithmException
	{
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt.toString();
	}

	private static String toHex(final byte[] array) throws NoSuchAlgorithmException
	{
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
		if(paddingLength > 0)
		{
			return String.format("%0"  +paddingLength + "d", 0) + hex;
		}else{
			return hex;
		}
	}

	private static byte[] fromHex(final String hex) throws NoSuchAlgorithmException
	{
		byte[] bytes = new byte[hex.length() / 2];
		for(int i = 0; i<bytes.length ;i++)
		{
			bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;
	}

	public static void main(final String [] args) throws NoSuchAlgorithmException, InvalidKeySpecException{
		CredentialUtils.encryptPassword("swadhin4");
	}
}
