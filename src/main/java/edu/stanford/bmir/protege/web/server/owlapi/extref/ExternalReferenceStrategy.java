package edu.stanford.bmir.protege.web.server.owlapi.extref;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/10/2012
 */
public abstract class ExternalReferenceStrategy<T extends OWLEntity> {

    /**
     * Gets the {@link EntityType} which describes the types of entities that can be processed by this strategy.
     * @return The EntityType.  Not <code>null</code>.
     */
    public abstract EntityType<T> getSubjectType();

    /**
     * Gets a string that can be used to generate a human readable description of this strategy.  The description should
     * be such that it can be formulated into a string "Add term as " + description.  e.g. "Add term as SubClass", or
     * "Add term as rdfs:seeAlso".
     * @return A human readable (partial) description of this strategy.  Not <code>null</code>.
     */
    public abstract String getAddTermAsDescription();

    /**
     * Generates the ontology changes that will add an external reference to root ontology in the specified project.
     * @param referenceSubject The subject of the external reference.  The external reference is create "in relation"
     * to this subject. For example, if the subject is a class, the external reference might be added as a subclass of
     * it, or as an equivalent class to it, or as a seeAlso annotation on the subject class.
     * @param externalReferenceData The data that describes the external reference.
     * @param project
     * @return
     */
    public final List<OWLOntologyChange> generateOntologyChanges(T referenceSubject, ExternalReferenceData externalReferenceData, OWLAPIProject project) {
        List<OWLOntologyChange> result = new ArrayList<OWLOntologyChange>();
        List<OWLOntologyChange> strategySpecifcChanges = generateStrategySpecificOntologyChanges(referenceSubject, externalReferenceData, project);
        result.addAll(strategySpecifcChanges);
        List<OWLOntologyChange> annotationChanges = generateTermAnnotationChanges(externalReferenceData, project);
        result.addAll(annotationChanges);
        return result;
    }

    protected abstract List<OWLOntologyChange> generateStrategySpecificOntologyChanges(T referenceSubject, ExternalReferenceData externalReferenceData, OWLAPIProject project);

    /**
     * Generates a list of {@link OWLOntologyChange} objects which will apply the appropriate annotations to the IRI
     * corresponding to the external reference.  These annotations contain the annotation which specifies the preferred
     * name of the external reference and also any custom annotation defined by the ExternalReferenceData object.
     * @param externalReferenceData The ExternalReferenceData object which describes the external reference, the
     * preferred name for the external reference and custom annotations for the external reference.
     * @param project The project that the changes should be applied to.
     * @return A list of changes.  Not <code>null</code>.
     */
    protected List<OWLOntologyChange> generateTermAnnotationChanges(ExternalReferenceData externalReferenceData, OWLAPIProject project) {
        List<OWLOntologyChange> result = new ArrayList<OWLOntologyChange>();
        OWLDataFactory df = project.getDataFactory();
        OWLAnnotation prefLabelAnnotation = externalReferenceData.getPreferredLabelAnnotation(df);
        generateAddAnnotationChange(prefLabelAnnotation, externalReferenceData, project, result);
        for(OWLAnnotation annotation : externalReferenceData.getSourceSpecificAnnotations(df)) {
            generateAddAnnotationChange(annotation, externalReferenceData, project, result);
        }
        return result;
    }

    /**
     *
     * @param prefLabelAnnotation
     * @param externalReferenceData
     * @param project
     * @param result
     */
    private void generateAddAnnotationChange(OWLAnnotation prefLabelAnnotation, ExternalReferenceData externalReferenceData, OWLAPIProject project, List<OWLOntologyChange> result) {
        OWLOntology rootOntology = project.getRootOntology();
        OWLDataFactory df = project.getDataFactory();
        OWLAnnotationAssertionAxiom annoAssertion = df.getOWLAnnotationAssertionAxiom(externalReferenceData.getTermIRI(), prefLabelAnnotation);
        result.add(new AddAxiom(rootOntology, annoAssertion));
    }
}
