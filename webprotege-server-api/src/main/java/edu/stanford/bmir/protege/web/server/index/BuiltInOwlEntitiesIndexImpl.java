package edu.stanford.bmir.protege.web.server.index;

import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-27
 */
public class BuiltInOwlEntitiesIndexImpl implements BuiltInOwlEntitiesIndex {


    @Nonnull
    private final ImmutableSet<OWLClass> classes;

    @Nonnull
    private final ImmutableSet<OWLObjectProperty> objectProperties;

    @Nonnull
    private final ImmutableSet<OWLDataProperty> dataProperties;

    @Nonnull
    private final ImmutableSet<OWLAnnotationProperty> annotationProperties;


    @Nonnull
    private final ImmutableSet<OWLEntity> builtInEntities;

    @Inject
    public BuiltInOwlEntitiesIndexImpl(@Nonnull OWLDataFactory dataFactory) {
        classes = ImmutableSet.of(
                dataFactory.getOWLThing(),
                dataFactory.getOWLNothing()
        );
        objectProperties = ImmutableSet.of(
                dataFactory.getOWLTopObjectProperty(),
                dataFactory.getOWLBottomObjectProperty()
        );
        dataProperties = ImmutableSet.of(
                dataFactory.getOWLTopDataProperty(),
                dataFactory.getOWLBottomDataProperty()
        );
        annotationProperties = ImmutableSet.of(
                dataFactory.getRDFSLabel(),
                dataFactory.getRDFSComment(),
                dataFactory.getRDFSIsDefinedBy(),
                dataFactory.getRDFSSeeAlso(),
                dataFactory.getOWLBackwardCompatibleWith(),
                dataFactory.getOWLIncompatibleWith()
        );
        builtInEntities = ImmutableSet.of(classes, objectProperties, dataProperties, annotationProperties)
                .stream()
                .flatMap(Collection::stream)
                .collect(toImmutableSet());
    }

    @Nonnull
    @Override
    public Stream<OWLEntity> getBuiltInEntities() {
        return builtInEntities.stream();
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
