package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.apache.commons.lang.NotImplementedException;
import org.semanticweb.owlapi.change.*;

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
        throw new NotImplementedException();
//        var ontologyDocumentId = record.getOntologyID();
//        OWLOntologyChangeDataVisitor<OntologyChange, RuntimeException> visitor = new OWLOntologyChangeDataVisitor<>() {
//            @Nonnull
//            @Override
//            public OntologyChange visit(AddAxiomData data) throws RuntimeException {
//                return translateAddAxiom(data, ontologyDocumentId);
//            }
//
//            @Override
//            public OntologyChange visit(RemoveAxiomData data) throws RuntimeException {
//                return translateRemoveAxiom(data, ontologyDocumentId);
//            }
//
//            @Override
//            public OntologyChange visit(AddOntologyAnnotationData data) throws RuntimeException {
//                return translateAddOntologyAnnotation(data, ontologyDocumentId);
//            }
//
//            @Override
//            public OntologyChange visit(RemoveOntologyAnnotationData data) throws RuntimeException {
//                return translateRemoveOntologyAnnotation(data, ontologyDocumentId);
//            }
//
//            @Override
//            public OntologyChange visit(SetOntologyIDData data) throws RuntimeException {
//                throw new UnsupportedOperationException();
//            }
//
//            @Override
//            public OntologyChange visit(AddImportData data) throws RuntimeException {
//                return translateAddImport(data, ontologyDocumentId);
//            }
//
//            @Override
//            public OntologyChange visit(RemoveImportData data) throws RuntimeException {
//                return translateRemoveImport(data, ontologyDocumentId);
//            }
//        };
//        return record.getData()
//                     .accept(visitor);
    }

    private AddAxiomChange translateAddAxiom(AddAxiomData data, OntologyDocumentId ontologyDocumentId) {
        return AddAxiomChange.of(ontologyDocumentId, data.getAxiom());
    }

    private RemoveAxiomChange translateRemoveAxiom(RemoveAxiomData data, OntologyDocumentId ontologyDocumentId) {
        return RemoveAxiomChange.of(ontologyDocumentId, data.getAxiom());
    }

    private AddOntologyAnnotationChange translateAddOntologyAnnotation(AddOntologyAnnotationData data,
                                                                       OntologyDocumentId ontologyDocumentId) {
        return AddOntologyAnnotationChange.of(ontologyDocumentId, data.getAnnotation());
    }

    private RemoveOntologyAnnotationChange translateRemoveOntologyAnnotation(RemoveOntologyAnnotationData data,
                                                                             OntologyDocumentId ontologyDocumentId) {
        return RemoveOntologyAnnotationChange.of(ontologyDocumentId, data.getAnnotation());
    }

    private AddImportChange translateAddImport(AddImportData data, OntologyDocumentId ontologyId) {
        return AddImportChange.of(ontologyId, data.getDeclaration());
    }

    private RemoveImportChange translateRemoveImport(RemoveImportData data, OntologyDocumentId ontologyId) {
        return RemoveImportChange.of(ontologyId, data.getDeclaration());
    }
}
