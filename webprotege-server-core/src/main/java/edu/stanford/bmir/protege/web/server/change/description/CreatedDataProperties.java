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
public abstract class CreatedDataProperties extends AbstractCreatedProperties {
    
    @Nonnull
    public static CreatedDataProperties get(@Nonnull ImmutableSet<OWLDataProperty> properties,
                                              @Nonnull ImmutableSet<OWLDataProperty> parentProperties) {
        return new AutoValue_CreatedDataProperties(properties, parentProperties);
    }

    @Nonnull
    @Override
    public String getTypeName() {
        return "CreatedDataProperties";
    }

}
