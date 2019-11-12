package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.form.EntityFormSubjectFactoryDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/04/16
 */
public class SubFormFieldDescriptor implements FormFieldDescriptor {

    public static final String TYPE = "SUB_FORM";

    private FormDescriptor formDescriptor;

    private EntityFormSubjectFactoryDescriptor freshSubjectStrategy;

    private SubFormFieldDescriptor() {
    }

    public SubFormFieldDescriptor(@Nonnull FormDescriptor formDescriptor,
                                  EntityFormSubjectFactoryDescriptor freshSubjectStrategy) {
        this.formDescriptor = checkNotNull(formDescriptor);
        this.freshSubjectStrategy = freshSubjectStrategy;
    }

    public static String getFieldTypeId() {
        return TYPE;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof SubFormFieldDescriptor)) {
            return false;
        }
        SubFormFieldDescriptor other = (SubFormFieldDescriptor) obj;
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

    @Nonnull
    public EntityFormSubjectFactoryDescriptor getFreshSubjectStrategy() {
        return freshSubjectStrategy;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(formDescriptor);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("SubFormFieldDescriptor")
                .add("formDescriptor", formDescriptor)
                .toString();
    }
}
