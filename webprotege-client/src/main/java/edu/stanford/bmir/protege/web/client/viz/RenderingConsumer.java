package edu.stanford.bmir.protege.web.client.viz;

import jsinterop.annotations.JsFunction;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
@JsFunction
public interface RenderingConsumer {

    void render(String rendering);
}
