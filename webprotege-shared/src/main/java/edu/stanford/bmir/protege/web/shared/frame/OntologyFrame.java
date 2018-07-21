package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.HasSignature;
import org.semanticweb.owlapi.model.HasShortForm;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.io.Serializable;
import java.util.Set;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/01/2013
 */
public class OntologyFrame implements HasSignature, Serializable, HasPropertyValueList, HasPropertyValues, HasAnnotationPropertyValues, HasShortForm {

    private String shortForm;

    private OWLOntologyID subject;

    private PropertyValueList propertyValueList;

    private OntologyFrame() {
    }

    public OntologyFrame(OWLOntologyID subject, PropertyValueList propertyValueList, String shortForm) {
        this.subject = checkNotNull(subject);
        this.propertyValueList = checkNotNull(propertyValueList);
        this.shortForm = checkNotNull(shortForm);
    }

    public String getShortForm() {
        return shortForm;
    }

    @Override
    public ImmutableSet<PropertyAnnotationValue> getAnnotationPropertyValues() {
        return propertyValueList.getAnnotationPropertyValues();
    }

    @Override
    public PropertyValueList getPropertyValueList() {
        return propertyValueList;
    }

    @Override
    public ImmutableSet<PropertyValue> getPropertyValues() {
        return propertyValueList.getPropertyValues();
    }

    /**
     * Gets the signature of the object that implements this interface.
     * @return A set of entities that represent the signature of this object
     */
    @Override
    public Set<OWLEntity> getSignature() {
        return propertyValueList.getSignature();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(subject, propertyValueList, shortForm);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof OntologyFrame)) {
            return false;
        }
        OntologyFrame other = (OntologyFrame) obj;
        return this.subject.equals(other.subject) && this.propertyValueList.equals(other.propertyValueList) && this.shortForm.equals(other.shortForm);
    }


    @Override
    public String toString() {
        return toStringHelper("OntologyFrame")
                .addValue(subject)
                .addValue(propertyValueList)
                .add("shortForm", shortForm)
                .toString();
    }
}
