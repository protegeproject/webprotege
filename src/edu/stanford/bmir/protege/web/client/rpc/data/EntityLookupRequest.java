package edu.stanford.bmir.protege.web.client.rpc.data;

import org.semanticweb.owlapi.model.EntityType;

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

    private Set<EntityType<?>> entityMatchTypes = new HashSet<EntityType<?>>();
    
    private int matchLimit = DEFAULT_MATCH_LIMIT;

    private EntityLookupRequestType type = EntityLookupRequestType.getDefault();

    private EntityLookupRequest() {
    }

    public EntityLookupRequest(String searchString) {
        this(searchString, EntityLookupRequestType.getDefault());
    }

    public EntityLookupRequest(String searchString, EntityLookupRequestType type) {
        this.searchString = searchString;
        entityMatchTypes.addAll(EntityType.values());
        this.type = type;
    }

    public EntityLookupRequest(String searchString, EntityLookupRequestType type, EntityType<?>... entityTypes) {
        this.searchString = searchString;
        this.entityMatchTypes.addAll(Arrays.asList(entityTypes));
        this.type = type;
    }

    public EntityLookupRequest(String searchString, EntityLookupRequestType type, int matchLimit, Set<EntityType<?>> entityMatchTypes) {
        this.searchString = searchString;
        this.matchLimit = matchLimit;
        this.entityMatchTypes = entityMatchTypes;
        this.type = type;
    }

    public EntityLookupRequest(String searchString, EntityLookupRequestType type, int matchLimit, EntityType<?>... entityMatchTypes) {
        this.searchString = searchString;
        this.matchLimit = matchLimit;
        this.entityMatchTypes.addAll(Arrays.asList(entityMatchTypes));
        this.type = type;
    }

    public String getSearchString() {
        return searchString;
    }

    public int getMatchLimit() {
        return matchLimit;
    }

    public EntityLookupRequestType getMatchType() {
        return type;
    }

    public boolean isMatchType(EntityType<?> type) {
        return entityMatchTypes.contains(type);
    }

    public Set<EntityType<?>> getEntityMatchTypes() {
        return new HashSet<EntityType<?>>(entityMatchTypes);
    }



}
