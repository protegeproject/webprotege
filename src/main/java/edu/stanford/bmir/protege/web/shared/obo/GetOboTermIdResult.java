package edu.stanford.bmir.protege.web.shared.obo;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class GetOboTermIdResult implements Result {

    private OWLEntity entity;

    private OBOTermId termId;

    @GwtSerializationConstructor
    private GetOboTermIdResult() {
    }

    public GetOboTermIdResult(@Nonnull OWLEntity entity, @Nonnull OBOTermId termId) {
        this.entity = entity;
        this.termId = termId;
    }

    @Nonnull
    public OWLEntity getEntity() {
        return entity;
    }

    @Nonnull
    public OBOTermId getTermId() {
        return termId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(entity, termId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetOboTermIdResult)) {
            return false;
        }
        GetOboTermIdResult other = (GetOboTermIdResult) obj;
        return this.entity.equals(other.entity)
                && this.termId.equals(other.termId);
    }


    @Override
    public String toString() {
        return toStringHelper("GetOboTermIdResult")
                .addValue(entity)
                .addValue(termId)
                .toString();
    }
}
