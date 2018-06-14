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

    @Inject
    public AnnotationValueCriteriaPresenter(@Nonnull SelectableCriteriaTypeView view,
                                            @Nonnull StringEqualsCriteriaPresenterFactory equalsFactory, @Nonnull StringStartsWithCriteriaPresenterFactory startsWithFactory,
                                            @Nonnull StringEndsWithCriteriaPresenterFactory endsWithFactory,
                                            @Nonnull StringContainsCriteriaPresenterFactory containsFactory,
                                            @Nonnull StringMatchesRegexCriteriaPresenterFactory regexFactory,
                                            @Nonnull StringHasUntrimmedSpaceCriteriaPresenterFactory untrimmedFactory,
                                            @Nonnull StringContainsRepeatedSpacesCriteriaPresenterFactory repeatedSpacesFactory,
                                            @Nonnull LexicalValueIsNotInLiteralLexcialSpaceCriteriaPresenterFactory lexicalValueFactory, @Nonnull IriHasAnnotationsCriteriaPresenterFactory iriAnnotationsFactory, @Nonnull NumericValueCriteriaPresenterFactory numericValueFactory, @Nonnull LangTagMatchesCriteriaPresenterFactory langTagMatchesFactory, @Nonnull LangTagIsEmptyCriteriaPresenterFactory emptyLangTagFactory, @Nonnull AnyAnnotationValueCriteriaPresenterFactory anyValueFactory, @Nonnull DateTimeValueCriteriaPresenterFactory dateTimeFactory) {
        super(view);
        this.equalsFactory = checkNotNull(equalsFactory);
        this.startsWithFactory = checkNotNull(startsWithFactory);
        this.endsWithFactory = checkNotNull(endsWithFactory);
        this.containsFactory = checkNotNull(containsFactory);
        this.regexFactory = checkNotNull(regexFactory);
        this.untrimmedFactory = checkNotNull(untrimmedFactory);
        this.repeatedSpacesFactory = checkNotNull(repeatedSpacesFactory);
        this.lexicalValueFactory = checkNotNull(lexicalValueFactory);
        this.iriAnnotationsFactory = checkNotNull(iriAnnotationsFactory);
        this.numericValueFactory = checkNotNull(numericValueFactory);
        this.langTagMatchesFactory = checkNotNull(langTagMatchesFactory);
        this.emptyLangTagFactory = checkNotNull(emptyLangTagFactory);
        this.anyValueFactory = checkNotNull(anyValueFactory);
        this.dateTimeFactory = checkNotNull(dateTimeFactory);
    }

    @Override
    protected void start(@Nonnull PresenterFactoryRegistry<AnnotationValueCriteria> factoryRegistry) {
        factoryRegistry.addPresenter(containsFactory);
        factoryRegistry.addPresenter(equalsFactory);
        factoryRegistry.addPresenter(startsWithFactory);
        factoryRegistry.addPresenter(endsWithFactory);
        factoryRegistry.addPresenter(regexFactory);
        factoryRegistry.addPresenter(untrimmedFactory);
        factoryRegistry.addPresenter(repeatedSpacesFactory);
        factoryRegistry.addPresenter(lexicalValueFactory);
        factoryRegistry.addPresenter(numericValueFactory);
        factoryRegistry.addPresenter(langTagMatchesFactory);
        factoryRegistry.addPresenter(emptyLangTagFactory);
        factoryRegistry.addPresenter(iriAnnotationsFactory);
        factoryRegistry.addPresenter(anyValueFactory);
        factoryRegistry.addPresenter(dateTimeFactory);
    }
}
