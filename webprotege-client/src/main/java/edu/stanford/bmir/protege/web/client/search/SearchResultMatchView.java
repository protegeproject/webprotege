package edu.stanford.bmir.protege.web.client.search;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-24
 */
public interface SearchResultMatchView extends IsWidget {

    void setValue(@Nonnull String value);

    void setRendering(@Nonnull SafeHtml rendering);

    void setLanguageRendering(@Nonnull String languageRendering);
}
