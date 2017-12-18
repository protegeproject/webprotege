package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.obo.GetOboNamespacesAction;
import edu.stanford.bmir.protege.web.shared.obo.GetOboNamespacesResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class GetOboNamespacesActionHandler extends AbstractProjectActionHandler<GetOboNamespacesAction, GetOboNamespacesResult> {

    @Nonnull
    private final OBONamespaceCache cache;

    @Inject
    public GetOboNamespacesActionHandler(@Nonnull AccessManager accessManager,
                                         @Nonnull OBONamespaceCache cache) {
        super(accessManager);
        this.cache = cache;
    }

    @Nonnull
    @Override
    public Class<GetOboNamespacesAction> getActionClass() {
        return GetOboNamespacesAction.class;
    }

    @Nonnull
    @Override
    public GetOboNamespacesResult execute(@Nonnull GetOboNamespacesAction action, @Nonnull ExecutionContext executionContext) {
        return new GetOboNamespacesResult(cache.getNamespaces());
    }
}
