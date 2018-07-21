package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.HasSignature;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.OWLEntity;

import java.io.Serializable;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
public class PropertyValueList implements Serializable, HasSignature, HasPropertyValues, HasAnnotationPropertyValues, HasLogicalPropertyValues {

    private ImmutableSet<PropertyValue> propertyValues;

    private PropertyValueList() {
    }

    public PropertyValueList(Collection<? extends PropertyValue> propertyValues) {
        this.propertyValues = ImmutableSet.copyOf(checkNotNull(propertyValues));
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
        return MoreObjects.toStringHelper("PropertyValueList")
                          .addValue(propertyValues)
                          .toString();
    }

    public ImmutableSet<PropertyValue> getPropertyValues() {
        return propertyValues;
    }

    @Override
    public ImmutableSet<PropertyAnnotationValue> getAnnotationPropertyValues() {
        return propertyValues.stream()
                             .filter(PropertyValue::isAnnotation)
                             .map(pv -> (PropertyAnnotationValue) pv)
                             .collect(toImmutableSet());
    }

    @Override
    public ImmutableList<PropertyValue> getLogicalPropertyValues() {
        return propertyValues.stream()
                             .filter(PropertyValue::isLogical)
                             .collect(toImmutableList());
    }

    /**
     * Gets the signature of the object that implements this interface.
     *
     * @return A set of entities that represent the signature of this object
     */
    @Override
    public Set<OWLEntity> getSignature() {
        Set<OWLEntity> result = new HashSet<OWLEntity>();
        for (PropertyValue propertyValue : propertyValues) {
            result.addAll(propertyValue.getProperty().getSignature());
            if (propertyValue instanceof PropertyAnnotationValue) {
                Optional<OWLEntityData> entityValue = ((PropertyAnnotationValue) propertyValue).getValueAsEntity();
                if (entityValue.isPresent()) {
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
