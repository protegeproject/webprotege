
package edu.stanford.bmir.protege.web.shared.place;

import java.util.Optional;

import edu.stanford.bmir.protege.web.shared.collection.CollectionElementId;
import edu.stanford.bmir.protege.web.shared.collection.CollectionId;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class CollectionViewPlace_TestCase {

    private CollectionViewPlace collectionViewPlace;

    @Mock
    private ProjectId projectId;

    @Mock
    private CollectionId collectionId;

    @Mock
    private FormId formId;

    @Mock
    private CollectionElementId theSel;

    private Optional<CollectionElementId> selection;

    @Before
    public void setUp() {
        selection = Optional.of(theSel);
        collectionViewPlace = new CollectionViewPlace(projectId, collectionId, formId, selection);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new CollectionViewPlace(null, collectionId, formId, selection);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(collectionViewPlace.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_collectionId_IsNull() {
        new CollectionViewPlace(projectId, null, formId, selection);
    }

    @Test
    public void shouldReturnSupplied_collectionId() {
        assertThat(collectionViewPlace.getCollectionId(), is(this.collectionId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_formId_IsNull() {
        new CollectionViewPlace(projectId, collectionId, null, selection);
    }

    @Test
    public void shouldReturnSupplied_formId() {
        assertThat(collectionViewPlace.getFormId(), is(this.formId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_selection_IsNull() {
        new CollectionViewPlace(projectId, collectionId, formId, null);
    }

    @Test
    public void shouldReturnSupplied_selection() {
        assertThat(collectionViewPlace.getSelection(), is(this.selection));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(collectionViewPlace, is(collectionViewPlace));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(collectionViewPlace.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(collectionViewPlace, is(new CollectionViewPlace(projectId, collectionId, formId, selection)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(collectionViewPlace, is(not(new CollectionViewPlace(mock(ProjectId.class), collectionId, formId, selection))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_collectionId() {
        assertThat(collectionViewPlace, is(not(new CollectionViewPlace(projectId, mock(CollectionId.class), formId, selection))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_formId() {
        assertThat(collectionViewPlace, is(not(new CollectionViewPlace(projectId, collectionId, mock(FormId.class), selection))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_selection() {
        assertThat(collectionViewPlace, is(not(new CollectionViewPlace(projectId, collectionId, formId, Optional.of(mock(CollectionElementId.class))))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(collectionViewPlace.hashCode(), is(new CollectionViewPlace(projectId, collectionId, formId, selection).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(collectionViewPlace.toString(), Matchers.startsWith("CollectionViewPlace"));
    }

}
