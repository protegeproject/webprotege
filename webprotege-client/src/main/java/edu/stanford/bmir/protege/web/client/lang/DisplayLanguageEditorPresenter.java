package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import edu.stanford.bmir.protege.web.client.renderer.AnnotationPropertyIriRenderer;
import edu.stanford.bmir.protege.web.shared.lang.DisplayDictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.lang.PreferredLanguageManager;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31 Jul 2018
 */
public class DisplayLanguageEditorPresenter {

    interface ChangeHandler {

        void handleDisplayLanguageChanged();
    }

    @Nonnull
    private final DisplayLanguageEditorView view;

    @Nonnull
    private final AnnotationPropertyIriRenderer annotationPropertyIriRenderer;

    @Nonnull
    private final PreferredLanguageManager preferredLanguageManager;

    @Inject
    public DisplayLanguageEditorPresenter(@Nonnull DisplayLanguageEditorView view,
                                          @Nonnull AnnotationPropertyIriRenderer annotationPropertyIriRenderer,
                                          @Nonnull PreferredLanguageManager preferredLanguageManager) {
        this.view = checkNotNull(view);
        this.annotationPropertyIriRenderer = checkNotNull(annotationPropertyIriRenderer);
        this.preferredLanguageManager = checkNotNull(preferredLanguageManager);
    }

    @Nonnull
    public DisplayLanguageEditorView getView() {
        return view;
    }

    @Nonnull
    private DisplayDictionaryLanguage getDisplayLanguage() {
        Optional<DictionaryLanguage> primaryLanguage = view.getPrimaryLanguageProperty()
                                                           .map(primaryProp -> DictionaryLanguage.create(primaryProp.getEntity().getIRI(),
                                                                                                         view.getPrimaryLanguageTag()));
        Optional<DictionaryLanguage> secondaryLanguage = view.getSecondaryLanguageProperty()
                                                             .map(secondaryProp -> DictionaryLanguage.create(secondaryProp.getEntity().getIRI(),
                                                                                                             view.getSecondaryLanguageTag()));
        return DisplayDictionaryLanguage.get(primaryLanguage,
                                             secondaryLanguage);
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
        displayLanguage.getPrimaryLanguage()
                       .ifPresent(primaryLanguage -> annotationPropertyIriRenderer.renderAnnotationPropertyIri(
                               primaryLanguage.getAnnotationPropertyIri(), prop -> {
                                   view.setPrimaryDisplayLanguage(prop, primaryLanguage.getLang());
                               }));
        displayLanguage.getSecondaryLanguage()
                       .ifPresent(secondaryLanguage -> {
                           annotationPropertyIriRenderer.renderAnnotationPropertyIri(
                                   secondaryLanguage.getAnnotationPropertyIri(), prop -> {
                                       view.setSecondaryDisplayLanguage(prop, secondaryLanguage.getLang());
                                   }
                           );
                       });
    }
}
