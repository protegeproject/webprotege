package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.HasSignature;
import org.semanticweb.owlapi.model.*;

import java.io.Serializable;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/11/2012
 * <p>
 *     A class frame describes some class in terms of other classEntries and property values.
 * </p>
 */
public class ClassFrame implements EntityFrame<OWLClass>, HasSignature, Serializable, HasPropertyValueList, HasPropertyValues, HasAnnotationPropertyValues, HasLogicalPropertyValues {

    private OWLClass subject;

    private Set<OWLClass> classEntries;

    private Set<PropertyValue> propertyValues;


    private ClassFrame() {
    }

    public ClassFrame(OWLClass subject) {
        this(subject, Collections.<OWLClass>emptySet(), Collections.<PropertyValue>emptySet());
    }

    public ClassFrame(OWLClass subject, Set<OWLClass> classes, Set<PropertyValue> propertyValues) {
        this.subject = checkNotNull(subject);
        this.classEntries = checkNotNull(classes);
        this.propertyValues = checkNotNull(propertyValues);
    }

    /**
     * Gets a Builder which can be used to create a new frame based on this one.
     * @return A builder initialised with the content of this frame.
     */
    public Builder builder() {
        return new Builder(subject, classEntries, propertyValues);
    }


    /**
     * Gets the subject of this class frame.
     * @return The subject.  Not {@code null}.
     */
    public OWLClass getSubject() {
        return subject;
    }

    public Set<OWLClass> getClassEntries() {
        return classEntries;
    }

    public Set<OWLEntity> getSignature() {
        Set<OWLEntity> result = new HashSet<OWLEntity>();
        result.add(subject);
        for(OWLClass entry : classEntries) {
            result.add(entry);
        }
        for(PropertyValue propertyValue : propertyValues) {
            result.addAll(propertyValue.getProperty().getSignature());
            result.addAll(propertyValue.getValue().getSignature());
        }

        return result;
    }

    @Override
    public PropertyValueList getPropertyValueList() {
        return new PropertyValueList(propertyValues);
    }

    /**
     * Gets the {@link PropertyValue}s in this frame.
     * @return The (possibly empty) set of property values in this frame. Not {@code null}.  The returned set is unmodifiable.
     */
    public Set<PropertyValue> getPropertyValues() {
        return propertyValues;
    }

    public Set<PropertyAnnotationValue> getAnnotationPropertyValues() {
        Set<PropertyAnnotationValue> result = new LinkedHashSet<PropertyAnnotationValue>();
        for(PropertyValue propertyValue : propertyValues) {
            if(propertyValue.isAnnotation()) {
                result.add((PropertyAnnotationValue) propertyValue);
            }
        }
        return result;
    }

    public Set<PropertyValue> getLogicalPropertyValues() {
        Set<PropertyValue> result = new LinkedHashSet<PropertyValue>();
        for(PropertyValue propertyValue : propertyValues) {
            if(propertyValue.isLogical()) {
                result.add(propertyValue);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ClassFrame");
        sb.append("(");
        sb.append(subject);
        sb.append(" Classes(");
        for(Iterator<OWLClass> it = classEntries.iterator(); it.hasNext(); ) {
            OWLClass entry = it.next();
            sb.append(entry);
            if(it.hasNext()) {
                sb.append(" ");
            }
        }
        sb.append(") ");
        sb.append("PropertyValues(");
        for(Iterator<PropertyValue> it = propertyValues.iterator(); it.hasNext(); ) {
            PropertyValue pv = it.next();
            sb.append(pv);
            if(it.hasNext()) {
                sb.append(" ");
            }
        }
        sb.append(")");
        sb.append(")");
        return sb.toString();
    }


    @Override
    public int hashCode() {
        return "SimpleClassFrame".hashCode() + subject.hashCode() + classEntries.hashCode() + propertyValues.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ClassFrame)) {
            return false;
        }
        ClassFrame other = (ClassFrame) obj;
        return this.subject.equals(other.subject) && this.classEntries.equals(other.classEntries) && this.propertyValues.equals(other.propertyValues);
    }


    public static Builder builder(OWLClass subject) {
        return new Builder(subject);
    }

    public List<ClassFrameChange> getChangesFrom(ClassFrame classFrame) {
        if(subject.equals(classFrame.getSubject())) {
            throw new RuntimeException("subject of classFrame must be equal to the subject of caller class frame");
        }

        List<ClassFrameChange> result = new ArrayList<ClassFrameChange>();

        Set<PropertyValue> addedPropertyValues = new HashSet<PropertyValue>(propertyValues);
        addedPropertyValues.removeAll(classFrame.getPropertyValues());

        for(PropertyValue addedPropertyValue : addedPropertyValues) {
            result.add(new AddPropertyValueChange(subject, addedPropertyValue));
        }

        Set<PropertyValue> removedPropertyValues = new HashSet<PropertyValue>(classFrame.getPropertyValues());
        removedPropertyValues.removeAll(propertyValues);

        for(PropertyValue removedPropertyValue : removedPropertyValues) {
            result.add(new RemovePropertyValueChange(subject, removedPropertyValue));
        }

        return result;


    }


    /**
     * A Builder for SimpleClassFrames.  A builder may be obtained by instantiating one directly (in which case it will
     * be empty) or by instantiating one based on an existing frame where it is populated with classEntries and property
     * values for that frame.  See the {@link ClassFrame#builder()}
     * method.
     */
    public static class Builder {

        private OWLClass subject;

        // Mutable
        private final Set<OWLClass> classes = new HashSet<OWLClass>();

        // Mutable
        private final Set<PropertyValue> propertyValues = new LinkedHashSet<PropertyValue>();

        public Builder(OWLClass subject) {
            this(subject, Collections.<OWLClass>emptySet(), Collections.<PropertyValue>emptySet());
        }

        public Builder(OWLClass subject, Set<OWLClass> classes, Set<PropertyValue> propertyValues) {
            this.subject = subject;
            this.classes.addAll(classes);
            this.propertyValues.addAll(propertyValues);
        }

        public OWLClass getSubject() {
            return subject;
        }

        /**
         * Sets the subject for the class frame built by this builder.
         * @param subject The subject. Not {@code null}.
         * @throws NullPointerException if {@code subject} is {@code null}.
         */
        public void setSubject(OWLClass subject) {
            if(subject == null) {
                throw new NullPointerException("subject must not be null");
            }
            this.subject = subject;
        }

        public void addClass(OWLClass cls) {
            classes.add(cls);
        }

        public void addPropertyValue(PropertyValue propertyValue) {
            propertyValues.add(propertyValue);
        }

        public void addPropertyValues(Collection<PropertyValue> propertyValue) {
            propertyValues.addAll(propertyValue);
        }

        public void addPropertyValue(OWLObjectProperty property, OWLClass value, PropertyValueState propertyValueState) {
            addPropertyValue(new PropertyClassValue(property, value, propertyValueState));
        }

        public void addPropertyValue(OWLObjectProperty property, OWLNamedIndividual value, PropertyValueState propertyValueState) {
            addPropertyValue(new PropertyIndividualValue(property, value, propertyValueState));
        }

        public void addPropertyValue(OWLDataProperty property, OWLDatatype value, PropertyValueState propertyValueState) {
            addPropertyValue(new PropertyDatatypeValue(property, value, propertyValueState));
        }

        public void addPropertyValue(OWLDataProperty property, OWLLiteral value, PropertyValueState propertyValueState) {
            addPropertyValue(new PropertyLiteralValue(property, value, propertyValueState));
        }

        public void addPropertyValue(OWLDataProperty property, int value, PropertyValueState propertyValueState) {
            addPropertyValue(property, DataFactory.getOWLLiteral(value), propertyValueState);
        }

        public void addPropertyValue(OWLDataProperty property, double value, PropertyValueState propertyValueState) {
            addPropertyValue(property, DataFactory.getOWLLiteral(value), propertyValueState);
        }

        public void addPropertyValue(OWLDataProperty property, boolean value, PropertyValueState propertyValueState) {
            addPropertyValue(property, DataFactory.getOWLLiteral(value), propertyValueState);
        }

        public void addPropertyValue(OWLDataProperty property, String value, PropertyValueState propertyValueState) {
            addPropertyValue(property, DataFactory.getOWLLiteral(value), propertyValueState);
        }

        public void addPropertyValue(OWLAnnotationProperty property, OWLAnnotationValue value, PropertyValueState propertyValueState) {
            addPropertyValue(new PropertyAnnotationValue(property, value, propertyValueState));
        }

        /**
         * Builds a class frame from information held by this builder.
         * @return The class frame.  Not {@code null}.
         */
        public ClassFrame build() {
            return new ClassFrame(subject, classes, propertyValues);
        }

    }

}
