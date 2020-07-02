package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class AnnotationValueCriteriaPresenter extends SelectableCriteriaTypePresenter<AnnotationValueCriteria> {

    @Nonnull
    private final StringEqualsCriteriaPresenterFactory equalsFactory;

    @Nonnull
    private final StringStartsWithCriteriaPresenterFactory startsWithFactory;

    @Nonnull
    private final StringEndsWithCriteriaPresenterFactory endsWithFactory;

    @Nonnull
    private final StringContainsCriteriaPresenterFactory containsFactory;

    @Nonnull
    private final StringMatchesRegexCriteriaPresenterFactory regexFactory;

    @Nonnull
    private final StringDoesNotMatchRegexCriteriaPresenterFactory negRegexFactory;

    @Nonnull
    private final StringHasUntrimmedSpaceCriteriaPresenterFactory untrimmedFactory;

    @Nonnull
    private final StringContainsRepeatedSpacesCriteriaPresenterFactory repeatedSpacesFactory;

    @Nonnull
    private final LexicalValueIsNotInLiteralLexcialSpaceCriteriaPresenterFactory lexicalValueFactory;

    @Nonnull
    private final IriHasAnnotationsCriteriaPresenterFactory iriAnnotationsFactory;

    @Nonnull
    private final NumericValueCriteriaPresenterFactory numericValueFactory;

    @Nonnull
    private final LangTagMatchesCriteriaPresenterFactory langTagMatchesFactory;

    @Nonnull
    private final LangTagIsEmptyCriteriaPresenterFactory emptyLangTagFactory;

    @Nonnull
    private final AnyAnnotationValueCriteriaPresenterFactory anyValueFactory;

    @Nonnull
    private final DateTimeValueCriteriaPresenterFactory dateTimeFactory;

    @Nonnull
    private final IriEqualsCriteriaFactoryPresenter iriEqualsFactory;

    @Inject
    public AnnotationValueCriteriaPresenter(@Nonnull SelectableCriteriaTypeView view,
                                            @Nonnull StringEqualsCriteriaPresenterFactory equalsFactory, @Nonnull StringStartsWithCriteriaPresenterFactory startsWithFactory,
                                            @Nonnull StringEndsWithCriteriaPresenterFactory endsWithFactory,
                                            @Nonnull StringContainsCriteriaPresenterFactory containsFactory,
                                            @Nonnull StringMatchesRegexCriteriaPresenterFactory regexFactory,
                                            @Nonnull StringDoesNotMatchRegexCriteriaPresenterFactory negRegexFactory, @Nonnull StringHasUntrimmedSpaceCriteriaPresenterFactory untrimmedFactory,
                                            @Nonnull StringContainsRepeatedSpacesCriteriaPresenterFactory repeatedSpacesFactory,
                                            @Nonnull LexicalValueIsNotInLiteralLexcialSpaceCriteriaPresenterFactory lexicalValueFactory, @Nonnull IriHasAnnotationsCriteriaPresenterFactory iriAnnotationsFactory, @Nonnull NumericValueCriteriaPresenterFactory numericValueFactory, @Nonnull LangTagMatchesCriteriaPresenterFactory langTagMatchesFactory, @Nonnull LangTagIsEmptyCriteriaPresenterFactory emptyLangTagFactory, @Nonnull AnyAnnotationValueCriteriaPresenterFactory anyValueFactory, @Nonnull DateTimeValueCriteriaPresenterFactory dateTimeFactory, @Nonnull IriEqualsCriteriaFactoryPresenter iriEqualsFactory) {
        super(view);
        this.equalsFactory = checkNotNull(equalsFactory);
        this.startsWithFactory = checkNotNull(startsWithFactory);
        this.endsWithFactory = checkNotNull(endsWithFactory);
        this.containsFactory = checkNotNull(containsFactory);
        this.regexFactory = checkNotNull(regexFactory);
        this.negRegexFactory = checkNotNull(negRegexFactory);
        this.untrimmedFactory = checkNotNull(untrimmedFactory);
        this.repeatedSpacesFactory = checkNotNull(repeatedSpacesFactory);
        this.lexicalValueFactory = checkNotNull(lexicalValueFactory);
        this.iriAnnotationsFactory = checkNotNull(iriAnnotationsFactory);
        this.numericValueFactory = checkNotNull(numericValueFactory);
        this.langTagMatchesFactory = checkNotNull(langTagMatchesFactory);
        this.emptyLangTagFactory = checkNotNull(emptyLangTagFactory);
        this.anyValueFactory = checkNotNull(anyValueFactory);
        this.dateTimeFactory = checkNotNull(dateTimeFactory);
        this.iriEqualsFactory = checkNotNull(iriEqualsFactory);
    }

    @Override
    protected void start(@Nonnull PresenterFactoryRegistry<AnnotationValueCriteria> factoryRegistry) {
        factoryRegistry.addPresenter(containsFactory);
        factoryRegistry.addPresenter(equalsFactory);
        factoryRegistry.addPresenter(startsWithFactory);
        factoryRegistry.addPresenter(endsWithFactory);
        factoryRegistry.addPresenter(regexFactory);
        factoryRegistry.addPresenter(negRegexFactory);
        factoryRegistry.addPresenter(numericValueFactory);
        factoryRegistry.addPresenter(dateTimeFactory);
        factoryRegistry.addPresenter(iriEqualsFactory);

        factoryRegistry.addPresenter(langTagMatchesFactory);
        factoryRegistry.addPresenter(emptyLangTagFactory);

        factoryRegistry.addPresenter(lexicalValueFactory);

        factoryRegistry.addPresenter(untrimmedFactory);
        factoryRegistry.addPresenter(repeatedSpacesFactory);

        factoryRegistry.addPresenter(iriAnnotationsFactory);
        factoryRegistry.addPresenter(anyValueFactory);
    }

    @Nonnull
    @Override
    protected CriteriaPresenterFactory<? extends AnnotationValueCriteria> getPresenterFactoryForCriteria(@Nonnull AnnotationValueCriteria criteria) {
        return criteria.accept(new AnnotationValueCriteriaVisitor<CriteriaPresenterFactory<? extends AnnotationValueCriteria>>() {
            @Override
            public CriteriaPresenterFactory<? extends AnnotationValueCriteria> visit(@Nonnull AnyAnnotationValueCriteria criteria) {
                return anyValueFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends AnnotationValueCriteria> visit(@Nonnull IriEqualsCriteria criteria) {
                return iriEqualsFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends AnnotationValueCriteria> visit(@Nonnull IriHasAnnotationCriteria criteria) {
                return iriAnnotationsFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends AnnotationValueCriteria> visit(@Nonnull CompositeAnnotationValueCriteria criteria) {
                throw new RuntimeException("Not implemented");
            }

            @Override
            public CriteriaPresenterFactory<? extends AnnotationValueCriteria> visit(@Nonnull StringStartsWithCriteria criteria) {
                return startsWithFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends AnnotationValueCriteria> visit(@Nonnull StringEndsWithCriteria criteria) {
                return endsWithFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends AnnotationValueCriteria> visit(@Nonnull StringContainsCriteria criteria) {
                return containsFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends AnnotationValueCriteria> visit(@Nonnull StringEqualsCriteria criteria) {
                return equalsFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends AnnotationValueCriteria> visit(@Nonnull NumericValueCriteria criteria) {
                return numericValueFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends AnnotationValueCriteria> visit(@Nonnull StringContainsRepeatedSpacesCriteria criteria) {
                return repeatedSpacesFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends AnnotationValueCriteria> visit(@Nonnull StringHasUntrimmedSpaceCriteria criteria) {
                return untrimmedFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends AnnotationValueCriteria> visit(@Nonnull StringContainsRegexMatchCriteria criteria) {
                return regexFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends AnnotationValueCriteria> visit(@Nonnull StringDoesNotContainRegexMatchCriteria criteria) {
                return negRegexFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends AnnotationValueCriteria> visit(@Nonnull DateIsBeforeCriteria criteria) {
                return dateTimeFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends AnnotationValueCriteria> visit(@Nonnull DateIsAfterCriteria criteria) {
                return dateTimeFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends AnnotationValueCriteria> visit(@Nonnull LiteralLexicalValueNotInDatatypeLexicalSpaceCriteria criteria) {
                return lexicalValueFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends AnnotationValueCriteria> visit(@Nonnull LangTagMatchesCriteria criteria) {
                return langTagMatchesFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends AnnotationValueCriteria> visit(@Nonnull LangTagIsEmptyCriteria criteria) {
                return emptyLangTagFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends AnnotationValueCriteria> visit(@Nonnull AnyLangTagOrEmptyLangTagCriteria criteria) {
                return null;
            }

            @Override
            public CriteriaPresenterFactory<? extends AnnotationValueCriteria> visit(CompositeLiteralCriteria compositeLiteralCriteria) {
                return null;
            }
        });
    }
}
