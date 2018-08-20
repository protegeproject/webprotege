package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.renderer.AnnotationPropertyIriRenderer;
import edu.stanford.bmir.protege.web.shared.lang.DisplayDictionaryLanguage;
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
    private final PreferredLanguageManager preferredLanguageManager;

    @Inject
    public DisplayNamedSettingsPresenter(@Nonnull DisplayNameSettingsView view,
                                         @Nonnull AnnotationPropertyIriRenderer annotationPropertyIriRenderer,
                                         @Nonnull PreferredLanguageManager preferredLanguageManager) {
        this.view = checkNotNull(view);
        this.annotationPropertyIriRenderer = checkNotNull(annotationPropertyIriRenderer);
        this.preferredLanguageManager = checkNotNull(preferredLanguageManager);
    }

    @Nonnull
    public DisplayNameSettingsView getView() {
        return view;
    }

    @Nonnull
    private DisplayDictionaryLanguage getDisplayLanguage() {
        return DisplayDictionaryLanguage.get(view.getPrimaryDisplayNameLanguages().stream().map(DictionaryLanguageData::toDictionaryLanguage).collect(toImmutableList()),
                                             view.getSecondaryDisplayNameLanguages().stream().map(DictionaryLanguageData::toDictionaryLanguage).collect(toImmutableList()));
    }

    public void setChangeHandler(@Nonnull ChangeHandler changeHandler) {
        checkNotNull(changeHandler);
        this.view.setChangeHandler(changeHandler::handleDisplayLanguageChanged);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        setDisplayLanguage(preferredLanguageManager.getDisplayLanguage());
    }


    public void stop() {
        preferredLanguageManager.setDisplayLanguage(getDisplayLanguage());
    }



    private void setDisplayLanguage(@Nonnull DisplayDictionaryLanguage displayLanguage) {
        checkNotNull(displayLanguage);
        GWT.log("[DisplayNamedSettingsPresenter] setDisplayLanguage: " + displayLanguage);

    }
}
