package edu.stanford.bmir.protege.web.client.graphlib;

import com.google.common.base.MoreObjects;
import edu.stanford.bmir.protege.web.client.JSON;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import jsinterop.annotations.*;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Oct 2018
 *
 */
@JsType(namespace = "webprotege.graph")
public class NodeDetails {

    private final OWLEntityData entityData;

    public NodeDetails(@Nonnull OWLEntityData entityData, int width, int height, String label) {
        this.entityData = checkNotNull(entityData);
        OWLEntity entity = entityData.getEntity();
        setId(entity.getEntityType().getName() + "(" + entity.getIRI() + ")");
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

    public final double getTopLeftX() {
        return getX() - (getWidth() / 2.0);
    }

    public final double getTopLeftY() {
        return getY() - (getHeight() / 2.0);
    }

    @JsProperty
    public native void setLabel(String label);

    @JsProperty
    public native String getLabel();

    @JsProperty
    public native String getNodeStyleNames();

    @JsProperty
    public native void setNodeStyleNames(String name);

    @JsProperty
    public native String getNodeShapeStyleNames();

    @JsProperty
    public native void setNodeShapeStyleNames(String name);

    @JsProperty
    public native String getNodeTextStyleNames();

    @JsProperty
    public native void setNodeTextStyleNames(String name);

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


    @Nonnull
    public OWLEntity getEntity() {
        return entityData.getEntity();
    }

    @Nonnull
    public OWLEntityData getEntityData() {
        return entityData;
    }
}
