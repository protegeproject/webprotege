package edu.stanford.bmir.protege.web.client.graphlib;

import edu.stanford.bmir.protege.web.client.JSON;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import jsinterop.annotations.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Oct 2018
 */
@JsType(namespace = "webprotege.graph")
public class EdgeDetails {

    @Nullable
    private OWLEntityData relation = null;

    private EdgeDetails(String label, @Nullable OWLEntityData relation) {
        setLabel(label);
        this.relation = relation;
    }

    public static EdgeDetails create(@Nonnull String label) {
        return new EdgeDetails(label, null);
    }

    public static EdgeDetails createWithRelation(@Nonnull String label, @Nonnull Optional<OWLEntityData> relation) {
        return new EdgeDetails(label, relation.orElse(null));
    }

    @Nonnull
    public Optional<OWLEntityData> getRelation() {
        return Optional.ofNullable(relation);
    }

    @JsProperty
    public native int getX();

    @JsProperty
    public native int getY();

    @JsProperty
    public native String getTailId();
    
    @JsProperty
    public native void setTailId(String tailId);

    @JsProperty
    public native String getHeadId();
    
    @JsProperty
    public native void setHeadId(String HeadId);

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

    @JsProperty(name = "width")
    public native int getLabelWidth();

    @JsProperty(name = "height")
    public native int getLabelHeight();

    @JsProperty(name = "labeloffset")
    public native void setLabelOffset(int offset);

    @JsMethod
    public String stringify() {
        return JSON.stringify(this);
    }

    @JsProperty
    public native String getArrowHeadStyle();

    @JsProperty
    public native void setArrowHeadStyle(String style);

}
