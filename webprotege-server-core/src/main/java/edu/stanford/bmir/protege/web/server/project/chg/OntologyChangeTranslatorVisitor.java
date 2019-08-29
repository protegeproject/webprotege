package edu.stanford.bmir.protege.web.server.project.chg;

import edu.stanford.bmir.protege.web.server.change.*;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-28
 */
public class OntologyChangeTranslatorVisitor implements OntologyChangeVisitorEx<OWLOntologyChange> {

    @Nonnull
    private final OWLOntologyManager manager;

    @Inject
    public OntologyChangeTranslatorVisitor(@Nonnull OWLOntologyManager manager) {
        this.manager = checkNotNull(manager);
    }

    @Override
    public OWLOntologyChange getDefaultReturnValue() {
        throw new RuntimeException();
    }

    @Override
    public OWLOntologyChange visit(@Nonnull AddAxiomChange change) {
        return new AddAxiom(getOntology(change), change.getAxiom());
    }

    @Override
    public OWLOntologyChange visit(@Nonnull RemoveAxiomChange change) {
        return new RemoveAxiom(getOntology(change), change.getAxiom());
    }

    @Override
    public OWLOntologyChange visit(@Nonnull AddOntologyAnnotationChange change) {
        return new AddOntologyAnnotation(getOntology(change), change.getAnnotation());
    }

    @Override
    public OWLOntologyChange visit(@Nonnull RemoveOntologyAnnotationChange change) {
        return new RemoveOntologyAnnotation(getOntology(change), change.getAnnotation());
    }

    @Override
    public OWLOntologyChange visit(@Nonnull AddImportChange change) {
        return new AddImport(getOntology(change), change.getImportsDeclaration());
    }

    @Override
    public OWLOntologyChange visit(@Nonnull RemoveImportChange change) {
        return new RemoveImport(getOntology(change), change.getImportsDeclaration());
    }



    private OWLOntology getOntology(OntologyChange change) {
        var ontologyId = change.getOntologyId();
        var ontology = manager.getOntology(ontologyId);
        if(ontology == null) {
            throw new UnknownOWLOntologyException(ontologyId);
        }
        else {
            return ontology;
        }
    }
}
