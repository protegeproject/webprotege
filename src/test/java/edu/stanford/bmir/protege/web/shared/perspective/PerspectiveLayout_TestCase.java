
package edu.stanford.bmir.protege.web.shared.perspective;

import edu.stanford.protege.widgetmap.shared.node.Node;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.NullPointerException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class PerspectiveLayout_TestCase {

    private PerspectiveLayout perspectiveLayout;

    @Mock
    private PerspectiveId perspectiveId;

    @Mock
    private Node rootNode;

    @Before
    public void setUp() {
        when(rootNode.duplicate()).thenReturn(rootNode);
        perspectiveLayout = new PerspectiveLayout(perspectiveId, rootNode);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_perspectiveId_IsNull() {
        new PerspectiveLayout(null, rootNode);
    }

    @Test
    public void shouldReturnSupplied_perspectiveId() {
        assertThat(perspectiveLayout.getPerspectiveId(), is(this.perspectiveId));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_rootNode_IsNull() {
        new PerspectiveLayout(perspectiveId, null);
    }

    @Test
    public void shouldReturnSupplied_rootNode() {
        assertThat(perspectiveLayout.getRootNode(), is(this.rootNode));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(perspectiveLayout, is(perspectiveLayout));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(perspectiveLayout.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(perspectiveLayout, is(new PerspectiveLayout(perspectiveId, rootNode)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_perspectiveId() {
        assertThat(perspectiveLayout, is(not(new PerspectiveLayout(Mockito.mock(PerspectiveId.class), rootNode))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_rootNode() {
        assertThat(perspectiveLayout, is(not(new PerspectiveLayout(perspectiveId, Mockito.mock(Node.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(perspectiveLayout.hashCode(), is(new PerspectiveLayout(perspectiveId, rootNode).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(perspectiveLayout.toString(), startsWith("PerspectiveLayout"));
    }

}
