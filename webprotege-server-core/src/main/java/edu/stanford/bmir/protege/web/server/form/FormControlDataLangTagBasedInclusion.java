package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.lang.LangTag;
import edu.stanford.bmir.protege.web.shared.lang.LangTagFilter;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLPrimitive;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    public boolean isIncluded(@Nonnull FormControlData formControlData) {
        if(langTagFilter.isAnyLangTagIncluded()) {
            return true;
        }
        return formControlData.accept(new FormControlDataVisitorEx<Boolean>() {
            @Override
            public Boolean visit(@Nonnull EntityNameControlData entityNameControlData) {
                return true;
            }

            @Override
            public Boolean visit(@Nonnull FormData formData) {
                return true;
            }

            @Override
            public Boolean visit(@Nonnull GridControlData gridControlData) {
                return true;
            }

            @Override
            public Boolean visit(@Nonnull ImageControlData imageControlData) {
                return true;
            }

            @Override
            public Boolean visit(@Nonnull MultiChoiceControlData multiChoiceControlData) {
                return multiChoiceControlData.getValues()
                                             .stream()
                                             .map(PrimitiveFormControlData::asLiteral)
                                             .filter(Optional::isPresent)
                                             .map(Optional::get)
                                             .map(this::isIncluded)
                                             .findFirst()
                                             .orElse(false);
            }

            @Override
            public Boolean visit(@Nonnull SingleChoiceControlData singleChoiceControlData) {
                return singleChoiceControlData.getChoice()
                                              .flatMap(PrimitiveFormControlData::asLiteral)
                                              .map(this::isIncluded)
                                              .orElse(false);
            }

            @Override
            public Boolean visit(@Nonnull NumberControlData numberControlData) {
                return true;
            }

            @Override
            public Boolean visit(@Nonnull TextControlData textControlData) {
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
