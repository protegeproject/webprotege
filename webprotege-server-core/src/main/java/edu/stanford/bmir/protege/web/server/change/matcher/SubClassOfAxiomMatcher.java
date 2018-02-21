package edu.stanford.bmir.protege.web.server.change.matcher;

import com.google.common.reflect.TypeToken;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import javax.inject.Inject;
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
    protected Optional<String> getDescriptionForAddAxiomChange(OWLSubClassOfAxiom axiom) {
        PropertyFiller propertyFiller = new PropertyFiller(axiom.getSubClass(),
                                                           axiom.getSuperClass());
        Optional<OWLProperty> property = propertyFiller.getProperty();
        Optional<OWLObject> filler = propertyFiller.getFiller();
        if(property.isPresent() && filler.isPresent()) {
            return formatter.format("Added relationship (%s %s) on %s", property.get(), filler.get(), axiom.getSubClass());
        }
        else {
            return formatter.format("Made %s a subclass of %s", axiom.getSubClass(), axiom.getSuperClass());
        }
    }

    @Override
    protected Optional<String> getDescriptionForRemoveAxiomChange(OWLSubClassOfAxiom axiom) {
        PropertyFiller propertyFiller = new PropertyFiller(axiom.getSuperClass(),
                                                           axiom.getSuperClass());
        Optional<OWLProperty> property = propertyFiller.getProperty();
        Optional<OWLObject> filler = propertyFiller.getFiller();
        if(property.isPresent() && filler.isPresent()) {
            return formatter.format("Removed relationship (%s %s) from %s", property.get(), filler.get(), axiom.getSubClass());
        }
        else {
            return formatter.format("Removed %s as a subclass of %s" , axiom.getSubClass(), axiom.getSuperClass());
        }
    }


}
