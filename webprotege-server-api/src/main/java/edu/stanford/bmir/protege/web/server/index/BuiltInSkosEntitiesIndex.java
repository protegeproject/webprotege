package edu.stanford.bmir.protege.web.server.index;

import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-27
 */
public interface BuiltInSkosEntitiesIndex extends BuiltInEntitiesIndex {

    /**
     * Gets the annotation properties in the SKOS vocabulary
     */
    @Nonnull
    Stream<OWLAnnotationProperty> getAnnotationProperties();

    /**
     * Gets the classes in the SKOS vocabulary
     */
    @Nonnull
    Stream<OWLClass> getClasses();

    /**
     * Gets the object properties in the SKOS vocabulary
     */
    @Nonnull
    Stream<OWLObjectProperty> getObjectProperties();

    /**
     * Gets the data properties in the SKOS vocabulary
     */
    @Nonnull
    Stream<OWLDataProperty> getDataProperties();

}
