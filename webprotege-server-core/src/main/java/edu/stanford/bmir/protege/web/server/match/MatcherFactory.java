package edu.stanford.bmir.protege.web.server.match;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.match.criteria.*;
import org.apache.commons.lang.StringUtils;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jun 2018
 */
public class MatcherFactory {

    @Nonnull
    private final SubClassOfMatcherFactory subClassOfMatcherFactory;

    @Nonnull
    private final InstanceOfMatcherFactory instanceOfMatcherFactory;

    @Nonnull
    private final ConflictingBooleanValuesMatcherFactory conflictingBooleanValuesMatcherFactory;

    @Nonnull
    private final EntityIsDeprecatedMatcherFactory entityIsDeprecatedMatcherFactory;

    @Nonnull
    private final AnnotationValuesAreNotDisjointMatcherFactory annotationValuesAreNotDisjointMatcherFactory;

    @Nonnull
    private final NonUniqueLangTagsMatcherFactory nonUniqueLangTagsMatcherFactory;

    @Nonnull
    private final EntityAnnotationMatcherFactory entityAnnotationMatcherFactory;

    @Nonnull
    private final IriAnnotationsMatcherFactory iriAnnotationsMatcherFactory;

    @Inject
    public MatcherFactory(@Nonnull SubClassOfMatcherFactory subClassOfMatcherFactory,
                          @Nonnull InstanceOfMatcherFactory instanceOfMatcherFactory, @Nonnull ConflictingBooleanValuesMatcherFactory conflictingBooleanValuesMatcherFactory,
                          @Nonnull EntityIsDeprecatedMatcherFactory entityIsDeprecatedMatcherFactory,
                          @Nonnull AnnotationValuesAreNotDisjointMatcherFactory annotationValuesAreNotDisjointMatcherFactory,
                          @Nonnull NonUniqueLangTagsMatcherFactory nonUniqueLangTagsMatcherFactory,
                          @Nonnull EntityAnnotationMatcherFactory entityAnnotationMatcherFactory,
                          @Nonnull IriAnnotationsMatcherFactory iriAnnotationsMatcherFactory) {
        this.subClassOfMatcherFactory = checkNotNull(subClassOfMatcherFactory);
        this.instanceOfMatcherFactory = checkNotNull(instanceOfMatcherFactory);
        this.conflictingBooleanValuesMatcherFactory = checkNotNull(conflictingBooleanValuesMatcherFactory);
        this.entityIsDeprecatedMatcherFactory = checkNotNull(entityIsDeprecatedMatcherFactory);
        this.annotationValuesAreNotDisjointMatcherFactory = checkNotNull(annotationValuesAreNotDisjointMatcherFactory);
        this.nonUniqueLangTagsMatcherFactory = checkNotNull(nonUniqueLangTagsMatcherFactory);
        this.entityAnnotationMatcherFactory = checkNotNull(entityAnnotationMatcherFactory);
        this.iriAnnotationsMatcherFactory = checkNotNull(iriAnnotationsMatcherFactory);
    }

    public Matcher<OWLEntity> getMatcher(@Nonnull RootCriteria criteria) {
        return criteria.accept(new RootCriteriaVisitor<Matcher<OWLEntity>>() {

            @Nonnull
            @Override
            public Matcher<OWLEntity> visit(@Nonnull CompositeRootCriteria criteria) {
                ImmutableList<Matcher<OWLEntity>> matchers = criteria.getRootCriteria().stream()
                                                                     .map(c -> c.accept(this))
                                                                     .collect(toImmutableList());
                switch (criteria.getMatchType()) {
                    case ANY:
                        return new OrMatcher<>(matchers);
                    case ALL:
                        return new AndMatcher<>(matchers);
                    default:
                        throw new RuntimeException();
                }
            }

            @Nonnull
            @Override
            public Matcher<OWLEntity> visit(@Nonnull IsNotBuiltInEntityCriteria criteria) {
                return entity -> !entity.isBuiltIn();
            }

            @Nonnull
            @Override
            public Matcher<OWLEntity> visit(@Nonnull EntityAnnotationCriteria criteria) {
                AnnotationCriteria annotationCriteria = criteria.getAnnotationCriteria();
                Matcher<OWLAnnotation> annotationMatcher = getAnnotationMatcher(annotationCriteria);
                return entityAnnotationMatcherFactory.create(
                        annotationMatcher,
                        criteria.getAnnotationPresence());
            }

            @Nonnull
            @Override
            public Matcher<OWLEntity> visit(@Nonnull EntityIsDeprecatedCriteria criteria) {
                return entityIsDeprecatedMatcherFactory.create();
            }

            @Nonnull
            @Override
            public Matcher<OWLEntity> visit(@Nonnull EntityIsNotDeprecatedCriteria criteria) {
                return new NotMatcher<>(entityIsDeprecatedMatcherFactory.create());
            }

            @Nonnull
            @Override
            public Matcher<OWLEntity> visit(@Nonnull EntityTypeIsOneOfCriteria criteria) {
                return entity -> criteria.getEntityTypes().contains(entity.getEntityType());
            }

            @Nonnull
            @Override
            public Matcher<OWLEntity> visit(@Nonnull EntityHasNonUniqueLangTagsCriteria criteria) {
                Matcher<OWLAnnotationProperty> propertyMatcher = getAnnotationPropertyMatcher(criteria.getPropertyCriteria());
                return nonUniqueLangTagsMatcherFactory.create(propertyMatcher);
            }

            @Nonnull
            @Override
            public Matcher<OWLEntity> visit(@Nonnull EntityHasConflictingBooleanAnnotationValuesCriteria criteria) {
                return conflictingBooleanValuesMatcherFactory.create();
            }

            @Nonnull
            @Override
            public Matcher<OWLEntity> visit(@Nonnull EntityAnnotationValuesAreNotDisjointCriteria criteria) {
                return annotationValuesAreNotDisjointMatcherFactory.create(
                        getAnnotationPropertyMatcher(criteria.getFirstProperty()),
                        getAnnotationPropertyMatcher(criteria.getSecondProperty()));
            }

            @Nonnull
            @Override
            public Matcher<OWLEntity> visit(@Nonnull SubClassOfCriteria criteria) {
                return subClassOfMatcherFactory.create(criteria.getTarget(),
                                                       criteria.getFilterType());
            }

            @Nonnull
            @Override
            public Matcher<OWLEntity> visit(@Nonnull InstanceOfCriteria criteria) {
                return instanceOfMatcherFactory.create(criteria.getTarget(),
                                                       criteria.getFilterType());
            }
        });
    }

    private Matcher<OWLAnnotation> getAnnotationMatcher(AnnotationCriteria annotationCriteria) {
        return annotationCriteria.accept(new AnnotationCriteriaVisitor<Matcher<OWLAnnotation>>() {
            @Nonnull
            @Override
            public Matcher<OWLAnnotation> visit(@Nonnull AnnotationComponentsCriteria criteria) {
                AnnotationPropertyCriteria propertyCriteria = criteria.getAnnotationPropertyCriteria();
                Matcher<OWLAnnotationProperty> propertyMatcher = getAnnotationPropertyMatcher(propertyCriteria);
                AnnotationValueCriteria valueCriteria = criteria.getAnnotationValueCriteria();
                Matcher<OWLAnnotationValue> valueMatcher = getAnnotationValueMatcher(valueCriteria);
                return new AnnotationMatcher(propertyMatcher, valueMatcher);
            }
        });
    }

    private Matcher<OWLAnnotationProperty> getAnnotationPropertyMatcher(@Nonnull AnnotationPropertyCriteria criteria) {
        return criteria.accept(new AnnotationPropertyCriteriaVisitor<Matcher<OWLAnnotationProperty>>() {
            @Override
            public Matcher<OWLAnnotationProperty> visit(@Nonnull AnyAnnotationPropertyCriteria criteria) {
                return (prop) -> true;
            }

            @Override
            public Matcher<OWLAnnotationProperty> visit(@Nonnull IriEqualsCriteria criteria) {
                return (prop) -> prop.getIRI().equals(criteria.getIri());
            }
        });
    }

    private Matcher<OWLAnnotationValue> getAnnotationValueMatcher(@Nonnull AnnotationValueCriteria criteria) {
        return criteria.accept(new AnnotationValueCriteriaVisitor<Matcher<OWLAnnotationValue>>() {
            @Nonnull
            @Override
            public Matcher<OWLAnnotationValue> visit(@Nonnull AnyAnnotationValueCriteria criteria) {
                return val -> true;
            }

            @Nonnull
            @Override
            public Matcher<OWLAnnotationValue> visit(@Nonnull CompositeAnnotationValueCriteria criteria) {
                ImmutableList<Matcher<OWLAnnotationValue>> matchers = criteria.getAnnotationValueCriteria().stream()
                                                                              .map(c -> c.accept(this))
                                                                              .collect(toImmutableList());
                switch (criteria.getMultiMatchType()) {
                    case ALL:
                        return new AndMatcher<>(matchers);
                    case ANY:
                        return new OrMatcher<>(matchers);
                    default:
                        throw new IllegalStateException();
                }

            }

            @Nonnull
            @Override
            public Matcher<OWLAnnotationValue> visit(@Nonnull LiteralLexicalValueNotInDatatypeLexicalSpaceCriteria criteria) {
                return new LiteralAnnotationValueMatcher(new LexicalValueNotInDatatypeSpaceMatcher());
            }

            @Nonnull
            @Override
            public Matcher<OWLAnnotationValue> visit(@Nonnull IriEqualsCriteria criteria) {
                return val -> val.equals(criteria.getIri());
            }

            @Nonnull
            @Override
            public Matcher<OWLAnnotationValue> visit(@Nonnull IriHasAnnotationCriteria criteria) {
                return new IriAnnotationValueMatcher(
                        iriAnnotationsMatcherFactory.create(getAnnotationMatcher(criteria.getIriAnnotationCriteria()))
                );
            }

            @Override
            public Matcher<OWLAnnotationValue> visit(@Nonnull StringStartsWithCriteria criteria) {
                if (criteria.isIgnoreCase()) {
                    return LiteralAnnotationValueMatcher.forLexicalPredicate(
                            s -> StringUtils.startsWithIgnoreCase(s, criteria.getValue())
                    );
                }
                else {
                    return LiteralAnnotationValueMatcher.forLexicalPredicate(
                            s -> s.startsWith(criteria.getValue())
                    );
                }
            }

            @Override
            public Matcher<OWLAnnotationValue> visit(@Nonnull StringEndsWithCriteria criteria) {
                if (criteria.isIgnoreCase()) {
                    return LiteralAnnotationValueMatcher.forLexicalPredicate(
                            s -> StringUtils.endsWithIgnoreCase(s, criteria.getValue())
                    );
                }
                else {
                    return LiteralAnnotationValueMatcher.forLexicalPredicate(
                            s -> s.endsWith(criteria.getValue())
                    );
                }
            }

            @Override
            public Matcher<OWLAnnotationValue> visit(@Nonnull StringContainsCriteria criteria) {
                if (criteria.isIgnoreCase()) {
                    return LiteralAnnotationValueMatcher.forLexicalPredicate(
                            s -> StringUtils.containsIgnoreCase(s, criteria.getValue())
                    );
                }
                else {
                    return LiteralAnnotationValueMatcher.forLexicalPredicate(
                            s -> s.contains(criteria.getValue())
                    );
                }
            }

            @Override
            public Matcher<OWLAnnotationValue> visit(@Nonnull StringEqualsCriteria criteria) {
                if (criteria.isIgnoreCase()) {
                    return LiteralAnnotationValueMatcher.forLexicalPredicate(
                            s -> s.equalsIgnoreCase(criteria.getValue())
                    );
                }
                else {
                    return LiteralAnnotationValueMatcher.forLexicalPredicate(
                            s -> s.equals(criteria.getValue())
                    );
                }
            }

            @Override
            public Matcher<OWLAnnotationValue> visit(@Nonnull NumericValueCriteria criteria) {
                return LiteralAnnotationValueMatcher.forLexicalValueMatcher(
                        new NumericValueMatcher(criteria.getPredicate(), criteria.getValue())
                );
            }

            @Override
            public Matcher<OWLAnnotationValue> visit(@Nonnull StringContainsRepeatedSpacesCriteria criteria) {
                return LiteralAnnotationValueMatcher.forLexicalValueMatcher(
                        new StringContainsRepeatedWhiteSpaceMatcher()
                );
            }

            @Override
            public Matcher<OWLAnnotationValue> visit(@Nonnull StringContainsRegexMatchCriteria criteria) {
                int flags = 0;
                if(criteria.isIgnoreCase()) {
                    flags |= Pattern.CASE_INSENSITIVE;
                }
                Pattern pattern = Pattern.compile(criteria.getPattern(), flags);
                return LiteralAnnotationValueMatcher.forLexicalValueMatcher(
                        new StringContainsRegexMatchMatcher(pattern)
                );
            }

            @Override
            public Matcher<OWLAnnotationValue> visit(@Nonnull StringDoesNotContainRegexMatchCriteria criteria) {
                return LiteralAnnotationValueMatcher.forLexicalValueMatcher(
                        new NotMatcher<>(new StringContainsRegexMatchMatcher(Pattern.compile(criteria.getPattern())))
                );
            }

            @Override
            public Matcher<OWLAnnotationValue> visit(@Nonnull StringHasUntrimmedSpaceCriteria criteria) {
                return LiteralAnnotationValueMatcher.forLexicalValueMatcher(new StringHasUntrimmedSpaceMatcher());
            }

            @Override
            public Matcher<OWLAnnotationValue> visit(@Nonnull DateIsBeforeCriteria criteria) {
                return LiteralAnnotationValueMatcher.forLexicalValueMatcher(
                        new DateIsBeforeMatcher(LocalDate.of(criteria.getYear(),
                                                             criteria.getMonth(),
                                                             criteria.getDay()))
                );
            }

            @Override
            public Matcher<OWLAnnotationValue> visit(@Nonnull DateIsAfterCriteria criteria) {
                return LiteralAnnotationValueMatcher.forLexicalValueMatcher(
                        new DateIsAfterMatcher(LocalDate.of(criteria.getYear(),
                                                            criteria.getMonth(),
                                                            criteria.getDay()))
                );
            }

            @Override
            public Matcher<OWLAnnotationValue> visit(@Nonnull LangTagMatchesCriteria criteria) {
                LangTagMatchesMatcher langTagMatchesMatcher = LangTagMatchesMatcher.fromPattern(criteria.getLanguageRange());
                return LiteralAnnotationValueMatcher.forLangTagMatcher(
                        langTagMatchesMatcher
                );
            }

            @Override
            public Matcher<OWLAnnotationValue> visit(@Nonnull LangTagIsEmptyCriteria criteria) {
                return LiteralAnnotationValueMatcher.forLangTagMatcher(
                        String::isEmpty
                );
            }

            @Override
            public Matcher<OWLAnnotationValue> visit(@Nonnull AnyLangTagOrEmptyLangTagCriteria criteria) {
                return LiteralAnnotationValueMatcher.forLangTagMatcher(
                        langTag -> true
                );
            }
        });
    }
}
