package edu.stanford.bmir.protege.web.server.merge_add;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.server.project.Ontology;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MergeOntologyCalculator {

    public MergeOntologyCalculator() {
    }

    ImmutableSet<OWLAxiom> getMergeAxioms(Collection<Ontology> projectOntologies, Collection<Ontology> uploadedOntologies, List<OWLOntologyID> ontologyList){
        ArrayList<OWLOntologyID> list = (ArrayList<OWLOntologyID>) ontologyList;
        var axioms = ImmutableSet.<OWLAxiom>builder();
        for (Ontology o: projectOntologies) {
            if(list.contains(o.getOntologyId())) {
                var projectAxioms = o.getAxioms();
                for (OWLAxiom x : projectAxioms) {
                    axioms.add(x);
                }
            }
        }
        for (Ontology o: uploadedOntologies){
            if (list.contains(o.getOntologyId())){
                var uploadedAxioms = o.getAxioms();
                for (OWLAxiom x: uploadedAxioms) {
                    axioms.add(x);
                }
            }
        }
        return axioms.build();
    }

    ImmutableSet<OWLAnnotation> getMergeAnnotations(Collection<Ontology> projectOntologies, Collection<Ontology> uploadedOntologies, List<OWLOntologyID> ontologyList){
        ArrayList<OWLOntologyID> list = (ArrayList<OWLOntologyID>) ontologyList;
        var annotations = ImmutableSet.<OWLAnnotation>builder();
        for (Ontology o: projectOntologies) {
            if(list.contains(o.getOntologyId())) {
                var projectAnnotations = o.getAnnotations();
                for (OWLAnnotation x : projectAnnotations) {
                    annotations.add(x);
                }
            }
        }
        for (Ontology o: uploadedOntologies){
            if (list.contains(o.getOntologyId())){
                var uploadedAnnotations = o.getAnnotations();
                for (OWLAnnotation x: uploadedAnnotations) {
                    annotations.add(x);
                }
            }
        }
        return annotations.build();
    }

}
