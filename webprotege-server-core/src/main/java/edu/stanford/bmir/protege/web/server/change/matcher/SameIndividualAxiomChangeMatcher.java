package edu.stanford.bmir.protege.web.server.change.matcher;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.change.description.AddedSameAs;
import edu.stanford.bmir.protege.web.server.change.description.RemovedSameAs;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Mar 2017
 */
public class SameIndividualAxiomChangeMatcher extends AbstractAxiomMatcher<OWLSameIndividualAxiom> {

    @Inject
    public SameIndividualAxiomChangeMatcher() {
        super(new TypeToken<OWLSameIndividualAxiom>() {});
    }

    @Override
    protected Optional<ChangeSummary> getDescriptionForAddAxiomChange(OWLSameIndividualAxiom axiom,
                                                                      List<OntologyChange> changes) {
        return Optional.of(ChangeSummary.get(AddedSameAs.get(ImmutableSet.copyOf(axiom.getIndividuals()))));
    }

    @Override
    protected Optional<ChangeSummary> getDescriptionForRemoveAxiomChange(OWLSameIndividualAxiom axiom) {
        return Optional.of(ChangeSummary.get(RemovedSameAs.get(ImmutableSet.copyOf(axiom.getAnonymousIndividuals()))));
    }
}
