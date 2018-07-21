package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLDataPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLDatatypeData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

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
    private OWLDataPropertyData subject;

    @Mock
    private ImmutableSet<PropertyValue> propertyValueList;

    private ImmutableSet<OWLClassData> domains;

    @Mock
    private OWLClassData domainA, domainB;

    @Mock
    private OWLDatatypeData rangeA, rangeB;

    private ImmutableSet<OWLDatatypeData> ranges;

    private boolean functional = true;

    @Before
    public void setUp() throws Exception {
        domains = ImmutableSet.of(domainA, domainB);
        ranges = ImmutableSet.of(rangeA, rangeB);
        dataPropertyFrame = DataPropertyFrame.get(subject, propertyValueList, domains, ranges, functional);
        otherDataPropertyFrame = DataPropertyFrame.get(subject, propertyValueList, domains, ranges, functional);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Subject_IsNull() {
        DataPropertyFrame.get(null, propertyValueList, domains, ranges, functional);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_PropertyValueList_IsNull() {
        DataPropertyFrame.get(subject, null, domains, ranges, functional);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Domains_IsNull() {
        DataPropertyFrame.get(subject, propertyValueList, null, ranges, functional);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Ranges_IsNull() {
        DataPropertyFrame.get(subject, propertyValueList, domains, null, functional);
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
        assertThat(dataPropertyFrame.getPropertyValueList().getPropertyValues(), is(propertyValueList));
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
}