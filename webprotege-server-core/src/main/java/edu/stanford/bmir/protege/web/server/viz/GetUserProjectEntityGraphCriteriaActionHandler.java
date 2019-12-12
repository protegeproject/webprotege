package edu.stanford.bmir.protege.web.server.viz;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.viz.GetUserProjectEntityGraphCriteriaAction;
import edu.stanford.bmir.protege.web.shared.viz.GetUserProjectEntityGraphCriteriaResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-10
 */
public class GetUserProjectEntityGraphCriteriaActionHandler extends AbstractProjectActionHandler<GetUserProjectEntityGraphCriteriaAction, GetUserProjectEntityGraphCriteriaResult> {

    private final EntityGraphSettingsRepository repository;

    @Inject
    public GetUserProjectEntityGraphCriteriaActionHandler(@Nonnull AccessManager accessManager,
                                                          EntityGraphSettingsRepository repository) {
        super(accessManager);
        this.repository = checkNotNull(repository);
    }

    @Nonnull
    @Override
    public Class<GetUserProjectEntityGraphCriteriaAction> getActionClass() {
        return GetUserProjectEntityGraphCriteriaAction.class;
    }

    @Nonnull
    @Override
    public GetUserProjectEntityGraphCriteriaResult execute(@Nonnull GetUserProjectEntityGraphCriteriaAction action,
                                                           @Nonnull ExecutionContext executionContext) {

        var projectId = action.getProjectId();
        var userId = executionContext.getUserId();
        var settings = repository.getSettingsForUserOrProjectDefault(projectId, userId);
        return GetUserProjectEntityGraphCriteriaResult.get(projectId,
                                                           userId,
                                                           settings.getSettings());
    }
}
