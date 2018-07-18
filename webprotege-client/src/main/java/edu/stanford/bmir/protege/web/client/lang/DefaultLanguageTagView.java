package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Jul 2018
 */
public interface DefaultLanguageTagView extends IsWidget {

    /**
     * Sets the language tag that is displayed by this view.
     */
    void setLanguageTag(@Nonnull String languageTag);

    /**
     * Gets the language tag that is displayed by this view.  This language tag
     * may be empty.
     */
    @Nonnull
    String getLanguageTag();
}
