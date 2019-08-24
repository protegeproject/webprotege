package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.server.index.OntologyIndex;
import org.semanticweb.owlapi.change.*;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-16
 */
public class OntologyChangeRecordTranslatorImpl implements OntologyChangeRecordTranslator {

    @Nonnull
    private final OntologyIndex ontologyIndex;

    @Inject
    public OntologyChangeRecordTranslatorImpl(@Nonnull OntologyIndex ontologyIndex) {
        this.ontologyIndex = checkNotNull(ontologyIndex);
    }

    @Nonnull
    @Override
    public OWLOntologyChange getOntologyChange(@Nonnull OWLOntologyChangeRecord record) {
        var ontologyId = record.getOntologyID();
        OWLOntologyChangeDataVisitor<OWLOntologyChange, RuntimeException> visitor = new OWLOntologyChangeDataVisitor<>() {
            @Nonnull
            @Override
            public OWLOntologyChange visit(AddAxiomData data) throws RuntimeException {
                return translateAddAxiom(data, ontologyId);
            }

            @Override
            public OWLOntologyChange visit(RemoveAxiomData data) throws RuntimeException {
                return translateRemoveAxiom(data, ontologyId);
            }

            @Override
            public OWLOntologyChange visit(AddOntologyAnnotationData data) throws RuntimeException {
                return translateAddOntologyAnnotation(data, ontologyId);
            }

            @Override
            public OWLOntologyChange visit(RemoveOntologyAnnotationData data) throws RuntimeException {
                return translateRemoveOntologyAnnotation(data, ontologyId);
            }

            @Override
            public OWLOntologyChange visit(SetOntologyIDData data) throws RuntimeException {
                return translateSetOntologyId(data, ontologyId);
            }

            @Override
            public OWLOntologyChange visit(AddImportData data) throws RuntimeException {
                return translateAddImport(data, ontologyId);
            }

            @Override
            public OWLOntologyChange visit(RemoveImportData data) throws RuntimeException {
                return translateRemoveImport(data, ontologyId);
            }
        };
        return record.getData()
                     .accept(visitor);
    }

    private AddAxiom translateAddAxiom(AddAxiomData data, OWLOntologyID ontologyId) {
        return ontologyIndex.getOntology(ontologyId)
                            .map(ont -> new AddAxiom(ont, data.getAxiom()))
                            .orElseThrow(() -> new UnknownOWLOntologyException(ontologyId));
    }

    private RemoveAxiom translateRemoveAxiom(RemoveAxiomData data, OWLOntologyID ontologyId) {
        return ontologyIndex.getOntology(ontologyId)
                            .map(ont -> new RemoveAxiom(ont, data.getAxiom()))
                            .orElseThrow(() -> new UnknownOWLOntologyException(ontologyId));
    }

    private AddOntologyAnnotation translateAddOntologyAnnotation(AddOntologyAnnotationData data,
                                                                 OWLOntologyID ontologyId) {
        return ontologyIndex.getOntology(ontologyId)
                            .map(ont -> new AddOntologyAnnotation(ont, data.getAnnotation()))
                            .orElseThrow(() -> new UnknownOWLOntologyException(ontologyId));
    }

    private RemoveOntologyAnnotation translateRemoveOntologyAnnotation(RemoveOntologyAnnotationData data,
                                                                       OWLOntologyID ontologyId) {
        return ontologyIndex.getOntology(ontologyId)
                            .map(ont -> new RemoveOntologyAnnotation(ont, data.getAnnotation()))
                            .orElseThrow(() -> new UnknownOWLOntologyException(ontologyId));
    }

    private AddImport translateAddImport(AddImportData data, OWLOntologyID ontologyId) {
        return ontologyIndex.getOntology(ontologyId)
                            .map(ont -> new AddImport(ont, data.getDeclaration()))
                            .orElseThrow(() -> new UnknownOWLOntologyException(ontologyId));
    }

    private RemoveImport translateRemoveImport(RemoveImportData data, OWLOntologyID ontologyId) {
        return ontologyIndex.getOntology(ontologyId)
                            .map(ont -> new RemoveImport(ont, data.getDeclaration()))
                            .orElseThrow(() -> new UnknownOWLOntologyException(ontologyId));
    }

    private SetOntologyID translateSetOntologyId(SetOntologyIDData data, OWLOntologyID ontologyId) {
        return ontologyIndex.getOntology(ontologyId)
                            .map(ont -> new SetOntologyID(ont, data.getNewId()))
                            .orElseThrow(() -> new UnknownOWLOntologyException(ontologyId));
    }
}
