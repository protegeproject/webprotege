package edu.stanford.bmir.protege.web.server.change.description;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2018-12-10
 */
@AutoValue
public abstract class UndeprecatedEntities implements StructuredChangeDescription {

    public static UndeprecatedEntities get(@Nonnull ImmutableSet<IRI> entities) {
        return new AutoValue_UndeprecatedEntities(entities);
    }

    @Nonnull
    public abstract ImmutableSet<IRI> getEntities();

    @Nonnull
    @Override
    public String getTypeName() {
        return "UndeprecatedEntities";
    }

    @Nonnull
    @Override
    public String formatDescription(@Nonnull OWLObjectStringFormatter formatter) {
        return formatter.formatString("Undeprecated %s", getEntities());
    }
}
