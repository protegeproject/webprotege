package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLProperty;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15/01/15
 */
@RunWith(MockitoJUnitRunner.class)
public class PropertyValueList_TestCase {


    private PropertyValueList propertyValueList;

    private PropertyValueList otherPropertyValueList;

    private List<PropertyValue> propertyValues;

    @Mock
    private PropertyValue propertyValueA, propertyValueB;

    @Before
    public void setUp() throws Exception {
        propertyValues = Lists.newArrayList(propertyValueA, propertyValueB);
        propertyValueList = new PropertyValueList(propertyValues);
        otherPropertyValueList = new PropertyValueList(propertyValues);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        new PropertyValueList(null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(propertyValueList, is(equalTo(propertyValueList)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(propertyValueList, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(propertyValueList, is(equalTo(otherPropertyValueList)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(propertyValueList.hashCode(), is(otherPropertyValueList.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(propertyValueList.toString(), startsWith("PropertyValueList"));
    }

    @Test
    public void shouldReturnSignature() {
        OWLEntity propertyA = mock(OWLEntity.class);
        when(propertyA.getSignature()).thenReturn(Collections.singleton(propertyA));
        OWLEntity valueA = mock(OWLEntity.class);
        when(valueA.getSignature()).thenReturn(Collections.singleton(valueA));
        when(propertyValueA.getProperty()).thenReturn(propertyA);
        when(propertyValueA.getValue()).thenReturn(valueA);

        OWLEntity propertyB = mock(OWLEntity.class);
        when(propertyB.getSignature()).thenReturn(Collections.singleton(propertyB));
        OWLEntity valueB = mock(OWLEntity.class);
        when(valueB.getSignature()).thenReturn(Collections.singleton(valueB));
        when(propertyValueB.getProperty()).thenReturn(propertyB);
        when(propertyValueB.getValue()).thenReturn(valueB);

        assertThat(propertyValueList.getSignature(), containsInAnyOrder(
                propertyA,
                propertyB,
                valueA,
                valueB
        ));
    }


}