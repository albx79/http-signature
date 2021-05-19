package it.albx79.satispay.signature;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.NonNull;
import lombok.Value;

import java.util.Collections;
import java.util.List;

/**
 * @author albx
 */
@Value
@Builder(toBuilder = true)
public class SignatureParams {
    @Default @NonNull String keyId = "signature-test-66289";
    @Default @NonNull String algorithm = "rsa-sha256";
    @Default @NonNull List<String> headers = Collections.singletonList("date");
}
