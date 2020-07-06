package edu.stanford.bmir.protege.web.server.change;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jul 16
 */
public class ChangeListMinimiser {


    public List<OntologyChange> getMinimisedChanges(List<OntologyChange> changes) {

        var additions = new HashSet<OntologyAxiomPair>(changes.size());
        var removals = new HashSet<OntologyAxiomPair>(changes.size());

        for (OntologyChange change : changes) {
            if (change.isAddAxiom()) {
                var pair = OntologyAxiomPair.get(change.getOntologyId(), change.getAxiomOrThrow());
                if(!removals.remove(pair)) {
                    additions.add(pair);
                }
            }
            else if (change.isRemoveAxiom()) {
                var pair = OntologyAxiomPair.get(change.getOntologyId(), change.getAxiomOrThrow());
                if (!additions.remove(pair)) {
                    removals.add(pair);
                }

            }
        }

        // Minimise changes
        var minimisedChanges = new ArrayList<OntologyChange>();
        for (OntologyChange change : changes) {
            if (change.isAddAxiom()) {
                var pair = OntologyAxiomPair.get(change.getOntologyId(), change.getAxiomOrThrow());
                if (additions.contains(pair)) {
                    minimisedChanges.add(change);
                    additions.remove(pair);
                }
            }
            else if (change.isRemoveAxiom()) {
                var pair = OntologyAxiomPair.get(change.getOntologyId(), change.getAxiomOrThrow());
                if (removals.contains(pair)) {
                    minimisedChanges.add(change);
                    removals.remove(pair);
                }
            }
            else {
                minimisedChanges.add(change);
            }
        }
        return minimisedChanges;
    }
}
