package ch.unifr.digits.webprotege.attestation.server;

import ch.unifr.digits.webprotege.attestation.server.contracts.OntologyAttestation;
import ch.unifr.digits.webprotege.attestation.shared.VerifyResult;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static ch.unifr.digits.webprotege.attestation.server.SettingsManager.ADDRESS_ATTESTATION;

public class FileAttestationService<T> extends AttestationService<T> {

    @Override
    public String contractAddress() {
        return ADDRESS_ATTESTATION;
    }

    @Override
    public TransactionReceipt attest(String iri, String versionIri, String name, String hash, T params) throws Exception {
        OntologyAttestation contract = OntologyAttestation.load(contractAddress(), WEB3_REF.get(), CREDENTIALS,
                DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT);
        RemoteFunctionCall<TransactionReceipt> call = contract.attest(iri, versionIri, name, hash);
        TransactionReceipt receipt = call.send();
        return receipt;
    }

    @Override
    public VerifyResult verify(String iri, String versionIri, String hash, T params) throws Exception {
        OntologyAttestation contract = OntologyAttestation.load(contractAddress(), WEB3_REF.get(), CREDENTIALS,
                DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT);
        RemoteFunctionCall<Tuple4<Boolean, String, String, BigInteger>> remoteFunctionCall =
                contract.verify(iri, versionIri, hash);
        Tuple4<Boolean, String, String, BigInteger> result = remoteFunctionCall.send();
        BigInteger bigInteger = result.component4();
        int time = (bigInteger == null) ? -1 : bigInteger.intValue();
        return new VerifyResult(result.component1(), result.component2(), result.component3(), time);
    }

    public String hashFile(byte[] data) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data);
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
