package edu.stanford.bmir.protege.web.server.change.matcher;

import com.google.common.reflect.TypeToken;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.model.OWLPropertyAssertionAxiom;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Mar 2017
 */
public class PropertyAssertionAxiomMatcher extends AbstractAxiomMatcher<OWLPropertyAssertionAxiom<?,?>> {

    private final OWLObjectStringFormatter formatter;

    @Inject
    public PropertyAssertionAxiomMatcher(OWLObjectStringFormatter formatter) {
        super(new TypeToken<OWLPropertyAssertionAxiom<?,?>>() {});
        this.formatter = formatter;
    }

    @Override
    protected Optional<String> getDescriptionForAddAxiomChange(OWLPropertyAssertionAxiom<?,?> axiom) {
        return Optional.of(formatter.formatString(
                "Added relationship (%s %s) on individual %s",
                axiom.getProperty(),
                axiom.getObject(),
                axiom.getSubject()
        ));
    }

    @Override
    protected Optional<String> getDescriptionForRemoveAxiomChange(OWLPropertyAssertionAxiom<?,?> axiom) {
        return Optional.of(formatter.formatString(
                "Removed relationship (%s %s) on individual %s",
                axiom.getProperty(),
                axiom.getObject(),
                axiom.getSubject()
        ));
    }
}
