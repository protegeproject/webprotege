package edu.stanford.bmir.protege.web.server.change.matcher;

import com.google.common.reflect.TypeToken;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/03/16
 */
public class FunctionalDataPropertyAxiomChangeMatcher extends AbstractAxiomMatcher<OWLFunctionalDataPropertyAxiom> {

    private final OWLObjectStringFormatter formatter;

    @Inject
    public FunctionalDataPropertyAxiomChangeMatcher(OWLObjectStringFormatter formatter) {
        super(new TypeToken<OWLFunctionalDataPropertyAxiom>(){});
        this.formatter = formatter;
    }

    @Override
    protected Optional<ChangeSummary> getDescriptionForAddAxiomChange(OWLFunctionalDataPropertyAxiom axiom,
                                                                      List<OWLOntologyChangeData> changes) {
        var msg = formatter.formatString("Made property %s functional", axiom.getProperty());
        return Optional.of(ChangeSummary.get(msg));
    }

    @Override
    protected Optional<ChangeSummary> getDescriptionForRemoveAxiomChange(OWLFunctionalDataPropertyAxiom axiom) {
        var msg = formatter.formatString("Removed the functional property characteristic from %s", axiom.getProperty());
        return Optional.of(ChangeSummary.get(msg));
    }
}
