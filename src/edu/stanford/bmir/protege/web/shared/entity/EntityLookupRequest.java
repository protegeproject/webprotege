package edu.stanford.bmir.protege.web.shared.entity;

import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.search.SearchType;
import org.semanticweb.owlapi.model.EntityType;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2012
 * <p>
 *     An {@link EntityLookupRequest} describes a a request to find entities based on some search string.  The types
 *     of entities to be looked up can be specified as well as a match limit.
 * </p>
 */
public class EntityLookupRequest implements Serializable, IsSerializable {

    public static final int DEFAULT_MATCH_LIMIT = 20;

    private String searchString;

    private SearchType searchType = SearchType.getDefault();

    private Set<EntityType<?>> searchedEntityTypes = new HashSet<EntityType<?>>();
    
    private int searchLimit = DEFAULT_MATCH_LIMIT;



    /**
     * For serialization purposes only.
     */
    private EntityLookupRequest() {
    }

    /**
     * Creates an {@link EntityLookupRequest} that specifies entities should be matched according to the specified
     * string.  The search type against the specified string is determined by {@link edu.stanford.bmir.protege.web.shared.search.SearchType#getDefault()}.
     * @param searchString The {@link String} to be matched.  Not {@code null}.
     * @throws NullPointerException if {@code searchString} is {@code null}.
     */
    public EntityLookupRequest(String searchString) {
        this(searchString, SearchType.getDefault());
    }

    /**
     * Creates an {@link EntityLookupRequest} that specifies entities should be search for by the sepcified string
     * and search type.
     * @param searchString The search {@link String}.  Not {@code null}.
     * @param searchType The type of search to be performed..  See {@link edu.stanford.bmir.protege.web.shared.search.SearchType}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public EntityLookupRequest(String searchString, SearchType searchType) {
        this(searchString, searchType, DEFAULT_MATCH_LIMIT, EntityType.values());
    }

    /**
     * Creates an {@link EntityLookupRequest} that specifies how entities should be searched for.
     * @param searchString The search string to be searched against.  Not {@code null}.
     * @param searchType The type of search.  Not {@code null}.
     * @param searchLimit The maximum number of search results.
     * @param searchedEntityTypes The types of entities to be searched.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public EntityLookupRequest(String searchString, SearchType searchType, int searchLimit, Collection<EntityType<?>> searchedEntityTypes) {
        this.searchString = checkNotNull(searchString);
        this.searchType = checkNotNull(searchType);
        if(searchLimit < 0) {
            throw new IllegalArgumentException("Search limit must not be less than zero");
        }
        this.searchLimit = searchLimit;
        this.searchedEntityTypes = new HashSet<EntityType<?>>(checkNotNull(searchedEntityTypes));
    }

    /**
     * Gets the search string that should be matched.
     * @return The search string Not {@code null}.
     */
    public String getSearchString() {
        return searchString;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    /**
     * Gets the maximum number of matches.  Matching terminates when this limit has been reached.
     * @return The match limit.  Will be greator or equal to zero.
     */
    public int getSearchLimit() {
        return searchLimit;
    }


    public boolean isSearchType(EntityType<?> type) {
        return searchedEntityTypes.contains(type);
    }

    public Set<EntityType<?>> getSearchedEntityTypes() {
        return new HashSet<EntityType<?>>(searchedEntityTypes);
    }

    @Override
    public int hashCode() {
        return "EntityLookupRequest".hashCode() + searchString.hashCode() + searchedEntityTypes.hashCode() + searchType.hashCode() + searchLimit;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof EntityLookupRequest)) {
            return false;
        }
        EntityLookupRequest other = (EntityLookupRequest) o;
        return this.searchString.equals(other.searchString) && this.searchedEntityTypes.equals(other.searchedEntityTypes) && this.searchType.equals(other.searchType) && this.searchLimit == other.searchLimit;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("EntityLookupRequest")
                .add("searchString", searchString)
                .add("searchType", searchType)
                .add("searchedEntityTypes", searchedEntityTypes)
                .add("searchLimit", searchLimit).toString();
    }


}
