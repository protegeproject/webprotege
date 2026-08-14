package edu.stanford.bmir.protege.web.server.integration.dto;

import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;

/**
 * OWL property kind used by the integration property REST resource.
 */
public enum OntologyPropertyKind {

    OBJECT(EntityType.OBJECT_PROPERTY),
    DATA(EntityType.DATA_PROPERTY),
    ANNOTATION(EntityType.ANNOTATION_PROPERTY);

    @Nonnull
    private final EntityType<?> entityType;

    OntologyPropertyKind(@Nonnull EntityType<?> entityType) {
        this.entityType = entityType;
    }

    @Nonnull
    public EntityType<?> getEntityType() {
        return entityType;
    }

    /**
     * Parses a query {@code kind} value. Blank/null returns {@code null} (list-all).
     *
     * @throws IllegalArgumentException if the value is present but not OBJECT, DATA, or ANNOTATION
     */
    @Nullable
    public static OntologyPropertyKind fromQuery(@Nullable String kind) {
        if (kind == null) {
            return null;
        }
        String trimmed = kind.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        try {
            return valueOf(trimmed.toUpperCase(Locale.ROOT));
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Query parameter 'kind' must be OBJECT, DATA, or ANNOTATION");
        }
    }
}
