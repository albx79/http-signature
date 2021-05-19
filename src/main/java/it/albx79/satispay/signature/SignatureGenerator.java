package it.albx79.satispay.signature;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;

import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@RequiredArgsConstructor
public class SignatureGenerator {

    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    /**
     * The signing string is signed with the key associated with keyId and the algorithm corresponding to algorithm
     * <p>
     * For algorithm RSA (rsa-sha256): the signature string hashing function is SHA-256, and the signing algorithm is
     * the one defined in RFC3447. The result of the signature creation algorithm specified in RFC 3447 results
     * in a binary string.
     * <p>
     * For algorithm AES (hmac-sha256): you need to update the Kauth coherently with the Sequence generated (please
     * note that the Sequence is provided during the App-Startup. The resulting strings should be converted in a binary
     * string encoded with utf-8te con encoding utf-8, then the hmac is calculated with algorithm sha-256 using the
     * Kauth as algorithm key, which is then base 64 encoded and placed into the signature value.
     */
    @SneakyThrows
    public byte[] generate(SignatureParams params, String signingString) {
        if (!"rsa-sha256".equals(params.getAlgorithm())) {
            throw new UnsupportedOperationException(params.getAlgorithm());
        }

        val signature = Signature.getInstance("SHA256withRSA");

        signature.initSign(privateKey);
        signature.update(signingString.getBytes());
        return signature.sign();
    }

}
