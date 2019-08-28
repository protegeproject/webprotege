package edu.stanford.bmir.protege.web.server.change.matcher;

import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.change.description.*;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import edu.stanford.bmir.protege.web.server.renderer.LiteralLangTagTransformer;
import edu.stanford.bmir.protege.web.shared.lang.LanguageTagFormatter;
import org.semanticweb.owlapi.model.*;

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
    public Optional<ChangeSummary> getDescription(List<OntologyChange> changes) {
        if(changes.size() != 2) {
            return Optional.empty();
        }
        OntologyChange change0 = changes.get(0);
        OntologyChange change1 = changes.get(1);
        CandidateAxiomEdit<OWLAnnotationAssertionAxiom> edit = new CandidateAxiomEdit<>(change0, change1, AxiomType.ANNOTATION_ASSERTION);
        if(!edit.hasAddAndRemove()) {
            return Optional.empty();
        }
        // Same property?
        OWLAnnotationAssertionAxiom removed = edit.getRemoveAxiom().get();
        OWLAnnotationAssertionAxiom added = edit.getAddAxiom().get();
        if(!removed.getSubject().equals(added.getSubject())) {
            return Optional.empty();
        }
        if(!added.getSubject().isIRI()) {
            return Optional.empty();
        }
        var subject = (IRI) removed.getSubject();
        if(!removed.getProperty().equals(added.getProperty())) {
            var msg = formatter.formatString("Changed annotation property from %s to %s on %s", removed.getProperty(), added
                    .getProperty(), added.getSubject());
            return Optional.of(ChangeSummary.get(SwitchedAnnotationProperty.get(subject,
                                                                                removed.getProperty(),
                                                                                added.getProperty(),
                                                                                added.getValue())));
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
                            return Optional.of(ChangeSummary.get(RemovedLanguageTag.get(subject,
                                                                                        added.getProperty(),
                                                                                        removeValue,
                                                                                        getTransformedLangTag(removedLiteral.getLang()))));
                        }
                        else {
                            if(removedLiteral.getLang().isEmpty()) {
                                return Optional.of(ChangeSummary.get(AddedLanguageTag.get(subject,
                                                                                          added.getProperty(),
                                                                                          addedValue,
                                                                                          getTransformedLangTag(addedLiteral.getLang()))));

                            }
                            else {
                                return Optional.of(ChangeSummary.get(EditedLanguageTag.get(subject,
                                                                                           added.getProperty(),
                                                                                           removeValue,
                                                                                           removedLiteral.getLang(),
                                                                                           addedLiteral.getLang())));
                            }
                        }
                    }
                    else {
                        return getValueChangedDescription(added, removed);
                    }
                }
                else {
                    return getValueChangedDescription(added, removed);
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

    private Optional<ChangeSummary> getValueChangedDescription(OWLAnnotationAssertionAxiom added,
                                                               OWLAnnotationAssertionAxiom removed) {
        return Optional.of(ChangeSummary.get(EditedAnnotationValue.get((IRI) added.getSubject(),
                                                                       added.getProperty(),
                                                                       removed.getValue(),
                                                                       added.getValue())));
    }
}
