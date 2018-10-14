package edu.stanford.bmir.protege.web.client.graphlib;

import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

import static com.google.gwt.thirdparty.guava.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Oct 2018
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public interface Point {

    @JsProperty
    int getX();

    @JsProperty
    int getY();
}
