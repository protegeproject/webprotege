package edu.stanford.bmir.protege.web.server.project.chg;

import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeVisitor;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-28
 */
public class OntologyChangeTranslator {

    @Nonnull
    private final OntologyChangeTranslatorVisitor visitor;

    @Inject
    public OntologyChangeTranslator(@Nonnull OntologyChangeTranslatorVisitor visitor) {
        this.visitor = checkNotNull(visitor);
    }

    @Nonnull
    public OWLOntologyChange toOwlOntologyChange(@Nonnull OntologyChange change) {
        return change.accept(visitor);
    }
}
