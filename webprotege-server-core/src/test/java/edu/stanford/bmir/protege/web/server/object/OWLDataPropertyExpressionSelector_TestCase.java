package edu.stanford.bmir.protege.web.server.object;

import edu.stanford.bmir.protege.web.shared.object.OWLDataPropertyExpressionSelector;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class OWLDataPropertyExpressionSelector_TestCase {

    public static final int BEFORE = -1;

    public static final int AFTER = 1;

    private OWLDataPropertyExpressionSelector selector;

    @Mock
    private Comparator<OWLDataPropertyExpression> propertyComparator;

    @Mock
    private OWLDataProperty property1, property2;

    @Mock
    private OWLDataPropertyExpression propertyExpression1, propertyExpression2;

    @Before
    public void setUp() throws Exception {
        selector = new OWLDataPropertyExpressionSelector(propertyComparator);
    }


    @Test
    public void shouldNotSelectAnythingForEmptyList() {
        assertThat(selector.selectOne(Collections.emptyList()),
                is(Optional.empty()));
    }

    @Test
    public void shouldSelectAbsentForNoPropertyName() {
        List<OWLDataPropertyExpression> input = Arrays.asList(propertyExpression1, propertyExpression2);
        assertThat(selector.selectOne(input),
                is(Optional.empty()));
    }

    @Test
    public void shouldSelectSingleOWLDataProperty() {
        List<OWLDataPropertyExpression> input = Arrays.asList(propertyExpression1, propertyExpression2, property2);
        assertThat(selector.selectOne(input),
                is(Optional.empty()));
    }

    @Test
    public void shouldSelectSmallerOWLDataProperty() {
        List<OWLDataPropertyExpression> input = Arrays.asList(property2, property1, propertyExpression1);
        assertThat(selector.selectOne(input),
                is(Optional.empty()));
    }
}
