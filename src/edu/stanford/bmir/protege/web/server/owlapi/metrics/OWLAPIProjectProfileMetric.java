package edu.stanford.bmir.protege.web.server.owlapi.metrics;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
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
public class OWLAPIProjectProfileMetric extends OWLAPIProjectMetric {

    private OWLProfile profile;

    public OWLAPIProjectProfileMetric(OWLAPIProject project, OWLProfile profile) {
        super(project);
        this.profile = profile;
    }

    @Override
    protected OWLAPIProjectMetricValue computeValue() {
        OWLProfileReport report = profile.checkOntology(getRootOntology());
        return new OWLAPIProjectMetricValue("In " + profile.getName() + " profile", report.isInProfile() ? "Yes" : "No");
    }

    @Override
    protected OWLAPIProjectMetricState getStateAfterChanges(List<OWLOntologyChange> changes) {
        return OWLAPIProjectMetricState.DIRTY;
    }
}
