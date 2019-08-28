package edu.stanford.bmir.protege.web.server.change.matcher;

import com.google.common.reflect.TypeToken;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.change.description.SetDataPropertyFunctional;
import edu.stanford.bmir.protege.web.server.change.description.UnsetDataPropertyFunctional;
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

    @Inject
    public FunctionalDataPropertyAxiomChangeMatcher() {
        super(new TypeToken<OWLFunctionalDataPropertyAxiom>(){});
    }

    @Override
    protected Optional<ChangeSummary> getDescriptionForAddAxiomChange(OWLFunctionalDataPropertyAxiom axiom,
                                                                      List<OntologyChange> changes) {
        return Optional.of(ChangeSummary.get(SetDataPropertyFunctional.get(axiom.getProperty().asOWLDataProperty())));
    }

    @Override
    protected Optional<ChangeSummary> getDescriptionForRemoveAxiomChange(OWLFunctionalDataPropertyAxiom axiom) {
        return Optional.of(ChangeSummary.get(UnsetDataPropertyFunctional.get(axiom.getProperty().asOWLDataProperty())));
    }
}
