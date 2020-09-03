package edu.stanford.bmir.protege.web.server.search;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.repository.ProjectEntitySearchFiltersManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.search.SetSearchSettingsAction;
import edu.stanford.bmir.protege.web.shared.search.SetSearchSettingsResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-17
 */
public class SetSearchSettingsActionHandler extends AbstractProjectActionHandler<SetSearchSettingsAction, SetSearchSettingsResult> {

    @Nonnull
    private final ProjectEntitySearchFiltersManager filtersManager;

    @Inject
    public SetSearchSettingsActionHandler(@Nonnull AccessManager accessManager,
                                          @Nonnull ProjectEntitySearchFiltersManager filtersManager) {
        super(accessManager);
        this.filtersManager = checkNotNull(filtersManager);
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(SetSearchSettingsAction action) {
        return BuiltInAction.EDIT_PROJECT_SETTINGS;
    }

    @Nonnull
    @Override
    public Class<SetSearchSettingsAction> getActionClass() {
        return SetSearchSettingsAction.class;
    }

    @Nonnull
    @Override
    public synchronized SetSearchSettingsResult execute(@Nonnull SetSearchSettingsAction action,
                                                        @Nonnull ExecutionContext executionContext) {

        var searchFilters = action.getTo();
        filtersManager.setSearchFilters(searchFilters);
        return new SetSearchSettingsResult();
    }
}
