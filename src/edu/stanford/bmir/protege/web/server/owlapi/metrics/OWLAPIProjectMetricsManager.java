package edu.stanford.bmir.protege.web.server.owlapi.metrics;

import edu.stanford.bmir.protege.web.server.owlapi.*;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyChangeListener;
import org.semanticweb.owlapi.profiles.OWL2DLProfile;
import org.semanticweb.owlapi.profiles.OWL2ELProfile;
import org.semanticweb.owlapi.profiles.OWL2QLProfile;
import org.semanticweb.owlapi.profiles.OWL2RLProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class OWLAPIProjectMetricsManager {

    private OWLAPIProject project;

    private List<OWLAPIProjectMetric> metrics = new ArrayList<OWLAPIProjectMetric>();
    
    public OWLAPIProjectMetricsManager(OWLAPIProject project) {
        this.project = project;

        metrics.add(new OWLAPIProjectAxiomCountMetric(project));
        metrics.add(new OWLAPIProjectLogicalAxiomCount(project));
        metrics.add(new OWLAPIProjectAnnotationAxiomCountMetric(project));


        metrics.add(new OWLAPIProjectClassCountMetric(project));
        metrics.add(new OWLAPIProjectObjectPropertyCountMetric(project));
        metrics.add(new OWLAPIProjectDataPropertyCountMetric(project));
        metrics.add(new OWLAPIProjectAnnotationPropertyCountMetric(project));
        metrics.add(new OWLAPIProjectNamedIndividualCountMetric(project));




        metrics.add(new OWLAPIProjectProfileMetric(project, new OWL2DLProfile()));
        metrics.add(new OWLAPIProjectProfileMetric(project, new OWL2ELProfile()));
        metrics.add(new OWLAPIProjectProfileMetric(project, new OWL2QLProfile()));
        metrics.add(new OWLAPIProjectProfileMetric(project, new OWL2RLProfile()));

        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.SUBCLASS_OF));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.EQUIVALENT_CLASSES));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.EQUIVALENT_CLASSES));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.DISJOINT_CLASSES));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.DISJOINT_UNION));

        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.CLASS_ASSERTION));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.OBJECT_PROPERTY_ASSERTION));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.DATA_PROPERTY_ASSERTION));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.NEGATIVE_OBJECT_PROPERTY_ASSERTION));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.NEGATIVE_DATA_PROPERTY_ASSERTION));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.SAME_INDIVIDUAL));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.DIFFERENT_INDIVIDUALS));

        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.SUB_OBJECT_PROPERTY));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.SUB_PROPERTY_CHAIN_OF));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.EQUIVALENT_OBJECT_PROPERTIES));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.DISJOINT_OBJECT_PROPERTIES));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.OBJECT_PROPERTY_DOMAIN));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.OBJECT_PROPERTY_RANGE));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.TRANSITIVE_OBJECT_PROPERTY));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.FUNCTIONAL_OBJECT_PROPERTY));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.INVERSE_FUNCTIONAL_OBJECT_PROPERTY));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.SYMMETRIC_OBJECT_PROPERTY));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.ASYMMETRIC_OBJECT_PROPERTY));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.REFLEXIVE_OBJECT_PROPERTY));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.IRREFLEXIVE_OBJECT_PROPERTY));

        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.SUB_DATA_PROPERTY));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.EQUIVALENT_DATA_PROPERTIES));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.DISJOINT_DATA_PROPERTIES));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.DATA_PROPERTY_DOMAIN));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.DATA_PROPERTY_RANGE));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.FUNCTIONAL_DATA_PROPERTY));



        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.SUB_ANNOTATION_PROPERTY_OF));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.ANNOTATION_PROPERTY_DOMAIN));
        metrics.add(new OWLAPIProjectAxiomTypeCountMetric(project, AxiomType.ANNOTATION_PROPERTY_RANGE));




        project.getRootOntology().getOWLOntologyManager().addOntologyChangeListener(new OWLOntologyChangeListener() {
            public void ontologiesChanged(List<? extends OWLOntologyChange> changes) throws OWLException {
                handleOntologyChanges(new ArrayList<OWLOntologyChange>(changes));
            }
        });
    }

 
    private void handleOntologyChanges(List<OWLOntologyChange> changes) {
        for(OWLAPIProjectMetric metric : metrics) {
            metric.handleChanges(changes);
        }
    }
    
    public List<OWLAPIProjectMetric> getMetrics() {
        return new ArrayList<OWLAPIProjectMetric>(metrics);
    }
}
