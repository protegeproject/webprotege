package edu.stanford.bmir.protege.web.server.change.matcher;

import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.shortform.WebProtegeIRIShortFormProvider;
import edu.stanford.bmir.protege.web.server.shortform.WebProtegeShortFormProvider;
import org.semanticweb.owlapi.model.*;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/03/16
 */
public class EditedAnnotationAssertion implements ChangeMatcher {

    private WebProtegeShortFormProvider webProtegeShortFormProvider;

    private WebProtegeIRIShortFormProvider iriShortFormProvider;

    @Inject
    public EditedAnnotationAssertion(WebProtegeShortFormProvider webProtegeShortFormProvider, WebProtegeIRIShortFormProvider iriShortFormProvider) {
        this.webProtegeShortFormProvider = webProtegeShortFormProvider;
        this.iriShortFormProvider = iriShortFormProvider;
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
            return Optional.of(String.format("Changed annotation property from %s to %s",
                    webProtegeShortFormProvider.getShortForm(removed.getProperty()),
                    webProtegeShortFormProvider.getShortForm(added.getProperty())
            ));
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
                            return Optional.of(String.format("Removed language tag '%s' from %s on %s",
                                    removedLiteral.getLang(),
                                    webProtegeShortFormProvider.getShortForm(added.getProperty()),
                                    iriShortFormProvider.getShortForm((IRI) removed.getSubject())
                            ));
                        }
                        else {
                            if (removedLiteral.getLang().isEmpty()) {
                                return Optional.of(String.format("Added language tag '%s' to %s on %s",
                                        addedLiteral.getLang(),
                                        webProtegeShortFormProvider.getShortForm(added.getProperty()),
                                        iriShortFormProvider.getShortForm((IRI) removed.getSubject())
                                ));
                            }
                            else {
                                return Optional.of(String.format("Changed language tag (from '%s' to '%s') in %s to %s",
                                        removedLiteral.getLang(),
                                        addedLiteral.getLang(),
                                        webProtegeShortFormProvider.getShortForm(added.getProperty()),
                                        iriShortFormProvider.getShortForm((IRI) removed.getSubject())
                                ));
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
        return Optional.of(String.format("Edited %s on %s",
                webProtegeShortFormProvider.getShortForm(added.getProperty()),
                render(added.getSubject())
        ));
    }

    private String render(OWLAnnotationSubject subject) {
        if (subject instanceof IRI) {
            return iriShortFormProvider.getShortForm((IRI) subject);
        }
        else {
            return subject.toString();
        }
    }
}
