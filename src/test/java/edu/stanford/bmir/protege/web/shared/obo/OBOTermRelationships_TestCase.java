
package edu.stanford.bmir.protege.web.shared.obo;

import java.util.Set;

import com.google.common.collect.Sets;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class OBOTermRelationships_TestCase {

    private OBOTermRelationships termRelationships;

    private Set<OBORelationship> relationships;

    @Before
    public void setUp()
        throws Exception
    {
        relationships = Sets.newHashSet(mock(OBORelationship.class));
        termRelationships = new OBOTermRelationships(relationships);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_relationships_IsNull() {
        new OBOTermRelationships(null);
    }

    @Test
    public void shouldReturnSupplied_relationships() {
        MatcherAssert.assertThat(termRelationships.getRelationships(), is(this.relationships));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(termRelationships, is(termRelationships));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(termRelationships.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(termRelationships, is(new OBOTermRelationships(relationships)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_relationships() {
        Set<OBORelationship> other = Sets.newHashSet(mock(OBORelationship.class));
        MatcherAssert.assertThat(termRelationships, is(not(new OBOTermRelationships(other))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(termRelationships.hashCode(), is(new OBOTermRelationships(relationships).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(termRelationships.toString(), startsWith("OBOTermRelationships"));
    }

    @Test
    public void shouldReturn_false_For_isEmpty() {
        MatcherAssert.assertThat(termRelationships.isEmpty(), is(false));
    }

}
