package edu.stanford.bmir.protege.web.server.change.matcher;

import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.change.description.EditedRelationshipValue;
import edu.stanford.bmir.protege.web.server.change.description.SwitchedRelationshipProperty;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Mar 2017
 */
public class SubClassOfEditChangeMatcher implements ChangeMatcher {

    @Inject
    public SubClassOfEditChangeMatcher() {
    }

    @Override
    public Optional<ChangeSummary> getDescription(List<OntologyChange> changeData) {
        if (changeData.size() != 2) {
            return Optional.empty();
        }
        var change0 = changeData.get(0);
        var change1 = changeData.get(1);
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
        var addedProp = extractorAdd.getProperty().get();
        var removedProp = extractorRem.getProperty().get();

        var addedFiller = extractorAdd.getFiller().get();
        var removedFiller = extractorRem.getFiller().get();

        if (addedFiller.equals(removedFiller)) {
            if (addedProp.equals(removedProp)) {
                return Optional.empty();
            }
            else {
                return Optional.of(ChangeSummary.get(SwitchedRelationshipProperty.get(
                        addAx.getSubClass(),
                        removedProp,
                        addedProp,
                        addedFiller
                )));
            }
        }
        return Optional.of(ChangeSummary.get(EditedRelationshipValue.get(addAx.getSubClass(),
                                                                         addedProp,
                                                                         removedFiller,
                                                                         addedFiller)));

    }
}
