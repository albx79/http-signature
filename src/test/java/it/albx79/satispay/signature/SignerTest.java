package it.albx79.satispay.signature;

import it.albx79.satispay.Context;
import lombok.val;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class SignerTest
{
    static final String EXAMPLE_STAGING = "http://example.com";
    static final MediaType JSON = MediaType.get("application/json");

    Signer signer;
    SignatureParams params;

    @BeforeEach
    void setup() {
        params = SignatureParams.builder()
                .keyId("Test")
                .headers(Arrays.asList("(request-target)", "host", "date", "digest"))
                .build();
        signer = new Context(params).getSigner();
    }

    @Test
    void signatureGenerationEndToEndTest() {
        val request = new Request.Builder()
                .url(EXAMPLE_STAGING + "/foo?param=value&pet=dog")
                .header("Date", "Thu, 05 Jan 2014 21:31:40 GMT")
                .header("Digest", "SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu9DBPE=")
                .post(RequestBody.create("{\"hello\": \"world\"}", JSON));

        val signed = signer.sign(request);

        assertThat(
                signed.headers("Authorization").get(0),
                equalTo("Signature keyId=\"Test\", algorithm=\"rsa-sha256\", satispayresign=\"enable\", headers=\"(request-target) host date digest\", signature=\"t4ZPDDAW3YvonulVN6pfhWKncacQpvjaUbZMDj2n45WE6pb6WtV4u2X41TJmHTavCbhqcrgrt8PipkY92cakUKiX528vq1rONVbx1HPmVSZYY5DmTV1856yOTFGYutQjkQOHbEen4HkIYt+83EY6KUYxzJogQEc5HBR6DW5nOoYUTeveS4enl4y11omnPgh01FBNqzaIXZAcsa1AOoQygW16QDXOkMBuN8hL/UINB8CQ76m5OlS9Bur0H75jeTxmlTnS9W1zmX3GHyg8mcWck7VLWu+MYTtLW6bssewZEblx/lwG8/lFQ3SeGrEwH2kVjCw1xEPd6kVPWuWKWQJXpg==\"")
        );
    }
}
