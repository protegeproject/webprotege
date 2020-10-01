package edu.stanford.bmir.protege.web.server.sharing;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.sharing.SetProjectSharingSettingsAction;
import edu.stanford.bmir.protege.web.shared.sharing.SetProjectSharingSettingsResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_SHARING_SETTINGS;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 07/02/15
 */
public class SetProjectSharingSettingsActionHandler extends AbstractProjectActionHandler<SetProjectSharingSettingsAction, SetProjectSharingSettingsResult> {

    @Nonnull
    private final ProjectSharingSettingsManager sharingSettingsManager;

    @Inject
    public SetProjectSharingSettingsActionHandler(@Nonnull AccessManager accessManager,
                                                  @Nonnull ProjectSharingSettingsManager sharingSettingsManager) {
        super(accessManager);
        this.sharingSettingsManager = sharingSettingsManager;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(SetProjectSharingSettingsAction action) {
        return EDIT_SHARING_SETTINGS;
    }

    @Nonnull
    @Override
    public SetProjectSharingSettingsResult execute(@Nonnull SetProjectSharingSettingsAction action, @Nonnull ExecutionContext executionContext) {
        sharingSettingsManager.setProjectSharingSettings(action.getProjectSharingSettings());
        return new SetProjectSharingSettingsResult();
    }

    @Nonnull
    @Override
    public Class<SetProjectSharingSettingsAction> getActionClass() {
        return SetProjectSharingSettingsAction.class;
    }
}
