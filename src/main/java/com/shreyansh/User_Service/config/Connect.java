package com.shreyansh.User_Service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.KeyFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
@Component
public class Connect {
    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${spring.datasource.username}")
    private String databaseUsername;

    @Value("${spring.datasource.password}")
    private String databasePassword;

    @Value("${jwt.access.private.key}")
    private String accessPrivateKeyString;

    @Value("${jwt.access.public.key}")
    private String accessPublicKeyString;

    @Value("${jwt.refresh.private.key}")
    private String refreshPrivateKeyString;

    @Value("${jwt.refresh.public.key}")
    private String refreshPublicKeyString;

    @Value("${jwt.refresh.expiry}")
    private String refreshTokenExpiry;

    @Value("${jwt.access.expiry}")
    private String accessTokenExpiry;

    private ECPrivateKey accessPrivateKey;
    private ECPublicKey accessPublicKey;
    private ECPrivateKey refreshPrivateKey;
    private ECPublicKey refreshPublicKey;

    @PostConstruct
    public void init() {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            
            String accessPrivateKeyContent = extractBase64FromPEM(accessPrivateKeyString);
            String accessPublicKeyContent = extractBase64FromPEM(accessPublicKeyString);
            String refreshPrivateKeyContent = extractBase64FromPEM(refreshPrivateKeyString);
            String refreshPublicKeyContent = extractBase64FromPEM(refreshPublicKeyString);

            // Convert keys
            accessPrivateKey = (ECPrivateKey) keyFactory.generatePrivate(
                new PKCS8EncodedKeySpec(Base64.getDecoder().decode(accessPrivateKeyContent)));
            
            accessPublicKey = (ECPublicKey) keyFactory.generatePublic(
                new X509EncodedKeySpec(Base64.getDecoder().decode(accessPublicKeyContent)));
            
            refreshPrivateKey = (ECPrivateKey) keyFactory.generatePrivate(
                new PKCS8EncodedKeySpec(Base64.getDecoder().decode(refreshPrivateKeyContent)));
            
            refreshPublicKey = (ECPublicKey) keyFactory.generatePublic(
                new X509EncodedKeySpec(Base64.getDecoder().decode(refreshPublicKeyContent)));
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize JWT keys", e);
        }
    }

    private String extractBase64FromPEM(String pemKey) {
        return pemKey.replaceAll("-----(BEGIN|END)\\s+(EC\\s+PRIVATE|PUBLIC)\\s+KEY-----", "")
                     .replaceAll("\\s+", "");
    }

    // Getters
    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public ECPrivateKey getAccessPrivateKey() {
        return accessPrivateKey;
    }

    public ECPublicKey getAccessPublicKey() {
        return accessPublicKey;
    }

    public ECPrivateKey getRefreshPrivateKey() {
        return refreshPrivateKey;
    }

    public ECPublicKey getRefreshPublicKey() {
        return refreshPublicKey;
    }

    public String getRefreshTokenExpiry() {
        return refreshTokenExpiry;
    }

    public String getAccessTokenExpiry() {
        return accessTokenExpiry;
    }
}