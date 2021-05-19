package it.albx79.satispay.signature;

import it.albx79.satispay.signature.SignatureParams.SignatureParamsBuilder;
import lombok.val;
import okhttp3.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author albx
 */
class CanonicalizerTest {

    Canonicalizer canonicalizer;

    SignatureParamsBuilder paramsBuilder;
    Request.Builder requestBuilder;

    @BeforeEach
    void setup() {
        canonicalizer = new Canonicalizer();
        paramsBuilder = SignatureParams.builder();
        requestBuilder = new Request.Builder().url("http://example.com/path").get();
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testCanonicalize(Request.Builder requestBuilder, SignatureParamsBuilder paramsBuilder, String expected) {
        val canonicalized = canonicalizer.canonicalize(paramsBuilder.build(), requestBuilder.build());
        assertThat(canonicalized, equalTo(expected));
    }

    static Stream<Arguments> testData() {
        Request request = new Request.Builder().url("http://example.com/path").get().build();
        return Stream.of(
                Arguments.of(
                        new Request.Builder(request),
                        SignatureParams.builder().headers(singletonList("(request-target)")),
                        "(request-target): get /path"
                ),
                Arguments.of(
                        new Request.Builder(request)
                                .header("Digest", "12345")
                                .header("Date", "2021-01-01"),
                        SignatureParams.builder().headers(asList("date", "digest")),
                        "date: 2021-01-01\ndigest: 12345"
                ),
                Arguments.of(
                        new Request.Builder(request)
                                .addHeader("header", "value 1")
                                .addHeader("header", "value 2"),
                        SignatureParams.builder().headers(singletonList("header")),
                        "header: value 1, value 2"
                ),
                Arguments.of(
                        new Request.Builder(request)
                                .header("x-example", "Example header with some whitespace.    "),
                        SignatureParams.builder().headers(singletonList("X-Example")),
                        "x-example: Example header with some whitespace."
                )
        );
    }
}