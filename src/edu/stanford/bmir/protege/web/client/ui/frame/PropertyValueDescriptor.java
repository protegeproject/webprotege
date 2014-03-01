package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.frame.*;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 27/02/2014
 */
public class PropertyValueDescriptor {

    private OWLPropertyData property;

    private OWLPrimitiveData value;

    private PropertyValueState state = PropertyValueState.ASSERTED;

    private boolean mostSpecific;

    public PropertyValueDescriptor(OWLPropertyData property, OWLPrimitiveData value, PropertyValueState state, boolean mostSpecific) {
        this.property = property;
        this.value = value;
        this.state = state;
        this.mostSpecific = mostSpecific;
    }

    @Override
    public int hashCode() {
        return "PropertyValueDescriptor".hashCode() + property.hashCode() + value.hashCode() + state.hashCode() + (mostSpecific ? 1 : 0);
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof PropertyValueDescriptor)) {
            return false;
        }
        PropertyValueDescriptor other = (PropertyValueDescriptor) o;
        return this.property.equals(other.property) &&
                this.value.equals(other.value) &&
                this.state == other.state &&
                this.mostSpecific == other.mostSpecific;
    }

    public PropertyValueState getState() {
        return state;
    }

    public OWLPropertyData getProperty() {
        return property;
    }

    public OWLPrimitiveData getValue() {
        return value;
    }

    public boolean isMostSpecific() { return mostSpecific; }

    public Optional<PropertyValue> toPropertyValue() {
            return property.accept(new OWLPrimitiveDataVisitorAdapter<Optional<PropertyValue>, RuntimeException>() {
                @Override
                protected Optional<PropertyValue> getDefaultReturnValue() {
                    return Optional.absent();
                }

                @Override
                public Optional<PropertyValue> visit(final OWLObjectPropertyData propertyData) throws RuntimeException {
                    return value.accept(new OWLPrimitiveDataVisitorAdapter<Optional<PropertyValue>, RuntimeException>() {
                        @Override
                        public Optional<PropertyValue> visit(OWLClassData data) throws RuntimeException {
                            return Optional.<PropertyValue>of(new PropertyClassValue(propertyData.getEntity(), data.getEntity(), state));
                        }

                        @Override
                        public Optional<PropertyValue> visit(OWLNamedIndividualData data) throws RuntimeException {
                            return Optional.<PropertyValue>of(new PropertyIndividualValue(propertyData.getEntity(), data.getEntity(), state));
                        }
                    });
                }

                @Override
                public Optional<PropertyValue> visit(final OWLDataPropertyData propertyData) throws RuntimeException {
                    return value.accept(new OWLPrimitiveDataVisitorAdapter<Optional<PropertyValue>, RuntimeException>() {
                        @Override
                        public Optional<PropertyValue> visit(OWLDatatypeData data) throws RuntimeException {
                            return Optional.<PropertyValue>of(new PropertyDatatypeValue(propertyData.getEntity(), data.getEntity(), state));
                        }

                        @Override
                        public Optional<PropertyValue> visit(OWLLiteralData data) throws RuntimeException {
                            return Optional.<PropertyValue>of(new PropertyLiteralValue(propertyData.getEntity(), data.getLiteral(), state));
                        }
                    });
                }

                @Override
                public Optional<PropertyValue> visit(final OWLAnnotationPropertyData propertyData) throws RuntimeException {
                    return value.accept(new OWLPrimitiveDataVisitorAdapter<Optional<PropertyValue>, RuntimeException>() {
                        @Override
                        public Optional<PropertyValue> visit(OWLLiteralData data) throws RuntimeException {
                            return Optional.<PropertyValue>of(new PropertyAnnotationValue(propertyData.getEntity(), data.getLiteral(), state));
                        }

                        @Override
                        public Optional<PropertyValue> visit(IRIData data) throws RuntimeException {
                            return Optional.<PropertyValue>of(new PropertyAnnotationValue(propertyData.getEntity(), data.getObject(), state));
                        }

                        @Override
                        public Optional<PropertyValue> visit(OWLClassData data) throws RuntimeException {
                            return Optional.<PropertyValue>of(new PropertyAnnotationValue(propertyData.getEntity(), data.getEntity(), state));
                        }

                        @Override
                        public Optional<PropertyValue> visit(OWLObjectPropertyData data) throws RuntimeException {
                            return Optional.<PropertyValue>of(new PropertyAnnotationValue(propertyData.getEntity(), data.getEntity(), state));
                        }

                        @Override
                        public Optional<PropertyValue> visit(OWLDataPropertyData data) throws RuntimeException {
                            return Optional.<PropertyValue>of(new PropertyAnnotationValue(propertyData.getEntity(), data.getEntity(), state));
                        }

                        @Override
                        public Optional<PropertyValue> visit(OWLAnnotationPropertyData data) throws RuntimeException {
                            return Optional.<PropertyValue>of(new PropertyAnnotationValue(propertyData.getEntity(), data.getEntity(), state));
                        }

                        @Override
                        public Optional<PropertyValue> visit(OWLNamedIndividualData data) throws RuntimeException {
                            return Optional.<PropertyValue>of(new PropertyAnnotationValue(propertyData.getEntity(), data.getEntity(), state));
                        }

                        @Override
                        public Optional<PropertyValue> visit(OWLDatatypeData data) throws RuntimeException {
                            return Optional.<PropertyValue>of(new PropertyAnnotationValue(propertyData.getEntity(), data.getEntity(), state));
                        }
                    });
                }
            });
    }
}
