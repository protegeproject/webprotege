package edu.stanford.bmir.protege.web.server.change;

import org.semanticweb.owlapi.model.OWLOntologyChange;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-29
 */
public class OwlOntologyChangeTranslator {

    @Nonnull
    private final OwlOntologyChangeTranslatorVisitor visitor;

    @Inject
    public OwlOntologyChangeTranslator(@Nonnull OwlOntologyChangeTranslatorVisitor visitor) {
        this.visitor = checkNotNull(visitor);
    }

    @Nonnull
    public OntologyChange toOntologyChange(@Nonnull OWLOntologyChange change) {
        return change.accept(visitor);
    }
}
