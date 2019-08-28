package edu.stanford.bmir.protege.web.server.change.matcher;

import com.google.common.reflect.TypeToken;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.change.description.AddedPropertyDomain;
import edu.stanford.bmir.protege.web.server.change.description.RemovedPropertyDomain;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLPropertyDomainAxiom;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/03/16
 */
public class PropertyDomainAxiomChangeMatcher extends AbstractAxiomMatcher<OWLPropertyDomainAxiom<?>> {

    @Inject
    public PropertyDomainAxiomChangeMatcher() {
        super(new TypeToken<OWLPropertyDomainAxiom<?>>(){});
    }

    @Override
    protected boolean allowSignatureDeclarations() {
        return true;
    }

    @Override
    protected Optional<ChangeSummary> getDescriptionForAddAxiomChange(OWLPropertyDomainAxiom<?> axiom,
                                                                      List<OntologyChange> changes) {
        return Optional.of(ChangeSummary.get(AddedPropertyDomain.get((OWLProperty) axiom.getProperty(), axiom.getDomain())));
    }

    @Override
    protected Optional<ChangeSummary> getDescriptionForRemoveAxiomChange(OWLPropertyDomainAxiom<?> axiom) {
        return Optional.of(ChangeSummary.get(RemovedPropertyDomain.get((OWLProperty) axiom.getProperty(), axiom.getDomain())));
    }
}
