package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2012
 */
public class EntityLookupRequest implements Serializable {

    public static final int DEFAULT_MATCH_LIMIT = 100;

    private String searchString;

    private Set<EntityLookupRequestEntityMatchType> entityMatchTypes = new HashSet<EntityLookupRequestEntityMatchType>();
    
    private int matchLimit = DEFAULT_MATCH_LIMIT;

    private EntityLookupRequest() {
    }

    public EntityLookupRequest(String searchString) {
        this.searchString = searchString;
        entityMatchTypes.addAll(Arrays.asList(EntityLookupRequestEntityMatchType.values()));
    }

    public EntityLookupRequest(String searchString, EntityLookupRequestEntityMatchType... entityMatchTypes) {
        this.searchString = searchString;
        this.entityMatchTypes.addAll(Arrays.asList(entityMatchTypes));
    }

    public EntityLookupRequest(String searchString, int matchLimit, Set<EntityLookupRequestEntityMatchType> entityMatchTypes) {
        this.searchString = searchString;
        this.matchLimit = matchLimit;
        this.entityMatchTypes = entityMatchTypes;
    }

    public EntityLookupRequest(String searchString, int matchLimit, EntityLookupRequestEntityMatchType... entityMatchTypes) {
        this.searchString = searchString;
        this.matchLimit = matchLimit;
        this.entityMatchTypes.addAll(Arrays.asList(entityMatchTypes));
    }

    public String getSearchString() {
        return searchString;
    }

    public int getMatchLimit() {
        return matchLimit;
    }

    public boolean isMatchType(EntityLookupRequestEntityMatchType type) {
        return entityMatchTypes.contains(type);
    }

    public Set<EntityLookupRequestEntityMatchType> getEntityMatchTypes() {
        return new HashSet<EntityLookupRequestEntityMatchType>(entityMatchTypes);
    }
    
}
