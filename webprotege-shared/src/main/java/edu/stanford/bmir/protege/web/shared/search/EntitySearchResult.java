package edu.stanford.bmir.protege.web.shared.search;

import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;

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
        this.matchedEntity = matchedEntity;
        this.field = field;
        this.fieldRendering = fieldRendering;
    }

    public OWLEntityData getMatchedEntity() {
        return matchedEntity;
    }

    public SearchField getField() {
        return field;
    }

    public String getFieldRendering() {
        return fieldRendering;
    }
}
