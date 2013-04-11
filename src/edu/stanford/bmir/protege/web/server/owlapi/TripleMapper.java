package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.Triple;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/04/2012
 */
public abstract class TripleMapper<E extends OWLEntity> {

    private OWLAPIProject project;

    private E entity;

    private AnnotationsTreatment annotationsTreatment;

    private NonAnnotationTreatment nonAnnotationTreatment;

    public TripleMapper(OWLAPIProject project, E entity, AnnotationsTreatment annotationsTreatment, NonAnnotationTreatment nonAnnotationTreatment) {
        this.project = project;
        this.entity = entity;
        this.annotationsTreatment = annotationsTreatment;
        this.nonAnnotationTreatment = nonAnnotationTreatment;
    }

    public OWLAPIProject getProject() {
        return project;
    }

    public RenderingManager getRenderingManager() {
        return project.getRenderingManager();
    }

    public Set<OWLOntology> getRootOntologyImportsClosure() {
        return project.getRootOntology().getImportsClosure();
    }

    public E getEntity() {
        return entity;
    }
    
    public List<Triple> getTriples() {
        Set<Triple> result = new LinkedHashSet<Triple>();
        if (annotationsTreatment == AnnotationsTreatment.INCLUDE_ANNOTATIONS) {
            result.addAll(getAnnotationAssertionTriples());
        }
        if (nonAnnotationTreatment == NonAnnotationTreatment.INCLUDE_NON_ANNOTATIONS) {
            result.addAll(getNonAnnotationAssertionTriples());
        }
        return new ArrayList<Triple>(result);
    }

    public abstract  List<Triple> getNonAnnotationAssertionTriples();
    
    public List<Triple> getAnnotationAssertionTriples() {
        List<Triple> result = new ArrayList<Triple>();
        for (OWLOntology ontology : getRootOntologyImportsClosure()) {
            for(OWLAnnotationAssertionAxiom ax : ontology.getAnnotationAssertionAxioms(entity.getIRI())) {
                RenderingManager rm = getRenderingManager();
                EntityData subjectData = rm.getEntityData(entity);
                PropertyEntityData propData = rm.getPropertyEntityData(ax.getProperty());
                EntityData valueData = rm.getEntityData(ax.getValue());
                Triple triple = new Triple(subjectData, propData, valueData);
                result.add(triple);
            }
        }
        return result;
    }
    


}
