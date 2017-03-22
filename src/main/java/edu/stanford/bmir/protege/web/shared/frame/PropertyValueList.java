package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.HasSignature;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.OWLEntity;

import java.io.Serializable;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
public class PropertyValueList implements Serializable, HasSignature, HasPropertyValues, HasAnnotationPropertyValues, HasLogicalPropertyValues {

    private List<PropertyValue> propertyValues;

    private PropertyValueList() {
    }

    public PropertyValueList(Collection<? extends PropertyValue> propertyValues) {
        this.propertyValues = new ArrayList<>(checkNotNull(propertyValues));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(propertyValues);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PropertyValueList)) {
            return false;
        }
        PropertyValueList other = (PropertyValueList) obj;
        return this.propertyValues.equals(other.propertyValues);
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("PropertyValueList")
                .addValue(propertyValues)
                .toString();
    }

    public Set<PropertyValue> getPropertyValues() {
        return new LinkedHashSet<PropertyValue>(propertyValues);
    }

    @Override
    public Set<PropertyAnnotationValue> getAnnotationPropertyValues() {
        Set<PropertyAnnotationValue> result = new LinkedHashSet<PropertyAnnotationValue>();
        for(PropertyValue propertyValue : propertyValues) {
            if(propertyValue.isAnnotation()) {
                result.add((PropertyAnnotationValue) propertyValue);
            }
        }
        return result;
    }

    @Override
    public Set<PropertyValue> getLogicalPropertyValues() {
        Set<PropertyValue> result = new LinkedHashSet<PropertyValue>();
        for(PropertyValue propertyValue : propertyValues) {
            if(propertyValue.isLogical()) {
                result.add(propertyValue);
            }
        }
        return result;
    }

    /**
     * Gets the signature of the object that implements this interface.
     * @return A set of entities that represent the signature of this object
     */
    @Override
    public Set<OWLEntity> getSignature() {
        Set<OWLEntity> result = new HashSet<OWLEntity>();
        for(PropertyValue propertyValue : propertyValues) {
            result.addAll(propertyValue.getProperty().getSignature());
            if(propertyValue instanceof PropertyAnnotationValue) {
                Optional<OWLEntityData> entityValue = ((PropertyAnnotationValue) propertyValue).getValueAsEntity();
                if(entityValue.isPresent()) {
                    result.add(entityValue.get().getEntity());
                }
            }
            else {
                result.addAll(propertyValue.getValue().getSignature());
            }
        }
        return result;
    }
}
