package edu.stanford.bmir.protege.web.server.viz;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.viz.EntityGraphSettings;
import edu.stanford.bmir.protege.web.shared.viz.ProjectUserEntityGraphSettings;
import edu.stanford.bmir.protege.web.shared.viz.SetEntityGraphActiveFiltersAction;
import edu.stanford.bmir.protege.web.shared.viz.SetEntityGraphActiveFiltersResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-14
 */
public class SetEntityGraphActiveFiltersActionHandler extends AbstractProjectActionHandler<SetEntityGraphActiveFiltersAction, SetEntityGraphActiveFiltersResult> {

    @Nonnull
    private final EntityGraphSettingsRepository repository;

    @Inject
    public SetEntityGraphActiveFiltersActionHandler(@Nonnull AccessManager accessManager,
                                                    @Nonnull EntityGraphSettingsRepository repository) {
        super(accessManager);
        this.repository = checkNotNull(repository);
    }

    @Nonnull
    @Override
    public Class<SetEntityGraphActiveFiltersAction> getActionClass() {
        return SetEntityGraphActiveFiltersAction.class;
    }

    @Nonnull
    @Override
    public SetEntityGraphActiveFiltersResult execute(@Nonnull SetEntityGraphActiveFiltersAction action,
                                                     @Nonnull ExecutionContext executionContext) {
        var userId = executionContext.getUserId();
        if(userId.isGuest()) {
            return new SetEntityGraphActiveFiltersResult();
        }
        var activeFilters = action.getActiveFilters();
        var projectId = action.getProjectId();
        var settings = repository.getSettingsForUserOrProjectDefault(projectId, userId);

        var entityGraphSettings = settings.getSettings();
        var filters = entityGraphSettings.getFilters()
                           .stream()
                           .map(filter -> {
                               var active = activeFilters.contains(filter.getName());
                               return filter.withActive(active);
                           })
                           .collect(toImmutableList());
        var updatedSettings = EntityGraphSettings.get(filters, entityGraphSettings.getRankSpacing());
        repository.saveSettings(ProjectUserEntityGraphSettings.get(projectId,
                                                                   userId,
                                                                   updatedSettings));
        return new SetEntityGraphActiveFiltersResult();
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(SetEntityGraphActiveFiltersAction action) {
        return BuiltInAction.VIEW_PROJECT;
    }
}
