package edu.stanford.bmir.protege.web.shared.form.field;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.OWLClass;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public interface ClassNameFieldDescriptor extends FormFieldDescriptor {

    Optional<OWLClass> getFilteringSuperClass();
}
