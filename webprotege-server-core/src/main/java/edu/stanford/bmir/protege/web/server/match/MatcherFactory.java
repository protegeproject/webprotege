package edu.stanford.bmir.protege.web.server.match;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.HasAnnotationAssertionAxioms;
import edu.stanford.bmir.protege.web.shared.match.criteria.*;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jun 2018
 */
public class MatcherFactory {

    private final HasAnnotationAssertionAxioms axioms;

    private final StringMatcherFactory stringMatcherFactory = new StringMatcherFactory();

    @Inject
    public MatcherFactory(HasAnnotationAssertionAxioms axioms) {
        this.axioms = axioms;
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
                AnnotationComponentCriteria annotationComponentCriteria = criteria.getAnnotationCriteria();
                Matcher<OWLAnnotation> annotationMatcher = getAnnotationMatcher(annotationComponentCriteria);
                return new EntityAnnotationMatcher(axioms,
                                                   annotationMatcher,
                                                   criteria.getAnnotationCriteria().getPresence());
            }

            @Nonnull
            @Override
            public Matcher<OWLEntity> visit(@Nonnull EntityIsDeprecatedCriteria criteria) {
                return new EntityIsDeprecatedMatcher(axioms);
            }

            @Nonnull
            @Override
            public Matcher<OWLEntity> visit(@Nonnull EntityIsNotDeprecatedCriteria criteria) {
                return new NotMatcher<>(new EntityIsDeprecatedMatcher(axioms));
            }

            @Nonnull
            @Override
            public Matcher<OWLEntity> visit(@Nonnull EntityTypeOneOfCriteria criteria) {
                return entity -> criteria.getEntityTypes().contains(entity.getEntityType());
            }

            @Nonnull
            @Override
            public Matcher<OWLEntity> visit(@Nonnull EntityHasNonUniqueLangTagsCriteria criteria) {
                Matcher<OWLAnnotationProperty> propertyMatcher = getAnnotationPropertyMatcher(criteria.getPropertyCriteria());
                return new NonUniqueLangTagsMatcher(axioms, propertyMatcher);
            }

            @Nonnull
            @Override
            public Matcher<OWLEntity> visit(@Nonnull EntityHasConflictingBooleanAnnotationValuesCriteria criteria) {
                return new ConflictingBooleanValuesMatcher(axioms);
            }

            @Nonnull
            @Override
            public Matcher<OWLEntity> visit(@Nonnull EntityAnnotationValuesAreNotDisjointCriteria criteria) {
                return new AnnotationValuesAreNotDisjointMatcher(axioms,
                                                                 getAnnotationPropertyMatcher(criteria.getFirstProperty()),
                                                                 getAnnotationPropertyMatcher(criteria.getSecondProperty()));
            }
        });
    }

    private Matcher<OWLAnnotation> getAnnotationMatcher(AnnotationComponentCriteria annotationComponentCriteria) {
        AnnotationPropertyCriteria propertyCriteria = annotationComponentCriteria.getAnnotationPropertyCriteria();
        Matcher<OWLAnnotationProperty> propertyMatcher = getAnnotationPropertyMatcher(propertyCriteria);
        AnnotationValueCriteria valueCriteria = annotationComponentCriteria.getAnnotationValueCriteria();
        Matcher<OWLAnnotationValue> valueMatcher = getAnnotationValueMatcher(valueCriteria);
        return new AnnotationMatcher(propertyMatcher, valueMatcher);
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
            public Matcher<OWLAnnotationValue> visit(@Nonnull LiteralComponentCriteria criteria) {
                return new LiteralAnnotationValueMatcher(getLiteralMatcher(criteria));
            }

            @Nonnull
            @Override
            public Matcher<OWLAnnotationValue> visit(@Nonnull LiteralLexicalValueNotInDatatypeLexicalSpaceCriteria criteria) {
                return new LiteralAnnotationValueMatcher(new LexicalValueNotInDatatypeSpaceMatcher());
            }

            @Nonnull
            @Override
            public Matcher<OWLAnnotationValue> visit(@Nonnull IriEqualsCriteria criteria) {
                return val -> val instanceof IRI && ((IRI) val).equals(criteria.getIri());
            }

            @Nonnull
            @Override
            public Matcher<OWLAnnotationValue> visit(@Nonnull IriHasAnnotationsCriteria criteria) {
                return new IriAnnotationValueMatcher(
                        new IriAnnotationsMatcher(axioms,
                                                  getAnnotationMatcher(criteria.getIriAnnotationsCriteria().get(0)))
                );
            }
        });
    }

    private Matcher<OWLLiteral> getLiteralMatcher(@Nonnull LiteralComponentCriteria criteria) {
        return new LiteralMatcher(
                getLexicalValueMatcher(criteria.getLexicalValueCriteria()),
                getLangTagMatcher(criteria.getLangTagCriteria()),
                getDatatypeMatcher(criteria.getDatatypeCriteria())
        );
    }

    private Matcher<String> getLexicalValueMatcher(@Nonnull LexicalValueCriteria criteria) {
        return stringMatcherFactory.getMatcher(criteria);
    }

    private Matcher<String> getLangTagMatcher(@Nonnull LangTagCriteria criteria) {
        return criteria.accept(new LangTagCriteriaVisitor<Matcher<String>>() {
            @Override
            public Matcher<String> visit(@Nonnull LangTagMatchesCriteria criteria) {
                return LangTagMatchesMatcher.fromPattern(criteria.getPattern());
            }

            @Override
            public Matcher<String> visit(@Nonnull LangTagIsEmptyCriteria criteria) {
                return String::isEmpty;
            }

            @Override
            public Matcher<String> visit(@Nonnull AnyLangTagOrEmptyLangTagCriteria criteria) {
                return tag -> true;
            }
        });
    }

    private Matcher<OWLDatatype> getDatatypeMatcher(@Nonnull DatatypeCriteria criteria) {
        return criteria.accept(new DatatypeCriteriaVisitor<Matcher<OWLDatatype>>() {
            @Override
            public Matcher<OWLDatatype> visit(@Nonnull AnyDatatypeCriteria criteria) {
                return dt -> true;
            }
        });
    }
}
