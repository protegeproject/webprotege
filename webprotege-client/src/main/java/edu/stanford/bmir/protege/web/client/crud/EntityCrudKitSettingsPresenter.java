package edu.stanford.bmir.protege.web.client.crud;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.gen.GeneratedAnnotationsSettings;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-06
 */
public class EntityCrudKitSettingsPresenter {

    @Nonnull
    private final EntityCrudKitSettingsView view;

    @Nonnull
    private final EntityCrudKitPrefixSettingsPresenter prefixSettingsPresenter;

    @Nonnull
    private final EntityCrudKitSuffixSettingsPresenter suffixSettingsPresenter;

    private EntityCrudKitGeneratedAnnotationsSettingsPresenter generatedAnnotationsSettingsPresenter;

    @Inject
    public EntityCrudKitSettingsPresenter(@Nonnull EntityCrudKitSettingsView view,
                                          @Nonnull EntityCrudKitPrefixSettingsPresenter prefixSettingsPresenter,
                                          @Nonnull EntityCrudKitSuffixSettingsPresenter suffixSettingsPresenter,
                                          @Nonnull EntityCrudKitGeneratedAnnotationsSettingsPresenter generatedAnnotationsSettingsPresenter) {
        this.view = view;
        this.prefixSettingsPresenter = prefixSettingsPresenter;
        this.suffixSettingsPresenter = suffixSettingsPresenter;
        this.generatedAnnotationsSettingsPresenter = generatedAnnotationsSettingsPresenter;
    }

    public void clear() {
        prefixSettingsPresenter.clear();
        suffixSettingsPresenter.clear();
        generatedAnnotationsSettingsPresenter.clear();
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        prefixSettingsPresenter.start(view.getPrefixSettingsViewContainer());
        suffixSettingsPresenter.start(view.getSuffixSettingsViewContainer());
        generatedAnnotationsSettingsPresenter.start(view.getGeneratedAnnotationsViewContainer());
    }

    public void setSettings(@Nonnull EntityCrudKitSettings<?> settings) {
        EntityCrudKitPrefixSettings prefixSettings = settings.getPrefixSettings();
        prefixSettingsPresenter.setPrefixSettings(prefixSettings);
        EntityCrudKitSuffixSettings suffixSettings = settings.getSuffixSettings();
        suffixSettingsPresenter.setSuffixSettings(suffixSettings);
        GeneratedAnnotationsSettings generatedAnnotationsSettings = settings.getGeneratedAnnotationsSettings();
        generatedAnnotationsSettingsPresenter.setSettings(generatedAnnotationsSettings);
    }

    @Nonnull
    public EntityCrudKitSettings<?> getSettings() {
        EntityCrudKitPrefixSettings prefixSettings = prefixSettingsPresenter.getPrefixSettings();
        EntityCrudKitSuffixSettings suffixSettings = suffixSettingsPresenter.getSuffixSettings();
        GeneratedAnnotationsSettings generatedAnnotationsSettings = generatedAnnotationsSettingsPresenter.getSettings();
        return EntityCrudKitSettings.get(prefixSettings, suffixSettings, generatedAnnotationsSettings);
    }
}
