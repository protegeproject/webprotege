package edu.stanford.bmir.protege.web.client.d3;

import elemental.dom.Element;
import elemental.dom.Node;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Oct 2018
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "d3")
public class d3 {

    @JsMethod(name = "select")
    @Nonnull
    public static native Selection select(@Nonnull String selector);

    @JsMethod(name = "select")
    public static native Selection selectElement(@Nonnull Element element);

    @JsMethod(name = "selectAll")
    @Nonnull
    public static native Selection selectAll(@Nonnull String selector);


    @JsMethod(name = "zoom")
    @Nonnull
    public static native Zoom zoom();

    @JsMethod(name = "zoomTransform")
    public static native Transform zoomTransform(Node node);

    @JsProperty(name = "event")
    public static native Event getEvent();

}
