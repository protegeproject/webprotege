package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.renderer.AnnotationPropertyIriRenderer;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageData;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31 Jul 2018
 */
public class DisplayNamedSettingsPresenter {

    interface ChangeHandler {

        void handleDisplayLanguageChanged();
    }

    @Nonnull
    private final DisplayNameSettingsView view;

    @Nonnull
    private final AnnotationPropertyIriRenderer annotationPropertyIriRenderer;

    @Nonnull
    private final DisplayNameSettingsManager displayNameSettingsManager;

    @Inject
    public DisplayNamedSettingsPresenter(@Nonnull DisplayNameSettingsView view,
                                         @Nonnull AnnotationPropertyIriRenderer annotationPropertyIriRenderer,
                                         @Nonnull DisplayNameSettingsManager displayNameSettingsManager) {
        this.view = checkNotNull(view);
        this.annotationPropertyIriRenderer = checkNotNull(annotationPropertyIriRenderer);
        this.displayNameSettingsManager = checkNotNull(displayNameSettingsManager);
    }

    @Nonnull
    public DisplayNameSettingsView getView() {
        return view;
    }

    @Nonnull
    private DisplayNameSettings getDisplayLanguage() {
        return DisplayNameSettings.get(view.getPrimaryDisplayNameLanguages().stream().map(DictionaryLanguageData::toDictionaryLanguage).collect(toImmutableList()),
                                       view.getSecondaryDisplayNameLanguages().stream().map(DictionaryLanguageData::toDictionaryLanguage).collect(toImmutableList()));
    }

    public void setChangeHandler(@Nonnull ChangeHandler changeHandler) {
        checkNotNull(changeHandler);
        this.view.setChangeHandler(changeHandler::handleDisplayLanguageChanged);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        setDisplayLanguage(displayNameSettingsManager.getDisplayLanguage());
    }


    public void stop() {
        displayNameSettingsManager.setDisplayLanguage(getDisplayLanguage());
    }



    private void setDisplayLanguage(@Nonnull DisplayNameSettings displayLanguage) {
        checkNotNull(displayLanguage);
        GWT.log("[DisplayNamedSettingsPresenter] setDisplayLanguage: " + displayLanguage);

    }
}
