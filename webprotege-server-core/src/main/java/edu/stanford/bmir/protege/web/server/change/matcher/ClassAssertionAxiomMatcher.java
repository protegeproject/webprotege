package edu.stanford.bmir.protege.web.server.change.matcher;

import com.google.common.reflect.TypeToken;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/03/16
 */
public class ClassAssertionAxiomMatcher extends AbstractAxiomMatcher<OWLClassAssertionAxiom> {

    private final OWLObjectStringFormatter formatter;

    @Inject
    public ClassAssertionAxiomMatcher(OWLObjectStringFormatter formatter) {
        super(new TypeToken<OWLClassAssertionAxiom>(){});
        this.formatter = formatter;
    }

    @Override
    protected Optional<String> getDescriptionForAddAxiomChange(OWLClassAssertionAxiom axiom) {
        PropertyFiller propertyFiller = new PropertyFiller(axiom.getIndividual(),
                                                           axiom.getClassExpression());
        Optional<OWLProperty> property = propertyFiller.getProperty();
        Optional<OWLObject> filler = propertyFiller.getFiller();
        if(property.isPresent() && filler.isPresent()) {
            return formatter.format("Added relationship (%s %s) on %s", property.get(), filler.get(), axiom.getIndividual());
        }
        else {
            return formatter.format("Added %s as a type to %s", axiom.getClassExpression(), axiom.getIndividual());
        }
    }

    @Override
    protected Optional<String> getDescriptionForRemoveAxiomChange(OWLClassAssertionAxiom axiom) {
        PropertyFiller propertyFiller = new PropertyFiller(axiom.getIndividual(),
                                                           axiom.getClassExpression());
        Optional<OWLProperty> property = propertyFiller.getProperty();
        Optional<OWLObject> filler = propertyFiller.getFiller();
        if(property.isPresent() && filler.isPresent()) {
            return formatter.format("Removed relationship (%s %s) on %s", property.get(), filler.get(), axiom.getIndividual());
        }
        else {
            return formatter.format("Removed %s as a type from %s", axiom.getClassExpression(), axiom.getIndividual());
        }
    }
}
