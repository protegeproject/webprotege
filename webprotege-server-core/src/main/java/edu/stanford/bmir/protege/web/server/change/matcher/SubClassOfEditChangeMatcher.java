package edu.stanford.bmir.protege.web.server.change.matcher;

import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.model.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Mar 2017
 */
public class SubClassOfEditChangeMatcher implements ChangeMatcher {

    private final OWLObjectStringFormatter formatter;

    @Inject
    public SubClassOfEditChangeMatcher(OWLObjectStringFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public Optional<String> getDescription(List<OWLOntologyChangeData> changeData) {
        if (changeData.size() != 2) {
            return Optional.empty();
        }
        OWLOntologyChangeData change0 = changeData.get(0);
        OWLOntologyChangeData change1 = changeData.get(1);
        CandidateAxiomEdit<OWLSubClassOfAxiom> edit = new CandidateAxiomEdit<>(change0, change1, AxiomType.SUBCLASS_OF);
        if (!edit.hasAddAndRemove()) {
            return Optional.empty();
        }
        OWLSubClassOfAxiom addAx = edit.getAddAxiom().get();
        OWLSubClassOfAxiom remAx = edit.getRemoveAxiom().get();
        PropertyFiller extractorAdd = new PropertyFiller(addAx.getSubClass(),
                                                         addAx.getSuperClass());
        PropertyFiller extractorRem = new PropertyFiller(remAx.getSubClass(),
                                                         remAx.getSuperClass());
        if (!extractorAdd.isPropertyAndFillerPresent() || !extractorRem.isPropertyAndFillerPresent()) {
            return Optional.empty();
        }
        OWLProperty addedProp = extractorAdd.getProperty().get();
        OWLProperty removedProp = extractorRem.getProperty().get();
        if (!addedProp.equals(removedProp)) {
            return Optional.empty();
        }
        OWLObject addedFiller = extractorAdd.getFiller().get();
        OWLObject removedFiller = extractorRem.getFiller().get();
        if (addedFiller.equals(removedFiller)) {
            return Optional.empty();
        }
        return formatter.format("Changed the value of %s from %s to %s on %s" ,
                                addedProp,
                                removedFiller,
                                addedFiller,
                                addAx.getSubClass());

    }
}
