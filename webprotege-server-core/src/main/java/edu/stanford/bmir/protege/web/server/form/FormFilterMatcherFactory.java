package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.match.LiteralMatcherFactory;
import edu.stanford.bmir.protege.web.server.match.Matcher;
import edu.stanford.bmir.protege.web.server.match.MatcherFactory;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-19
 */
public class FormFilterMatcherFactory {

    @Nonnull
    private final LiteralMatcherFactory literalMatcherFactory;

    @Inject
    public FormFilterMatcherFactory(@Nonnull LiteralMatcherFactory literalMatcherFactory) {
        this.literalMatcherFactory = literalMatcherFactory;
    }

    public Matcher<FormControlDataDto> getMatcher(@Nonnull FormRegionFilter formRegionFilter) {
        var matchCriteria = formRegionFilter.getMatchCriteria();
        return getMatcher(matchCriteria);
    }

    public Matcher<FormControlDataDto> getMatcher(PrimitiveFormControlDataMatchCriteria matchCriteria) {
        return matchCriteria.accept(new PrimitiveFormControlDataMatchCriteriaVisitor<>() {
            @Override
            public Matcher<FormControlDataDto> visit(EntityFormControlDataMatchCriteria criteria) {
                return m -> false;
            }

            @Override
            public Matcher<FormControlDataDto> visit(LiteralFormControlDataMatchCriteria literalFormControlDataMatchCriteria) {
                var literalCriteria = literalFormControlDataMatchCriteria.getLexicalValueCriteria();
                var literalMatcher = literalMatcherFactory.getMatcher(literalCriteria);
                return data -> data.accept(new FormControlDataDtoVisitorEx<Boolean>() {
                    @Override
                    public Boolean visit(@Nonnull EntityNameControlDataDto entityNameControlData) {
                        return false;
                    }

                    @Override
                    public Boolean visit(@Nonnull FormDataDto formData) {
                        return false;
                    }

                    @Override
                    public Boolean visit(@Nonnull GridControlDataDto gridControlData) {
                        return false;
                    }

                    @Override
                    public Boolean visit(@Nonnull ImageControlDataDto imageControlData) {
                        return false;
                    }

                    @Override
                    public Boolean visit(@Nonnull MultiChoiceControlDataDto multiChoiceControlData) {
                        return false;
                    }

                    @Override
                    public Boolean visit(@Nonnull SingleChoiceControlDataDto singleChoiceControlData) {
                        return false;
                    }

                    @Override
                    public Boolean visit(@Nonnull NumberControlDataDto numberControlData) {
                        return numberControlData.getValue().map(literalMatcher::matches).orElse(Boolean.FALSE);
                    }

                    @Override
                    public Boolean visit(@Nonnull TextControlDataDto textControlData) {
                        return textControlData.getValue().map(literalMatcher::matches).orElse(Boolean.TRUE);
                    }
                });
            }

            @Override
            public Matcher<FormControlDataDto> visit(CompositePrimitiveFormControlDataMatchCriteria compositeCriteria) {
                return m -> true;
            }
        });
    }
}
