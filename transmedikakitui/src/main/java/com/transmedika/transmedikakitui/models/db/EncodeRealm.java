package com.transmedika.transmedikakitui.models.db;

import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncodeRealm {
    private static final String TAG = EncodeRealm.class.getSimpleName();
    public static final String METODE_ENCRYPTION = "AES";
    static final String KEY = "oWdYnt0Wh1t3Op3n"; // 16 bit key
    public static final String KEY64 = "Jdids37hsKsudhd79sjdjdya7a99ekdkdhfhss7shaakziaus"; // 64 bytes

    public static byte[] encrypt(String strClearText){
        byte[] encrypted = new byte[0];
        try {
            Key key = generateKey();
            Cipher cipher=Cipher.getInstance(METODE_ENCRYPTION);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encrypted = cipher.doFinal(strClearText.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypted;
    }

    private static Key generateKey() {
        Key key = null;
        try {
            byte[] keyValue = KEY.getBytes(StandardCharsets.UTF_8);
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            keyValue = sha.digest(keyValue);
            keyValue = Arrays.copyOf(keyValue, 16); // ic_16 byte
            key = new SecretKeySpec(keyValue, METODE_ENCRYPTION);
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
        return key;
    }
}
