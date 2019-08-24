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
public abstract class MergedEntities implements StructuredChangeDescription {

    private static final String MERGED_ENTITIES = "MergedEntities";

    public static MergedEntities get(@Nonnull ImmutableSet<IRI> mergedEntities,
                                     @Nonnull IRI targetEntity) {
        return new AutoValue_MergedEntities(mergedEntities, targetEntity);
    }

    public abstract ImmutableSet<IRI> getMergedEntities();

    public abstract IRI getTargetEntity();

    @Nonnull
    public static String getAssociatedTypeName() {
        return MERGED_ENTITIES;
    }

    @Nonnull
    @Override
    public String getTypeName() {
        return MERGED_ENTITIES;
    }

    @Nonnull
    @Override
    public String formatDescription(@Nonnull OWLObjectStringFormatter formatter) {
        return formatter.formatString("Merged %s into %s", getMergedEntities(), getTargetEntity());
    }
}
