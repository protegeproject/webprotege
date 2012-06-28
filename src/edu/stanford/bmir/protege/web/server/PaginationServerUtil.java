package edu.stanford.bmir.protege.web.server;

import java.util.List;

import edu.stanford.bmir.protege.web.client.rpc.data.PaginationData;

public class PaginationServerUtil {

    /**
     * Returns a paged collection of allRecords by taking into account the start and limit arguments.
     * The sort and dir are currently disregarded.
     *
     * @param <T> - the type of the content in all records
     * @param allRecords - the list containing all records to be paged
     * @param start - the start index
     * @param limit - count of records to be included in the page
     * @param sort - the sorting order
     * @param dir - the direction?
     * @return - a paged collection: allRecords[start, start+limit]
     */
    public static <T> PaginationData<T> pagedRecords(List<T> allRecords, int start, int limit, String sort, String dir) {

        PaginationData<T> searchResults = new PaginationData<T>();

        int totalRecords = allRecords.size();
        searchResults.setTotalRecords(totalRecords);
        if (start >= totalRecords) {
            return searchResults;
        }

        int end = start + limit - 1;
        int lastIndex = totalRecords - 1;
        if (lastIndex >= start && lastIndex <= end) {
            end = lastIndex;
        }

        for (int i = start, k = 0; i <= end; i++, k++) {
            T record = allRecords.get(i);
            searchResults.getData().add(record);
        }
        return searchResults;
    }

}
