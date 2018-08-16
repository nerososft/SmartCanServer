package com.iot.nero.smartcan.utils.encryption;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密解密工具.
 * <p>
 * 密钥长度是128比特, 随机生成. Cipher transformation是AES/ECB/PKCS5Padding.
 * 
 * @author nerosoft@outlook.com
 */
public class AesUtility {

    private static final String SECRET_KEY_ALGORITHM = "AES";
    private static final int SECRET_KEY_SIZE = 128;
    private static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * 获取密钥.
     * 
     * @return 字节数组.
     * @throws Exception
     */
    public static byte[] generateSecretKey() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(SECRET_KEY_ALGORITHM);
        SecureRandom sr = new SecureRandom();
        kg.init(SECRET_KEY_SIZE, sr);
        SecretKey sk = kg.generateKey();
        return sk.getEncoded();
    }

    public static Key toKey(byte[] secretKey) {
        SecretKey sk = new SecretKeySpec(secretKey, SECRET_KEY_ALGORITHM);
        return sk;
    }

    private static Cipher generateCipher(int mode, byte[] secretKey) throws Exception {
        Key k = toKey(secretKey);
        Cipher c = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        c.init(mode, k, new IvParameterSpec(new byte[c.getBlockSize()]));
        return c;
    }

    private static byte[] process(int mode, byte[] secretKey, byte[] data) throws Exception {
        Cipher c = generateCipher(mode, secretKey);
        return c.doFinal(data);
    }

    /**
     * 加密字节数组.
     * 
     * @param secretKey 密钥的字节数组.
     * @param plaintext 明文的字节数组.
     * @return 密文的字节数组.
     * @throws Exception
     */
    public static byte[] encrypt(byte[] secretKey, byte[] plaintext) throws Exception {
        return process(Cipher.ENCRYPT_MODE, secretKey, plaintext);
    }

    /**
     * 解密字节数组.
     *
     * @param secretKey  密钥的字节数组.
     * @param ciphertext 密文的字节数组.
     * @return 明文的字节数组.
     * @throws Exception
     */
    public static byte[] decrypt(byte[] secretKey, byte[] ciphertext) throws Exception {
        return process(Cipher.DECRYPT_MODE, secretKey, ciphertext);
    }
}
