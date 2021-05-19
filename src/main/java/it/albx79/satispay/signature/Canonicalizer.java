package it.albx79.satispay.signature;

import okhttp3.Request;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

/**
 * In order to generate the string that is signed with a key, the client must use the values of each HTTP header field in the headers Signature parameter, in the order they appear in the headers Signature parameter. Implementers should at minimum include the request target and Date header fields. To include the HTTP request target in the signature calculation, use the special (request-target) header field name.
 * <p>
 * If the header field name is (request-target) then generate the header field value by concatenating the lowercased :method, an ASCII space, and the :path pseudo-headers as specified in the RFC7540.
 * <p>
 * Create the header field string by concatenating the lowercased header field name followed with an ASCII colon :, an ASCII space , and the header field value. Leading and trailing optional whitespace (OWS) in the header field value MUST be omitted. If there are multiple instances of the same header field, all header field values associated with the header field MUST be concatenated, separated by a ASCII comma and an ASCII space ,, and used in the order in which they will appear in the transmitted HTTP message. Any other modification to the header field value MUST NOT be made.
 * <p>
 * If value is not the last value, then append an ASCII newline \n
 */
public class Canonicalizer {
    /**
     * uses the algorithm and headers signature parameters to form a canonicalized signing string
     */
    public String canonicalize(SignatureParams params, final Request request) {
        return params.getHeaders().stream()
                .map(header -> header.toLowerCase() + ": " + canonicalizeHeaders(request, header))
                .collect(Collectors.joining("\n"));
    }

    @NotNull
    private String canonicalizeHeaders(Request request, String header) {
        if ("(request-target)".equalsIgnoreCase(header)) {
            return request.method().toLowerCase() + " " + request.url().encodedPath();
        }
        return String.join(", ", request.headers(header));
    }
}
