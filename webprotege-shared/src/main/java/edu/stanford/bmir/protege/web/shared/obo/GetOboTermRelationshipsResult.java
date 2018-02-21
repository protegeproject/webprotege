package edu.stanford.bmir.protege.web.shared.obo;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class GetOboTermRelationshipsResult implements Result {

    private OBOTermRelationships relationships;

    public GetOboTermRelationshipsResult(@Nonnull OBOTermRelationships relationships) {
        this.relationships = checkNotNull(relationships);
    }

    @GwtSerializationConstructor
    private GetOboTermRelationshipsResult() {
    }

    public OBOTermRelationships getRelationships() {
        return relationships;
    }

    @Override
    public int hashCode() {
        return relationships.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetOboTermRelationshipsResult)) {
            return false;
        }
        GetOboTermRelationshipsResult other = (GetOboTermRelationshipsResult) obj;
        return this.relationships.equals(other.relationships);
    }


    @Override
    public String toString() {
        return toStringHelper("GetOboTermRelationshipsResult")
                .addValue(relationships)
                .toString();
    }
}
