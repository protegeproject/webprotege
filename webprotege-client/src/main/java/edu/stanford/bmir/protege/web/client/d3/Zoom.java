package edu.stanford.bmir.protege.web.client.d3;

import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Oct 2018
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Zoom")
public class Zoom {

    @JsMethod(name = "zoom")
    public native void zoom(@Nonnull Selection selection);

    @JsMethod(name = "transform")
    public native void transform(@Nonnull Selection selection, @Nonnull Transform transform);

    @JsMethod(name = "on")
    public native Object on(@Nonnull String event, @Nullable Function function);
}
