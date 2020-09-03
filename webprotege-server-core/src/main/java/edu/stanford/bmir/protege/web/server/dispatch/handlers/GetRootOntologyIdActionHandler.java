package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetRootOntologyIdAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetRootOntologyIdResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class GetRootOntologyIdActionHandler extends AbstractProjectActionHandler<GetRootOntologyIdAction, GetRootOntologyIdResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;


    @Inject
    public GetRootOntologyIdActionHandler(@Nonnull AccessManager accessManager,
                                          @Nonnull ProjectId projectId,
                                          @Nonnull DefaultOntologyIdManager defaultOntologyIdManager) {
        super(accessManager);
        this.projectId = projectId;
        this.defaultOntologyIdManager = defaultOntologyIdManager;
    }

    /**
     * Gets the class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action} handled by this handler.
     * @return The class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action}.  Not {@code null}.
     */
    @Nonnull
    @Override
    public Class<GetRootOntologyIdAction> getActionClass() {
        return GetRootOntologyIdAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetRootOntologyIdAction action) {
        return VIEW_PROJECT;
    }

    /**
     * Executes the specified action, against the specified project in the specified context.
     * @param action The action to be handled/executed
     * @param executionContext The {@link edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext} that should be
     * used to provide details such as the
     * {@link edu.stanford.bmir.protege.web.shared.user.UserId} of the user who requested the action be executed.
     * @return The result of the execution to be returned to the client.
     */
    @Nonnull
    @Override
    public GetRootOntologyIdResult execute(@Nonnull GetRootOntologyIdAction action, @Nonnull ExecutionContext executionContext) {
        var ontologyId = defaultOntologyIdManager.getDefaultOntologyId();
        return new GetRootOntologyIdResult(projectId, ontologyId);
    }
}
