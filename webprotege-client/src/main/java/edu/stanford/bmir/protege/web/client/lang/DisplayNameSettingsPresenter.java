package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.renderer.AnnotationPropertyIriRenderer;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31 Jul 2018
 */
public class DisplayNameSettingsPresenter {

    interface ChangeHandler {

        void handleDisplayLanguageChanged();
    }

    @Nonnull
    private final DisplayNameSettingsView view;

    @Nonnull
    private final DisplayNameSettingsManager displayNameSettingsManager;

    @Inject
    public DisplayNameSettingsPresenter(@Nonnull DisplayNameSettingsView view,
                                        @Nonnull DisplayNameSettingsManager displayNameSettingsManager) {
        this.view = checkNotNull(view);
        this.displayNameSettingsManager = checkNotNull(displayNameSettingsManager);
    }

    @Nonnull
    public DisplayNameSettingsView getView() {
        return view;
    }

    @Nonnull
    private DisplayNameSettings getDisplayLanguage() {
        return DisplayNameSettings.get(view.getPrimaryDisplayNameLanguages(),
                                       view.getSecondaryDisplayNameLanguages());
    }

    public void setChangeHandler(@Nonnull ChangeHandler changeHandler) {
        checkNotNull(changeHandler);
        this.view.setChangeHandler(changeHandler::handleDisplayLanguageChanged);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        setDisplayLanguage(displayNameSettingsManager.getLocalDisplayNameSettings());
    }


    public void stop() {
        displayNameSettingsManager.setLocalDisplayNameSettings(getDisplayLanguage());
    }



    private void setDisplayLanguage(@Nonnull DisplayNameSettings displayLanguage) {
        checkNotNull(displayLanguage);
        GWT.log("[DisplayNameSettingsPresenter] setLocalDisplayNameSettings: " + displayLanguage);
        view.setPrimaryDisplayNameLanguages(displayLanguage.getPrimaryDisplayNameLanguages());
        view.setSecondaryDisplayNameLanguages(displayLanguage.getSecondaryDisplayNameLanguages());

    }
}
