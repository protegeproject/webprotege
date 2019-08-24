package edu.stanford.bmir.protege.web.server.change.description;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2018-12-10
 */
@AutoValue
public abstract class MovedObjectProperties extends AbstractMovedProperties {

    public static MovedObjectProperties get(@Nonnull ImmutableSet<OWLObjectProperty> properties,
                                   @Nonnull ImmutableSet<OWLObjectProperty> from,
                                   @Nonnull OWLObjectProperty to) {
        return new AutoValue_MovedObjectProperties(properties,
                                                   from,
                                                   to);
    }

    @Nonnull
    @Override
    public String getTypeName() {
        return "MovedObjectProperties";
    }
}
