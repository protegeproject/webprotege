package edu.stanford.bmir.protege.web.server.change;

import org.semanticweb.owlapi.change.*;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/03/15
 */
public class OWLOntologyChangeDataReverter {

    @Inject
    public OWLOntologyChangeDataReverter() {
    }

    public OWLOntologyChangeData getRevertingChange(final OWLOntologyChangeRecord record) {
        return record.getData().accept(new OWLOntologyChangeDataVisitor<OWLOntologyChangeData, RuntimeException>() {
            @Override
            public OWLOntologyChangeData visit(AddAxiomData data) throws RuntimeException {
                return new RemoveAxiomData(data.getAxiom());
            }

            @Override
            public OWLOntologyChangeData visit(RemoveAxiomData data) throws RuntimeException {
                return new AddAxiomData(data.getAxiom());
            }

            @Override
            public OWLOntologyChangeData visit(AddOntologyAnnotationData data) throws RuntimeException {
                return new RemoveOntologyAnnotationData(data.getAnnotation());
            }

            @Override
            public OWLOntologyChangeData visit(RemoveOntologyAnnotationData data) throws RuntimeException {
                return new AddOntologyAnnotationData(data.getAnnotation());
            }

            @Override
            public OWLOntologyChangeData visit(SetOntologyIDData data) throws RuntimeException {
                return new SetOntologyIDData(record.getOntologyID());
            }

            @Override
            public OWLOntologyChangeData visit(AddImportData data) throws RuntimeException {
                return new RemoveImportData(data.getDeclaration());
            }

            @Override
            public OWLOntologyChangeData visit(RemoveImportData data) throws RuntimeException {
                return new AddImportData(data.getDeclaration());
            }
        });
    }
}
