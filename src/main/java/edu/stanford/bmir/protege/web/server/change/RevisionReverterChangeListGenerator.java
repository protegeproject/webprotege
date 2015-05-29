package edu.stanford.bmir.protege.web.server.change;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.server.owlapi.change.RevisionManager;
import edu.stanford.bmir.protege.web.server.owlapi.change.Revision;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import org.semanticweb.owlapi.change.*;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/03/15
 */
public class RevisionReverterChangeListGenerator implements ChangeListGenerator<OWLEntity> {

    private RevisionNumber revisionNumber;

    private OWLOntologyChangeDataReverter changeDataReverter;

    @Inject
    public RevisionReverterChangeListGenerator(RevisionNumber revisionNumber, OWLOntologyChangeDataReverter changeDataReverter) {
        this.revisionNumber = revisionNumber;
        this.changeDataReverter = changeDataReverter;
    }

    @Override
    public OWLEntity getRenamedResult(OWLEntity result, RenameMap renameMap) {
        return result;
    }

    @Override
    public OntologyChangeList<OWLEntity> generateChanges(OWLAPIProject project, ChangeGenerationContext context) {
        RevisionManager changeManager = project.getChangeManager();
        Optional<Revision> revision = changeManager.getRevision(revisionNumber);
        if(!revision.isPresent()) {
            return OntologyChangeList.<OWLEntity>builder().build();
        }

        List<OWLOntologyChange> changes = new ArrayList<>();
        for(OWLOntologyChangeRecord record : revision.get()) {
            OWLOntologyChangeData revertingChangeData = changeDataReverter.getRevertingChange(record);
            OWLOntologyChangeRecord revertingRecord = new OWLOntologyChangeRecord(record.getOntologyID(), revertingChangeData);
            OWLOntologyManager manager = project.getRootOntology().getOWLOntologyManager();
            OWLOntologyChange change = revertingRecord.createOntologyChange(manager);
            changes.add(0, change);
        }
        return OntologyChangeList.<OWLEntity>builder().addAll(changes).build();
    }



}
