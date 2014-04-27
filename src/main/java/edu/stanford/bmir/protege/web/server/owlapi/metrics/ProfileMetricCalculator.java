package edu.stanford.bmir.protege.web.server.owlapi.metrics;

import edu.stanford.bmir.protege.web.shared.metrics.MetricValue;
import edu.stanford.bmir.protege.web.shared.metrics.ProfileMetricValue;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.profiles.OWLProfile;
import org.semanticweb.owlapi.profiles.OWLProfileReport;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class ProfileMetricCalculator extends MetricCalculator {

    private OWLProfile profile;

    public ProfileMetricCalculator(OWLOntology rootOntology, OWLProfile profile) {
        super(rootOntology);
        this.profile = profile;
    }

    @Override
    public ProfileMetricValue computeValue() {
        OWLProfileReport report = profile.checkOntology(getRootOntology());
        return new ProfileMetricValue(profile.getName(), report.isInProfile());
    }

    @Override
    public OWLAPIProjectMetricState getStateAfterChanges(List<? extends OWLOntologyChange> changes) {
        return OWLAPIProjectMetricState.DIRTY;
    }
}
