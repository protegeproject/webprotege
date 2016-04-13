package edu.stanford.bmir.protege.web.server.change.matcher;

import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/04/16
 */
public class SubClassOfChangeMatcher implements ChangeMatcher {

    private final OWLObjectStringFormatter formatter;

    @Inject
    public SubClassOfChangeMatcher(OWLObjectStringFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public Optional<String> getDescription(ChangeApplicationResult<?> result) {
        if(result.getChangeList().size() != 2) {
            return Optional.empty();
        }
        OWLOntologyChange change0 = result.getChangeList().get(0);
        OWLOntologyChange change1 = result.getChangeList().get(1);
        CandidateAxiomEdit<OWLSubClassOfAxiom> edit = new CandidateAxiomEdit<>(change0, change1, AxiomType.SUBCLASS_OF);
        if (!edit.hasAddAndRemove()) {
            return Optional.empty();
        }
        OWLSubClassOfAxiom addAx = edit.getAddAxiom().get();
        OWLSubClassOfAxiom remAx = edit.getRemoveAxiom().get();
        if(!addAx.getSubClass().equals(remAx.getSubClass())) {
            return Optional.empty();
        }
        if(addAx.getSuperClass().isAnonymous()) {
            return Optional.empty();
        }
        if(remAx.getSuperClass().isAnonymous()) {
            return Optional.empty();
        }
        return formatter.format("Moved %s from %s to %s", addAx.getSubClass(), remAx.getSuperClass(), addAx.getSuperClass());
    }
}
