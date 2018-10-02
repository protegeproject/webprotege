package edu.stanford.bmir.protege.web.server.change;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import javax.annotation.Nonnull;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jul 16
 */
public class ChangeListMinimiser {


    public List<OWLOntologyChange> getMinimisedChanges(List<? extends OWLOntologyChange> changes) {

        final Set<OntologyAxiomPair> additions = new HashSet<>();
        final Set<OntologyAxiomPair> removals = new HashSet<>();

        for (OWLOntologyChange change : changes) {
            if (change.isAddAxiom()) {
                OntologyAxiomPair pair = OntologyAxiomPair.get(change.getOntology(), change.getAxiom());
                if(!removals.remove(pair)) {
                    additions.add(pair);
                }
            }
            else if (change.isRemoveAxiom()) {
                OntologyAxiomPair pair = OntologyAxiomPair.get(change.getOntology(), change.getAxiom());
                if (!additions.remove(pair)) {
                    removals.add(pair);
                }

            }
        }

        // Minimise changes
        final List<OWLOntologyChange> minimisedChanges = new ArrayList<>();
        for (OWLOntologyChange change : changes) {
            if (change.isAddAxiom()) {
                OntologyAxiomPair pair = OntologyAxiomPair.get(change.getOntology(), change.getAxiom());
                if (additions.contains(pair)) {
                    minimisedChanges.add(change);
                    additions.remove(pair);
                }
            }
            else if (change.isRemoveAxiom()) {
                OntologyAxiomPair pair = OntologyAxiomPair.get(change.getOntology(), change.getAxiom());
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
