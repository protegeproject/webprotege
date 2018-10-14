package edu.stanford.bmir.protege.web.client.dagre;

import edu.stanford.bmir.protege.web.client.graphlib.Graph;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Oct 2018
 */
@JsType(isNative = true)
public class Dagre {

    private Dagre() {
    }

    /**
     * Gets the global property "dagre"
     */
    @JsProperty(name = "dagre", namespace = JsPackage.GLOBAL)
    public static native Dagre get();

    /**
     * Lays out the specified graph
     * @param graph The graph
     */
    public native void layout(Graph graph);
}
