package ch.unifr.digits.webprotege.attestation.client.web3.core;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative = true)
public class TransactionReceipt {
    @JsProperty
    public boolean status;
    @JsProperty
    public String transactionHash;
    @JsProperty
    public int transactionIndex;
    @JsProperty
    public String blockHash;
    @JsProperty
    public int blockNumber;
    @JsProperty
    public String from;
    @JsProperty
    public String to;
    @JsProperty
    public String contractAddress;
    @JsProperty
    public int cumulativeGasUsed;
    @JsProperty
    public int gasUsed;
}
