package edu.stanford.bmir.protege.web.shared.form;

import java.util.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class FormId {

    private final String id;

    public FormId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FormId)) {
            return false;
        }
        FormId other = (FormId) obj;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


    @Override
    public String toString() {
        return toStringHelper("FormId")
                .addValue(id)
                .toString();
    }
}
