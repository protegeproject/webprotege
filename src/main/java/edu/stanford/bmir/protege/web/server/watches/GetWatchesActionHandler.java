package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.watches.EntityBasedWatch;
import edu.stanford.bmir.protege.web.shared.watches.GetWatchesAction;
import edu.stanford.bmir.protege.web.shared.watches.GetWatchesResult;
import edu.stanford.bmir.protege.web.shared.watches.Watch;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/02/16
 */
public class GetWatchesActionHandler extends AbstractHasProjectActionHandler<GetWatchesAction, GetWatchesResult> {

    @Inject
    public GetWatchesActionHandler(OWLAPIProjectManager projectManager) {
        super(projectManager);
    }

    @Override
    public Class<GetWatchesAction> getActionClass() {
        return GetWatchesAction.class;
    }

    @Override
    protected RequestValidator<GetWatchesAction> getAdditionalRequestValidator(GetWatchesAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    protected GetWatchesResult execute(GetWatchesAction action, OWLAPIProject project, ExecutionContext executionContext) {
        WatchManager watchManager = project.getWatchManager();
        Set<EntityBasedWatch> watches = new HashSet<>();
        for(Watch<?> watch : watchManager.getDirectWatches(action.getEntity(), action.getUserId())) {
            watches.add((EntityBasedWatch) watch);
        }
        return new GetWatchesResult(watches);
    }
}
