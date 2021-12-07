package ch.unifr.digits.webprotege.attestation.client.jszip;

import com.google.gwt.regexp.shared.RegExp;
import elemental2.promise.Promise;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class JSZip {

    @JsProperty
    public static Support support;
    @JsMethod
    public static native Promise<JSZip> loadAsync(Object data);
    @JsMethod
    public native ZipObject[] file(RegExp regex);
}
