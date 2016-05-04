
package edu.stanford.bmir.protege.web.shared.obo;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.NullPointerException;

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
        MatcherAssert.assertThat(crossProduct.getGenus(), is(this.genus));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_relationships_IsNull() {
        new OBOTermCrossProduct(genus, null);
    }

    @Test
    public void shouldReturnSupplied_relationships() {
        MatcherAssert.assertThat(crossProduct.getRelationships(), is(this.relationships));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(crossProduct, is(crossProduct));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(crossProduct.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(crossProduct, is(new OBOTermCrossProduct(genus, relationships)));
    }


    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_relationships() {
        MatcherAssert.assertThat(crossProduct, is(Matchers.not(new OBOTermCrossProduct(genus, mock(OBOTermRelationships.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(crossProduct.hashCode(), is(new OBOTermCrossProduct(genus, relationships).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(crossProduct.toString(), startsWith("OBOTermCrossProduct"));
    }

    @Test
    public void shouldReturn_true_For_isEmpty() {
        when(relationships.isEmpty()).thenReturn(true);
        MatcherAssert.assertThat(crossProduct.isEmpty(), is(true));
    }

    @Test
    public void shouldReturn_false_For_isEmpty() {
        when(relationships.isEmpty()).thenReturn(false);
        when(genus.isPresent()).thenReturn(true);
        MatcherAssert.assertThat(crossProduct.isEmpty(), is(false));
    }

}
