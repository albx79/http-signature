package it.albx79.satispay;

import it.albx79.satispay.signature.SignatureParams;
import lombok.SneakyThrows;
import lombok.val;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.io.IOUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.Collections;
import java.util.List;

@Command(name = "httpsignature", mixinStandardHelpOptions = true, version = "0.1")
public class App implements Runnable {
    static final String SATISPAY_STAGING = "https://staging.authservices.satispay.com/wally-services/protocol/tests/signature";

    @Parameters(defaultValue = SATISPAY_STAGING, description = "Defaults to " + SATISPAY_STAGING)
    String url;

    @Option(names = {"--method", "-m"}, defaultValue = "GET")
    String method;

    @Option(names = {"--body", "-b"}, defaultValue = "")
    String body;

    @Option(names = {"--type", "-t"}, defaultValue = "text/plain")
    String mediaType;

    @Option(names = {"--debug", "-d"}, description = "Print the request.")
    boolean debug;

    @Option(names = {"-H", "--header"}, description = "HTTP headers to include in signature.")
    List<String> headers = Collections.emptyList();

    private final SignatureParams.SignatureParamsBuilder params = SignatureParams.builder();

    @Override
    @SneakyThrows
    public void run() {
        if (!headers.isEmpty()) {
            params.headers(headers);
        }
        val ctx = new Context(params.build());
        val client = new OkHttpClient.Builder().build();
        val requestBuilder = new Request.Builder().url(url);
        if ("GET".equalsIgnoreCase(method)) {
            requestBuilder.get();
        } else {
            requestBuilder
                    .method(method, RequestBody.create(body, MediaType.parse(mediaType)))
                    .header("Content-Type", mediaType);
        }
        val request = ctx.getSigner().sign(requestBuilder);
        if (debug) {
            System.out.println(request.toString());
        }
        val responseBody = client.newCall(request).execute().body();
        IOUtils.copy(responseBody.charStream(), System.out);
    }

    public static void main(String[] args) {
        new CommandLine(new App()).execute(args);
    }
}
