package edu.stanford.bmir.protege.web.server.change;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.server.revision.Revision;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/03/15
 */
@AutoFactory
public class RevisionReverterChangeListGenerator implements ChangeListGenerator<Boolean> {

    @Nonnull
    private final RevisionNumber revisionNumber;

    @Nonnull
    private final OWLOntologyChangeDataReverter changeDataReverter;

    @Nonnull
    private final RevisionManager revisionManager;

    @Nonnull
    @RootOntology
    private final OWLOntology rootOntology;

    @Inject
    public RevisionReverterChangeListGenerator(@Nonnull RevisionNumber revisionNumber,
                                               @Provided @Nonnull OWLOntologyChangeDataReverter changeDataReverter,
                                               @Provided @Nonnull RevisionManager revisionManager,
                                               @Provided @Nonnull @RootOntology OWLOntology rootOntology) {
        this.revisionNumber = checkNotNull(revisionNumber);
        this.changeDataReverter = changeDataReverter;
        this.revisionManager = revisionManager;
        this.rootOntology = rootOntology;
    }

    @Override
    public Boolean getRenamedResult(Boolean result, RenameMap renameMap) {
        return result;
    }

    @Override
    public OntologyChangeList<Boolean> generateChanges(ChangeGenerationContext context) {
        Optional<Revision> revision = revisionManager.getRevision(revisionNumber);
        if(!revision.isPresent()) {
            return OntologyChangeList.<Boolean>builder().build(false);
        }

        List<OWLOntologyChange> changes = new ArrayList<>();
        for(OWLOntologyChangeRecord record : revision.get()) {
            OWLOntologyChangeData revertingChangeData = changeDataReverter.getRevertingChange(record);
            OWLOntologyChangeRecord revertingRecord = new OWLOntologyChangeRecord(record.getOntologyID(), revertingChangeData);
            OWLOntologyManager manager = rootOntology.getOWLOntologyManager();
            OWLOntologyChange change = revertingRecord.createOntologyChange(manager);
            changes.add(0, change);
        }
        return OntologyChangeList.<Boolean>builder().addAll(changes).build(true);
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<Boolean> result) {
        return "Reverted revision " + revisionNumber.getValue();
    }
}
