package ch.unifr.digits.webprotege.attestation.client.jszip;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class Support {
    @JsProperty
    public boolean arraybuffer;
    @JsProperty
    public boolean uint8array;
    @JsProperty
    public boolean blob;
    @JsProperty
    public boolean nodebuffer;
    @JsProperty
    public boolean nodestream;
    @JsProperty
    public boolean base64;
    @JsProperty
    public boolean array;
    @JsProperty
    public boolean string;

}
