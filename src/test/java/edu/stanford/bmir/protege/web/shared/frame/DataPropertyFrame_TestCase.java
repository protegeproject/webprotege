package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.collect.Sets;
import edu.stanford.bmir.protege.web.shared.entity.OWLDataPropertyData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Set;

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
public class DataPropertyFrame_TestCase {


    private DataPropertyFrame dataPropertyFrame;

    private DataPropertyFrame otherDataPropertyFrame;

    @Mock
    private OWLDataProperty subject;

    @Mock
    private PropertyValueList propertyValueList;

    private Set<OWLClass> domains;

    @Mock
    private OWLClass domainA, domainB;

    @Mock
    private OWLDatatype rangeA, rangeB;

    private Set<OWLDatatype> ranges;

    private boolean functional = true;

    @Before
    public void setUp() throws Exception {
        domains = Sets.newHashSet(domainA, domainB);
        ranges = Sets.newHashSet(rangeA, rangeB);
        dataPropertyFrame = new DataPropertyFrame(subject, propertyValueList, domains, ranges, functional);
        otherDataPropertyFrame = new DataPropertyFrame(subject, propertyValueList, domains, ranges, functional);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Subject_IsNull() {
        new DataPropertyFrame(null, propertyValueList, domains, ranges, functional);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_PropertyValueList_IsNull() {
        new DataPropertyFrame(subject, null, domains, ranges, functional);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Domains_IsNull() {
        new DataPropertyFrame(subject, propertyValueList, null, ranges, functional);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Ranges_IsNull() {
        new DataPropertyFrame(subject, propertyValueList, domains, null, functional);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(dataPropertyFrame, is(equalTo(dataPropertyFrame)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(dataPropertyFrame, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(dataPropertyFrame, is(equalTo(otherDataPropertyFrame)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(dataPropertyFrame.hashCode(), is(otherDataPropertyFrame.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(dataPropertyFrame.toString(), startsWith("DataPropertyFrame"));
    }

    @Test
    public void shouldReturnSupplied_Subject() {
        assertThat(dataPropertyFrame.getSubject(), is(subject));
    }

    @Test
    public void shouldReturnSupplied_PropertyValueList() {
        assertThat(dataPropertyFrame.getPropertyValueList(), is(propertyValueList));
    }

    @Test
    public void shouldReturnSupplied_Domains() {
        assertThat(dataPropertyFrame.getDomains(), is(domains));
    }

    @Test
    public void shouldReturnSupplied_Ranges() {
        assertThat(dataPropertyFrame.getRanges(), is(ranges));
    }

    @Test
    public void shouldReturnSupplied_Functional() {
        assertThat(dataPropertyFrame.isFunctional(), is(true));
    }

    @Test
    public void shouldReturnSignature() {
        OWLEntity property = mock(OWLEntity.class);
        OWLEntity value = mock(OWLEntity.class);
        when(propertyValueList.getSignature()).thenReturn(Sets.newHashSet(property, value));
        assertThat(dataPropertyFrame.getSignature(), containsInAnyOrder(
                subject,
                domainA, domainB,
                rangeA, rangeB,
                property, value
        ));
    }


}