package it.albx79.satispay;

import it.albx79.satispay.signature.Canonicalizer;
import it.albx79.satispay.signature.SignatureParams;
import it.albx79.satispay.signature.SignatureGenerator;
import it.albx79.satispay.signature.Signer;
import lombok.*;
import org.apache.commons.io.IOUtils;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static java.util.Objects.requireNonNull;

@RequiredArgsConstructor
@Data
public class Context {

    private final SignatureParams params;

    @Getter(lazy = true)
    private final RSAPublicKey rsaPublicKey = readPublicKey();

    @Getter(lazy = true)
    private final RSAPrivateKey rsaPrivateKey = readPrivateKey();

    @Getter(lazy = true)
    private final Canonicalizer canonicalizer = new Canonicalizer();

    @Getter(lazy = true)
    private final SignatureGenerator signatureGenerator = new SignatureGenerator(getRsaPrivateKey(), getRsaPublicKey());

    @Getter(lazy = true)
    private final Signer signer = new Signer(getParams(), getCanonicalizer(), getSignatureGenerator());


    @SneakyThrows
    private RSAPublicKey readPublicKey() {
        String key = IOUtils.toString(requireNonNull(getClass().getResourceAsStream("/keys/client-rsa-public-key.pub")));

        String publicKeyPEM = key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");

        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    @SneakyThrows
    private RSAPrivateKey readPrivateKey() {
        String key = IOUtils.toString(requireNonNull(getClass().getResourceAsStream("/keys/client-rsa-private-key.pem")));

        String privateKeyPEM = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");

        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

}
