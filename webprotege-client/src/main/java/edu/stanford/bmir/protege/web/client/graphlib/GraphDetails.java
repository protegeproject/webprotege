package edu.stanford.bmir.protege.web.client.graphlib;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Oct 2018
 */
@JsType
public class GraphDetails {

    @JsProperty
    public native int getWidth();

    @JsProperty
    public native int getHeight();
}
