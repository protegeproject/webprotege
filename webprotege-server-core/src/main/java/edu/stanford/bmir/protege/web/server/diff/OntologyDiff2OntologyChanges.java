package edu.stanford.bmir.protege.web.server.diff;

import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.shared.merge.Diff;
import edu.stanford.bmir.protege.web.shared.merge.OntologyDiff;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;

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

    @Inject
    public OntologyDiff2OntologyChanges() {
    }

    @Nonnull
    public List<OntologyChange> getOntologyChangesFromDiff(@Nonnull OntologyDiff diff) {
        checkNotNull(diff);
        var changeList = new ArrayList<OntologyChange>();
        var ont = diff.getFromOntologyId();
        Diff<OWLAnnotation> annotationDiff = diff.getAnnotationDiff();
        for (OWLAnnotation anno : annotationDiff.getAdded()) {
            changeList.add(AddOntologyAnnotationChange.of(ont, anno));
        }
        for (OWLAnnotation anno : annotationDiff.getRemoved()) {
            changeList.add(RemoveOntologyAnnotationChange.of(ont, anno));
        }
        Diff<OWLAxiom> axiomDiff = diff.getAxiomDiff();
        for (OWLAxiom axiom : axiomDiff.getRemoved()) {
            changeList.add(RemoveAxiomChange.of(ont, axiom));
        }
        for (OWLAxiom axiom : axiomDiff.getAdded()) {
            changeList.add(AddAxiomChange.of(ont, axiom));
        }
        return changeList;
    }
}
