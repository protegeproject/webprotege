package edu.stanford.bmir.protege.web.server.diff;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.diff.DiffOperation;
import org.semanticweb.owlapi.change.*;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.util.OntologyIRIShortFormProvider;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/15
 */
public class Revision2DiffElementsTranslator {

    private final OWLOntologyChangeDataVisitor<DiffOperation, RuntimeException> changeOperationVisitor;

    private final OntologyIRIShortFormProvider ontologyIRIShortFormProvider;

    public Revision2DiffElementsTranslator(OntologyIRIShortFormProvider ontologyIRIShortFormProvider) {
        this.ontologyIRIShortFormProvider = ontologyIRIShortFormProvider;
        changeOperationVisitor = new OWLOntologyChangeDataVisitor<DiffOperation, RuntimeException>() {
            @Nonnull
            @Override
            public DiffOperation visit(AddAxiomData data) throws RuntimeException {
                return DiffOperation.ADD;
            }

            @Override
            public DiffOperation visit(RemoveAxiomData data) throws RuntimeException {
                return DiffOperation.REMOVE;
            }

            @Override
            public DiffOperation visit(AddOntologyAnnotationData data) throws RuntimeException {
                return DiffOperation.ADD;
            }

            @Override
            public DiffOperation visit(RemoveOntologyAnnotationData data) throws RuntimeException {
                return DiffOperation.REMOVE;
            }

            @Override
            public DiffOperation visit(SetOntologyIDData data) throws RuntimeException {
                return DiffOperation.ADD;
            }

            @Override
            public DiffOperation visit(AddImportData data) throws RuntimeException {
                return DiffOperation.ADD;
            }

            @Override
            public DiffOperation visit(RemoveImportData data) throws RuntimeException {
                return DiffOperation.REMOVE;
            }
        };
    }

    public List<DiffElement<String, OWLOntologyChangeRecord>> getDiffElementsFromRevision(List<OWLOntologyChangeRecord> revision) {
        final List<DiffElement<String, OWLOntologyChangeRecord>> changeRecordElements = new ArrayList<>();
        for (final OWLOntologyChangeRecord changeRecord : revision) {
            changeRecordElements.add(toElement(changeRecord));
        }
        return changeRecordElements;
    }

    private DiffElement<String, OWLOntologyChangeRecord> toElement(OWLOntologyChangeRecord changeRecord) {
        if (changeRecord.getOntologyID().isAnonymous()) {
            // This used to be possible, but isn't anymore.  Some projects may have anonymous ontologies, so we
            // still need to support this.
            return new DiffElement<>(
                    getDiffOperation(changeRecord),
                    "Root Ontology" ,
                    changeRecord
            );
        }
        else {
            Optional<IRI> ontologyIRI = changeRecord.getOntologyID().getOntologyIRI();
            final String ontologyIRIShortForm;
            if (ontologyIRI.isPresent()) {
                ontologyIRIShortForm = ontologyIRIShortFormProvider.getShortForm(ontologyIRI.get());
            }
            else {
                ontologyIRIShortForm = "Anonymous Ontology";
            }
            return new DiffElement<>(
                    getDiffOperation(changeRecord),
                    ontologyIRIShortForm,
                    changeRecord);
        }
    }

    private DiffOperation getDiffOperation(OWLOntologyChangeRecord changeRecord) {
        return changeRecord.getData().accept(changeOperationVisitor);
    }
}
