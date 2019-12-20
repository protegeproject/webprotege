package edu.stanford.bmir.protege.web.shared.viz;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.annotation.Nonnull;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-11
 */
@RunWith(MockitoJUnitRunner.class)
public class EntityGraphFilter_TestCase {


    private static final String THE_DESCRIPTION = "TheDescription";


    private EntityGraphFilter filter;

    @Mock
    private FilterName filterName;

    @Mock
    private CompositeEdgeCriteria inclusionCriteria;

    @Mock
    private CompositeEdgeCriteria exclusionCriteria;

    @Before
    public void setUp() {
        filter = EntityGraphFilter.get(filterName,
                                       THE_DESCRIPTION,
                                       inclusionCriteria,
                                       exclusionCriteria,
                                       true);
    }

    @Test
    public void shouldGetSuppliedName() {
        assertThat(filter.getName(), is(filterName));
    }

    @Test
    public void shouldGetSuppliedInclusionCriteria() {
        assertThat(filter.getInclusionCriteria(), is(inclusionCriteria));
    }

    @Test
    public void shouldGetSuppliedExclusionCriteria() {
        assertThat(filter.getExclusionCriteria(), is(exclusionCriteria));
    }
}
