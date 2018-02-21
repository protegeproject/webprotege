
package edu.stanford.bmir.protege.web.shared.obo;

import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class OBORelationship_TestCase {

    private OBORelationship relationship;

    @Mock
    private OWLObjectPropertyData relation;

    @Mock
    private OWLClassData value;

    @Before
    public void setUp()
        throws Exception
    {
        relationship = new OBORelationship(relation, value);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_relation_IsNull() {
        new OBORelationship(null, value);
    }

    @Test
    public void shouldReturnSupplied_relation() {
        assertThat(relationship.getRelation(), is(this.relation));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_value_IsNull() {
        new OBORelationship(relation, null);
    }

    @Test
    public void shouldReturnSupplied_value() {
        assertThat(relationship.getValue(), is(this.value));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(relationship, is(relationship));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(relationship.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(relationship, is(new OBORelationship(relation, value)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_relation() {
        assertThat(relationship, is(not(new OBORelationship(Mockito.mock(OWLObjectPropertyData.class), value))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_value() {
        assertThat(relationship, is(not(new OBORelationship(relation, Mockito.mock(OWLClassData.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(relationship.hashCode(), is(new OBORelationship(relation, value).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(relationship.toString(), startsWith("OBORelationship"));
    }


}
