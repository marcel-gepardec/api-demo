package com.example.apidemo.domain.utils;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

public abstract class KeyUtils {

    public static PrivateKey getPrivateKey(String filename)
            throws Exception {

        byte[] keyBytes = Objects.requireNonNull(KeyUtils.class.getResourceAsStream("/RSA/" + filename)).readAllBytes();

        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public static PublicKey getPublicKey(String filename)
            throws Exception {

        byte[] keyBytes = Objects.requireNonNull(KeyUtils.class.getResourceAsStream("/RSA/" + filename)).readAllBytes();

        X509EncodedKeySpec spec =
                new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }
}
