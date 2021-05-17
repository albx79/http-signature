package it.albx79.satispay;

import it.albx79.satispay.signature.Parameters;
import lombok.RequiredArgsConstructor;
import okhttp3.Request;

/**
 * @author albx
 */
@RequiredArgsConstructor
public class Signer {
    private final Parameters params;
    
    Request sign(Request.Builder requestBuilder) {
        return requestBuilder.build();
    }
}
