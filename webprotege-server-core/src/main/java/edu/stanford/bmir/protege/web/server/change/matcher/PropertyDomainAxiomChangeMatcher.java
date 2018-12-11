package edu.stanford.bmir.protege.web.server.change.matcher;

import com.google.common.reflect.TypeToken;
import edu.stanford.bmir.protege.web.server.change.description.AddedPropertyDomain;
import edu.stanford.bmir.protege.web.server.change.description.RemovedPropertyDomain;
import edu.stanford.bmir.protege.web.server.change.description.RemovedPropertyRange;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
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

    private final OWLObjectStringFormatter formatter;

    @Inject
    public PropertyDomainAxiomChangeMatcher(OWLObjectStringFormatter formatter) {
        super(new TypeToken<OWLPropertyDomainAxiom<?>>(){});
        this.formatter = formatter;
    }

    @Override
    protected boolean allowSignatureDeclarations() {
        return true;
    }

    @Override
    protected Optional<ChangeSummary> getDescriptionForAddAxiomChange(OWLPropertyDomainAxiom<?> axiom,
                                                                      List<OWLOntologyChangeData> changes) {
        var msg = formatter.formatString("Added %s to the domain of %s", axiom.getDomain(), axiom.getProperty());
        return Optional.of(ChangeSummary.get(AddedPropertyDomain.get((OWLProperty) axiom.getProperty(), axiom.getDomain())));
    }

    @Override
    protected Optional<ChangeSummary> getDescriptionForRemoveAxiomChange(OWLPropertyDomainAxiom<?> axiom) {
        var msg = formatter.formatString("Removed %s from the domain of %s", axiom.getDomain(), axiom.getProperty());
        return Optional.of(ChangeSummary.get(RemovedPropertyDomain.get((OWLProperty) axiom.getProperty(), axiom.getDomain())));
    }
}
