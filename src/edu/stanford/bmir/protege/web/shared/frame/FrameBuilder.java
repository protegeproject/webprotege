package edu.stanford.bmir.protege.web.shared.frame;

import org.semanticweb.owlapi.model.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
public interface FrameBuilder<S extends OWLEntity> {

    public OWLClass getSubject();

    /**
     * Sets the subject for the class frame built by this builder.
     * @param subject The subject. Not {@code null}.
     * @throws NullPointerException if {@code subject} is {@code null}.
     */
    void setSubject(OWLClass subject);


    void addClass(OWLClass cls);

    void addPropertyValue(PropertyValue propertyValue);

    void addPropertyValue(OWLObjectProperty property, OWLClass value);

    void addPropertyValue(OWLObjectProperty property, OWLNamedIndividual value);

    void addPropertyValue(OWLDataProperty property, OWLDatatype value);

    void addPropertyValue(OWLDataProperty property, OWLLiteral value);

    void addPropertyValue(OWLDataProperty property, int value);

    void addPropertyValue(OWLDataProperty property, double value);

    void addPropertyValue(OWLDataProperty property, boolean value);

    void addPropertyValue(OWLDataProperty property, String value);

    void addPropertyValue(OWLAnnotationProperty property, OWLAnnotationValue value);

    /**
     * Builds a class frame from information held by this builder.
     * @return The class frame.  Not {@code null}.
     */
    public ClassFrame build();
}
