package edu.stanford.bmir.protege.web.client.graphlib;

import jsinterop.annotations.JsType;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Oct 2018
 */
@JsType(isNative = true, namespace = "graphlib", name = "json")
public class GraphLibJson {

    public static native Object write(Graph g);

    public static native Graph read(Object s);
}
