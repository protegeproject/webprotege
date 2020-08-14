package edu.stanford.bmir.protege.web.client.search;

import com.google.gwt.safehtml.shared.SafeHtml;
import edu.stanford.bmir.protege.web.client.form.LanguageMapCurrentLocaleMapper;
import edu.stanford.bmir.protege.web.shared.search.SearchResultMatch;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-24
 */
public class SearchResultMatchPresenter {

    @Nonnull
    private final SearchResultMatchHighlighter highlighter;

    @Nonnull
    private final SearchResultMatchView view;

    @Nonnull
    private final SearchResultLanguageRenderer languageRenderer;

    @Inject
    public SearchResultMatchPresenter(@Nonnull SearchResultMatchHighlighter highlighter,
                                      @Nonnull SearchResultMatchView view,
                                      @Nonnull SearchResultLanguageRenderer languageRenderer) {
        this.highlighter = checkNotNull(highlighter);
        this.view = checkNotNull(view);
        this.languageRenderer = checkNotNull(languageRenderer);
    }

    public void setSearchResultMatch(@Nonnull SearchResultMatch match) {
        String value = match.getValue();
        SafeHtml rendering = highlighter.highlightSearchResult(match);
        view.setRendering(rendering);
        view.setValue(value);
        view.setLanguageRendering(languageRenderer.getRendering(match));
    }

    @Nonnull
    public SearchResultMatchView getView() {
        return view;
    }
}
