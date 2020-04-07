package edu.stanford.bmir.protege.web.client.crud;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.crud.obo.OboIdSuffixSettingsPresenter;
import edu.stanford.bmir.protege.web.client.crud.supplied.SuppliedNameSuffixSettingsPresenter;
import edu.stanford.bmir.protege.web.client.crud.uuid.UuidSuffixSettingsPresenter;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.oboid.OboIdSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UuidSuffixSettings;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-06
 */
public class EntityCrudKitSuffixSettingsPresenter {

    public static final String UUID = "Universally Unique Identifier (UUID)";

    public static final String SUPPLIED_NAME = "Supplied Name";

    public static final String OBO_IDENTIFIER = "OBO Identifier";

    @Nonnull
    private final EntityCrudKitSuffixSettingsView view;

    @Nonnull
    private final UuidSuffixSettingsPresenter uuidSuffixSettingsPresenter;

    @Nonnull
    private final SuppliedNameSuffixSettingsPresenter suppliedNameSuffixSettingsPresenter;

    @Nonnull
    private final OboIdSuffixSettingsPresenter oboIdSuffixSettingsPresenter;

    @Inject
    public EntityCrudKitSuffixSettingsPresenter(@Nonnull EntityCrudKitSuffixSettingsView view,
                                                @Nonnull UuidSuffixSettingsPresenter uuidSuffixSettingsPresenter,
                                                @Nonnull SuppliedNameSuffixSettingsPresenter suppliedNameSuffixSettingsPresenter,
                                                @Nonnull OboIdSuffixSettingsPresenter oboIdSuffixSettingsPresenter) {
        this.view = view;
        this.uuidSuffixSettingsPresenter = uuidSuffixSettingsPresenter;
        this.suppliedNameSuffixSettingsPresenter = suppliedNameSuffixSettingsPresenter;
        this.oboIdSuffixSettingsPresenter = oboIdSuffixSettingsPresenter;
    }

    public void clear() {
        view.setSelectedSuffixSettingName(UUID);
        uuidSuffixSettingsPresenter.start(view.getSettingsViewContainer());
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        view.setAvailableSuffixSettingNames(ImmutableList.of(
                UUID, SUPPLIED_NAME, OBO_IDENTIFIER
        ));
        uuidSuffixSettingsPresenter.clear();
        suppliedNameSuffixSettingsPresenter.clear();
        oboIdSuffixSettingsPresenter.clear();
        view.setSelectedSuffixSettingName(UUID);
        view.setSelectedSuffixSettingsChangedHandler(this::handleSuffixSettingNameChanged);
        handleSuffixSettingNameChanged();
    }

    private void handleSuffixSettingNameChanged() {
        String settingName = view.getSelectedSuffixSettingName();
        AcceptsOneWidget settingsViewContainer = view.getSettingsViewContainer();
        switch(settingName) {
            case UUID:
                uuidSuffixSettingsPresenter.start(settingsViewContainer);
                break;
            case SUPPLIED_NAME:
                suppliedNameSuffixSettingsPresenter.start(settingsViewContainer);
                break;
            case OBO_IDENTIFIER:
                oboIdSuffixSettingsPresenter.start(settingsViewContainer);
                break;
            default:
                uuidSuffixSettingsPresenter.start(settingsViewContainer);
        }
    }

    public void setSuffixSettings(@Nonnull EntityCrudKitSuffixSettings settings) {
        AcceptsOneWidget settingsViewContainer = view.getSettingsViewContainer();
        if(settings instanceof UuidSuffixSettings) {
            uuidSuffixSettingsPresenter.setSettings((UuidSuffixSettings) settings);
            uuidSuffixSettingsPresenter.start(settingsViewContainer);
            view.setSelectedSuffixSettingName(UUID);
        }
        else if(settings instanceof SuppliedNameSuffixSettings) {
            suppliedNameSuffixSettingsPresenter.setSettings((SuppliedNameSuffixSettings) settings);
            suppliedNameSuffixSettingsPresenter.start(settingsViewContainer);
            view.setSelectedSuffixSettingName(SUPPLIED_NAME);
        }
        else if(settings instanceof OboIdSuffixSettings) {
            oboIdSuffixSettingsPresenter.setSettings((OboIdSuffixSettings) settings);
            oboIdSuffixSettingsPresenter.start(settingsViewContainer);
            view.setSelectedSuffixSettingName(OBO_IDENTIFIER);
        }
        else {
            throw new RuntimeException("Unknown kind of suffix settings: " + settings);
        }
    }

    @Nonnull
    public EntityCrudKitSuffixSettings getSuffixSettings() {
        String settingName = view.getSelectedSuffixSettingName();
        switch(settingName) {
            case UUID:
                return uuidSuffixSettingsPresenter.getSettings();
            case SUPPLIED_NAME:
                return suppliedNameSuffixSettingsPresenter.getSettings();
            case OBO_IDENTIFIER:
                return oboIdSuffixSettingsPresenter.getSettings();
            default:
                return uuidSuffixSettingsPresenter.getSettings();
        }
    }
}
