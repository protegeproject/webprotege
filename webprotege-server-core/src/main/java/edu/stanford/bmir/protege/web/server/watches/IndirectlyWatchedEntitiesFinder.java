package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.server.hierarchy.HasGetAncestors;
import edu.stanford.bmir.protege.web.server.index.ProjectClassAssertionAxiomsByIndividualIndex;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public class IndirectlyWatchedEntitiesFinder {

    @Nonnull
    private final HasGetAncestors<OWLClass> classAncestorsProvider;

    @Nonnull
    private final HasGetAncestors<OWLObjectProperty> objectAncestorsProvider;

    @Nonnull
    private final HasGetAncestors<OWLDataProperty> dataPropertyAncestorsProvider;

    @Nonnull
    private final HasGetAncestors<OWLAnnotationProperty> annotationPropertyAncestorsProvider;

    @Nonnull
    private final ProjectClassAssertionAxiomsByIndividualIndex classAssertionAxiomsIndex;


    @Inject
    public IndirectlyWatchedEntitiesFinder(@Nonnull HasGetAncestors<OWLClass> classAncestorsProvider,
                                           @Nonnull HasGetAncestors<OWLObjectProperty> objectAncestorsProvider,
                                           @Nonnull HasGetAncestors<OWLDataProperty> dataPropertyAncestorsProvider,
                                           @Nonnull HasGetAncestors<OWLAnnotationProperty> annotationPropertyAncestorsProvider,
                                           @Nonnull ProjectClassAssertionAxiomsByIndividualIndex classAssertionAxiomsIndex) {

        this.classAncestorsProvider = classAncestorsProvider;
        this.objectAncestorsProvider = objectAncestorsProvider;
        this.dataPropertyAncestorsProvider = dataPropertyAncestorsProvider;
        this.annotationPropertyAncestorsProvider = annotationPropertyAncestorsProvider;
        this.classAssertionAxiomsIndex = classAssertionAxiomsIndex;
    }

    public Collection<? extends OWLEntity> getRelatedWatchedEntities(OWLEntity entity) {
        return entity.accept(new OWLEntityVisitorEx<>() {

            @Nonnull
            @Override
            public Collection<? extends OWLEntity> visit(@Nonnull OWLClass desc) {
                return classAncestorsProvider.getAncestors(desc);
            }

            @Nonnull
            @Override
            public Collection<? extends OWLEntity> visit(@Nonnull OWLDataProperty property) {
                return dataPropertyAncestorsProvider.getAncestors(property);
            }

            @Nonnull
            @Override
            public Collection<? extends OWLEntity> visit(@Nonnull OWLObjectProperty property) {
                return objectAncestorsProvider.getAncestors(property);
            }

            @Nonnull
            @Override
            public Collection<? extends OWLEntity> visit(@Nonnull OWLNamedIndividual individual) {
                return classAssertionAxiomsIndex.getClassAssertionAxioms(individual)
                                                .map(OWLClassAssertionAxiom::getClassExpression)
                                                .filter(OWLClassExpression::isNamed)
                                                .map(OWLClassExpression::asOWLClass)
                                                .flatMap(cls -> classAncestorsProvider.getAncestors(cls).stream())
                                                .collect(toImmutableSet());
            }

            @Nonnull
            @Override
            public Collection<? extends OWLEntity> visit(@Nonnull OWLDatatype owlDatatype) {
                return Collections.singleton(owlDatatype);
            }

            @Nonnull
            @Override
            public Collection<? extends OWLEntity> visit(@Nonnull OWLAnnotationProperty owlAnnotationProperty) {
                return annotationPropertyAncestorsProvider.getAncestors(owlAnnotationProperty);
            }
        });
    }

}
