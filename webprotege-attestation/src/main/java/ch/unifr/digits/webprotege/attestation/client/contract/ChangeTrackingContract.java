package ch.unifr.digits.webprotege.attestation.client.contract;


import ch.unifr.digits.webprotege.attestation.client.web3.Web3;
import ch.unifr.digits.webprotege.attestation.client.web3.core.TransactionReceipt;
import elemental2.promise.Promise;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

import java.util.List;

@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class ChangeTrackingContract {

    public ChangeTrackingContract(Web3 web3, Object jsonInterface, String address) {}

    @JsMethod
    public native Promise<VerifyContractReturn> verifyEntity(String from, String id, int entityHash);

    @JsMethod
    public native Promise<VerifyContractReturn> verify(String from, String ontologyIri, String versionIri, String hash);

    @JsMethod
    public native Promise<TransactionReceipt> attest(String from, String ontologyIri, String versionIri, String name,
                                                     String hash, List<Integer> classHashes);

}
