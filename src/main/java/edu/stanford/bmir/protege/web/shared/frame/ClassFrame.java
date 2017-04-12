package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.HasSignature;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
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
@SuppressWarnings("GwtInconsistentSerializableClass" )
public class ClassFrame implements EntityFrame<OWLClassData>, HasSignature, Serializable, HasPropertyValueList, HasPropertyValues, HasAnnotationPropertyValues, HasLogicalPropertyValues {

    private OWLClassData subject;

    private Set<OWLClassData> classEntries;

    private Set<PropertyValue> propertyValues;


    private ClassFrame() {
    }

    public ClassFrame(OWLClassData subject) {
        this(subject, Collections.emptySet(), Collections.emptySet());
    }

    public ClassFrame(OWLClassData subject, Set<OWLClassData> classes, Set<PropertyValue> propertyValues) {
        this.subject = checkNotNull(subject);
        this.classEntries = new HashSet<>(checkNotNull(classes));
        this.propertyValues = new HashSet<>(checkNotNull(propertyValues));
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
    public OWLClassData getSubject() {
        return subject;
    }

    public Set<OWLClassData> getClassEntries() {
        return classEntries;
    }

    public Set<OWLEntity> getSignature() {
        Set<OWLEntity> result = new HashSet<>();
        result.add(subject.getEntity());
        for(OWLClassData entry : classEntries) {
            result.add(entry.getEntity());
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
        return new TreeSet<>(propertyValues);
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
        for(Iterator<OWLClassData> it = classEntries.iterator(); it.hasNext(); ) {
            OWLClassData entry = it.next();
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


    public static Builder builder(OWLClassData subject) {
        return new Builder(subject);
    }

    /**
     * A Builder for SimpleClassFrames.  A builder may be obtained by instantiating one directly (in which case it will
     * be empty) or by instantiating one based on an existing frame where it is populated with classEntries and property
     * values for that frame.  See the {@link ClassFrame#builder()}
     * method.
     */
    public static class Builder {

        private OWLClassData subject;

        // Mutable
        private final Set<OWLClassData> classes = new HashSet<>();

        // Mutable
        private final Set<PropertyValue> propertyValues = new LinkedHashSet<PropertyValue>();

        public Builder(OWLClassData subject) {
            this(subject, Collections.emptySet(), Collections.emptySet());
        }

        public Builder(OWLClassData subject, Set<OWLClassData> classes, Set<PropertyValue> propertyValues) {
            this.subject = subject;
            this.classes.addAll(classes);
            this.propertyValues.addAll(propertyValues);
        }

        public OWLClassData getSubject() {
            return subject;
        }

        /**
         * Sets the subject for the class frame built by this builder.
         * @param subject The subject. Not {@code null}.
         * @throws NullPointerException if {@code subject} is {@code null}.
         */
        public void setSubject(OWLClassData subject) {
            if(subject == null) {
                throw new NullPointerException("subject must not be null");
            }
            this.subject = subject;
        }

        public void addClass(OWLClassData cls) {
            classes.add(cls);
        }

        public void addPropertyValues(Collection<PropertyValue> propertyValue) {
            propertyValues.addAll(propertyValue);
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
