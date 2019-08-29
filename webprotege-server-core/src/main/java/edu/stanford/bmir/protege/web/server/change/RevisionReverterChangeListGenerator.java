package edu.stanford.bmir.protege.web.server.change;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.server.revision.Revision;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/03/15
 */
public class RevisionReverterChangeListGenerator implements ChangeListGenerator<Boolean> {

    @Nonnull
    private final RevisionNumber revisionNumber;

    @Nonnull
    private final RevisionManager revisionManager;

    @AutoFactory
    @Inject
    public RevisionReverterChangeListGenerator(@Nonnull RevisionNumber revisionNumber,
                                               @Provided @Nonnull RevisionManager revisionManager) {
        this.revisionNumber = checkNotNull(revisionNumber);
        this.revisionManager = checkNotNull(revisionManager);
    }

    @Override
    public Boolean getRenamedResult(Boolean result, RenameMap renameMap) {
        return result;
    }

    @Override
    public OntologyChangeList<Boolean> generateChanges(ChangeGenerationContext context) {
        Optional<Revision> revision = revisionManager.getRevision(revisionNumber);
        if(revision.isEmpty()) {
            return OntologyChangeList.<Boolean>builder().build(false);
        }
        var changes = new ArrayList<OntologyChange>();
        var theRevision = revision.get();
        for(OntologyChange change : theRevision.getChanges()) {
            var inverseChange = change.getInverseChange();
            changes.add(0, inverseChange);
        }
        return OntologyChangeList.<Boolean>builder().addAll(changes).build(true);
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<Boolean> result) {
        return "Reverted revision " + revisionNumber.getValue();
    }
}
