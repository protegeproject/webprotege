package edu.stanford.bmir.protege.web.server.change.matcher;

import com.google.common.reflect.TypeToken;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.model.OWLPropertyRangeAxiom;

import javax.inject.Inject;
import java.util.List;
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
    protected boolean allowSignatureDeclarations() {
        return true;
    }

    @Override
    protected Optional<ChangeSummary> getDescriptionForAddAxiomChange(OWLPropertyRangeAxiom<?, ?> axiom,
                                                                      List<OWLOntologyChangeData> changes) {
        var msg = formatter.formatString("Added %s to the range of %s", axiom.getRange(), axiom.getProperty());
        return Optional.of(ChangeSummary.get(msg));
    }

    @Override
    protected Optional<ChangeSummary> getDescriptionForRemoveAxiomChange(OWLPropertyRangeAxiom<?, ?> axiom) {
        var msg = formatter.formatString("Removed %s from the range of %s", axiom.getRange(), axiom.getProperty());
        return Optional.of(ChangeSummary.get(msg));
    }
}
