package edu.stanford.bmir.protege.web.server.change.matcher;

import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.model.*;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/03/16
 */
public class EditedAnnotationAssertionChangeMatcher implements ChangeMatcher {

    private OWLObjectStringFormatter formatter;

    @Inject
    public EditedAnnotationAssertionChangeMatcher(OWLObjectStringFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public Optional<String> getDescription(ChangeApplicationResult<?> result) {
        if (result.getChangeList().size() != 2) {
            return Optional.empty();
        }
        OWLOntologyChange change0 = result.getChangeList().get(0);
        OWLOntologyChange change1 = result.getChangeList().get(1);
        CandidateAxiomEdit<OWLAnnotationAssertionAxiom> edit = new CandidateAxiomEdit<>(change0, change1, AxiomType.ANNOTATION_ASSERTION);
        if (!edit.hasAddAndRemove()) {
            return Optional.empty();
        }
        // Same property?
        OWLAnnotationAssertionAxiom removed = edit.getRemoveAxiom().get();
        OWLAnnotationAssertionAxiom added = edit.getAddAxiom().get();
        if (!removed.getProperty().equals(added.getProperty())) {
            return formatter.format("Changed annotation property from %s to %s on %s",
                                    removed.getProperty(),
                                    added.getProperty(),
                                    added.getSubject());
        }
        else {
            OWLAnnotationValue removeValue = removed.getValue();
            OWLAnnotationValue addedValue = added.getValue();
            if (!removeValue.equals(addedValue)) {
                if (removeValue instanceof OWLLiteral && addedValue instanceof OWLLiteral) {
                    OWLLiteral addedLiteral = (OWLLiteral) addedValue;
                    OWLLiteral removedLiteral = (OWLLiteral) removeValue;
                    if (addedLiteral.getLiteral().equals(removedLiteral.getLiteral())) {
                        if (addedLiteral.getLang().isEmpty()) {
                            return formatter.format("Removed language tag '%s' from %s on %s",
                                    removedLiteral.getLang(),
                                    added.getProperty(),
                                    removed.getSubject());
                        }
                        else {
                            if (removedLiteral.getLang().isEmpty()) {
                                return formatter.format("Added language tag '%s' to %s on %s",
                                        addedLiteral.getLang(),
                                        added.getProperty(),
                                        removed.getSubject());
                            }
                            else {
                                return formatter.format("Changed language tag (from '%s' to '%s') in %s to %s",
                                        removedLiteral.getLang(),
                                        addedLiteral.getLang(),
                                        added.getProperty(),
                                        removed.getSubject());
                            }
                        }
                    }
                    else {
                        return getValueChangedDescription(added);
                    }
                }
                else {
                    return getValueChangedDescription(added);
                }
            }
            else {
                return Optional.empty();
            }
        }
    }

    private Optional<String> getValueChangedDescription(OWLAnnotationAssertionAxiom added) {
        return formatter.format("Edited %s annotation on %s",
                added.getProperty(),
                added.getSubject());
    }
}
