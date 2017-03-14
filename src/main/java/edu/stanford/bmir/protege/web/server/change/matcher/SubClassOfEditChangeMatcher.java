package edu.stanford.bmir.protege.web.server.change.matcher;

import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.model.*;

import javax.inject.Inject;
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
    public Optional<String> getDescription(ChangeApplicationResult<?> result) {
        if (result.getChangeList().size() != 2) {
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
        PropertyFillerExtractor extractorAdd = new PropertyFillerExtractor(addAx.getSuperClass());
        PropertyFillerExtractor extractorRem = new PropertyFillerExtractor(remAx.getSuperClass());
        if (!extractorAdd.isPropertyAndFillerExtracted() || !extractorRem.isPropertyAndFillerExtracted()) {
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
