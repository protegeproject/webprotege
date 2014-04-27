package edu.stanford.bmir.protege.web.server.owlapi.metrics;

import com.beust.jcommander.internal.Lists;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.profiles.*;

import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 22/04/2014
 */
public class DefaultMetricsCalculators {

    public static List<MetricCalculator> getDefaultMetrics(OWLOntology rootOntology) {
        List<MetricCalculator> metrics = Lists.newArrayList();
        metrics.add(new ClassCountMetricCalculator(rootOntology));
        metrics.add(new ObjectPropertyCountMetricCalculator(rootOntology));
        metrics.add(new DataPropertyCountMetricCalculator(rootOntology));
        metrics.add(new AnnotationPropertyCountMetricCalculator(rootOntology));
        metrics.add(new NamedIndividualCountMetricCalculator(rootOntology));


        metrics.add(new AxiomCountMetricCalculator(rootOntology));
        metrics.add(new LogicalAxiomCountCalculator(rootOntology));
        metrics.add(new AnnotationAxiomCountMetricCalculator(rootOntology));

        metrics.add(new ProfileMetricCalculator(rootOntology, new OWL2Profile()));
        metrics.add(new ProfileMetricCalculator(rootOntology, new OWL2DLProfile()));
        metrics.add(new ProfileMetricCalculator(rootOntology, new OWL2ELProfile()));
        metrics.add(new ProfileMetricCalculator(rootOntology, new OWL2QLProfile()));
        metrics.add(new ProfileMetricCalculator(rootOntology, new OWL2RLProfile()));

        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.SUBCLASS_OF));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.EQUIVALENT_CLASSES));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.EQUIVALENT_CLASSES));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.DISJOINT_CLASSES));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.DISJOINT_UNION));

        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.CLASS_ASSERTION));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.OBJECT_PROPERTY_ASSERTION));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.DATA_PROPERTY_ASSERTION));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.NEGATIVE_OBJECT_PROPERTY_ASSERTION));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.NEGATIVE_DATA_PROPERTY_ASSERTION));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.SAME_INDIVIDUAL));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.DIFFERENT_INDIVIDUALS));

        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.SUB_OBJECT_PROPERTY));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.SUB_PROPERTY_CHAIN_OF));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.EQUIVALENT_OBJECT_PROPERTIES));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.DISJOINT_OBJECT_PROPERTIES));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.OBJECT_PROPERTY_DOMAIN));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.OBJECT_PROPERTY_RANGE));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.TRANSITIVE_OBJECT_PROPERTY));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.FUNCTIONAL_OBJECT_PROPERTY));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.INVERSE_FUNCTIONAL_OBJECT_PROPERTY));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.SYMMETRIC_OBJECT_PROPERTY));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.ASYMMETRIC_OBJECT_PROPERTY));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.REFLEXIVE_OBJECT_PROPERTY));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.IRREFLEXIVE_OBJECT_PROPERTY));

        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.SUB_DATA_PROPERTY));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.EQUIVALENT_DATA_PROPERTIES));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.DISJOINT_DATA_PROPERTIES));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.DATA_PROPERTY_DOMAIN));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.DATA_PROPERTY_RANGE));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.FUNCTIONAL_DATA_PROPERTY));


        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.SUB_ANNOTATION_PROPERTY_OF));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.ANNOTATION_PROPERTY_DOMAIN));
        metrics.add(new AxiomTypeCountMetricCalculator(rootOntology, AxiomType.ANNOTATION_PROPERTY_RANGE));

        return metrics;
    }

}
