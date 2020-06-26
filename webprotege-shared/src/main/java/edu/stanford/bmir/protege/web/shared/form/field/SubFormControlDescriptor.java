package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/04/16
 */
public class SubFormControlDescriptor implements FormControlDescriptor {

    public static final String TYPE = "SUB_FORM";

    private FormDescriptor formDescriptor;

    private SubFormControlDescriptor() {
    }

    public SubFormControlDescriptor(@Nonnull FormDescriptor formDescriptor) {
        this.formDescriptor = checkNotNull(formDescriptor);
    }

    public static String getFieldTypeId() {
        return TYPE;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof SubFormControlDescriptor)) {
            return false;
        }
        SubFormControlDescriptor other = (SubFormControlDescriptor) obj;
        return this.formDescriptor.equals(other.formDescriptor);
    }

    @Nonnull
    @Override
    public String getAssociatedType() {
        return TYPE;
    }

    @JsonUnwrapped
    public FormDescriptor getFormDescriptor() {
        return formDescriptor;
    }

    @Override
    public <R> R accept(@Nonnull FormControlDescriptorVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(formDescriptor);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("SubFormControlDescriptor")
                .add("formDescriptor", formDescriptor)
                .toString();
    }
}
