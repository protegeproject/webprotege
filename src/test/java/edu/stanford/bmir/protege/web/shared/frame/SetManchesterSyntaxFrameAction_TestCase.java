package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.collect.Sets;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/01/15
 */
@RunWith(MockitoJUnitRunner.class)
public class SetManchesterSyntaxFrameAction_TestCase {


    private SetManchesterSyntaxFrameAction setManchesterSyntaxFrameAction;

    private SetManchesterSyntaxFrameAction otherSetManchesterSyntaxFrameAction;

    @Mock
    private ProjectId projectId;

    @Mock
    private OWLEntity subject;

    private String fromRendering;

    private String toRendering;

    private Set<OWLEntityData> freshEntities;

    @Mock
    private Optional<String> commitMessage;

    @Before
    public void setUp() throws Exception {
        freshEntities = Sets.newHashSet();
        fromRendering = "FROM";
        toRendering = "TO";
        setManchesterSyntaxFrameAction = new SetManchesterSyntaxFrameAction(projectId, subject, fromRendering, toRendering, freshEntities, commitMessage);
        otherSetManchesterSyntaxFrameAction = new SetManchesterSyntaxFrameAction(projectId, subject, fromRendering, toRendering, freshEntities, commitMessage);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_ProjectId_IsNull() {
        new SetManchesterSyntaxFrameAction(null, subject, fromRendering, toRendering, freshEntities, commitMessage);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Subject_IsNull() {
        new SetManchesterSyntaxFrameAction(projectId, null, fromRendering, toRendering, freshEntities, commitMessage);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_FromRendering_IsNull() {
        new SetManchesterSyntaxFrameAction(projectId, subject, null, toRendering, freshEntities, commitMessage);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_ToRendering_IsNull() {
        new SetManchesterSyntaxFrameAction(projectId, subject, fromRendering, null, freshEntities, commitMessage);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_FreshEntities_IsNull() {
        new SetManchesterSyntaxFrameAction(projectId, subject, fromRendering, toRendering, null, commitMessage);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_CommitMessage_IsNull() {
        new SetManchesterSyntaxFrameAction(projectId, subject, fromRendering, toRendering, freshEntities, null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(setManchesterSyntaxFrameAction, is(equalTo(setManchesterSyntaxFrameAction)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(setManchesterSyntaxFrameAction, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(setManchesterSyntaxFrameAction, is(equalTo(otherSetManchesterSyntaxFrameAction)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(setManchesterSyntaxFrameAction.hashCode(), is(otherSetManchesterSyntaxFrameAction.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(setManchesterSyntaxFrameAction.toString(), startsWith("SetManchesterSyntaxFrameAction"));
    }

    @Test
    public void shouldReturnSuppliedProjectId() {
        assertThat(setManchesterSyntaxFrameAction.getProjectId(), is(projectId));
    }

    @Test
    public void shouldReturnSuppliedObject() {
        assertThat(setManchesterSyntaxFrameAction.getSubject(), is(subject));
    }

    @Test
    public void shouldReturnSuppliedFromRendering() {
        assertThat(setManchesterSyntaxFrameAction.getFromRendering(), is(fromRendering));
    }

    @Test
    public void shouldReturnSuppliedToRendering() {
        assertThat(setManchesterSyntaxFrameAction.getToRendering(), is(toRendering));
    }


}