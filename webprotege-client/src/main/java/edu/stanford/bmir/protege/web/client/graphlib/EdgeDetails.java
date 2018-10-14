package edu.stanford.bmir.protege.web.client.graphlib;

import edu.stanford.bmir.protege.web.client.JSON;
import jsinterop.annotations.*;

import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Oct 2018
 */
@JsType(namespace = "webprotege.graph")
public class EdgeDetails {

    public EdgeDetails(String label) {
        setLabel(label);
    }

    @JsProperty
    private native void setLabel(String label);

    @JsProperty
    public native String getLabel();

    @JsProperty(name = "points")
    private native Point [] points();

    @JsMethod
    public Stream<Point> getPoints() {
        return Stream.of(points());
    }

    @JsMethod
    public String stringify() {
        return JSON.stringify(this);
    }
}
