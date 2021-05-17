package it.albx79.satispay;

import it.albx79.satispay.signature.Parameters;
import lombok.val;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * Unit test for simple App.
 */
class AppTest
{
    static final String SATISPAY_STAGING = "https://staging.authservices.satispay.com/wally-services/protocol/tests/signature";
    static final String EXAMPLE_STAGING = "example.com";
    static final MediaType JSON = MediaType.get("application/json");

    Signer signer;
    Parameters params;

    @BeforeEach
    void setup() {
        params = Parameters.builder()
                .algorithm("hmac-sha256")
                .keyId("Test")
                .headers(Arrays.asList("(request-target)", "host", "date", "digest"))
                .build();
        signer = new Signer(params);
    }

    @Test
    void signatureGenerationEndToEndTest() {
        val request = new Request.Builder()
                .url(EXAMPLE_STAGING + "/foo?param=value&pet=dog")
                .header("Date", "Thu, 05 Jan 2014 21:31:40 GMT")
                .header("Digest", "SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu9DBPE=")
                .post(RequestBody.create(JSON, "{\"hello\": \"world\"}"));

        val signed = signer.sign(request);

        MatcherAssert.assertThat(
                signed.headers("Authorization").get(0),
                Matchers.equalTo("Signature keyId=\"Test\", algorithm=\"hmac-sha256\", headers=\"(request-target) host date digest\", signature=\"ATp0r26dbMIxOopqw0OfABDT7CKMIoENumuruOtarj8n/97Q3htHFYpH8yOSQk3Z5zh8UxUym6FYTb5+A0Nz3NRsXJibnYi7brE/4tx5But9kkFGzG+xpUmimN4c3TMN7OFH//+r8hBf7BT9/GmHDUVZT2JzWGLZES2xDOUuMtA=\"")
        );
    }
}
