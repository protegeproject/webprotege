package edu.stanford.bmir.protege.web.server.change.matcher;

import com.google.common.reflect.TypeToken;
import edu.stanford.bmir.protege.web.server.change.description.AddedParent;
import edu.stanford.bmir.protege.web.server.change.description.AddedRelationship;
import edu.stanford.bmir.protege.web.server.change.description.RemovedParent;
import edu.stanford.bmir.protege.web.server.change.description.RemovedRelationship;
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
            return Optional.of(ChangeSummary.get(AddedRelationship.get(axiom.getSubClass(),
                                                                       property.get(),
                                                                       filler.get())));
        }
        else if(changes.size() == 1) {
            return Optional.of(ChangeSummary.get(AddedParent.get(axiom.getSubClass().asOWLClass(),
                                                                 axiom.getSuperClass().asOWLClass())));
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
            return Optional.of(ChangeSummary.get(AddedRelationship.get(axiom.getSubClass(),
                                                                       property.get(),
                                                                       filler.get())));
        }
        else {
            if(axiom.getSubClass().isAnonymous()) {
                return Optional.empty();
            }
            if(axiom.getSuperClass().isAnonymous()) {
                return Optional.empty();
            }
            return Optional.of(ChangeSummary.get(RemovedParent.get(axiom.getSubClass().asOWLClass(),
                                                                   axiom.getSuperClass().asOWLClass())));
        }
    }

    @Override
    protected boolean allowSignatureDeclarations() {
        return true;
    }
}
