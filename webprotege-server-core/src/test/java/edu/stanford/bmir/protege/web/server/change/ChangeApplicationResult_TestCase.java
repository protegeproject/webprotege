package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-29
 */
@RunWith(MockitoJUnitRunner.class)
public class ChangeApplicationResult_TestCase<S> {

    private ChangeApplicationResult<S> result;

    @Mock
    private S subject;

    @Mock
    private OntologyChange change;

    private List<OntologyChange> changeList;

    @Mock
    private RenameMap renameMap;

    @Before
    public void setUp() {
        changeList = Collections.singletonList(change);
        result = new ChangeApplicationResult<>(subject,
                                               changeList,
                                               renameMap);
    }

    @Test
    public void shouldGetSuppliedSubject() {
        assertThat(result.getSubject(), is(subject));
    }

    @Test
    public void shouldGetSuppliedChangeList() {
        assertThat(result.getChangeList(), is(changeList));
    }

    @Test
    public void shouldGetSuppliedRenameMap() {
        assertThat(result.getRenameMap(), is(renameMap));
    }
}
