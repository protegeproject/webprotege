package edu.stanford.bmir.protege.web.server.diff;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.diff.DiffOperation;
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

    private final OntologyChangeVisitorEx<DiffOperation> changeOperationVisitor;

    private final OntologyIRIShortFormProvider ontologyIRIShortFormProvider;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @AutoFactory
    @Inject
    public Revision2DiffElementsTranslator(@Provided @Nonnull OntologyIRIShortFormProvider ontologyIRIShortFormProvider,
                                           @Provided @Nonnull DefaultOntologyIdManager defaultOntologyIdManager,
                                           @Provided @Nonnull ProjectOntologiesIndex projectOntologiesIndex) {
        this.ontologyIRIShortFormProvider = checkNotNull(ontologyIRIShortFormProvider);
        this.defaultOntologyIdManager = checkNotNull(defaultOntologyIdManager);
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        changeOperationVisitor = new OntologyChangeVisitorEx<DiffOperation>() {
            @Nonnull
            @Override
            public DiffOperation visit(@Nonnull AddAxiomChange change) {
                return DiffOperation.ADD;
            }

            @Override
            public DiffOperation visit(@Nonnull RemoveAxiomChange change) {
                return DiffOperation.REMOVE;
            }

            @Override
            public DiffOperation visit(@Nonnull AddOntologyAnnotationChange change) {
                return DiffOperation.ADD;
            }

            @Override
            public DiffOperation visit(@Nonnull RemoveOntologyAnnotationChange change) {
                return DiffOperation.REMOVE;
            }

            @Override
            public DiffOperation visit(@Nonnull AddImportChange change) {
                return DiffOperation.ADD;
            }

            @Override
            public DiffOperation visit(@Nonnull RemoveImportChange change) {
                return DiffOperation.REMOVE;
            }

            @Override
            public DiffOperation getDefaultReturnValue() {
                return DiffOperation.ADD;
            }
        };
    }

    public List<DiffElement<String, OntologyChange>> getDiffElementsFromRevision(List<OntologyChange> revision) {
        final List<DiffElement<String, OntologyChange>> changeRecordElements = new ArrayList<>();
        for (final OntologyChange change : revision) {
            changeRecordElements.add(toElement(change));
        }
        return changeRecordElements;
    }

    private DiffElement<String, OntologyChange> toElement(OntologyChange changeRecord) {
        var ontologyID = changeRecord.getOntologyId();
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
            var ontologyIRI = ontologyID.getOntologyIRI();
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
        return defaultOntologyIdManager.getDefaultOntologyId().equals(ontologyId)
                || projectOntologiesIndex.getOntologyIds().count() == 1;
    }

    private DiffOperation getDiffOperation(OntologyChange changeRecord) {
        return changeRecord.accept(changeOperationVisitor);
    }
}
