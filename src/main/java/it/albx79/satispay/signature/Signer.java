package it.albx79.satispay.signature;

import lombok.RequiredArgsConstructor;
import okhttp3.Request;

import java.util.Base64;

import static java.util.Objects.requireNonNull;

@RequiredArgsConstructor
public class Signer {
    private final SignatureParams params;
    private final Canonicalizer canonicalizer;
    private final SignatureGenerator signatureGenerator;

    /**
     * In order to create a signature, a client must:
     * <p>
     * Use the contents of the HTTP message, the headers value, and the Signature String Construction algorithm to
     * create the signature string.
     * <p>
     * The algorithm and key associated with keyId must then be used to generate a digital signature on the signature
     * string.
     * <p>
     * The signature is then generated by base 64 encoding the output of the digital signature algorithm.
     */
    public Request sign(Request.Builder requestBuilder) {
        Request request = requestBuilder.build();
        byte[] signature = signatureGenerator.generate(params, canonicalizer.canonicalize(params, request));
        String base64signature = new String(Base64.getEncoder().encode(signature));
        return new Request.Builder(request).header(
                "Authorization",
                String.format("Signature keyId=\"%s\", algorithm=\"%s\", satispayresign=\"enable\", headers=\"%s\", signature=\"%s\"",
                        params.getKeyId(), params.getAlgorithm(), String.join(" ", params.getHeaders()), base64signature
                )
        ).build();
    }
}
