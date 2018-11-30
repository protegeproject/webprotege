package edu.stanford.bmir.protege.web.server.change.matcher;

import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import edu.stanford.bmir.protege.web.shared.lang.LanguageTagFormatter;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.change.OWLOntologyChangeDataVisitor;
import org.semanticweb.owlapi.model.*;

import javax.inject.Inject;
import java.util.List;
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
    public Optional<String> getDescription(List<OWLOntologyChangeData> changeData) {
        if (changeData.size() != 2) {
            return Optional.empty();
        }
        OWLOntologyChangeData change0 = changeData.get(0);
        OWLOntologyChangeData change1 = changeData.get(1);
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
                            return formatter.format("Removed language tag %s from %s %s annotation",
                                                    LanguageTagFormatter.format(removedLiteral.getLang()),
                                                    removed.getSubject(),
                                                    added.getProperty());
                        }
                        else {
                            if (removedLiteral.getLang().isEmpty()) {
                                return formatter.format("Added language tag %s to %s %s annotation",
                                                        LanguageTagFormatter.format(addedLiteral.getLang()),
                                                        removed.getSubject(),
                                                        added.getProperty());
                            }
                            else {
                                return formatter.format("Changed language tag from %s to %s on %s %s annotation",
                                                        LanguageTagFormatter.format(removedLiteral.getLang()),
                                                        LanguageTagFormatter.format(addedLiteral.getLang()),
                                                        removed.getSubject(),
                                                        added.getProperty());
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
