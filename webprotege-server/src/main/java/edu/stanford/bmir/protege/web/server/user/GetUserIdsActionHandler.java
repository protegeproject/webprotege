package edu.stanford.bmir.protege.web.server.user;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.user.GetUserIdsAction;
import edu.stanford.bmir.protege.web.shared.user.GetUserIdsResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/01/15
 */
public class GetUserIdsActionHandler implements ActionHandler<GetUserIdsAction, GetUserIdsResult> {

    private HasUserIds hasUserIds;

    @Inject
    public GetUserIdsActionHandler(HasUserIds hasUserIds) {
        this.hasUserIds = checkNotNull(hasUserIds);
    }

    @Nonnull
    @Override
    public Class<GetUserIdsAction> getActionClass() {
        return GetUserIdsAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetUserIdsAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public GetUserIdsResult execute(@Nonnull GetUserIdsAction action, @Nonnull ExecutionContext executionContext) {
        return new GetUserIdsResult(ImmutableList.copyOf(hasUserIds.getUserIds()));
    }
}
