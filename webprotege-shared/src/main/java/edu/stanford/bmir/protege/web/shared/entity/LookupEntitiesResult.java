package edu.stanford.bmir.protege.web.shared.entity;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/11/2013
 */
public class LookupEntitiesResult implements Result {

    private ArrayList<EntityLookupResult> entityLookupResult;

    /**
     * Default constructor for serialization purposes onlu
     */
    private LookupEntitiesResult() {
    }

    /**
     * Creates a LookupEntitiesResult.
     * @param entityLookupResult The match result.  Not {@code null}.
     * @throws NullPointerException if {@code entityLookupResult} is {@code null}.
     */
    public LookupEntitiesResult(Collection<EntityLookupResult> entityLookupResult) {
        this.entityLookupResult = new ArrayList<EntityLookupResult>(checkNotNull(entityLookupResult));
    }

    /**
     * Gets the entity matches.
     * @return The EntityLookupResult that describes the matched entities.  Not {@code null}.
     */
    public List<EntityLookupResult> getEntityLookupResults() {
        return new ArrayList<EntityLookupResult>(entityLookupResult);
    }
}
