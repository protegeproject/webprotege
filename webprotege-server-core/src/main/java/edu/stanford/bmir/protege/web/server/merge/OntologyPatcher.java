package edu.stanford.bmir.protege.web.server.merge;

import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.diff.OntologyDiff2OntologyChanges;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.shared.merge.OntologyDiff;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-20
 */
public class OntologyPatcher {

    @Nonnull
    private final HasApplyChanges changeManager;

    @Nonnull
    private final OntologyDiff2OntologyChanges ontologyDiff2OntologyChanges;

    @Inject
    public OntologyPatcher(@Nonnull HasApplyChanges changeManager,
                           @Nonnull OntologyDiff2OntologyChanges ontologyDiff2OntologyChanges) {
        this.changeManager = checkNotNull(changeManager);
        this.ontologyDiff2OntologyChanges = checkNotNull(ontologyDiff2OntologyChanges);
    }

    public void applyPatch(@Nonnull Collection<OntologyDiff> diffSet,
                           @Nonnull String commitMessage,
                           @Nonnull ExecutionContext executionContext) {
        var changeList = new ArrayList<OntologyChange>();
        for(OntologyDiff diff : diffSet) {
            List<OntologyChange> changes = ontologyDiff2OntologyChanges.getOntologyChangesFromDiff(diff);
            changeList.addAll(changes);
        }
        applyChanges(commitMessage, changeList, executionContext);
    }

    private void applyChanges(String commitMessage,
                              final List<OntologyChange> changes,
                              ExecutionContext executionContext) {
        changeManager.applyChanges(executionContext.getUserId(), new ChangeListGenerator<Boolean>() {
            @Override
            public OntologyChangeList<Boolean> generateChanges(ChangeGenerationContext context) {
                OntologyChangeList.Builder<Boolean> builder = OntologyChangeList.builder();
                builder.addAll(changes);
                return builder.build(!changes.isEmpty());
            }

            @Override
            public Boolean getRenamedResult(Boolean result, RenameMap renameMap) {
                return true;
            }

            @Nonnull
            @Override
            public String getMessage(ChangeApplicationResult<Boolean> result) {
                return commitMessage;
            }
        });

    }
}
