package edu.stanford.bmir.protege.web.server.owlapi.metrics;

import edu.stanford.bmir.protege.web.shared.metrics.MetricValue;
import edu.stanford.bmir.protege.web.shared.metrics.ProfileMetricValue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.profiles.OWLProfile;
import org.semanticweb.owlapi.profiles.OWLProfileReport;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 22/04/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class ProfileMetricCalculatorTestCase {


    @Mock
    private OWLProfileReport report;

    @Mock
    private OWLOntology ontology;

    @Mock
    private OWLProfile profile;


    private ProfileMetricCalculator calculator;

    @Before
    public void setUp() {
        when(profile.checkOntology(any(OWLOntology.class))).thenReturn(report);
        when(profile.getName()).thenReturn("name");
        calculator = new ProfileMetricCalculator(ontology, profile);
    }

    @Test
    public void shouldCallCheckOntology() {
        calculator.computeValue();
        verify(profile, times(1)).checkOntology(ontology);
    }

    @Test
    public void shouldCalculateBooleanYesForInProfile() {
        when(report.isInProfile()).thenReturn(true);
        calculator = new ProfileMetricCalculator(ontology, profile);
        ProfileMetricValue value = calculator.computeValue();
        assertThat(value.isInProfile(), is(true));
    }


    @Test
    public void shouldCalculateBooleanNoForNotInProfile() {
        when(report.isInProfile()).thenReturn(false);
        ProfileMetricValue value = calculator.computeValue();
        assertThat(value.isInProfile(), is(false));
    }
}
