package edu.stanford.bmir.protege.web.shared.search;

import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityIsDeprecatedCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class EntitySearchFilter_TestCase {

    @Mock
    private EntitySearchFilterId id;

    @Mock
    private ProjectId projectId;

    @Mock
    private LanguageMap label;

    @Mock
    private EntityMatchCriteria matchCriteria;

    private EntitySearchFilter filter;

    @Before
    public void setUp() throws Exception {
        filter = EntitySearchFilter.get(id,
                                        projectId,
                                        label,
                                        matchCriteria);
    }

    @Test
    public void shouldReturnProvidedId() {
        assertThat(filter.getId(), is(id));
    }

    @Test
    public void shouldReturnProvidedProjectId() {
        assertThat(filter.getProjectId(), is(projectId));
    }

    @Test
    public void shouldReturnProvidedLabel() {
        assertThat(filter.getLabel(), is(label));
    }

    @Test
    public void shouldReturnProvidedCriteria() {
        assertThat(filter.getEntityMatchCriteria(), is(matchCriteria));
    }
}