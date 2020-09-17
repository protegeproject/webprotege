package edu.stanford.bmir.protege.web.shared.search;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class EntitySearchFilterId_TestCase {

    public static final String ID = "12345678-1234-1234-1234-123456789abc";

    private EntitySearchFilterId filterId;

    @Before
    public void setUp() throws Exception {
        filterId = EntitySearchFilterId.get(ID);
    }

    @Test
    public void shouldReturnSuppliedUuid() {
        assertThat(filterId.getId(), is(ID));
    }

    /** @noinspection ConstantConditions*/
    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEForNullId() {
        EntitySearchFilterId.get(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionForNonUuid() {
        EntitySearchFilterId.get("OtherId");
    }
}