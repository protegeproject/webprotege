package edu.stanford.bmir.protege.web.server.change.matcher;

import com.google.common.reflect.TypeToken;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/03/16
 */
public class ClassAssertionAxiomMatcher extends AbstractAxiomMatcher<OWLClassAssertionAxiom> {

    private final OWLObjectStringFormatter formatter;

    @Inject
    public ClassAssertionAxiomMatcher(OWLObjectStringFormatter formatter) {
        super(new TypeToken<OWLClassAssertionAxiom>(){});
        this.formatter = formatter;
    }

    @Override
    protected Optional<String> getDescriptionForAddAxiomChange(OWLClassAssertionAxiom axiom) {
        return formatter.format("Added %s as a type to %s", axiom.getClassExpression(), axiom.getIndividual());
    }

    @Override
    protected Optional<String> getDescriptionForRemoveAxiomChange(OWLClassAssertionAxiom axiom) {
        return formatter.format("Removed %s as a type from %s", axiom.getClassExpression(), axiom.getIndividual());
    }
}
