package ch.unifr.digits;

import ch.unifr.digits.contracts.Storage;
import ch.unifr.digits.webprotege.attestation.server.SettingsManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

import static ch.unifr.digits.FileSupport.saveMeasurementsSeries;
import static ch.unifr.digits.webprotege.attestation.server.SettingsManager.SERVER_SECRET;

@Disabled
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseLineTest {

    private static final String CONTRACT_ADDRESS = "0xc1Fb1Cb1A01C4F6B8456DdC3593A8CDA6b536c71";
    private static final int NUM_RUNS = 20;
    private static final String RESULTS_DIR = "results/";
    private static final Measurements measurements = new Measurements();

    private static Storage contract;

    @BeforeAll
    public static void beforeAll() throws Exception {
        Web3jService web3jService = new HttpService(SettingsManager.PROVIDER_HOST + ":" + SettingsManager.PROVIDER_PORT);
        Web3j web3j = Web3j.build(web3jService);
        contract = Storage.load(CONTRACT_ADDRESS, web3j, Credentials.create(SERVER_SECRET),
                DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT);
    }

    @Test
    public void test() throws Exception {
        for (int i = 0; i < NUM_RUNS; i++) {
            BigInteger bigInteger = BigInteger.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE));
            int ticket = measurements.begin();
            RemoteFunctionCall<TransactionReceipt> call = contract.store(bigInteger);
            TransactionReceipt receipt = call.send();
            measurements.finish("transact", ticket);
            // gas cost is constant, except for first call
            measurements.manualMeasurement("gas", receipt.getGasUsed().longValue());
            System.out.println(receipt.getGasUsed());
        }

        saveMeasurementsSeries(RESULTS_DIR+"baseline-transact.csv", "baseline",
                measurements.getSeries("transact")
                        .stream().map(Duration::toNanos).map(String::valueOf), NUM_RUNS);
        saveMeasurementsSeries(RESULTS_DIR+"baseline-gas.csv", "baseline",
                measurements.getManualSeries("gas")
                        .stream().map(String::valueOf), NUM_RUNS);
    }

}
