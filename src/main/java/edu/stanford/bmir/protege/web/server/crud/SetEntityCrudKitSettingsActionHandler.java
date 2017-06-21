package edu.stanford.bmir.protege.web.server.crud;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.FindAndReplaceIRIPrefixChangeGenerator;
import edu.stanford.bmir.protege.web.server.change.FixedMessageChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.crud.persistence.ProjectEntityCrudKitSettings;
import edu.stanford.bmir.protege.web.server.crud.persistence.ProjectEntityCrudKitSettingsRepository;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.crud.IRIPrefixUpdateStrategy;
import edu.stanford.bmir.protege.web.shared.crud.SetEntityCrudKitSettingsAction;
import edu.stanford.bmir.protege.web.shared.crud.SetEntityCrudKitSettingsResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntology;

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
public class SetEntityCrudKitSettingsActionHandler extends AbstractHasProjectActionHandler<SetEntityCrudKitSettingsAction, SetEntityCrudKitSettingsResult> {


    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ProjectEntityCrudKitSettingsRepository repository;

    @Nonnull
    private final HasApplyChanges changeManager;

    @Nonnull
    private final OWLOntology rootOntology;

    @Inject
    public SetEntityCrudKitSettingsActionHandler(@Nonnull AccessManager accessManager,
                                                 @Nonnull ProjectId projectId,
                                                 @Nonnull ProjectEntityCrudKitSettingsRepository repository,
                                                 @Nonnull HasApplyChanges changeManager,
                                                 @Nonnull @RootOntology OWLOntology rootOntology) {
        super(accessManager);
        this.projectId = projectId;
        this.repository = repository;
        this.changeManager = changeManager;
        this.rootOntology = rootOntology;
    }

    public Class<SetEntityCrudKitSettingsAction> getActionClass() {
        return SetEntityCrudKitSettingsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return EDIT_NEW_ENTITY_SETTINGS;
    }

    @Override
    public SetEntityCrudKitSettingsResult execute(SetEntityCrudKitSettingsAction action, ExecutionContext executionContext) {
        ProjectEntityCrudKitSettings projectSettings = new ProjectEntityCrudKitSettings(projectId, action.getToSettings());
        repository.save(projectSettings);
        if(action.getPrefixUpdateStrategy() == IRIPrefixUpdateStrategy.FIND_AND_REPLACE) {
            String fromPrefix = action.getFromSettings().getPrefixSettings().getIRIPrefix();
            String toPrefix = action.getToSettings().getPrefixSettings().getIRIPrefix();
            FindAndReplaceIRIPrefixChangeGenerator changeGenerator = new FindAndReplaceIRIPrefixChangeGenerator(fromPrefix, toPrefix, rootOntology);
            changeManager.applyChanges(executionContext.getUserId(), changeGenerator, new FixedMessageChangeDescriptionGenerator<Void>("Replaced IRI prefix <" + fromPrefix + "> with <" + toPrefix + ">"));
        }
        return new SetEntityCrudKitSettingsResult();
    }

}
