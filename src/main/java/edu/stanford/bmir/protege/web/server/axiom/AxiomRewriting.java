package edu.stanford.bmir.protege.web.server.axiom;

import org.semanticweb.owlapi.model.*;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/12/2012
 */
public class AxiomRewriting {


    private Map<OWLAxiom, Set<OWLAxiom>> source2RewritingMap = new HashMap<OWLAxiom, Set<OWLAxiom>>();

    public AxiomRewriting(Map<OWLAxiom, Set<OWLAxiom>> map) {
        for(OWLAxiom source : map.keySet()) {
            source2RewritingMap.put(source, new HashSet<OWLAxiom>(map.get(source)));
        }
    }

    public Set<OWLAxiom> getSourceAxioms() {
        return new HashSet<OWLAxiom>(source2RewritingMap.keySet());
    }

    public Set<OWLAxiom> getRewrittenAxioms() {
        Set<OWLAxiom> result = new HashSet<OWLAxiom>();
        for(OWLAxiom axiom : source2RewritingMap.keySet()) {
            result.addAll(source2RewritingMap.get(axiom));
        }
        return result;
    }


    public List<OWLOntologyChange> getOntologyChanges(OWLOntology rootOntology, Set<OWLAxiom> removedRewritings, Set<OWLAxiom> addedRewritings) {
        Set<OWLAxiom> resultingAxioms = new HashSet<OWLAxiom>(getRewrittenAxioms());
        resultingAxioms.removeAll(removedRewritings);
        resultingAxioms.addAll(addedRewritings);

        Set<OWLAxiom> toRemove = new HashSet<OWLAxiom>();
        Set<OWLAxiom> toAdd = new HashSet<OWLAxiom>(addedRewritings);
        for(OWLAxiom sourceAxiom : source2RewritingMap.keySet()) {
            Set<OWLAxiom> rewrittenAxioms = new HashSet<OWLAxiom>(source2RewritingMap.get(sourceAxiom));
            if(rewrittenAxioms.removeAll(removedRewritings)) {
                // Modified source, so remove it
                toRemove.add(sourceAxiom);
                // Add remaining rewriting that weren't removed
                toAdd.addAll(rewrittenAxioms);
            }
        }
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        for(OWLOntology ont : rootOntology.getImportsClosure()) {
            for(OWLAxiom ax : toRemove) {
                if(ont.containsAxiom(ax)) {
                    changes.add(new RemoveAxiom(ont, ax));
                }
            }
        }
        for(OWLAxiom ax : toAdd) {
            changes.add(new AddAxiom(rootOntology, ax));
        }
        return changes;

    }
//    public Set<OWLAxiom> getModificationRemovals() {
//        Set<OWLAxiom> axiomsToRemove = new HashSet<OWLAxiom>();
//        for(OWLAxiom sourceAxiom : source2RewritingMap.keySet()) {
//            Set<OWLAxiom> rewrittenAxioms = source2RewritingMap.get(sourceAxiom);
//            if(!endState.containsAll(rewrittenAxioms)) {
//                axiomsToRemove.add(sourceAxiom);
//            }
//        }
//        return axiomsToRemove;
//    }
//
//    public Set<OWLAxiom> getModificationAddtions(Set<OWLAxiom> removedRewritings, Set<OWLAxiom> addedRewritings) {
//        Set<OWLAxiom> axiomsToAdd = new HashSet<OWLAxiom>(endState);
//        for(OWLAxiom sourceAxiom : source2RewritingMap.keySet()) {
//            Set<OWLAxiom> rewrittenAxioms = source2RewritingMap.get(sourceAxiom);
//            axiomsToAdd.removeAll(rewrittenAxioms);
//
//        }
//        return axiomsToAdd;
//    }


    public static class Builder {

        private Map<OWLAxiom, Set<OWLAxiom>> map = new HashMap<OWLAxiom, Set<OWLAxiom>>();

        public Builder() {
        }

        public AxiomRewriting build() {
            return new AxiomRewriting(map);
        }

        public void addRewriting(OWLAxiom source, OWLAxiom rewriting) {
            addRewriting(source, Collections.singleton(rewriting));
        }

        public void addRewriting(OWLAxiom source, Set<OWLAxiom> rewriting) {
            Set<OWLAxiom> rewrittenAxioms = map.get(source);
            if(rewrittenAxioms == null) {
                rewrittenAxioms = new HashSet<OWLAxiom>(2);
                map.put(source, rewrittenAxioms);
            }
            rewrittenAxioms.addAll(rewriting);
        }

    }
    

}
