package edu.stanford.bmir.protege.web.client.viz;

import jsinterop.annotations.JsType;

import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
@JsType(isNative = true)
public class Promise {

    public native void then(RenderingConsumer consumer);
}
