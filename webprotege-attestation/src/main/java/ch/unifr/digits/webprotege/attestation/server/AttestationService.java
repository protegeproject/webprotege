package ch.unifr.digits.webprotege.attestation.server;

import ch.unifr.digits.webprotege.attestation.shared.VerifyResult;
import okhttp3.OkHttpClient;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static ch.unifr.digits.webprotege.attestation.server.SettingsManager.*;

public abstract class AttestationService<T> {

    protected static final Credentials CREDENTIALS = Credentials.create(SERVER_SECRET);
    protected static final AtomicReference<Web3j> WEB3_REF = new AtomicReference<>();

    public AttestationService() {
        init();
    }

    public abstract String contractAddress();
    public abstract TransactionReceipt attest(String iri, String versionIri, String name, String hash, T params) throws Exception;
    public abstract VerifyResult verify(String iri, String versionIri, String hash, T params) throws Exception;

    private static void init() {
        if (WEB3_REF.compareAndSet(null, null)) {
            Web3jService service = buildService(PROVIDER_HOST + ":" + PROVIDER_PORT);
            Web3j web3 = Web3j.build(service);
            boolean result = WEB3_REF.compareAndSet(null, web3);
            if (!result) web3.shutdown();
        }
    }

    private static Web3jService buildService(String url) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        long tos = 300L;
        builder.connectTimeout(tos, TimeUnit.SECONDS);
        builder.readTimeout(tos, TimeUnit.SECONDS);
        builder.writeTimeout(tos, TimeUnit.SECONDS);
        return new HttpService(url, builder.build());
    }

    public static class NopParam {}
}
