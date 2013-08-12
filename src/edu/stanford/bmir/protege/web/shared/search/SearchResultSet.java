package edu.stanford.bmir.protege.web.shared.search;

import java.io.Serializable;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/06/2013
 */
public interface SearchResultSet extends Serializable {


    public List<SearchResultGroup> getSearchResultGroups();

    public List<SearchResultItem> getSearchResultItems(SearchResultGroup group);



//
//    /**
//     * For serialization only
//     */
//    private SearchResultSet() {
//    }
//
//    private List<SearchResult> searchResults;
//
//
//    private Map<SearchCategoryGroupKey, List<SearchResult>> searchResultsByCategory;
//
//    public SearchResultSet(Collection<SearchResult> searchResults) {
//        this.searchResults = new ArrayList<SearchResult>(searchResults);
//        buildCatResults();
//    }
//
//    public int getCategoryResultsCount(String cat) {
//        SearchCategoryGroupKey key = getKeyForCategory(cat);
//        List<SearchResult> catResults = searchResultsByCategory.get(key);
//        if (catResults == null) {
//            return 0;
//        }
//        else {
//            return catResults.size();
//        }
//    }
//
//    public List<String> getCategories() {
//        List<String> result = new ArrayList<String>();
//        for (SearchCategoryGroupKey searchCategoryGroupKey : new TreeSet<SearchCategoryGroupKey>(searchResultsByCategory.keySet())) {
//            result.add(searchCategoryGroupKey.groupDescription);
//        }
//        return result;
//    }
//
//    public List<SearchResult> getCategoryResults(String category) {
//        SearchCategoryGroupKey key = getKeyForCategory(category);
//        List<SearchResult> catResults = searchResultsByCategory.get(key);
//        if (catResults == null) {
//            return Collections.emptyList();
//        }
//        return new ArrayList<SearchResult>(catResults);
//    }
//
//    public List<SearchResult> getCategoryResults(String category, int limit) {
//        List<SearchResult> trimmedResult = new ArrayList<SearchResult>();
//        SearchCategoryGroupKey key = getKeyForCategory(category);
//        if (key == null) {
//            return Collections.emptyList();
//        }
//        List<SearchResult> catResults = searchResultsByCategory.get(key);
//        if (catResults == null) {
//            return Collections.emptyList();
//        }
//        List<SearchResult> trimmedCatResults;
//        if (catResults.size() > limit) {
//            trimmedCatResults = catResults.subList(0, limit);
//        }
//        else {
//            trimmedCatResults = catResults;
//        }
//        trimmedResult.addAll(trimmedCatResults);
//        return trimmedResult;
//    }
//
//    private SearchCategoryGroupKey getKeyForCategory(String cat) {
//        for (SearchCategoryGroupKey searchCategoryGroupKey : searchResultsByCategory.keySet()) {
//            if (searchCategoryGroupKey.groupDescription.equals(cat)) {
//                return searchCategoryGroupKey;
//            }
//        }
//        return null;
//    }
//
//
//    private void buildCatResults() {
//        searchResultsByCategory = new HashMap<SearchCategoryGroupKey, List<SearchResult>>();
//        for (SearchResult searchResult : searchResults) {
//            String cat = searchResult.getGroupDescription();
//            SearchType type = searchResult.getCategory();
//            SearchCategoryGroupKey key = new SearchCategoryGroupKey(type, cat);
//            List<SearchResult> catResults = searchResultsByCategory.get(key);
//            if (catResults == null) {
//                catResults = new ArrayList<SearchResult>();
//                searchResultsByCategory.put(key, catResults);
//            }
//            catResults.add(searchResult);
//        }
//    }
//
//
//    public List<SearchResult> getSearchResults() {
//        return Collections.unmodifiableList(searchResults);
//    }
//
//
//    private class SearchCategoryGroupKey implements Comparable<SearchCategoryGroupKey>, Serializable {
//
//        private SearchType type;
//
//        private String groupDescription;
//
//        /**
//         * For serialization only
//         */
//        private SearchCategoryGroupKey() {
//        }
//
//        private SearchCategoryGroupKey(SearchType searchType, String groupDescription) {
//            this.type = searchType;
//            this.groupDescription = groupDescription;
//        }
//
//        public int compareTo(SearchCategoryGroupKey o) {
//            int typeDiff = this.type.ordinal() - o.type.ordinal();
//            if (typeDiff != 0) {
//                return typeDiff;
//            }
//            return this.groupDescription.compareToIgnoreCase(o.groupDescription);
//        }
//
//        @Override
//        public int hashCode() {
//            return "SearchCategoryGroupKey".hashCode() + this.type.hashCode() + type.hashCode();
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            if (obj == this) {
//                return true;
//            }
//            if (!(obj instanceof SearchCategoryGroupKey)) {
//                return false;
//            }
//            SearchCategoryGroupKey other = (SearchCategoryGroupKey) obj;
//            return this.type == other.type && this.groupDescription.equals(other.groupDescription);
//        }
//    }
}
