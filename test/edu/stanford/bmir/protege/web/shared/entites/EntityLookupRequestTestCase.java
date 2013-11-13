package edu.stanford.bmir.protege.web.shared.entites;

import edu.stanford.bmir.protege.web.shared.entity.EntityLookupRequest;
import edu.stanford.bmir.protege.web.shared.search.SearchType;
import org.junit.Test;
import org.semanticweb.owlapi.model.EntityType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/11/2013
 */
public class EntityLookupRequestTestCase {

    @Test(expected = NullPointerException.class)
    public void nullSearchStringThrowsNullPointerException() {
        new EntityLookupRequest(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullSearchStringThrowsNullPointerException2() {
        new EntityLookupRequest(null, SearchType.SUB_STRING_MATCH_IGNORE_CASE);
    }

    @Test(expected = NullPointerException.class)
    public void nullSearchStringThrowsNullPointerException3() {
        new EntityLookupRequest(null, SearchType.SUB_STRING_MATCH_IGNORE_CASE, 20, Arrays.<EntityType<?>>asList(EntityType.CLASS));
    }

    @Test(expected = NullPointerException.class)
    public void nullSearchTypeThrowsNullPointerException2() {
        new EntityLookupRequest("Test", null);
    }

    @Test(expected = NullPointerException.class)
    public void nullSearchTypeThrowsNullPointerException3() {
        new EntityLookupRequest("Test", null, 20, Arrays.<EntityType<?>>asList(EntityType.CLASS));
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeSearchLimitThrowsIllegalArgumentException() {
        new EntityLookupRequest("Test", SearchType.SUB_STRING_MATCH_IGNORE_CASE, -1, Arrays.<EntityType<?>>asList(EntityType.CLASS));
    }

    @Test(expected = NullPointerException.class)
    public void nullSearchedEntityTypesThrowsNullPointerExeception() {
        new EntityLookupRequest("Test", SearchType.SUB_STRING_MATCH_IGNORE_CASE, 1, null);
    }

    @Test
    public void defaultSearchTypeIsTheSameAsSearchTypeDefault() {
        EntityLookupRequest request = new EntityLookupRequest("Test");
        assertEquals(SearchType.getDefault(), request.getSearchType());
    }

    @Test
    public void suppliedSearchedEntityTypesIsCopied() {
        Set<EntityType<?>> types = new HashSet<EntityType<?>>();
        types.add(EntityType.CLASS);
        Set<EntityType<?>> typesCopy = new HashSet<EntityType<?>>(types);
        EntityLookupRequest request = new EntityLookupRequest("Test", SearchType.SUB_STRING_MATCH_IGNORE_CASE, 20, types);
        types.add(EntityType.OBJECT_PROPERTY);
        assertEquals(typesCopy, request.getSearchedEntityTypes());
    }

    @Test
    public void getSearchedEntityTypesReturnsCopy() {
        Set<EntityType<?>> types = new HashSet<EntityType<?>>();
        types.add(EntityType.CLASS);
        EntityLookupRequest request = new EntityLookupRequest("Test", SearchType.SUB_STRING_MATCH_IGNORE_CASE, 20, types);
        request.getSearchedEntityTypes().add(EntityType.OBJECT_PROPERTY);
        assertEquals(types, request.getSearchedEntityTypes());
    }

    @Test
    public void getSearchStringReturnsSuppliedString() {
        EntityLookupRequest request = new EntityLookupRequest("Test");
        assertEquals("Test", request.getSearchString());
    }

    @Test
    public void getSearchLimitReturnsSuppliedSearchLimit() {
        EntityLookupRequest request = new EntityLookupRequest("Test", SearchType.SUB_STRING_MATCH_IGNORE_CASE, 5, Arrays.<EntityType<?>>asList(EntityType.CLASS));
        assertEquals(5, request.getSearchLimit());
    }

    @Test
    public void getSearchTypeReturnsSuppliedType() {
        EntityLookupRequest request = new EntityLookupRequest("Test", SearchType.SUB_STRING_MATCH_IGNORE_CASE, 5, Arrays.<EntityType<?>>asList(EntityType.CLASS));
        assertEquals(SearchType.SUB_STRING_MATCH_IGNORE_CASE, request.getSearchType());
    }

    @Test
    public void equalArgsGiveEqualRequests() {
        EntityLookupRequest requestA = new EntityLookupRequest("Test", SearchType.SUB_STRING_MATCH_IGNORE_CASE, 5, Arrays.<EntityType<?>>asList(EntityType.CLASS));
        EntityLookupRequest requestB = new EntityLookupRequest("Test", SearchType.SUB_STRING_MATCH_IGNORE_CASE, 5, Arrays.<EntityType<?>>asList(EntityType.CLASS));
        assertEquals(requestA.hashCode(), requestB.hashCode());
        assertEquals(requestA, requestB);
    }
}
