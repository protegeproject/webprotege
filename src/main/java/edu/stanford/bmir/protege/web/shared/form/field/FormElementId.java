package edu.stanford.bmir.protege.web.shared.form.field;

import java.io.Serializable;
import java.util.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class FormElementId implements Serializable {

    private String id;

    private FormElementId() {
    }

    public FormElementId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FormElementId)) {
            return false;
        }
        FormElementId other = (FormElementId) obj;
        return this.id.equals(other.id);
    }


    @Override
    public String toString() {
        return toStringHelper("FormElementId")
                .addValue(id)
                .toString();
    }
}
