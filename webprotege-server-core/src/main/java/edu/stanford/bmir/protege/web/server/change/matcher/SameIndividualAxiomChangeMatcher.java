package edu.stanford.bmir.protege.web.server.change.matcher;

import com.google.common.reflect.TypeToken;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Mar 2017
 */
public class SameIndividualAxiomChangeMatcher extends AbstractAxiomMatcher<OWLSameIndividualAxiom> {

    private final OWLObjectStringFormatter formatter;

    @Inject
    public SameIndividualAxiomChangeMatcher(@Nonnull OWLObjectStringFormatter formatter) {
        super(new TypeToken<OWLSameIndividualAxiom>() {});
        this.formatter = checkNotNull(formatter);
    }

    @Override
    protected Optional<ChangeSummary> getDescriptionForAddAxiomChange(OWLSameIndividualAxiom axiom,
                                                                      List<OWLOntologyChangeData> changes) {
        if(axiom.getIndividuals().size() != 2) {
            return Optional.empty();
        }
        Iterator<OWLIndividual> it = axiom.getIndividuals().iterator();
        var msg = formatter.formatString("Added SameAs between %s and %s", it.next(), it.next());
        return Optional.of(ChangeSummary.get(msg));
    }

    @Override
    protected Optional<ChangeSummary> getDescriptionForRemoveAxiomChange(OWLSameIndividualAxiom axiom) {
        if(axiom.getIndividuals().size() != 2) {
            return Optional.empty();
        }
        Iterator<OWLIndividual> it = axiom.getIndividuals().iterator();
        var msg = formatter.formatString("Removed SameAs between %s and %s", it.next(), it.next());
        return Optional.of(ChangeSummary.get(msg));
    }
}
