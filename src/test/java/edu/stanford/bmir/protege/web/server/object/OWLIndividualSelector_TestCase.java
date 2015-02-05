package edu.stanford.bmir.protege.web.server.object;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.object.OWLIndividualSelector;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class OWLIndividualSelector_TestCase {

    public static final int BEFORE = -1;

    public static final int AFTER = 1;

    private OWLIndividualSelector selector;

    @Mock
    private Comparator<OWLIndividual> individualComparator;

    @Mock
    private OWLNamedIndividual namedIndividual1, namedIndividual2;

    @Mock
    private OWLIndividual individual1, individual2;

    @Before
    public void setUp() throws Exception {
        selector = new OWLIndividualSelector(individualComparator);
    }


    @Test
    public void shouldNotSelectAnythingForEmptyList() {
        assertThat(selector.selectOne(Collections.<OWLIndividual>emptyList()),
                is(Optional.<OWLIndividual>absent()));
    }

    @Test
    public void shouldSelectAbsentForNoPropertyName() {
        List<OWLIndividual> input = Arrays.asList(individual1, individual2);
        assertThat(selector.selectOne(input),
                is(Optional.<OWLIndividual>absent()));
    }

    @Test
    public void shouldSelectSingleOWLNamedIndividual() {
        List<OWLIndividual> input = Arrays.asList(individual1, individual2, namedIndividual2);
        assertThat(selector.selectOne(input),
                is(Optional.<OWLIndividual>absent()));
    }

    @Test
    public void shouldSelectSmallerOWLObjectProperty() {
        when(namedIndividual1.compareTo(namedIndividual2)).thenReturn(BEFORE);
        when(namedIndividual2.compareTo(namedIndividual1)).thenReturn(AFTER);
        List<OWLIndividual> input = Arrays.asList(individual2, namedIndividual2, namedIndividual1);
        assertThat(selector.selectOne(input),
                is(Optional.<OWLIndividual>absent()));
    }
}
