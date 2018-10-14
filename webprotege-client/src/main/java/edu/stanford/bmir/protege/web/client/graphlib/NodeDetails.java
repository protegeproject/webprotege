package edu.stanford.bmir.protege.web.client.graphlib;

import com.google.common.base.MoreObjects;
import edu.stanford.bmir.protege.web.client.JSON;
import jsinterop.annotations.*;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Oct 2018
 *
 */
@JsType(namespace = "webprotege.graph")
public class NodeDetails {

    public NodeDetails(@Nonnull String id, int width, int height, String label) {
        setId(id);
        setWidth(width);
        setHeight(height);
        setLabel(label);
    }

    @JsProperty
    public native String getId();

    @JsProperty
    public native void setId(String id);

    @JsProperty
    public native int getX();

    @JsProperty
    public native int getY();

    @JsProperty
    public native int getWidth();

    @JsProperty
    public native int getHeight();

    @JsProperty
    public native void setX(int x);

    @JsProperty
    public native void setY(int y);

    @JsProperty
    public native void setWidth(int width);

    @JsProperty
    public native void setHeight(int height);

    @JsProperty
    public native void setLabel(String label);

    @JsProperty
    public native String getLabel();

    @JsMethod
    @Override
    public final String toString() {
        return MoreObjects.toStringHelper("NodeDetails")
                .add("x", this.getX())
                .add("y", this.getY())
                .add("w", this.getWidth())
                .add("h", this.getHeight())
                .toString();
    }

    @JsMethod
    public String stringify() {
        return JSON.stringify(this);
    }
}
