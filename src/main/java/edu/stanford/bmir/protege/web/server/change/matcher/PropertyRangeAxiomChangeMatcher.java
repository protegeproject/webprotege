package edu.stanford.bmir.protege.web.server.change.matcher;

import com.google.common.reflect.TypeToken;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.model.OWLPropertyRangeAxiom;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/03/16
 */
public class PropertyRangeAxiomChangeMatcher extends AbstractAxiomMatcher<OWLPropertyRangeAxiom<?,?>> {

    private final OWLObjectStringFormatter formatter;

    @Inject
    public PropertyRangeAxiomChangeMatcher(OWLObjectStringFormatter formatter) {
        super(new TypeToken<OWLPropertyRangeAxiom<?,?>>(){});
        this.formatter = formatter;
    }

    @Override
    protected Optional<String> getDescriptionForAddAxiomChange(OWLPropertyRangeAxiom<?, ?> axiom) {
        return formatter.format("Added %s to the range of %s", axiom.getRange(), axiom.getProperty());
    }

    @Override
    protected Optional<String> getDescriptionForRemoveAxiomChange(OWLPropertyRangeAxiom<?, ?> axiom) {
        return formatter.format("Removed %s from the range of %s", axiom.getRange(), axiom.getProperty());
    }
}
