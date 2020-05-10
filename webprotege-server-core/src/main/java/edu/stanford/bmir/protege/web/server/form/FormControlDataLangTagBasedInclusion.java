package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.lang.LangTag;
import edu.stanford.bmir.protege.web.shared.lang.LangTagFilter;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
public class FormControlDataLangTagBasedInclusion {


    @Nonnull
    private final LangTagFilter langTagFilter;

    public FormControlDataLangTagBasedInclusion(@Nonnull LangTagFilter langTagFilter) {
        this.langTagFilter = checkNotNull(langTagFilter);
    }

    public boolean isIncluded(@Nonnull FormControlDataDto formControlData) {
        if(langTagFilter.isAnyLangTagIncluded()) {
            return true;
        }
        return formControlData.accept(new FormControlDataDtoVisitorEx<Boolean>() {
            @Override
            public Boolean visit(@Nonnull EntityNameControlDataDto entityNameControlData) {
                return true;
            }

            @Override
            public Boolean visit(@Nonnull FormDataDto formData) {
                return true;
            }

            @Override
            public Boolean visit(@Nonnull GridControlDataDto gridControlData) {
                return true;
            }

            @Override
            public Boolean visit(@Nonnull ImageControlDataDto imageControlData) {
                return true;
            }

            @Override
            public Boolean visit(@Nonnull MultiChoiceControlDataDto multiChoiceControlData) {
                return multiChoiceControlData.getValues()
                                             .stream()
                                             .map(PrimitiveFormControlDataDto::asLiteral)
                                             .filter(Optional::isPresent)
                                             .map(Optional::get)
                                             .map(this::isIncluded)
                                             .findFirst()
                                             .orElse(false);
            }

            @Override
            public Boolean visit(@Nonnull SingleChoiceControlDataDto singleChoiceControlData) {
                return singleChoiceControlData.getChoice()
                                              .flatMap(PrimitiveFormControlDataDto::asLiteral)
                                              .map(this::isIncluded)
                                              .orElse(false);
            }

            @Override
            public Boolean visit(@Nonnull NumberControlDataDto numberControlData) {
                return true;
            }

            @Override
            public Boolean visit(@Nonnull TextControlDataDto textControlData) {
                return textControlData.getValue().map(this::isIncluded).orElse(false);
            }

            public boolean isIncluded(OWLLiteral literal) {
                var lang = literal.getLang();
                return lang.isEmpty() || isLangTagIncluded(literal);
            }

        });
    }

    public boolean isLangTagIncluded(OWLLiteral literal) {
        var langTag = LangTag.get(literal.getLang());
        return langTagFilter.isIncluded(langTag);
    }


}
