package edu.stanford.bmir.protege.web.server.change;

import org.semanticweb.owlapi.change.*;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-16
 */
public class OntologyChangeRecordTranslatorImpl implements OntologyChangeRecordTranslator {

    @Inject
    public OntologyChangeRecordTranslatorImpl() {
    }

    @Nonnull
    @Override
    public OntologyChange getOntologyChange(@Nonnull OWLOntologyChangeRecord record) {
        var ontologyId = record.getOntologyID();
        OWLOntologyChangeDataVisitor<OntologyChange, RuntimeException> visitor = new OWLOntologyChangeDataVisitor<>() {
            @Nonnull
            @Override
            public OntologyChange visit(AddAxiomData data) throws RuntimeException {
                return translateAddAxiom(data, ontologyId);
            }

            @Override
            public OntologyChange visit(RemoveAxiomData data) throws RuntimeException {
                return translateRemoveAxiom(data, ontologyId);
            }

            @Override
            public OntologyChange visit(AddOntologyAnnotationData data) throws RuntimeException {
                return translateAddOntologyAnnotation(data, ontologyId);
            }

            @Override
            public OntologyChange visit(RemoveOntologyAnnotationData data) throws RuntimeException {
                return translateRemoveOntologyAnnotation(data, ontologyId);
            }

            @Override
            public OntologyChange visit(SetOntologyIDData data) throws RuntimeException {
                throw new UnsupportedOperationException();
            }

            @Override
            public OntologyChange visit(AddImportData data) throws RuntimeException {
                return translateAddImport(data, ontologyId);
            }

            @Override
            public OntologyChange visit(RemoveImportData data) throws RuntimeException {
                return translateRemoveImport(data, ontologyId);
            }
        };
        return record.getData()
                     .accept(visitor);
    }

    private AddAxiomChange translateAddAxiom(AddAxiomData data, OWLOntologyID ontologyId) {
        return AddAxiomChange.of(ontologyId, data.getAxiom());
    }

    private RemoveAxiomChange translateRemoveAxiom(RemoveAxiomData data, OWLOntologyID ontologyId) {
        return RemoveAxiomChange.of(ontologyId, data.getAxiom());
    }

    private AddOntologyAnnotationChange translateAddOntologyAnnotation(AddOntologyAnnotationData data,
                                                                       OWLOntologyID ontologyId) {
        return AddOntologyAnnotationChange.of(ontologyId, data.getAnnotation());
    }

    private RemoveOntologyAnnotationChange translateRemoveOntologyAnnotation(RemoveOntologyAnnotationData data,
                                                                             OWLOntologyID ontologyId) {
        return RemoveOntologyAnnotationChange.of(ontologyId, data.getAnnotation());
    }

    private AddImportChange translateAddImport(AddImportData data, OWLOntologyID ontologyId) {
        return AddImportChange.of(ontologyId, data.getDeclaration());
    }

    private RemoveImportChange translateRemoveImport(RemoveImportData data, OWLOntologyID ontologyId) {
        return RemoveImportChange.of(ontologyId, data.getDeclaration());
    }
}
