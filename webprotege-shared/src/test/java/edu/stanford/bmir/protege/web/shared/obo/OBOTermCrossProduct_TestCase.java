
package edu.stanford.bmir.protege.web.shared.obo;

import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class OBOTermCrossProduct_TestCase {

    private OBOTermCrossProduct oBOTermCrossProduct;

    private Optional<OWLClassData> genus = Optional.of(mock(OWLClassData.class));

    @Mock
    private OBOTermRelationships relationships;

    @Before
    public void setUp() {
        oBOTermCrossProduct = new OBOTermCrossProduct(genus, relationships);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_genus_IsNull() {
        new OBOTermCrossProduct(null, relationships);
    }

    @Test
    public void shouldReturnSupplied_genus() {
        assertThat(oBOTermCrossProduct.getGenus(), is(this.genus));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_relationships_IsNull() {
        new OBOTermCrossProduct(genus, null);
    }

    @Test
    public void shouldReturnSupplied_relationships() {
        assertThat(oBOTermCrossProduct.getRelationships(), is(this.relationships));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(oBOTermCrossProduct, is(oBOTermCrossProduct));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(oBOTermCrossProduct.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(oBOTermCrossProduct, is(new OBOTermCrossProduct(genus, relationships)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_genus() {
        assertThat(oBOTermCrossProduct, is(not(new OBOTermCrossProduct(Optional.of(mock(OWLClassData.class)), relationships))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_relationships() {
        assertThat(oBOTermCrossProduct, is(not(new OBOTermCrossProduct(genus, mock(OBOTermRelationships.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(oBOTermCrossProduct.hashCode(), is(new OBOTermCrossProduct(genus, relationships).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(oBOTermCrossProduct.toString(), Matchers.startsWith("OBOTermCrossProduct"));
    }

    @Test
    public void should_emptyOBOTermCrossProduct() {
        assertThat(OBOTermCrossProduct.emptyOBOTermCrossProduct(), is(new OBOTermCrossProduct(Optional.empty(), new OBOTermRelationships(
                Collections.emptySet()))));
    }

    @Test
    public void shouldReturn_true_For_isEmpty() {
        assertThat(OBOTermCrossProduct.emptyOBOTermCrossProduct().isEmpty(), is(true));
    }

    @Test
    public void shouldReturn_false_For_isEmpty() {
        assertThat(oBOTermCrossProduct.isEmpty(), is(false));
    }

}
