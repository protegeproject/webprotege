package edu.stanford.bmir.protege.web.shared.hierarchy;


import com.google.gwt.user.client.rpc.IsSerializable;
import org.semanticweb.owlapi.model.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/03/2013
 */
public class HierarchyId<T extends OWLEntity> implements IsSerializable {

    public static final HierarchyId<OWLClass> CLASS_HIERARCHY = get("Class");

    public static final HierarchyId<OWLObjectProperty> OBJECT_PROPERTY_HIERARCHY = get("ObjectProperty");

    public static final HierarchyId<OWLDataProperty> DATA_PROPERTY_HIERARCHY = get("DataProperty");

    public static final HierarchyId<OWLAnnotationProperty> ANNOTATION_PROPERTY_HIERARCHY = get("AnnotationProperty");

    private String id;

    private HierarchyId(String id) {
        this.id = id;
    }

    /**
     * For serialization only
     */
    private HierarchyId() {

    }

    public static <T extends OWLEntity> HierarchyId<T> get(String id) {
        return new HierarchyId<>(checkNotNull(id));
    }

    @Override
    public int hashCode() {
        return "HierarchyId".hashCode() + id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof HierarchyId)) {
            return false;
        }
        HierarchyId other = (HierarchyId) obj;
        return this.id.equals(other.id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HierarchyId");
        sb.append("(");
        sb.append(id);
        sb.append(")");
        return sb.toString();
    }
}
