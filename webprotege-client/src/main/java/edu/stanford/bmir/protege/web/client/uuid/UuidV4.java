package edu.stanford.bmir.protege.web.client.uuid;


import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true)
public class UuidV4 {

    @JsMethod(name = "uuidv4", namespace = JsPackage.GLOBAL)
    public static native String uuidv4();

}
