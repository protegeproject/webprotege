package edu.stanford.bmir.protege.web.server.persistence;

import edu.stanford.bmir.protege.web.shared.issues.Milestone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Aug 16
 */
@RunWith(MockitoJUnitRunner.class)
public class MilestoneWriteConverter_TestCase {

    public static final String THE_LABEL = "MyMilestone";

    private MilestoneWriteConverter converter;

    @Mock
    private Milestone milestone;

    @Before
    public void setUp() {
        converter = new MilestoneWriteConverter();
        when(milestone.getLabel()).thenReturn(THE_LABEL);
    }

    @Test
    public void shouldWriteMilestone() {
        String s = converter.convert(milestone);
        assertThat(s, is(THE_LABEL));
    }
}
