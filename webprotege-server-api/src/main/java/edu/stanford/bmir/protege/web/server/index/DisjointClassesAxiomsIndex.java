package edu.stanford.bmir.protege.web.server.index;


import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-22
 */
@ProjectSingleton
public interface DisjointClassesAxiomsIndex extends Index {

    @Nonnull
    Stream<OWLDisjointClassesAxiom> getDisjointClassesAxioms(@Nonnull OWLClass cls,
                                                             @Nonnull OntologyDocumentId ontologyDocumentId);
}
