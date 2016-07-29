package edu.stanford.bmir.protege.web.shared.change;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectChangeSubjectsComparator_TestCase {

    public static final String BROWSER_TEXT_1 = "a";
    public static final String BROWSER_TEXT_2 = "b";
    private ProjectChangeSubjectsComparator comparator;

    @Mock
    private ProjectChange change1;

    @Mock
    private ProjectChange change2;

    private ImmutableSet<OWLEntityData> subjects1;

    private ImmutableSet<OWLEntityData> subjects2;

    @Mock
    private OWLEntityData entityData1;

    @Mock
    private OWLEntityData entityData2;

    @Before
    public void setUp() throws Exception {
        subjects1 = ImmutableSet.of(entityData1);
        subjects2 = ImmutableSet.of(entityData2);

        when(entityData1.getBrowserText()).thenReturn(BROWSER_TEXT_1);
        when(entityData2.getBrowserText()).thenReturn(BROWSER_TEXT_2);


        comparator = new ProjectChangeSubjectsComparator();
        when(change1.getSubjects()).thenReturn(subjects1);
        when(change2.getSubjects()).thenReturn(subjects2);
    }

    @Test
    public void shouldReturnZeroForAbsentSubjects() {
        when(change1.getSubjects()).thenReturn(ImmutableSet.<OWLEntityData>of());
        when(change2.getSubjects()).thenReturn(ImmutableSet.<OWLEntityData>of());
        assertThat(comparator.compare(change1, change2), is(0));
    }

    @Test
    public void shouldPutAbsentSubjectsAfterPresentSubjects() {
        when(change1.getSubjects()).thenReturn(ImmutableSet.<OWLEntityData>of());
        assertThat(comparator.compare(change1, change2), is(greaterThan(0)));
    }

    @Test
    public void shouldPutPresentSubjectsBeforeAbsentSubjects() {
        when(change2.getSubjects()).thenReturn(ImmutableSet.<OWLEntityData>of());
        assertThat(comparator.compare(change1, change2), is(lessThan(0)));
    }

    @Test
    public void shouldCompareSubjects() {
        int expectedDiff = BROWSER_TEXT_1.compareTo(BROWSER_TEXT_2);
        assertThat(comparator.compare(change1, change2), is(expectedDiff));
    }
}
