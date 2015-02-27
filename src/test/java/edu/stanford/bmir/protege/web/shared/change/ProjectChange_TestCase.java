package edu.stanford.bmir.protege.web.shared.change;

import com.google.gwt.safehtml.shared.SafeHtml;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectChange_TestCase {


    private ProjectChange projectChange;

    private ProjectChange otherProjectChange;

    @Mock
    private RevisionNumber revisionNumber;

    @Mock
    private UserId userId;

    private long timestamp = 100;

    private String summary = "The Summary";

    @Mock
    private Page<DiffElement<String, SafeHtml>> diff;


    @Before
    public void setUp() throws Exception {
        projectChange = new ProjectChange(revisionNumber, userId, timestamp, summary, diff);
        otherProjectChange = new ProjectChange(revisionNumber, userId, timestamp, summary, diff);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_RevisionNumber_IsNull() {
        new ProjectChange(null, userId, timestamp, summary, diff);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_UserId_IsNull() {
        new ProjectChange(revisionNumber, null, timestamp, summary, diff);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Summary_IsNull() {
        new ProjectChange(revisionNumber, userId, timestamp, null, diff);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Diff_IsNull() {
        new ProjectChange(revisionNumber, userId, timestamp, summary, null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(projectChange, is(equalTo(projectChange)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(projectChange, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(projectChange, is(equalTo(otherProjectChange)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(projectChange.hashCode(), is(otherProjectChange.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(projectChange.toString(), startsWith("ProjectChange"));
    }

    @Test
    public void shouldReturnSupplied_RevisionNumber() {
        assertThat(projectChange.getRevisionNumber(), is(revisionNumber));
    }
    @Test
    public void shouldReturnSupplied_UserId() {
        assertThat(projectChange.getAuthor(), is(userId));
    }
    @Test
    public void shouldReturnSupplied_Timestamp() {
        assertThat(projectChange.getTimestamp(), is(timestamp));
    }
    @Test
    public void shouldReturnSupplied_Summary() {
        assertThat(projectChange.getSummary(), is(summary));
    }
    @Test
    public void shouldReturnSupplied_Diff() {
        assertThat(projectChange.getDiff(), is(diff));
    }
}