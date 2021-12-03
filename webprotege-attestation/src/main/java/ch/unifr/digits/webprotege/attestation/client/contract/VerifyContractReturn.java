package ch.unifr.digits.webprotege.attestation.client.contract;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class VerifyContractReturn {
    public boolean valid;
    public String signer;
    public String signerName;
    public int timestamp;
}
