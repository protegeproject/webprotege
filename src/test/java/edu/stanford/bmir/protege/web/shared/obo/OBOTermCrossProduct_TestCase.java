
package edu.stanford.bmir.protege.web.shared.obo;

import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBOTermCrossProduct_TestCase {

    private OBOTermCrossProduct crossProduct;

    @Mock
    private Optional<OWLClassData> genus;

    @Mock
    private OBOTermRelationships relationships;

    @Before
    public void setUp()
        throws Exception
    {
        crossProduct = new OBOTermCrossProduct(genus, relationships);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_genus_IsNull() {
        new OBOTermCrossProduct(null, relationships);
    }

    @Test
    public void shouldReturnSupplied_genus() {
        assertThat(crossProduct.getGenus(), is(this.genus));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_relationships_IsNull() {
        new OBOTermCrossProduct(genus, null);
    }

    @Test
    public void shouldReturnSupplied_relationships() {
        assertThat(crossProduct.getRelationships(), is(this.relationships));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(crossProduct, is(crossProduct));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(crossProduct.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(crossProduct, is(new OBOTermCrossProduct(genus, relationships)));
    }


    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_relationships() {
        assertThat(crossProduct, is(Matchers.not(new OBOTermCrossProduct(genus, mock(OBOTermRelationships.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(crossProduct.hashCode(), is(new OBOTermCrossProduct(genus, relationships).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(crossProduct.toString(), startsWith("OBOTermCrossProduct"));
    }

    @Test
    public void shouldReturn_true_For_isEmpty() {
        when(relationships.isEmpty()).thenReturn(true);
        assertThat(crossProduct.isEmpty(), is(true));
    }

    @Test
    public void shouldReturn_false_For_isEmpty() {
        when(relationships.isEmpty()).thenReturn(false);
        when(genus.isPresent()).thenReturn(true);
        assertThat(crossProduct.isEmpty(), is(false));
    }

}
