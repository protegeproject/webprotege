package edu.stanford.bmir.protege.web.server.change.matcher;

import javax.inject.Inject;
import com.google.inject.TypeLiteral;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.*;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/03/16
 */
public class AnnotationAssertionChangeMatcher extends AbstractAxiomMatcher<OWLAnnotationAssertionAxiom> {

    private final OWLObjectStringFormatter formatter;

    @Inject
    public AnnotationAssertionChangeMatcher(OWLObjectRenderer renderer, OWLObjectStringFormatter formatter) {
        super(new TypeLiteral<OWLAnnotationAssertionAxiom>(){});
        this.formatter = formatter;
    }

    @Override
    protected Optional<String> getDescriptionForAddAxiomChange(OWLAnnotationAssertionAxiom axiom) {
        return formatter.format("Added %s to %s", axiom.getProperty(), axiom.getSubject());
    }

    @Override
    protected Optional<String> getDescriptionForRemoveAxiomChange(OWLAnnotationAssertionAxiom axiom) {
        return formatter.format("Remove %s from %s", axiom.getProperty(), axiom.getSubject());
    }
}
