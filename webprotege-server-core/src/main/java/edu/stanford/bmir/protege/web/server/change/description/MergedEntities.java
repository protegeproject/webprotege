package edu.stanford.bmir.protege.web.server.change.description;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2018-12-10
 */
@AutoValue
public abstract class MergedEntities implements StructuredChangeDescription {

    private static final String MERGED_ENTITIES = "MergedEntities";

    public static MergedEntities get(@Nonnull ImmutableSet<OWLEntity> mergedEntities,
                                     @Nonnull OWLEntity targetEntity) {
        return new AutoValue_MergedEntities(mergedEntities, targetEntity);
    }

    public abstract ImmutableSet<OWLEntity> getMergedEntities();

    public abstract OWLEntity getTargetEntity();

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
