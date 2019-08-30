package edu.stanford.bmir.protege.web.server.change;

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
        return AddAxiomChange.of(change.getOntology().getOntologyID(),
                                 change.getAxiom());
    }

    @Nonnull
    @Override
    public OntologyChange visit(@Nonnull RemoveAxiom change) {
        return RemoveAxiomChange.of(change.getOntology().getOntologyID(),
                                    change.getAxiom());
    }

    @Nonnull
    @Override
    public OntologyChange visit(@Nonnull SetOntologyID change) {
        throw new UnsupportedOperationException("SetOntologyID changes are not supported");
    }

    @Nonnull
    @Override
    public OntologyChange visit(@Nonnull AddImport change) {
        return AddImportChange.of(change.getOntology().getOntologyID(),
                                  change.getImportDeclaration());
    }

    @Nonnull
    @Override
    public OntologyChange visit(@Nonnull RemoveImport change) {
        return RemoveImportChange.of(change.getOntology().getOntologyID(),
                                     change.getImportDeclaration());
    }

    @Nonnull
    @Override
    public OntologyChange visit(@Nonnull AddOntologyAnnotation change) {
        return AddOntologyAnnotationChange.of(change.getOntology().getOntologyID(),
                                              change.getAnnotation());
    }

    @Nonnull
    @Override
    public OntologyChange visit(@Nonnull RemoveOntologyAnnotation change) {
        return RemoveOntologyAnnotationChange.of(change.getOntology().getOntologyID(),
                                                 change.getAnnotation());
    }
}
