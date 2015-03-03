package edu.stanford.bmir.protege.web.server.diff;

import edu.stanford.bmir.protege.web.shared.merge.Diff;
import edu.stanford.bmir.protege.web.shared.merge.OntologyDiff;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 02/03/15
 */
public class OntologyDiff2OntologyChanges {

    public List<OWLOntologyChange> getOntologyChangesFromDiff(OntologyDiff diff, HasGetOntologyById ontologySupplier) {
        List<OWLOntologyChange> changeList = new ArrayList<>();
        OWLOntology ontology = ontologySupplier.getOntology(diff.getFromOntologyId());
        Diff<OWLAnnotation> annotationDiff = diff.getAnnotationDiff();
        for (OWLAnnotation anno : annotationDiff.getAdded()) {
            changeList.add(new RemoveOntologyAnnotation(ontology, anno));
        }
        for (OWLAnnotation anno : annotationDiff.getAdded()) {
            changeList.add(new AddOntologyAnnotation(ontology, anno));
        }
        Diff<OWLAxiom> axiomDiff = diff.getAxiomDiff();
        for (OWLAxiom axiom : axiomDiff.getRemoved()) {
            changeList.add(new RemoveAxiom(ontology, axiom));
        }
        for (OWLAxiom axiom : axiomDiff.getAdded()) {
            changeList.add(new AddAxiom(ontology, axiom));
        }
        if (!diff.getFromOntologyId().equals(diff.getToOntologyId())) {
            changeList.add(new SetOntologyID(ontology, diff.getToOntologyId()));
        }
        return changeList;
    }
}
