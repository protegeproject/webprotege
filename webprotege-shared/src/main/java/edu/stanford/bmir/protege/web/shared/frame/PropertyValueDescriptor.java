package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.entity.*;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.Optional;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 27/02/2014
 *
 */
public class PropertyValueDescriptor {

    private OWLPropertyData property;

    private OWLPrimitiveData value;

    private State state = State.ASSERTED;

    private boolean mostSpecific;

    private Set<OWLAxiom> additionalAxioms;

    public PropertyValueDescriptor(OWLPropertyData property, OWLPrimitiveData value, State state, boolean mostSpecific, Set<OWLAxiom> additionalAxioms) {
        this.property = property;
        this.value = value;
        this.state = state;
        this.mostSpecific = mostSpecific;
        this.additionalAxioms = additionalAxioms;
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

    public State getState() {
        return state;
    }

    public OWLPropertyData getProperty() {
        return property;
    }

    public OWLPrimitiveData getValue() {
        return value;
    }

    public boolean isMostSpecific() { return mostSpecific; }

    public Set<OWLAxiom> getAdditionalAxioms() {
        return additionalAxioms;
    }

    public Optional<PropertyValue> toPropertyValue() {
            return property.accept(new OWLPrimitiveDataVisitorAdapter<Optional<PropertyValue>, RuntimeException>() {
                @Override
                protected Optional<PropertyValue> getDefaultReturnValue() {
                    return Optional.empty();
                }

                @Override
                public Optional<PropertyValue> visit(final OWLObjectPropertyData propertyData) throws RuntimeException {
                    return value.accept(new OWLPrimitiveDataVisitorAdapter<Optional<PropertyValue>, RuntimeException>() {
                        @Override
                        public Optional<PropertyValue> visit(OWLClassData data) throws RuntimeException {
                            return Optional.of(PropertyClassValue.get(propertyData, data, state));
                        }

                        @Override
                        public Optional<PropertyValue> visit(OWLNamedIndividualData data) throws RuntimeException {
                            return Optional.of(PropertyIndividualValue.get(propertyData, data, state));
                        }
                    });
                }

                @Override
                public Optional<PropertyValue> visit(final OWLDataPropertyData propertyData) throws RuntimeException {
                    return value.accept(new OWLPrimitiveDataVisitorAdapter<Optional<PropertyValue>, RuntimeException>() {
                        @Override
                        public Optional<PropertyValue> visit(OWLDatatypeData data) throws RuntimeException {
                            return Optional.of(PropertyDatatypeValue.get(propertyData, data, state));
                        }

                        @Override
                        public Optional<PropertyValue> visit(OWLLiteralData data) throws RuntimeException {
                            return Optional.of(PropertyLiteralValue.get(propertyData, data, state));
                        }
                    });
                }

                @Override
                public Optional<PropertyValue> visit(final OWLAnnotationPropertyData propertyData) throws RuntimeException {
                    return value.accept(new OWLPrimitiveDataVisitorAdapter<Optional<PropertyValue>, RuntimeException>() {
                        @Override
                        public Optional<PropertyValue> visit(OWLLiteralData data) throws RuntimeException {
                            return Optional.of(PropertyAnnotationValue.get(propertyData, data, state));
                        }

                        @Override
                        public Optional<PropertyValue> visit(IRIData data) throws RuntimeException {
                            return Optional.of(PropertyAnnotationValue.get(propertyData, data, state));
                        }

                        @Override
                        public Optional<PropertyValue> visit(OWLClassData data) throws RuntimeException {
                            return Optional.of(PropertyAnnotationValue.get(propertyData, data, state));
                        }

                        @Override
                        public Optional<PropertyValue> visit(OWLObjectPropertyData data) throws RuntimeException {
                            return Optional.of(PropertyAnnotationValue.get(propertyData, data, state));
                        }

                        @Override
                        public Optional<PropertyValue> visit(OWLDataPropertyData data) throws RuntimeException {
                            return Optional.of(PropertyAnnotationValue.get(propertyData, data, state));
                        }

                        @Override
                        public Optional<PropertyValue> visit(OWLAnnotationPropertyData data) throws RuntimeException {
                            return Optional.of(PropertyAnnotationValue.get(propertyData, data, state));
                        }

                        @Override
                        public Optional<PropertyValue> visit(OWLNamedIndividualData data) throws RuntimeException {
                            return Optional.of(PropertyAnnotationValue.get(propertyData, data, state));
                        }

                        @Override
                        public Optional<PropertyValue> visit(OWLDatatypeData data) throws RuntimeException {
                            return Optional.of(PropertyAnnotationValue.get(propertyData, data, state));
                        }
                    });
                }
            });
    }
}
