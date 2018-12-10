package edu.stanford.bmir.protege.web.server.change.description;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.model.OWLDataProperty;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2018-12-10
 */
@AutoValue
public abstract class MovedDataProperties extends AbstractMovedProperties {


    public static MovedDataProperties get(@Nonnull ImmutableSet<OWLDataProperty> properties,
                                          @Nonnull ImmutableSet<OWLDataProperty> from,
                                          @Nonnull OWLDataProperty to) {
        return new AutoValue_MovedDataProperties(properties, from, to);
    }

    @Nonnull
    @Override
    public String getTypeName() {
        return "MovedDataProperties";
    }
}
