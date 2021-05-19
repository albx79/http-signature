package it.albx79.satispay.signature;

import lombok.val;
import okhttp3.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author albx
 */
class CanonicalizerTest {

    Canonicalizer canonicalizer;

    SignatureParams.SignatureParamsBuilder paramsBuilder;
    Request.Builder requestBuilder;

    @BeforeEach
    void setup() {
        canonicalizer = new Canonicalizer();
        paramsBuilder = SignatureParams.builder();
        requestBuilder = new Request.Builder().url("http://example.com/path").get();
    }

    @Test
    void ifHeaderIsRequestTargetThenConcatMethodAndPath() {
        val params = paramsBuilder.headers(singletonList("(request-target)")).build();
        val canonicalized = canonicalizer.canonicalize(params, requestBuilder.build());
        assertThat(canonicalized, equalTo("(request-target): get /path"));
    }

    @Test
    void headersAreConcatenatedInCorrectOrder() {
        val params = paramsBuilder.headers(asList("date", "digest")).build();
        requestBuilder
                .header("Digest", "12345")
                .header("Date", "2021-01-01");
        val canonicalized = canonicalizer.canonicalize(params, requestBuilder.build());
        assertThat(canonicalized, equalTo("date: 2021-01-01\ndigest: 12345"));
    }

    @Test
    void multipleInstanceOfTheSameHeaderAreConcatenatedWithCommaAndSpace() {
        val params = paramsBuilder.headers(singletonList("header")).build();
        requestBuilder
                .addHeader("header", "value 1")
                .addHeader("header", "value 2");
        val canonicalized = canonicalizer.canonicalize(params, requestBuilder.build());
        assertThat(canonicalized, equalTo("header: value 1, value 2"));
    }

    @Test
    void omitOptionalWhitespace() {
        val params = paramsBuilder.headers(singletonList("X-Example")).build();
        requestBuilder.header("x-example", "Example header with some whitespace.    ");
        val canonicalized = canonicalizer.canonicalize(params, requestBuilder.build());
        assertThat(canonicalized, equalTo("x-example: Example header with some whitespace."));
    }

}