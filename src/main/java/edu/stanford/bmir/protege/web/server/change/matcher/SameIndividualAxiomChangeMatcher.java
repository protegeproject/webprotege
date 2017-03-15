package edu.stanford.bmir.protege.web.server.change.matcher;

import com.google.common.reflect.TypeToken;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Iterator;
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
    protected Optional<String> getDescriptionForAddAxiomChange(OWLSameIndividualAxiom axiom) {
        if(axiom.getIndividuals().size() != 2) {
            return Optional.empty();
        }
        Iterator<OWLIndividual> it = axiom.getIndividuals().iterator();
        return formatter.format("Added SameAs between %s and %s", it.next(), it.next());
    }

    @Override
    protected Optional<String> getDescriptionForRemoveAxiomChange(OWLSameIndividualAxiom axiom) {
        if(axiom.getIndividuals().size() != 2) {
            return Optional.empty();
        }
        Iterator<OWLIndividual> it = axiom.getIndividuals().iterator();
        return formatter.format("Removed SameAs between %s and %s", it.next(), it.next());
    }
}
