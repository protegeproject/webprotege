package edu.stanford.bmir.protege.web.server.change.matcher;

import com.google.common.reflect.TypeToken;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20/03/16
 */
public class SubClassOfAxiomMatcher extends AbstractAxiomMatcher<OWLSubClassOfAxiom> {

    private final OWLObjectStringFormatter formatter;

    @Inject
    public SubClassOfAxiomMatcher(OWLObjectStringFormatter formatter) {
        super(new TypeToken<OWLSubClassOfAxiom>(){});
        this.formatter = formatter;
    }

    @Override
    protected Optional<ChangeSummary> getDescriptionForAddAxiomChange(OWLSubClassOfAxiom axiom,
                                                                      List<OWLOntologyChangeData> changes) {
        PropertyFiller propertyFiller = new PropertyFiller(axiom.getSubClass(),
                                                           axiom.getSuperClass());
        Optional<OWLProperty> property = propertyFiller.getProperty();
        Optional<OWLObject> filler = propertyFiller.getFiller();
        if(property.isPresent() && filler.isPresent()) {
            var msg = formatter.formatString("Added relationship (%s %s) on %s", property.get(), filler.get(), axiom.getSubClass());
            return Optional.of(ChangeSummary.get(msg));
        }
        else if(changes.size() == 1) {
            var msg = formatter.formatString("Made %s a subclass of %s", axiom.getSubClass(), axiom.getSuperClass());
            return Optional.of(ChangeSummary.get(msg));
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    protected Optional<ChangeSummary> getDescriptionForRemoveAxiomChange(OWLSubClassOfAxiom axiom) {
        PropertyFiller propertyFiller = new PropertyFiller(axiom.getSuperClass(),
                                                           axiom.getSuperClass());
        Optional<OWLProperty> property = propertyFiller.getProperty();
        Optional<OWLObject> filler = propertyFiller.getFiller();
        if(property.isPresent() && filler.isPresent()) {
            var msg = formatter.formatString("Removed relationship (%s %s) from %s", property.get(), filler.get(), axiom.getSubClass());
            return Optional.of(ChangeSummary.get(msg));
        }
        else {
            var msg = formatter.formatString("Removed %s as a subclass of %s" , axiom.getSubClass(), axiom.getSuperClass());
            return Optional.of(ChangeSummary.get(msg));
        }
    }

    @Override
    protected boolean allowSignatureDeclarations() {
        return true;
    }
}
