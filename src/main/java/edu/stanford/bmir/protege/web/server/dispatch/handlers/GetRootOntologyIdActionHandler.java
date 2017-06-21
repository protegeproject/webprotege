package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.actions.GetRootOntologyIdAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetRootOntologyIdResult;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;

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
public class GetRootOntologyIdActionHandler extends AbstractHasProjectActionHandler<GetRootOntologyIdAction, GetRootOntologyIdResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final OWLOntology rootOntology;


    @Inject
    public GetRootOntologyIdActionHandler(@Nonnull AccessManager accessManager,
                                          @Nonnull ProjectId projectId,
                                          @Nonnull @RootOntology OWLOntology rootOntology) {
        super(accessManager);
        this.projectId = projectId;
        this.rootOntology = rootOntology;
    }

    /**
     * Gets the class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action} handled by this handler.
     * @return The class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action}.  Not {@code null}.
     */
    @Override
    public Class<GetRootOntologyIdAction> getActionClass() {
        return GetRootOntologyIdAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
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
    @Override
    public GetRootOntologyIdResult execute(GetRootOntologyIdAction action, ExecutionContext executionContext) {
        final OWLOntologyID result = rootOntology.getOntologyID();
        return new GetRootOntologyIdResult(projectId, result);
    }
}
