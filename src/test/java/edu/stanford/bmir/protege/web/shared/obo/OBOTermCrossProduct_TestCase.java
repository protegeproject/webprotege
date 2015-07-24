
package edu.stanford.bmir.protege.web.shared.obo;

import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.NullPointerException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBOTermCrossProduct_TestCase {

    private OBOTermCrossProduct oBOTermCrossProduct;

    @Mock
    private OWLClassData genus;

    @Mock
    private OBOTermRelationships relationships;

    @Before
    public void setUp()
        throws Exception
    {
        oBOTermCrossProduct = new OBOTermCrossProduct(genus, relationships);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_genus_IsNull() {
        new OBOTermCrossProduct(null, relationships);
    }

    @Test
    public void shouldReturnSupplied_genus() {
        MatcherAssert.assertThat(oBOTermCrossProduct.getGenus(), is(this.genus));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_relationships_IsNull() {
        new OBOTermCrossProduct(genus, null);
    }

    @Test
    public void shouldReturnSupplied_relationships() {
        MatcherAssert.assertThat(oBOTermCrossProduct.getRelationships(), is(this.relationships));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(oBOTermCrossProduct, is(oBOTermCrossProduct));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(oBOTermCrossProduct.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(oBOTermCrossProduct, is(new OBOTermCrossProduct(genus, relationships)));
    }


    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_relationships() {
        MatcherAssert.assertThat(oBOTermCrossProduct, is(Matchers.not(new OBOTermCrossProduct(genus, mock(OBOTermRelationships.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(oBOTermCrossProduct.hashCode(), is(new OBOTermCrossProduct(genus, relationships).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(oBOTermCrossProduct.toString(), startsWith("OBOTermCrossProduct"));
    }

    @Test
    public void shouldReturn_true_For_isEmpty() {
        when(relationships.isEmpty()).thenReturn(true);
        MatcherAssert.assertThat(oBOTermCrossProduct.isEmpty(), is(true));
    }

    @Test
    public void shouldReturn_false_For_isEmpty() {
        when(relationships.isEmpty()).thenReturn(false);
        MatcherAssert.assertThat(oBOTermCrossProduct.isEmpty(), is(false));
    }

}
