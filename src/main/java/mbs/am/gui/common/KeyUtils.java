package mbs.am.gui.common;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import lombok.extern.jbosslog.JBossLog;
import mbs.am.gui.model.TenantKeyRegistry;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.StringReader;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JBossLog
public class KeyUtils {

    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    private KeyUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Java 1.8 compatible: Replaced Pattern Matching instanceof with explicit casting.
     */
    public static PrivateKey parsePrivateKey(String pemContent) {
        if (pemContent == null || pemContent.isEmpty()) {
            throw new IllegalArgumentException("Private Key content cannot be null or empty");
        }

        try (PEMParser pemParser = new PEMParser(new StringReader(pemContent))) {
            Object object = pemParser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

            PrivateKey privateKey;
            if (object instanceof PEMKeyPair) {
                PEMKeyPair pemKeyPair = (PEMKeyPair) object;
                privateKey = converter.getPrivateKey(pemKeyPair.getPrivateKeyInfo());
            } else if (object instanceof PrivateKeyInfo) {
                PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) object;
                privateKey = converter.getPrivateKey(privateKeyInfo);
            } else {
                String className = (object != null) ? object.getClass().getName() : "null";
                throw new IllegalArgumentException("Unknown Key Type: " + className);
            }

            if (privateKey instanceof RSAPrivateKey) {
                RSAPrivateKey rsa = (RSAPrivateKey) privateKey;
                log.debugf("Parsed RSA Key with bit length: %s", rsa.getModulus().bitLength());
            }
            return privateKey;
        } catch (Exception e) {
            log.errorf("Failed to parse Private Key: %s", e.getMessage());
            throw new RuntimeException("Private Key parsing failed", e);
        }
    }

    /**
     * Java 1.8 compatible: Replaced isBlank() and Pattern Matching.
     */
    public static RSAPublicKey parsePublicKey(String pemContent) {
        // isBlank() is Java 11+, using trim().isEmpty() for Java 8
        if (pemContent == null || pemContent.trim().isEmpty()) {
            throw new IllegalArgumentException("Public Key content cannot be null or empty");
        }
        try (PEMParser pemParser = new PEMParser(new StringReader(pemContent))) {
            Object object = pemParser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

            if (object instanceof SubjectPublicKeyInfo) {
                SubjectPublicKeyInfo publicKeyInfo = (SubjectPublicKeyInfo) object;
                return (RSAPublicKey) converter.getPublicKey(publicKeyInfo);
            } else {
                String className = (object != null) ? object.getClass().getName() : "null";
                throw new IllegalArgumentException("Invalid Public Key format. Expected SubjectPublicKeyInfo, got: " + className);
            }
        } catch (Exception e) {
            log.errorf("Failed to parse Public Key: %s", e.getMessage());
            throw new RuntimeException("Public Key parsing failed", e);
        }
    }

    /**
     * Java 1.8 compatible: Replaced Switch Expression with classic Switch statement.
     */
    public static SecureDigestAlgorithm<PrivateKey, PublicKey> getSignatureAlgorithm(String algo) {
        if (algo == null) return Jwts.SIG.RS256;

        switch (algo.toUpperCase()) {
            case "RS256":
                return Jwts.SIG.RS256;
            case "RS384":
                return Jwts.SIG.RS384;
            case "RS512":
                return Jwts.SIG.RS512;
            case "ES256":
                return Jwts.SIG.ES256;
            case "ES384":
                return Jwts.SIG.ES384;
            default:
                log.errorf("CRITICAL: Unsupported JWT algorithm %s found. Falling back to RS256.", algo);
                return Jwts.SIG.RS256;
        }
    }

    /**
     * Java 1.8 compatible: Replaced Map.of() with Collections.singletonMap or HashMap.
     */
    public static Map<String, Object> generateJwkSet(List<TenantKeyRegistry> activeKeys) {
        if (activeKeys == null || activeKeys.isEmpty()) {
            return Collections.singletonMap("keys", Collections.emptyList());
        }

        List<Map<String, Object>> keys = activeKeys.stream()
                .map(key -> Jwks.builder()
                        .key(parsePublicKey(key.getPublicKey()))
                        .id(key.getKid())
                        .build())
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("keys", keys);
        return response;
    }
}