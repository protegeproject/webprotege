package edu.stanford.bmir.protege.web.shared.form.field;

import org.semanticweb.owlapi.model.OWLClass;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public interface IndividualNameFieldDescriptor extends FormFieldDescriptor {

    Optional<OWLClass> getFilteringType();
}
