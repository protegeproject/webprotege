package edu.stanford.bmir.protege.web.server.diff;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.shortform.WebProtegeOntologyIRIShortFormProvider;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.diff.DiffOperation;
import org.semanticweb.owlapi.change.*;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.util.OntologyIRIShortFormProvider;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/15
 */
public class Revision2DiffElementsTranslator {

    private final OWLOntologyChangeDataVisitor<DiffOperation, RuntimeException> changeOperationVisitor;

    private final OntologyIRIShortFormProvider ontologyIRIShortFormProvider;

    @Nonnull
    private final OWLOntology rootOntology;

    @Inject
    public Revision2DiffElementsTranslator(@Nonnull OntologyIRIShortFormProvider ontologyIRIShortFormProvider,
                                           @Nonnull OWLOntology rootOntology) {
        this.ontologyIRIShortFormProvider = checkNotNull(ontologyIRIShortFormProvider);
        this.rootOntology = checkNotNull(rootOntology);
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
        OWLOntologyID ontologyID = changeRecord.getOntologyID();
        final String ontologyIRIShortForm;
        if(isRootOntologySingleton(ontologyID)) {
            ontologyIRIShortForm = "";
        }
        else if (ontologyID.isAnonymous()) {
            // This used to be possible, but isn't anymore.  Some projects may have anonymous ontologies, so we
            // still need to support this.
            ontologyIRIShortForm = "";
        }
        else {
            Optional<IRI> ontologyIRI = ontologyID.getOntologyIRI();
            if (ontologyIRI.isPresent()) {
                ontologyIRIShortForm = ontologyIRIShortFormProvider.getShortForm(ontologyIRI.get());
            }
            else {
                ontologyIRIShortForm = "Anonymous Ontology";
            }

        }
        return new DiffElement<>(
                getDiffOperation(changeRecord),
                ontologyIRIShortForm,
                changeRecord);
    }

    private boolean isRootOntologySingleton(@Nonnull OWLOntologyID ontologyId) {
        return rootOntology.getOntologyID().equals(ontologyId)
                && rootOntology.getImports().isEmpty();
    }

    private DiffOperation getDiffOperation(OWLOntologyChangeRecord changeRecord) {
        return changeRecord.getData().accept(changeOperationVisitor);
    }
}
