package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldId;
import edu.stanford.bmir.protege.web.shared.form.field.OwlPropertyBinding;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class FormDescriptor implements IsSerializable {

    private FormId formId;

    private LanguageMap label = LanguageMap.empty();

    private List<FormFieldDescriptor> elements;

    @Nullable
    private FormSubjectFactoryDescriptor subjectFactoryDescriptor;

    private FormDescriptor() {
        this.elements = new ArrayList<>();
    }

    public FormDescriptor(FormId id,
                          LanguageMap label,
                          List<FormFieldDescriptor> formFieldDescriptors,
                          Optional<FormSubjectFactoryDescriptor> subjectFactoryDescriptor) {
        this.formId = id;
        this.label = label;
        this.elements = new ArrayList<>(formFieldDescriptors);
        this.subjectFactoryDescriptor = subjectFactoryDescriptor.orElse(null);
    }

    public static FormDescriptor empty(FormId formId) {
        return new FormDescriptor(formId, LanguageMap.empty(), Collections.emptyList(),
                                  Optional.empty());
    }

    public FormId getFormId() {
        return formId;
    }

    public LanguageMap getLabel() {
        return label;
    }

    @Nonnull
    public Optional<FormSubjectFactoryDescriptor> getSubjectFactoryDescriptor() {
        return Optional.ofNullable(subjectFactoryDescriptor);
    }

    public List<FormFieldDescriptor> getFields() {
        return elements;
    }

    public static Builder builder(FormId formId) {
        return new Builder(formId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(formId, label, elements);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FormDescriptor)) {
            return false;
        }
        FormDescriptor other = (FormDescriptor) obj;
        return this.formId.equals(other.formId)
                && this.label.equals(other.label)
                && this.elements.equals(other.elements)
                && Objects.equal(this.subjectFactoryDescriptor, other.subjectFactoryDescriptor);
    }


    @Override
    public String toString() {
        return toStringHelper("FormDescriptor")
                .addValue(formId)
                .addValue(label)
                .addValue(elements)
                .toString();
    }

    @Nonnull
    public FormDescriptor withFormId(@Nonnull FormId formId) {
        return new FormDescriptor(
                formId,
                label,
                getFields(),
                getSubjectFactoryDescriptor()
        );
    }

    @Nonnull
    public FormDescriptor withLabel(@Nonnull LanguageMap newLabel) {
        return new FormDescriptor(
                formId,
                newLabel,
                getFields(),
                getSubjectFactoryDescriptor()
        );
    }

    public static class Builder {

        private final FormId formId;

        private LanguageMap label = LanguageMap.empty();

        private final List<FormFieldDescriptor> builder_elementDescriptors = new ArrayList<>();


        public Builder(FormId formId) {
            this.formId = checkNotNull(formId);
        }

        public Builder setLabel(LanguageMap label) {
            this.label = checkNotNull(label);
            return this;
        }

        public Builder addDescriptor(FormFieldDescriptor descriptor) {
            builder_elementDescriptors.add(descriptor);
            return this;
        }


        public FormDescriptor build() {
            return new FormDescriptor(formId, label, builder_elementDescriptors, Optional.empty());
        }
    }
}
