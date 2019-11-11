package edu.stanford.bmir.protege.web.client.form;

import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-10
 */
public interface FormDataFreshSubjectStrategy {

    Optional<OWLEntity> getFreshSubject();
}
