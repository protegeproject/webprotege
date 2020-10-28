package edu.stanford.bmir.protege.web.server.change;

import org.apache.commons.lang.NotImplementedException;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-29
 */
public class OwlOntologyChangeTranslatorVisitor implements OWLOntologyChangeVisitorEx<OntologyChange> {

    @Inject
    public OwlOntologyChangeTranslatorVisitor() {
    }

    @Nonnull
    @Override
    public OntologyChange visit(@Nonnull AddAxiom change) {
//        return AddAxiomChange.of(change.getOntology().getOntologyID(),
//                                 change.getAxiom());
        throw new NotImplementedException();
    }

    @Nonnull
    @Override
    public OntologyChange visit(@Nonnull RemoveAxiom change) {
        throw new NotImplementedException();
    }

    @Nonnull
    @Override
    public OntologyChange visit(@Nonnull SetOntologyID change) {
        throw new NotImplementedException();
    }

    @Nonnull
    @Override
    public OntologyChange visit(@Nonnull AddImport change) {
        throw new NotImplementedException();
    }

    @Nonnull
    @Override
    public OntologyChange visit(@Nonnull RemoveImport change) {
        throw new NotImplementedException();
    }

    @Nonnull
    @Override
    public OntologyChange visit(@Nonnull AddOntologyAnnotation change) {
        throw new NotImplementedException();
    }

    @Nonnull
    @Override
    public OntologyChange visit(@Nonnull RemoveOntologyAnnotation change) {
        throw new NotImplementedException();
    }
}
