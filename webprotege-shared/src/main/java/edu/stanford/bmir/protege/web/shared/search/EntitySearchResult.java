package edu.stanford.bmir.protege.web.shared.search;

import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Apr 2017
 */
public class EntitySearchResult implements IsSerializable {

    @SuppressWarnings("GwtInconsistentSerializableClass")
    private OWLEntityData matchedEntity;

    private SearchField field;

    private String fieldRendering;


    @GwtSerializationConstructor
    private EntitySearchResult() {
    }

    public EntitySearchResult(@Nonnull OWLEntityData matchedEntity,
                              @Nonnull SearchField field,
                              @Nonnull String fieldRendering) {
        this.matchedEntity = checkNotNull(matchedEntity);
        this.field = checkNotNull(field);
        this.fieldRendering = checkNotNull(fieldRendering);
    }

    @Nonnull
    public OWLEntityData getMatchedEntity() {
        return matchedEntity;
    }

    @Nonnull
    public SearchField getField() {
        return field;
    }

    @Nonnull
    public String getFieldRendering() {
        return fieldRendering;
    }
}
