package edu.stanford.bmir.protege.web.server.search;

import com.google.gwt.regexp.shared.RegExp;
import edu.stanford.bmir.protege.web.shared.search.SearchSubject;
import edu.stanford.bmir.protege.web.shared.search.SearchType;
import org.semanticweb.owlapi.model.OWLObject;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/06/2013
 */
public class SearchResult implements Serializable, Comparable<SearchResult> {

    private SearchMetadata searchMetadata;

    private String searchPatternSource;

    private int matchStart;

    private int matchEnd;

    private SearchResult() {
    }

    public SearchResult(SearchMetadata searchMetadata, RegExp searchPattern, int matchStart, int matchEnd) {
        this.searchMetadata = searchMetadata;
        this.searchPatternSource = searchPattern.getSource();
        this.matchStart = matchStart;
        this.matchEnd = matchEnd;
    }

    public SearchType getCategory() {
        return searchMetadata.getType();
    }

    public String getGroupDescription() {
        return searchMetadata.getGroupDescription();
    }

    public String getSearchString() {
        return searchMetadata.getSearchString();
    }

//    public StyledString getStyledSearchSearchString() {
//        return searchMetadata.getStyledSearchSearchString();
//    }

    public SearchSubject getSubject() {
        return searchMetadata.getSubject();
    }

    public String getSubjectRendering() {
        return searchMetadata.getSubjectRendering();
    }

    public RegExp getSearchPattern() {
        return RegExp.compile(searchPatternSource);
    }

    public int getMatchStart() {
        return matchStart;
    }

    public int getMatchEnd() {
        return matchEnd;
    }

    public int compareTo(SearchResult o) {
        int mdDiff = searchMetadata.compareTo(o.searchMetadata);
        if (mdDiff != 0) {
            return mdDiff;
        }
        int startDiff = this.matchStart - o.matchStart;
        if (startDiff != 0) {
            return startDiff;
        }
        return this.matchEnd - o.matchEnd;
    }

    @Override
    public int hashCode() {
        return "SearchResult".hashCode() + searchMetadata.hashCode() + matchStart + matchEnd;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof SearchResult)) {
            return false;
        }
        SearchResult other = (SearchResult) obj;
        return this.searchMetadata.equals(other.searchMetadata) && this.matchStart == other.matchStart && this.matchEnd == other.matchEnd;
    }
}
