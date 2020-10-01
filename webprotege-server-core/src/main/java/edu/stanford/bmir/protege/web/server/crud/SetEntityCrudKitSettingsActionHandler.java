package edu.stanford.bmir.protege.web.server.crud;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.FindAndReplaceIRIPrefixChangeGeneratorFactory;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.crud.persistence.ProjectEntityCrudKitSettings;
import edu.stanford.bmir.protege.web.server.crud.persistence.ProjectEntityCrudKitSettingsRepository;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.crud.IRIPrefixUpdateStrategy;
import edu.stanford.bmir.protege.web.shared.crud.SetEntityCrudKitSettingsAction;
import edu.stanford.bmir.protege.web.shared.crud.SetEntityCrudKitSettingsResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_NEW_ENTITY_SETTINGS;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class SetEntityCrudKitSettingsActionHandler extends AbstractProjectActionHandler<SetEntityCrudKitSettingsAction, SetEntityCrudKitSettingsResult> {


    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ProjectEntityCrudKitSettingsRepository repository;

    @Nonnull
    private final HasApplyChanges changeManager;

    @Nonnull
    private final FindAndReplaceIRIPrefixChangeGeneratorFactory findAndReplaceIRIPrefixChangeGeneratorFactory;

    @Inject
    public SetEntityCrudKitSettingsActionHandler(@Nonnull AccessManager accessManager,
                                                 @Nonnull ProjectId projectId,
                                                 @Nonnull ProjectEntityCrudKitSettingsRepository repository,
                                                 @Nonnull HasApplyChanges changeManager,
                                                 @Nonnull FindAndReplaceIRIPrefixChangeGeneratorFactory findAndReplaceIRIPrefixChangeGeneratorFactory) {
        super(accessManager);
        this.projectId = projectId;
        this.repository = repository;
        this.changeManager = changeManager;
        this.findAndReplaceIRIPrefixChangeGeneratorFactory = findAndReplaceIRIPrefixChangeGeneratorFactory;

    }

    @Nonnull
    public Class<SetEntityCrudKitSettingsAction> getActionClass() {
        return SetEntityCrudKitSettingsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(SetEntityCrudKitSettingsAction action) {
        return EDIT_NEW_ENTITY_SETTINGS;
    }

    @Nonnull
    @Override
    public SetEntityCrudKitSettingsResult execute(@Nonnull SetEntityCrudKitSettingsAction action,
                                                  @Nonnull ExecutionContext executionContext) {
        var projectSettings = ProjectEntityCrudKitSettings.get(projectId, action.getToSettings());
        repository.save(projectSettings);
        if(action.getPrefixUpdateStrategy() == IRIPrefixUpdateStrategy.FIND_AND_REPLACE) {
            var fromPrefix = action.getFromSettings().getPrefixSettings().getIRIPrefix();
            var toPrefix = action.getToSettings().getPrefixSettings().getIRIPrefix();
            var changeGenerator = findAndReplaceIRIPrefixChangeGeneratorFactory.create(fromPrefix, toPrefix);
            changeManager.applyChanges(executionContext.getUserId(), changeGenerator);
        }
        return new SetEntityCrudKitSettingsResult();
    }

}
