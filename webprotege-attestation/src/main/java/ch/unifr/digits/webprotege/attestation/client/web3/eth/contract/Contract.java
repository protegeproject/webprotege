package ch.unifr.digits.webprotege.attestation.client.web3.eth.contract;

import ch.unifr.digits.webprotege.attestation.client.web3.core.Common;
import ch.unifr.digits.webprotege.attestation.client.web3.core.Provider;
import elemental2.promise.Promise;
import jsinterop.annotations.*;

@JsType(isNative = true, namespace = "web3.eth.Contract")
public class Contract {
    @JsProperty
    public String defaultAccount;
    @JsProperty
    public String defaultBlock;
    @JsProperty
    public Common defaultCommon;
    @JsProperty
    public String defaultHardfork;
    @JsProperty
    public String defaultChain;
    @JsProperty
    public int transactionPollingTimeout;
    @JsProperty
    public int transactionConfirmationBlocks;
    @JsProperty
    public int transactionBlockTimeout;
    @JsProperty
    public boolean handleRevert;
    @JsProperty
    public Options options;

    public Contract(Object jsonInterface, String address) {}

    @JsOverlay
    public static <V extends Contract> V contractFor(Object jsonInterface, String address) {
        Contract contract = new Contract(jsonInterface, address);
        return (V) contract;
    }

    @JsMethod
    public static native boolean setProvider(Provider provider);

    @JsMethod
    public native Contract clone();

    public static interface ContractMethods {
    }

    public static interface ContractMethod {
        @JsMethod
        Promise<Object> call();
        @JsMethod
        Promise<Object> send();
    }
}
