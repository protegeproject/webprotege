package edu.stanford.bmir.protege.web.server.change.matcher;

import com.google.common.reflect.TypeToken;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/03/16
 */
public class AnnotationAssertionChangeMatcher extends AbstractAxiomMatcher<OWLAnnotationAssertionAxiom> {

    private final OWLObjectStringFormatter formatter;

    @Inject
    public AnnotationAssertionChangeMatcher(OWLObjectStringFormatter formatter) {
        super(new TypeToken<OWLAnnotationAssertionAxiom>(){});
        this.formatter = formatter;
    }

    @Override
    protected Optional<String> getDescriptionForAddAxiomChange(OWLAnnotationAssertionAxiom axiom) {
        return formatter.format("Added annotation (%s  %s) to %s",
                                axiom.getProperty(),
                                axiom.getValue(),
                                axiom.getSubject());
    }

    @Override
    protected Optional<String> getDescriptionForRemoveAxiomChange(OWLAnnotationAssertionAxiom axiom) {
        return formatter.format("Removed annotation (%s  %s) from %s",
                                axiom.getProperty(),
                                axiom.getValue(),
                                axiom.getSubject());
    }
}
