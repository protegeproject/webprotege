package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-18
 */
public class SelectableLiteralCriteriaPresenter extends SelectableCriteriaTypePresenter<LiteralCriteria> {

    /*

    @Type(LiteralLexicalValueNotInDatatypeLexicalSpaceCriteria.class),
        @Type(LangTagMatchesCriteria.class),
        @Type(AnyLangTagOrEmptyLangTagCriteria.class),
        @Type(LangTagIsEmptyCriteria.class),
        @Type(StringStartsWithCriteria.class),
        @Type(StringEndsWithCriteria.class),
        @Type(StringContainsCriteria.class),
        @Type(StringEqualsCriteria.class),
        @Type(StringContainsRegexMatchCriteria.class),
        @Type(StringDoesNotContainRegexMatchCriteria.class),
        @Type(NumericValueCriteria.class),
        @Type(DateIsBeforeCriteria.class),
        @Type(DateIsAfterCriteria.class),
        @Type(StringContainsRepeatedSpacesCriteria.class),
        @Type(StringHasUntrimmedSpaceCriteria.class)

     */

    @Nonnull
    private final LangTagMatchesCriteriaPresenterFactory langTagMatchesCriteriaPresenterFactory;

    @Nonnull
    private final LangTagIsEmptyCriteriaPresenterFactory langTagIsEmptyCriteriaPresenterFactory;

    @Nonnull
    private final StringStartsWithCriteriaPresenterFactory stringStartsWithCriteriaPresenterFactory;

    @Nonnull
    private final StringEndsWithCriteriaPresenterFactory stringEndsWithCriteriaPresenterFactory;

    @Nonnull
    private final StringContainsCriteriaPresenterFactory stringContainsCriteriaPresenterFactory;

    @Nonnull
    private final StringEqualsCriteriaPresenterFactory stringEqualsCriteriaPresenterFactory;

    @Nonnull
    private final StringMatchesRegexCriteriaPresenterFactory stringMatchesRegexCriteriaPresenterFactory;

    @Nonnull
    private final StringDoesNotMatchRegexCriteriaPresenterFactory stringDoesNotMatchRegexCriteriaPresenterFactory;

    @Nonnull
    private final NumericValueCriteriaPresenterFactory numericValueCriteriaPresenterFactory;

    @Nonnull
    private final DateIsBeforePresenterFactory dateIsBeforePresenterFactory;

    @Nonnull
    private final DateIsAfterPresenterFactory dateIsAfterPresenterFactory;

    @Nonnull
    private final StringContainsRepeatedSpacesCriteriaPresenterFactory stringContainsRepeatedSpacesCriteriaPresenterFactory;

    @Nonnull
    private final StringHasUntrimmedSpaceCriteriaPresenterFactory stringHasUntrimmedSpaceCriteriaPresenterFactory;

    @Inject
    public SelectableLiteralCriteriaPresenter(@Nonnull SelectableCriteriaTypeView view,
                                              @Nonnull LangTagMatchesCriteriaPresenterFactory langTagMatchesCriteriaPresenterFactory,
                                              @Nonnull LangTagIsEmptyCriteriaPresenterFactory langTagIsEmptyCriteriaPresenterFactory,
                                              @Nonnull StringStartsWithCriteriaPresenterFactory stringStartsWithCriteriaPresenterFactory,
                                              @Nonnull StringEndsWithCriteriaPresenterFactory stringEndsWithCriteriaPresenterFactory,
                                              @Nonnull StringContainsCriteriaPresenterFactory stringContainsCriteriaPresenterFactory,
                                              @Nonnull StringEqualsCriteriaPresenterFactory stringEqualsCriteriaPresenterFactory,
                                              @Nonnull StringMatchesRegexCriteriaPresenterFactory stringMatchesRegexCriteriaPresenterFactory,
                                              @Nonnull StringDoesNotMatchRegexCriteriaPresenterFactory stringDoesNotMatchRegexCriteriaPresenterFactory,
                                              @Nonnull NumericValueCriteriaPresenterFactory numericValueCriteriaPresenterFactory,
                                              @Nonnull DateIsBeforePresenterFactory dateIsBeforePresenterFactory,
                                              @Nonnull DateIsAfterPresenterFactory dateIsAfterPresenterFactory,
                                              @Nonnull StringContainsRepeatedSpacesCriteriaPresenterFactory stringContainsRepeatedSpacesCriteriaPresenterFactory,
                                              @Nonnull StringHasUntrimmedSpaceCriteriaPresenterFactory stringHasUntrimmedSpaceCriteriaPresenterFactory) {
        super(view);
        this.langTagMatchesCriteriaPresenterFactory = langTagMatchesCriteriaPresenterFactory;
        this.langTagIsEmptyCriteriaPresenterFactory = langTagIsEmptyCriteriaPresenterFactory;
        this.stringStartsWithCriteriaPresenterFactory = stringStartsWithCriteriaPresenterFactory;
        this.stringEndsWithCriteriaPresenterFactory = stringEndsWithCriteriaPresenterFactory;
        this.stringContainsCriteriaPresenterFactory = stringContainsCriteriaPresenterFactory;
        this.stringEqualsCriteriaPresenterFactory = stringEqualsCriteriaPresenterFactory;
        this.stringMatchesRegexCriteriaPresenterFactory = stringMatchesRegexCriteriaPresenterFactory;
        this.stringDoesNotMatchRegexCriteriaPresenterFactory = stringDoesNotMatchRegexCriteriaPresenterFactory;
        this.numericValueCriteriaPresenterFactory = numericValueCriteriaPresenterFactory;
        this.dateIsBeforePresenterFactory = dateIsBeforePresenterFactory;
        this.dateIsAfterPresenterFactory = dateIsAfterPresenterFactory;
        this.stringContainsRepeatedSpacesCriteriaPresenterFactory = stringContainsRepeatedSpacesCriteriaPresenterFactory;
        this.stringHasUntrimmedSpaceCriteriaPresenterFactory = stringHasUntrimmedSpaceCriteriaPresenterFactory;
    }

    @Override
    protected void start(@Nonnull PresenterFactoryRegistry<LiteralCriteria> factoryRegistry) {
        factoryRegistry.addPresenter(stringStartsWithCriteriaPresenterFactory);
        factoryRegistry.addPresenter(stringEndsWithCriteriaPresenterFactory);
        factoryRegistry.addPresenter(stringContainsCriteriaPresenterFactory);
        factoryRegistry.addPresenter(stringEqualsCriteriaPresenterFactory);
        factoryRegistry.addPresenter(stringMatchesRegexCriteriaPresenterFactory);
        factoryRegistry.addPresenter(stringDoesNotMatchRegexCriteriaPresenterFactory);
        factoryRegistry.addPresenter(numericValueCriteriaPresenterFactory);
        factoryRegistry.addPresenter(dateIsBeforePresenterFactory);
        factoryRegistry.addPresenter(dateIsAfterPresenterFactory);
        factoryRegistry.addPresenter(stringContainsRepeatedSpacesCriteriaPresenterFactory);
        factoryRegistry.addPresenter(stringHasUntrimmedSpaceCriteriaPresenterFactory);
        factoryRegistry.addPresenter(langTagMatchesCriteriaPresenterFactory);
        factoryRegistry.addPresenter(langTagIsEmptyCriteriaPresenterFactory);

    }

    @Nonnull
    @Override
    protected CriteriaPresenterFactory<? extends LiteralCriteria> getPresenterFactoryForCriteria(@Nonnull LiteralCriteria criteria) {
        return criteria.accept(new LiteralCriteriaVisitor<CriteriaPresenterFactory<? extends LiteralCriteria>>() {
            @Override
            public CriteriaPresenterFactory<? extends LiteralCriteria> visit(StringEndsWithCriteria stringEndsWithCriteria) {
                return stringEndsWithCriteriaPresenterFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends LiteralCriteria> visit(StringContainsCriteria stringContainsCriteria) {
                return stringContainsCriteriaPresenterFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends LiteralCriteria> visit(StringEqualsCriteria stringEqualsCriteria) {
                return stringEqualsCriteriaPresenterFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends LiteralCriteria> visit(StringStartsWithCriteria stringStartsWithCriteria) {
                return stringStartsWithCriteriaPresenterFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends LiteralCriteria> visit(StringContainsRegexMatchCriteria stringContainsRegexMatchCriteria) {
                return stringContainsCriteriaPresenterFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends LiteralCriteria> visit(StringDoesNotContainRegexMatchCriteria stringDoesNotContainRegexMatchCriteria) {
                return stringDoesNotMatchRegexCriteriaPresenterFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends LiteralCriteria> visit(NumericValueCriteria numericValueCriteria) {
                return numericValueCriteriaPresenterFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends LiteralCriteria> visit(DateIsBeforeCriteria dateIsBeforeCriteria) {
                return dateIsBeforePresenterFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends LiteralCriteria> visit(DateIsAfterCriteria dateIsAfterCriteria) {
                return dateIsAfterPresenterFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends LiteralCriteria> visit(StringContainsRepeatedSpacesCriteria stringContainsRepeatedSpacesCriteria) {
                return stringContainsRepeatedSpacesCriteriaPresenterFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends LiteralCriteria> visit(StringHasUntrimmedSpaceCriteria stringHasUntrimmedSpaceCriteria) {
                return stringHasUntrimmedSpaceCriteriaPresenterFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends LiteralCriteria> visit(AnyLangTagOrEmptyLangTagCriteria anyLangTagOrEmptyLangTagCriteria) {
                throw new RuntimeException();
            }

            @Override
            public CriteriaPresenterFactory<? extends LiteralCriteria> visit(LangTagIsEmptyCriteria langTagIsEmptyCriteria) {
                return langTagIsEmptyCriteriaPresenterFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends LiteralCriteria> visit(LangTagMatchesCriteria langTagMatchesCriteria) {
                return langTagMatchesCriteriaPresenterFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends LiteralCriteria> visit(CompositeLiteralCriteria compositeLiteralCriteria) {
                throw new RuntimeException();
            }
        });
    }
}
