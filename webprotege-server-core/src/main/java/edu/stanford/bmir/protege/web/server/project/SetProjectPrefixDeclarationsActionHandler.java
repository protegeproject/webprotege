package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.project.PrefixDeclaration;
import edu.stanford.bmir.protege.web.shared.project.PrefixDeclarations;
import edu.stanford.bmir.protege.web.shared.project.SetProjectPrefixDeclarationsAction;
import edu.stanford.bmir.protege.web.shared.project.SetProjectPrefixDeclarationsResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_PROJECT_PREFIXES;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Mar 2018
 */
public class SetProjectPrefixDeclarationsActionHandler extends AbstractProjectActionHandler<SetProjectPrefixDeclarationsAction, SetProjectPrefixDeclarationsResult> {

    @Nonnull
    private final PrefixDeclarationsStore store;

    @Inject
    public SetProjectPrefixDeclarationsActionHandler(@Nonnull AccessManager accessManager,
                                                     @Nonnull PrefixDeclarationsStore store) {
        super(accessManager);
        this.store = checkNotNull(store);
    }

    @Nonnull
    @Override
    public Class<SetProjectPrefixDeclarationsAction> getActionClass() {
        return SetProjectPrefixDeclarationsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(SetProjectPrefixDeclarationsAction action) {
        return EDIT_PROJECT_PREFIXES;
    }

    @Nonnull
    @Override
    public SetProjectPrefixDeclarationsResult execute(@Nonnull SetProjectPrefixDeclarationsAction action, @Nonnull ExecutionContext executionContext) {
        Map<String, String> decls = action.getPrefixDeclarations().stream()
                                          .collect(Collectors.toMap(PrefixDeclaration::getPrefixName,
                                                                    PrefixDeclaration::getPrefix));
        PrefixDeclarations prefixDeclarations = PrefixDeclarations.get(action.getProjectId(), decls);
        store.save(prefixDeclarations);
        return new SetProjectPrefixDeclarationsResult(action.getProjectId(), action.getPrefixDeclarations());
    }
}
