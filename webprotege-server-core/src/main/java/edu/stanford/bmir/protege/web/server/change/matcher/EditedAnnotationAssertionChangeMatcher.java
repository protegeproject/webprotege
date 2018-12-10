package edu.stanford.bmir.protege.web.server.change.matcher;

import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import edu.stanford.bmir.protege.web.server.renderer.LiteralLangTagTransformer;
import edu.stanford.bmir.protege.web.shared.lang.LanguageTagFormatter;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/03/16
 */
public class EditedAnnotationAssertionChangeMatcher implements ChangeMatcher {

    @Nonnull
    private final OWLObjectStringFormatter formatter;

    @Nonnull
    private final LiteralLangTagTransformer literalLangTagTransformer;

    @Inject
    public EditedAnnotationAssertionChangeMatcher(OWLObjectStringFormatter formatter,
                                                  @Nonnull LiteralLangTagTransformer literalLangTagTransformer) {
        this.formatter = formatter;
        this.literalLangTagTransformer = literalLangTagTransformer;
    }

    @Override
    public Optional<ChangeSummary> getDescription(List<OWLOntologyChangeData> changeData) {
        if(changeData.size() != 2) {
            return Optional.empty();
        }
        OWLOntologyChangeData change0 = changeData.get(0);
        OWLOntologyChangeData change1 = changeData.get(1);
        CandidateAxiomEdit<OWLAnnotationAssertionAxiom> edit = new CandidateAxiomEdit<>(change0, change1, AxiomType.ANNOTATION_ASSERTION);
        if(!edit.hasAddAndRemove()) {
            return Optional.empty();
        }
        // Same property?
        OWLAnnotationAssertionAxiom removed = edit.getRemoveAxiom().get();
        OWLAnnotationAssertionAxiom added = edit.getAddAxiom().get();
        if(!removed.getProperty().equals(added.getProperty())) {
            var msg = formatter.formatString("Changed annotation property from %s to %s on %s", removed.getProperty(), added
                    .getProperty(), added.getSubject());
            return Optional.of(ChangeSummary.get(msg));
        }
        else {
            OWLAnnotationValue removeValue = removed.getValue();
            OWLAnnotationValue addedValue = added.getValue();
            if(!removeValue.equals(addedValue)) {
                if(removeValue instanceof OWLLiteral && addedValue instanceof OWLLiteral) {
                    OWLLiteral addedLiteral = (OWLLiteral) addedValue;
                    OWLLiteral removedLiteral = (OWLLiteral) removeValue;
                    if(addedLiteral.getLiteral().equals(removedLiteral.getLiteral())) {
                        if(addedLiteral.getLang().isEmpty()) {
                            var msg = formatter.formatString("Removed language tag %s from %s %s annotation", getTransformedLangTag(removedLiteral
                                                                                                                                            .getLang()), removed
                                                                     .getSubject(), added.getProperty());
                            return Optional.of(ChangeSummary.get(msg));
                        }
                        else {
                            if(removedLiteral.getLang().isEmpty()) {
                                var msg = formatter.formatString("Added language tag %s to %s %s annotation", getTransformedLangTag(addedLiteral
                                                                                                                                            .getLang()), removed
                                                                         .getSubject(), added.getProperty());
                                return Optional.of(ChangeSummary.get(msg));
                            }
                            else {
                                var msg = formatter.formatString("Changed language tag from %s to %s on %s %s annotation", getTransformedLangTag(removedLiteral
                                                                                                                                                         .getLang()), getTransformedLangTag(addedLiteral
                                                                                                                                                                                                    .getLang()), removed
                                                                         .getSubject(), added.getProperty());
                                return Optional.of(ChangeSummary.get(msg));
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

    private String getTransformedLangTag(@Nonnull String langTag) {
        return literalLangTagTransformer.transformLangTag(LanguageTagFormatter.format(langTag));
    }

    private Optional<ChangeSummary> getValueChangedDescription(OWLAnnotationAssertionAxiom added) {
        var msg = formatter.formatString("Edited %s annotation on %s", added.getProperty(), added.getSubject());
        return Optional.of(ChangeSummary.get(msg));
    }
}
