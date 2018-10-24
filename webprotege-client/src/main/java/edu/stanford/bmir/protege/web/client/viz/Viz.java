package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.core.client.GWT;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class Viz {

    public Viz() {
        // This must remain empty
    }

    @JsOverlay
    public final void render(@Nonnull String dot, Consumer<String> rendering) {
        renderString(dot).then(rendering::accept);
    }

    private native Promise renderString(String s);
}
