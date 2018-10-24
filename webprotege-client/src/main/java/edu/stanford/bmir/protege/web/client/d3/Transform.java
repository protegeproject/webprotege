package edu.stanford.bmir.protege.web.client.d3;

import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Oct 2018
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class Transform {

    @JsProperty(name = "x")
    public native int getX();

    @JsProperty(name = "y")
    public native int getY();

    @JsProperty(name = "k")
    public native double getK();

    @JsMethod(name = "scale")
    public native String getScale();

    @JsMethod(name = "scale")
    public native Transform scale(double k);

    @JsMethod(name = "translate")
    public native Transform translate(double x, double y);

}
