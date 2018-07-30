package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.lang.PreferredLanguageManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29 Jul 2018
 */
public class PreferredLanguagePresenter {

    @Nonnull
    private final PreferredLanguageView view;

    @Nonnull
    private final PreferredLanguageManager preferredLanguageManager;

    @Inject
    public PreferredLanguagePresenter(@Nonnull PreferredLanguageView view,
                                      @Nonnull PreferredLanguageManager preferredLanguageManager) {
        this.view = checkNotNull(view);
        this.preferredLanguageManager = checkNotNull(preferredLanguageManager);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        view.setLanguage(preferredLanguageManager.getPrefLang());
        view.setChangeHandler(this::handlePreferredLanguageChanged);
    }

    private void handlePreferredLanguageChanged() {
        preferredLanguageManager.setPrefLang(view.getLanguage());
        GWT.log("Handling Preferred Lang Changed");
    }
}
