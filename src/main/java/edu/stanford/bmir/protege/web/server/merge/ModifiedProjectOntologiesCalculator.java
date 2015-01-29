package edu.stanford.bmir.protege.web.server.merge;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.merge.OntologyDiff;
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
                if(projectOntology.getOntologyID().equals(editedOntology.getOntologyID())) {
                    OntologyDiff ontologyDiff = diffCalculator.computeDiff(projectOntology, editedOntology);
                    if (!ontologyDiff.isEmpty()) {
                        diffs.add(ontologyDiff);
                    }
                }
            }
        }
        return diffs;
    }
}
