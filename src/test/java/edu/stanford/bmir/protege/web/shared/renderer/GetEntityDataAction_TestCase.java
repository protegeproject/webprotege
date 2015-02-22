package edu.stanford.bmir.protege.web.shared.renderer;

import com.google.common.collect.ImmutableCollection;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class GetEntityDataAction_TestCase {


    private GetEntityDataAction action;

    private GetEntityDataAction otherAction;

    @Mock
    private ProjectId projectId;

    @Mock
    private ImmutableCollection<OWLEntity> entities;


    @Before
    public void setUp() throws Exception {
        action = new GetEntityDataAction(projectId, entities);
        otherAction = new GetEntityDataAction(projectId, entities);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_ProjectId_IsNull() {
        new GetEntityDataAction(null, entities);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Entities_IsNull() {
        new GetEntityDataAction(projectId, null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(action, is(equalTo(action)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(action, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(action, is(equalTo(otherAction)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(action.hashCode(), is(otherAction.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(action.toString(), startsWith("GetEntityDataAction"));
    }

    @Test
    public void shouldReturnSuppliedProjectId() {
        assertThat(action.getProjectId(), is(projectId));
    }

    @Test
    public void shouldReturnSuppliedEntities() {
        assertThat(action.getEntities(), is(entities));
    }
}