package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/07/2012
 */
public class Annotation implements Serializable {
    
    private Collection<Annotation> annotations = null;

    private AnnotationProperty annotationProperty;

    private AnnotationPropertyEdgeValue value;

    public Annotation(AnnotationProperty annotationProperty, AnnotationPropertyEdgeValue value) {
        this.annotationProperty = annotationProperty;
        this.value = value;
    }

    public Annotation(Collection<Annotation> annotations, AnnotationProperty annotationProperty, AnnotationPropertyEdgeValue value) {
        this.annotations = new HashSet<Annotation>(annotations);
        this.annotationProperty = annotationProperty;
        this.value = value;
    }

    public AnnotationProperty getAnnotationProperty() {
        return annotationProperty;
    }

    public AnnotationPropertyEdgeValue getAnnotationValue() {
        return value;
    }

    public Collection<Annotation> getAnnotations() {
        if(annotations == null) {
            return Collections.emptySet();
        }
        else {
            return new ArrayList<Annotation>(annotations);
        }
    }


    @Override
    public int hashCode() {
        return "Annotation".hashCode() + annotationProperty.hashCode() + value.hashCode() + getAnnotations().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof Annotation)) {
            return false;
        }
        Annotation other = (Annotation) obj;
        return this.annotationProperty.equals(other.annotationProperty) && this.value.equals(other.value) && this.getAnnotations().equals(other.getAnnotations());
    }
}
