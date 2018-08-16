package com.iot.nero.smartcan.utils.encryption;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * RSA加密解密工具.
 * <p>
 * 密钥长度是1024比特, 随机生成. Cipher transformation是RSA/ECB/PKCS1Padding。
 * 
 * @author nerosoft@outlook.com
 */
public class RsaUtility {

    private static final String SECRET_KEY_ALGORITHM = "RSA";
    private static final int SECRET_KEY_SIZE = 1024;
    private static final String ENCRYPTION_ALGORITHM = "RSA/ECB/PKCS1Padding";

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(SECRET_KEY_ALGORITHM);
        SecureRandom sr = new SecureRandom();
        kpg.initialize(SECRET_KEY_SIZE, sr);
        KeyPair kp = kpg.generateKeyPair();
        return kp;
    }

    public static PublicKey getPublicKey(KeyPair kp) {
        return kp.getPublic();
    }

    public static PrivateKey getPrivateKey(KeyPair kp) {
        return kp.getPrivate();
    }

    public static PublicKey toPublicKey(byte[] publicKeyByteArray) throws Exception {
        X509EncodedKeySpec ks = new X509EncodedKeySpec(publicKeyByteArray);
        return KeyFactory.getInstance(SECRET_KEY_ALGORITHM).generatePublic(ks);
    }

    public static PrivateKey toPrivateKey(byte[] privateKeyByteArray) throws Exception {
        PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(privateKeyByteArray);
        return KeyFactory.getInstance(SECRET_KEY_ALGORITHM).generatePrivate(ks);
    }

    private static Cipher generateCipher(int mode, Key key) throws Exception {
        Cipher c = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        c.init(mode, key);
        return c;
    }

    private static byte[] process(int mode, Key key, byte[] data) throws Exception {
        Cipher c = generateCipher(mode, key);
        return c.doFinal(data);
    }

    public static byte[] encrypt(PublicKey publicKey, byte[] plaintext) throws Exception {
        return process(Cipher.ENCRYPT_MODE, publicKey, plaintext);
    }

    public static byte[] decrypt(PrivateKey privateKey, byte[] ciphertext) throws Exception {
        return process(Cipher.DECRYPT_MODE, privateKey, ciphertext);
    }
}
