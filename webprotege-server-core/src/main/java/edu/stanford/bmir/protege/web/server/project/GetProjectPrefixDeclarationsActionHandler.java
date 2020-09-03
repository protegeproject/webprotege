package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.project.GetProjectPrefixDeclarationsAction;
import edu.stanford.bmir.protege.web.shared.project.GetProjectPrefixDeclarationsResult;
import edu.stanford.bmir.protege.web.shared.project.PrefixDeclaration;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Feb 2018
 */
public class GetProjectPrefixDeclarationsActionHandler extends AbstractProjectActionHandler<GetProjectPrefixDeclarationsAction, GetProjectPrefixDeclarationsResult> {

    @Nonnull
    private final PrefixDeclarationsStore store;

    @Inject
    public GetProjectPrefixDeclarationsActionHandler(@Nonnull AccessManager accessManager,
                                                     @Nonnull PrefixDeclarationsStore store) {
        super(accessManager);
        this.store = checkNotNull(store);
    }

    @Nonnull
    @Override
    public Class<GetProjectPrefixDeclarationsAction> getActionClass() {
        return GetProjectPrefixDeclarationsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetProjectPrefixDeclarationsAction action) {
        return BuiltInAction.EDIT_PROJECT_PREFIXES;
    }

    @Nonnull
    @Override
    public GetProjectPrefixDeclarationsResult execute(@Nonnull GetProjectPrefixDeclarationsAction action,
                                                      @Nonnull ExecutionContext executionContext) {

        ProjectId projectId = action.getProjectId();
        List<PrefixDeclaration> decls = store.find(projectId)
                                             .getPrefixes()
                                             .entrySet()
                                             .stream()
                                             .map(entry -> PrefixDeclaration.get(entry.getKey(), entry.getValue()))
                                             .collect(toList());
        return new GetProjectPrefixDeclarationsResult(projectId, decls);
    }
}
