package edu.stanford.bmir.protege.web.shared.hierarchy;


import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 22/03/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class HierarchyId implements IsSerializable {

    public static final HierarchyId CLASS_HIERARCHY = get("Class");

    public static final HierarchyId OBJECT_PROPERTY_HIERARCHY = get("ObjectProperty");

    public static final HierarchyId DATA_PROPERTY_HIERARCHY = get("DataProperty");

    public static final HierarchyId ANNOTATION_PROPERTY_HIERARCHY = get("AnnotationProperty");

    @Nonnull
    public abstract String getId();

    @Nonnull
    public static HierarchyId get(@Nonnull String id) {
        return new AutoValue_HierarchyId(checkNotNull(id));
    }
}
