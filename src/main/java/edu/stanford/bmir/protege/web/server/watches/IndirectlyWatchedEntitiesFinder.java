package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.server.hierarchy.HasGetAncestors;
import edu.stanford.bmir.protege.web.server.hierarchy.OWLObjectHierarchyProvider;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLEntityVisitorExAdapter;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public class IndirectlyWatchedEntitiesFinder {

    private final OWLOntology rootOntology;

    @Nonnull
    private final HasGetAncestors<OWLClass> classAncestorsProvider;

    @Nonnull
    private final HasGetAncestors<OWLObjectProperty> objectAncestorsProvider;

    @Nonnull
    private final HasGetAncestors<OWLDataProperty> dataPropertyAncestorsProvider;

    @Nonnull
    private final HasGetAncestors<OWLAnnotationProperty> annotationPropertyAncestorsProvider;


    @Inject
    public IndirectlyWatchedEntitiesFinder(@RootOntology OWLOntology rootOntology,
                                           @Nonnull HasGetAncestors<OWLClass> classAncestorsProvider,
                                           @Nonnull HasGetAncestors<OWLObjectProperty> objectAncestorsProvider,
                                           @Nonnull HasGetAncestors<OWLDataProperty> dataPropertyAncestorsProvider,
                                           @Nonnull HasGetAncestors<OWLAnnotationProperty> annotationPropertyAncestorsProvider) {

        this.rootOntology = rootOntology;
        this.classAncestorsProvider = classAncestorsProvider;
        this.objectAncestorsProvider = objectAncestorsProvider;
        this.dataPropertyAncestorsProvider = dataPropertyAncestorsProvider;
        this.annotationPropertyAncestorsProvider = annotationPropertyAncestorsProvider;
    }

    public Set<? extends OWLEntity> getRelatedWatchedEntities(OWLEntity entity) {
        return entity.accept(new OWLEntityVisitorEx<Set<? extends OWLEntity>>() {

            @Nonnull
            @Override
            public Set<? extends OWLEntity> visit(@Nonnull OWLClass desc) {
                return classAncestorsProvider.getAncestors(desc);
            }

            @Nonnull
            @Override
            public Set<? extends OWLEntity> visit(@Nonnull OWLDataProperty property) {
                return dataPropertyAncestorsProvider.getAncestors(property);
            }

            @Nonnull
            @Override
            public Set<? extends OWLEntity> visit(@Nonnull OWLObjectProperty property) {
                return objectAncestorsProvider.getAncestors(property);
            }

            @Nonnull
            @Override
            public Set<? extends OWLEntity> visit(@Nonnull OWLNamedIndividual individual) {
                Collection<OWLClassExpression> types = EntitySearcher.getTypes(individual, rootOntology.getImportsClosure());
                Set<OWLClass> result = new HashSet<>();
                for(OWLClassExpression ce : types) {
                    if(!ce.isAnonymous()) {
                        result.addAll(classAncestorsProvider.getAncestors(ce.asOWLClass()));
                    }
                }
                return result;
            }

            @Nonnull
            @Override
            public Set<? extends OWLEntity> visit(@Nonnull OWLDatatype owlDatatype) {
                return Collections.singleton(owlDatatype);
            }

            @Nonnull
            @Override
            public Set<? extends OWLEntity> visit(@Nonnull OWLAnnotationProperty owlAnnotationProperty) {
                return annotationPropertyAncestorsProvider.getAncestors(owlAnnotationProperty);
            }
        });
    }

}
