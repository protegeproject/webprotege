package edu.stanford.bmir.protege.web.client;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Oct 2018
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class JSON {

    public static native String stringify(Object obj);

    public static native Object parse(String obj);
}
