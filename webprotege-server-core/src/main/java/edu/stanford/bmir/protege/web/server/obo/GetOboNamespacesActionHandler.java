package edu.stanford.bmir.protege.web.server.obo;

import dagger.Lazy;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.obo.GetOboNamespacesAction;
import edu.stanford.bmir.protege.web.shared.obo.GetOboNamespacesResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class GetOboNamespacesActionHandler extends AbstractProjectActionHandler<GetOboNamespacesAction, GetOboNamespacesResult> {

    @Nonnull
    private final Lazy<OBONamespaceCache> cache;

    @Inject
    public GetOboNamespacesActionHandler(@Nonnull AccessManager accessManager,
                                         @Nonnull Lazy<OBONamespaceCache> cache) {
        super(accessManager);
        this.cache = checkNotNull(cache);
    }

    @Nonnull
    @Override
    public Class<GetOboNamespacesAction> getActionClass() {
        return GetOboNamespacesAction.class;
    }

    @Nonnull
    @Override
    public GetOboNamespacesResult execute(@Nonnull GetOboNamespacesAction action, @Nonnull ExecutionContext executionContext) {
        return new GetOboNamespacesResult(cache.get().getNamespaces());
    }
}
