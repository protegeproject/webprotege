package ch.unifr.digits.webprotege.attestation.client.web3.eth.contract;

import jsinterop.annotations.JsProperty;

public class ContractOptions {
    // Sender to use for contract calls
    @JsProperty
    public String from;
    // Gas price to use for contract calls
    @JsProperty
    public String gasPrice;
    // Gas to use for contract calls
    @JsProperty
    public int gas;
    // Contract code
    @JsProperty
    public String data;
}
