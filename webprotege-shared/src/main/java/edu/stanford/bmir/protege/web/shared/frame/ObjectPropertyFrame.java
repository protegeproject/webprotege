package edu.stanford.bmir.protege.web.shared.frame;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/12/2012
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class ObjectPropertyFrame implements EntityFrame<OWLObjectPropertyData>, HasAnnotationPropertyValues, Serializable {

    public static ObjectPropertyFrame get(OWLObjectPropertyData subject,
                                          ImmutableList<PropertyAnnotationValue> annotationValues,
                                          ImmutableList<OWLClassData> domains,
                                          ImmutableList<OWLClassData> ranges,
                                          ImmutableList<OWLObjectPropertyData> inverseProperties,
                                          ImmutableList<ObjectPropertyCharacteristic> characteristics) {

        return new AutoValue_ObjectPropertyFrame(subject,
                                                 annotationValues,
                                                 domains,
                                                 ranges,
                                                 characteristics,
                                                 inverseProperties);
    }

    public abstract OWLObjectPropertyData getSubject();

    @Override
    public abstract ImmutableList<PropertyAnnotationValue> getAnnotationPropertyValues();

    public abstract ImmutableList<OWLClassData> getDomains();

    public abstract ImmutableList<OWLClassData> getRanges();

    public abstract ImmutableList<ObjectPropertyCharacteristic> getCharacteristics();

    public abstract ImmutableList<OWLObjectPropertyData> getInverseProperties();
}
