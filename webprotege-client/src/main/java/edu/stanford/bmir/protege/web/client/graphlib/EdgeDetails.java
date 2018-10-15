package edu.stanford.bmir.protege.web.client.graphlib;

import edu.stanford.bmir.protege.web.client.JSON;
import jsinterop.annotations.*;

import javax.annotation.Nonnull;
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

    @JsProperty
    public native String getStyleNames();

    @JsProperty
    public native void setStyleNames(@Nonnull String styleNames);

    @JsProperty(name = "points")
    private native Point [] points();

    @JsMethod
    public Stream<Point> getPoints() {
        return Stream.of(points());
    }

    @JsProperty(name = "minlen")
    public native void setMinLength(int minLength);

    @JsProperty(name = "weight")
    public native void setWeight(int weight);

    @JsProperty(name = "labelpos")
    private native void setLabelPos(String pos);

    public final void setLabelPosLeft() {
        setLabelPos("l");
    }

    public final void setLabelPosRight() {
        setLabelPos("r");
    }

    public final void setLabelPosCenter() {
        setLabelPos("c");
    }

    @JsProperty(name = "width")
    public native void setLabelWidth(int labelWidth);

    @JsProperty(name = "height")
    public native void setLabelHeight(int labelHeight);

    @JsProperty(name = "labeloffset")
    public native void setLabelOffset(int offset);

    @JsMethod
    public String stringify() {
        return JSON.stringify(this);
    }
}
