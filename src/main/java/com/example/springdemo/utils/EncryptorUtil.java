package com.example.springdemo.utils;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;


public class EncryptorUtil {

    private static final StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

    public static String encrypt(String data, String password) {
        if (!encryptor.isInitialized()) {
            encryptor.setPassword(password);
        }
        return encryptor.encrypt(data);
    }

    public static String decrypt(String data, String password) {
        if (!encryptor.isInitialized()) {
            encryptor.setPassword(password);
        }
        return encryptor.decrypt(data);
    }

}
