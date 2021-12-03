package ch.unifr.digits.webprotege.attestation.client.web3.core;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative = true)
public class Common {
    @JsProperty
    public CustomChainParams customChain;
    @JsProperty
    public String baseChain;
    @JsProperty
    public String hardfork;
}
