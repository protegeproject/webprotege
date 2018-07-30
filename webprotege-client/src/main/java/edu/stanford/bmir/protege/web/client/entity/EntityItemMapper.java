package edu.stanford.bmir.protege.web.client.entity;

import edu.stanford.bmir.protege.web.shared.place.*;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30 Jul 2018
 */
public class EntityItemMapper {

    private static final Mapper MAPPER = new Mapper();

    private static class Mapper implements OWLEntityVisitorEx<Item<?>> {
        @Nonnull
        @Override
        public Item<?> visit(@Nonnull OWLClass cls) {
            return new OWLClassItem(cls);
        }

        @Nonnull
        @Override
        public Item<?> visit(@Nonnull OWLObjectProperty property) {
            return new OWLObjectPropertyItem(property);
        }

        @Nonnull
        @Override
        public Item<?> visit(@Nonnull OWLDataProperty property) {
            return new OWLDataPropertyItem(property);
        }

        @Nonnull
        @Override
        public Item<?> visit(@Nonnull OWLNamedIndividual individual) {
            return new OWLNamedIndividualItem(individual);
        }

        @Nonnull
        @Override
        public Item<?> visit(@Nonnull OWLDatatype datatype) {
            return null;
        }

        @Nonnull
        @Override
        public Item<?> visit(@Nonnull OWLAnnotationProperty property) {
            return new OWLAnnotationPropertyItem(property);
        }
    }

    public static Optional<Item<?>> getItem(@Nonnull OWLEntity entity) {
        return Optional.ofNullable(entity.accept(MAPPER));
    }
}
