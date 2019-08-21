package edu.stanford.bmir.protege.web.server.diff;

import edu.stanford.bmir.protege.web.server.index.OntologyIndex;
import edu.stanford.bmir.protege.web.shared.merge.Diff;
import edu.stanford.bmir.protege.web.shared.merge.OntologyDiff;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 02/03/15
 */
public class OntologyDiff2OntologyChanges {

    @Nonnull
    private final OntologyIndex ontologyIndex;

    @Inject
    public OntologyDiff2OntologyChanges(@Nonnull OntologyIndex ontologyIndex) {
        this.ontologyIndex = checkNotNull(ontologyIndex);
    }

    @Nonnull
    public List<OWLOntologyChange> getOntologyChangesFromDiff(@Nonnull OntologyDiff diff) {
        checkNotNull(diff);
        var changeList = new ArrayList<OWLOntologyChange>();
        var ontology = ontologyIndex.getOntology(diff.getFromOntologyId());
        ontology.ifPresent(ont -> {
            Diff<OWLAnnotation> annotationDiff = diff.getAnnotationDiff();
            for (OWLAnnotation anno : annotationDiff.getAdded()) {
                changeList.add(new AddOntologyAnnotation(ont, anno));
            }
            for (OWLAnnotation anno : annotationDiff.getRemoved()) {
                changeList.add(new RemoveOntologyAnnotation(ont, anno));
            }
            Diff<OWLAxiom> axiomDiff = diff.getAxiomDiff();
            for (OWLAxiom axiom : axiomDiff.getRemoved()) {
                changeList.add(new RemoveAxiom(ont, axiom));
            }
            for (OWLAxiom axiom : axiomDiff.getAdded()) {
                changeList.add(new AddAxiom(ont, axiom));
            }
            if (!diff.getFromOntologyId().equals(diff.getToOntologyId())) {
                changeList.add(new SetOntologyID(ont, diff.getToOntologyId()));
            }
        });
        return changeList;
    }
}
