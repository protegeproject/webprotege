package edu.stanford.bmir.protege.web.server.revision;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.revision.GetHeadRevisionNumberAction;
import edu.stanford.bmir.protege.web.shared.revision.GetHeadRevisionNumberResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_CHANGES;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class GetHeadRevisionNumberActionHandler extends AbstractHasProjectActionHandler<GetHeadRevisionNumberAction, GetHeadRevisionNumberResult> {

    @Inject
    public GetHeadRevisionNumberActionHandler(OWLAPIProjectManager projectManager,
                                              AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Override
    public Class<GetHeadRevisionNumberAction> getActionClass() {
        return GetHeadRevisionNumberAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_CHANGES;
    }

    @Override
    protected GetHeadRevisionNumberResult execute(GetHeadRevisionNumberAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new GetHeadRevisionNumberResult(project.getRevisionNumber());
    }
}
