package edu.stanford.bmir.protege.web.client.search;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.lang.LanguageMapChangedHandler;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-16
 */
public interface EntitySearchFilterView extends IsWidget {

    void setLanguageMapChangedHandler(@Nonnull LanguageMapChangedHandler handler);

    void setLanguageMap(@Nonnull LanguageMap languageMap);

    @Nonnull
    LanguageMap getLanguageMap();

    @Nonnull
    AcceptsOneWidget getCriteriaContainer();
}
