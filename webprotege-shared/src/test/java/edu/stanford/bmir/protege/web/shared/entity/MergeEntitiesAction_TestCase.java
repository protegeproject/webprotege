
package edu.stanford.bmir.protege.web.shared.entity;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class MergeEntitiesAction_TestCase {

    private MergeEntitiesAction action;

    @Mock
    private ProjectId projectId;

    private ImmutableSet<OWLEntity> sourceEntities = ImmutableSet.of(mock(OWLEntity.class));

    @Mock
    private OWLEntity targetEntity;

    private MergedEntityTreatment treatment = MergedEntityTreatment.DELETE_MERGED_ENTITY;

    private String commitMessage = "The commit message";

    @Before
    public void setUp() {
        action = new MergeEntitiesAction(projectId, sourceEntities, targetEntity, treatment, commitMessage);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new MergeEntitiesAction(null, sourceEntities, targetEntity, treatment, commitMessage);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(action.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_sourceEntity_IsNull() {
        new MergeEntitiesAction(projectId, null, targetEntity, treatment, commitMessage);
    }

    @Test
    public void shouldReturnSupplied_sourceEntity() {
        assertThat(action.getSourceEntity(), is(this.sourceEntities));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_targetEntity_IsNull() {
        new MergeEntitiesAction(projectId, sourceEntities, null, treatment, commitMessage);
    }

    @Test
    public void shouldReturnSupplied_targetEntity() {
        assertThat(action.getTargetEntity(), is(this.targetEntity));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_treatment_IsNull() {
        new MergeEntitiesAction(projectId, sourceEntities, targetEntity, null, commitMessage);
    }

    @Test
    public void shouldReturnSupplied_treatment() {
        assertThat(action.getTreatment(), is(this.treatment));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(action, is(action));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(action.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(action, is(new MergeEntitiesAction(projectId, sourceEntities, targetEntity, treatment, commitMessage)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(action, is(not(new MergeEntitiesAction(mock(ProjectId.class), sourceEntities, targetEntity, treatment, commitMessage))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_sourceEntity() {
        assertThat(action, is(not(new MergeEntitiesAction(projectId, ImmutableSet.of(mock(OWLEntity.class)), targetEntity, treatment, commitMessage))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_targetEntity() {
        assertThat(action, is(not(new MergeEntitiesAction(projectId, sourceEntities, mock(OWLEntity.class), treatment, commitMessage))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_treatment() {
        assertThat(action, is(not(new MergeEntitiesAction(projectId, sourceEntities, targetEntity, MergedEntityTreatment.DEPRECATE_MERGED_ENTITY, commitMessage))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(action.hashCode(), is(new MergeEntitiesAction(projectId, sourceEntities, targetEntity, treatment, commitMessage).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(action.toString(), startsWith("MergeEntitiesAction"));
    }

    @Test
    public void should_createMergeEntitiesAction() {
        assertThat(MergeEntitiesAction.mergeEntities(projectId, sourceEntities, targetEntity, treatment, commitMessage), is(action));
    }

}
