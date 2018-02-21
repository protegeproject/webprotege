package edu.stanford.bmir.protege.web.shared.hierarchy;


import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 22/03/2013
 */
public class HierarchyId implements IsSerializable {

    public static final HierarchyId CLASS_HIERARCHY = get("Class");

    public static final HierarchyId OBJECT_PROPERTY_HIERARCHY = get("ObjectProperty");

    public static final HierarchyId DATA_PROPERTY_HIERARCHY = get("DataProperty");

    public static final HierarchyId ANNOTATION_PROPERTY_HIERARCHY = get("AnnotationProperty");

    private String id;

    private HierarchyId(@Nonnull String id) {
        this.id = checkNotNull(id);
    }

    @GwtSerializationConstructor
    private HierarchyId() {

    }

    @Nonnull
    public static HierarchyId get(@Nonnull String id) {
        return new HierarchyId(checkNotNull(id));
    }

    @Override
    public int hashCode() {
        return "HierarchyId".hashCode() + id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof HierarchyId)) {
            return false;
        }
        HierarchyId other = (HierarchyId) obj;
        return this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return toStringHelper("HierarchyId")
                .addValue(id)
                .toString();
    }
}
