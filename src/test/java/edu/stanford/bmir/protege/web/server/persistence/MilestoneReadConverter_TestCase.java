package edu.stanford.bmir.protege.web.server.persistence;

import edu.stanford.bmir.protege.web.shared.issues.Milestone;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Aug 16
 */
public class MilestoneReadConverter_TestCase {

    public static final String THE_LABEL = "MyMilestone";

    private MilestoneReadConverter converter;

    @Before
    public void setUp() {
        converter = new MilestoneReadConverter();
    }

    @Test
    public void shouldReadMilestone() {
        Milestone milestone = converter.convert(THE_LABEL);
        assertThat(milestone.getLabel(), is(THE_LABEL));
    }
}
