package edu.stanford.bmir.protege.web.shared.perspective;

import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/16
 */
public class PerspectiveId {

    private String id;

    public PerspectiveId(String id) {
        this.id = checkNotNull(id);
    }

    public String getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return "PerspectiveId".hashCode() + id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof PerspectiveId)) {
            return false;
        }
        PerspectiveId other = (PerspectiveId) o;
        return this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("PerspectiveId")
                .addValue(id).toString();
    }
}
