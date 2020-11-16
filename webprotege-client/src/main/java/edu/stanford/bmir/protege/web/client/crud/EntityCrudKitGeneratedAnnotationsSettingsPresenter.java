package edu.stanford.bmir.protege.web.client.crud;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.crud.gen.GeneratedAnnotationsSettings;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-01
 */
public class EntityCrudKitGeneratedAnnotationsSettingsPresenter {

    @Nonnull
    private final EntityCrudKitGeneratedAnnotationsSettingsView view;


    @Inject
    public EntityCrudKitGeneratedAnnotationsSettingsPresenter(@Nonnull EntityCrudKitGeneratedAnnotationsSettingsView view) {
        this.view = view;
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    public void clear() {

    }

    public void setSettings(@Nonnull GeneratedAnnotationsSettings generatedAnnotationsSettings) {
        view.setValues(generatedAnnotationsSettings.getDescriptors());
    }

    public GeneratedAnnotationsSettings getSettings() {
        return GeneratedAnnotationsSettings.get(
                view.getValues()
        );
    }
}
