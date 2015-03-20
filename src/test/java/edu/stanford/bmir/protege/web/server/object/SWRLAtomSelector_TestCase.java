package edu.stanford.bmir.protege.web.server.object;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.object.SWRLAtomSelector;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.SWRLAtom;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class SWRLAtomSelector_TestCase {

    public static final int BEFORE = -1;

    public static final int AFTER = 1;

    private SWRLAtomSelector selector;

    @Mock
    private Comparator<SWRLAtom> comparator;

    @Mock
    private SWRLAtom atom1, atom2;

    @Before
    public void setUp() throws Exception {
        selector = new SWRLAtomSelector(comparator);
    }

    @Test
    public void shouldReturnAbsent() {
        Iterable<SWRLAtom> input = Collections.emptyList();
        assertThat(selector.selectOne(input), is(Optional.<SWRLAtom>absent()));
    }

    @Test
    public void shouldReturnSingleAtom() {
        List<SWRLAtom> input = Arrays.asList(atom1);
        assertThat(selector.selectOne(input), is(Optional.of(atom1)));
    }

    @Test
    public void shouldReturnTheSmallestAtom() {
        when(comparator.compare(atom1, atom2)).thenReturn(BEFORE);
        when(comparator.compare(atom2, atom1)).thenReturn(AFTER);
        List<SWRLAtom> input = Arrays.asList(atom2, atom1);
        assertThat(selector.selectOne(input),
                is(Optional.of(atom1)));
    }
}
