package ch.unifr.digits.webprotege.attestation.client.web3.core;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative = true)
public class TransactionConfig {
    @JsProperty
    public String from;
    @JsProperty
    public String to;
    @JsProperty
    public String value;
    @JsProperty
    public String gas;
    @JsProperty
    public String gasPrice;
    @JsProperty
    public String data;
    @JsProperty
    public int nonce;
    @JsProperty
    public int chainId;
    @JsProperty
    public Common common;
    @JsProperty
    public String chain;
    @JsProperty
    public String hardfork;
}
