package edu.stanford.bmir.protege.web.shared.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.core.shared.GwtIncompatible;
import edu.stanford.bmir.protege.web.shared.util.UUIDUtil;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-20
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class OntologyDocumentId {

    @JsonCreator
    @Nonnull
    public static OntologyDocumentId get(@Nonnull String id) {
        if(!UUIDUtil.isWellFormed(id)) {
            throw new RuntimeException("Malformed OntologyDocumentId.  OntologyDocumentIds should be UUIDs");
        }
        return new AutoValue_OntologyDocumentId(id);
    }

    @JsonValue
    @Nonnull
    public abstract String getId();

    @GwtIncompatible
    @Nonnull
    public static OntologyDocumentId generate() {
        return get(UUID.randomUUID().toString());
    }
}
