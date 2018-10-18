package edu.stanford.bmir.protege.web.client.d3;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Oct 2018
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class Event {

    @JsProperty(name = "transform")
    public native Transform getTransform();
}
