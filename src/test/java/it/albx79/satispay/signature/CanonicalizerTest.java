package it.albx79.satispay.signature;

import lombok.val;
import okhttp3.Request;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

/**
 * @author albx
 */
class CanonicalizerTest {

    Canonicalizer canonicalizer;

    Parameters.ParametersBuilder paramsBuilder;
    Request.Builder requestBuilder;

    @BeforeEach
    void setup() {
        canonicalizer = new Canonicalizer();
        paramsBuilder = Parameters.builder();
        requestBuilder = new Request.Builder().url("example.com/path").get();
    }

    @Test
    void ifHeaderIsRequestTargetThenConcatMethodAndPath() {
        val params = paramsBuilder.headers(Collections.singletonList("(request-target")).build();
        val headers = canonicalizer.canonicalize(params, requestBuilder);
        MatcherAssert.assertThat(headers, Matchers.equalTo("(request-target): get /path"));
    }

}