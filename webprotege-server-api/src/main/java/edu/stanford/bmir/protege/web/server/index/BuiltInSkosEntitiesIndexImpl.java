package edu.stanford.bmir.protege.web.server.index;

import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-27
 */
public class BuiltInSkosEntitiesIndexImpl implements BuiltInSkosEntitiesIndex {

    @Nonnull
    private final ImmutableSet<OWLAnnotationProperty> annotationProperties;

    @Nonnull
    private final ImmutableSet<OWLClass> classes;

    @Nonnull
    private final ImmutableSet<OWLObjectProperty> objectProperties;

    @Nonnull
    private final ImmutableSet<OWLDataProperty> dataProperties;
    @Inject
    public BuiltInSkosEntitiesIndexImpl(@Nonnull OWLDataFactory dataFactory) {
        annotationProperties = SKOSVocabulary.getAnnotationProperties(dataFactory)
                      .stream()
                      .collect(toImmutableSet());

        classes = SKOSVocabulary.getClasses(dataFactory)
                .stream()
                .collect(toImmutableSet());
        objectProperties = SKOSVocabulary.getObjectProperties(dataFactory)
                .stream()
                .collect(toImmutableSet());
        dataProperties = SKOSVocabulary.getDataProperties(dataFactory)
                .stream()
                .collect(toImmutableSet());
    }

    @Nonnull
    @Override
    public Stream<OWLEntity> getBuiltInEntities() {
        return Stream.of(annotationProperties, classes, objectProperties, dataProperties)
                .flatMap(Collection::stream);
    }

    @Nonnull
    @Override
    public Stream<OWLAnnotationProperty> getAnnotationProperties() {
        return annotationProperties.stream();
    }

    @Nonnull
    @Override
    public Stream<OWLClass> getClasses() {
        return classes.stream();
    }

    @Nonnull
    @Override
    public Stream<OWLObjectProperty> getObjectProperties() {
        return objectProperties.stream();
    }

    @Nonnull
    @Override
    public Stream<OWLDataProperty> getDataProperties() {
        return dataProperties.stream();
    }
}
