package edu.stanford.bmir.protege.web.server.merge;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.merge.OntologyDiff;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class ModifiedProjectOntologiesCalculator {

    private final ImmutableSet<OWLOntology> projectOntologies;

    private final ImmutableSet<OWLOntology> editedOntologies;

    private final OntologyDiffCalculator diffCalculator;

    public ModifiedProjectOntologiesCalculator(ImmutableSet<OWLOntology> projectOntologies,
                                               ImmutableSet<OWLOntology> editedOntologies,
                                               OntologyDiffCalculator diffCalculator) {
        this.projectOntologies = checkNotNull(projectOntologies);
        this.editedOntologies = checkNotNull(editedOntologies);
        this.diffCalculator = checkNotNull(diffCalculator);
    }

    public Set<OntologyDiff> getModifiedOntologyDiffs() {
        Set<OntologyDiff> diffs = new HashSet<>();
        for(OWLOntology projectOntology : projectOntologies) {
            for(OWLOntology editedOntology : editedOntologies) {
                if(isDifferentVersionOfOntology(projectOntology, editedOntology)) {
                    OntologyDiff ontologyDiff = diffCalculator.computeDiff(projectOntology, editedOntology);
                    if (!ontologyDiff.isEmpty()) {
                        diffs.add(ontologyDiff);
                    }
                }
            }
        }
        return diffs;
    }


    private boolean isDifferentVersionOfOntology(OWLOntology ontology, OWLOntology otherOntology) {
        if(ontology.getOntologyID().isAnonymous()) {
            return false;
        }
        if(otherOntology.getOntologyID().isAnonymous()) {
            return false;
        }
        IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI();
        IRI otherOntologyIRI = otherOntology.getOntologyID().getOntologyIRI();
        return ontologyIRI.equals(otherOntologyIRI);
    }
}
