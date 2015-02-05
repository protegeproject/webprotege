package edu.stanford.bmir.protege.web.server.object;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.object.OWLObjectPropertyExpressionSelector;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;

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
public class OWLObjectPropertyExpressionSelector_TestCase {

    public static final int BEFORE = -1;

    public static final int AFTER = 1;

    private OWLObjectPropertyExpressionSelector selector;

    @Mock
    private Comparator<OWLObjectPropertyExpression> propertyComparator;

    @Mock
    private OWLObjectProperty property1, property2;

    @Mock
    private OWLObjectPropertyExpression propertyExpression1, propertyExpression2;

    @Before
    public void setUp() throws Exception {
        selector = new OWLObjectPropertyExpressionSelector(propertyComparator);
    }


    @Test
    public void shouldNotSelectAnythingForEmptyList() {
        assertThat(selector.selectOne(Collections.<OWLObjectPropertyExpression>emptyList()),
                is(Optional.<OWLObjectPropertyExpression>absent()));
    }

    @Test
    public void shouldSelectAbsentForNoPropertyName() {
        List<OWLObjectPropertyExpression> input = Arrays.asList(propertyExpression1, propertyExpression2);
        assertThat(selector.selectOne(input),
                is(Optional.<OWLObjectPropertyExpression>absent()));
    }

    @Test
    public void shouldSelectSingleOWLObjectProperty() {
        List<OWLObjectPropertyExpression> input = Arrays.asList(propertyExpression1, propertyExpression2, property2);
        assertThat(selector.selectOne(input),
                is(Optional.<OWLObjectPropertyExpression>absent()));
    }

    @Test
    public void shouldSelectSmallerOWLObjectProperty() {
        when(property1.compareTo(property2)).thenReturn(BEFORE);
        when(property2.compareTo(property1)).thenReturn(AFTER);
        List<OWLObjectPropertyExpression> input = Arrays.asList(property2, property1, propertyExpression1);
        assertThat(selector.selectOne(input),
                is(Optional.<OWLObjectPropertyExpression>absent()));
    }
}
