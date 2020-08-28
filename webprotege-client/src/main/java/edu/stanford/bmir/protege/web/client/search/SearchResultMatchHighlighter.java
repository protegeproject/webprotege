package edu.stanford.bmir.protege.web.client.search;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import edu.stanford.bmir.protege.web.shared.search.SearchResultMatch;
import edu.stanford.bmir.protege.web.shared.search.SearchResultMatchPosition;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationPropertyImpl;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.TreeSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-24
 */
public class SearchResultMatchHighlighter {

    @Inject
    public SearchResultMatchHighlighter() {
    }

    @Nonnull
    public SafeHtml highlightSearchResult(@Nonnull SearchResultMatch match) {
        StringBuilder highlighted = new StringBuilder();
        int lastEnd = 0;
        String matchedValue = match.getValue();

        TreeSet<SearchResultMatchPosition> sortedPositions = new TreeSet<>(match.getPositions());
        for (SearchResultMatchPosition matchPosition : sortedPositions) {
            int wordStart = matchPosition.getStart();
            int wordEnd = matchPosition.getEnd();
            if (lastEnd != wordStart) {
                // Gap from last search position
                highlighted.append("<span'>");
                highlighted.append(matchedValue, lastEnd, wordStart);
                highlighted.append("</span>");
            }
            highlighted.append("<strong>");
            highlighted.append(matchedValue, wordStart, wordEnd);
            highlighted.append("</strong>");
            lastEnd = wordEnd;
        }
        // Finish to end of shortForm from end of last match position
        if (lastEnd < matchedValue.length() - 1) {
            highlighted.append("<span>");
            highlighted.append(matchedValue.substring(lastEnd));
            highlighted.append("</span>");
        }
        return new SafeHtmlBuilder().appendHtmlConstant(highlighted.toString()).toSafeHtml();
    }
}
