package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;
import edu.stanford.bmir.protege.web.shared.form.field.OwlPropertyBinding;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static java.util.stream.Collectors.toMap;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class FormDescriptor implements Serializable {

    private FormId formId;

    private LanguageMap label = LanguageMap.empty();

    private List<FormElementDescriptor> elements;

    @Nullable
    private EntityFormSubjectFactoryDescriptor subjectFactoryDescriptor;

    private FormDescriptor() {
        this.elements = new ArrayList<>();
    }

    public FormDescriptor(FormId id,
                          LanguageMap label,
                          List<FormElementDescriptor> formElementDescriptors,
                          Optional<EntityFormSubjectFactoryDescriptor> subjectFactoryDescriptor) {
        this.formId = id;
        this.label = label;
        this.elements = new ArrayList<>(formElementDescriptors);
        this.subjectFactoryDescriptor = subjectFactoryDescriptor.orElse(null);
    }

    public static FormDescriptor empty() {
        return new FormDescriptor(FormId.get("EmptyForm"), LanguageMap.empty(), Collections.emptyList(),
                                  Optional.empty());
    }

    public FormId getFormId() {
        return formId;
    }

    public LanguageMap getLabel() {
        return label;
    }

    @JsonIgnore
    @Nonnull
    public ImmutableSet<OWLProperty> getOwlProperties() {
        return elements.stream()
                       .map(FormElementDescriptor::getOwlBinding)
                       .filter(Optional::isPresent)
                       .map(Optional::get)
                       .filter(binding -> binding instanceof OwlPropertyBinding)
                       .map(binding -> (OwlPropertyBinding) binding)
                       .map(OwlPropertyBinding::getProperty)
                       .collect(toImmutableSet());
    }

    public Optional<OWLProperty> getOwlProperty(@Nonnull FormElementId formElementId) {
        return elements.stream()
                .filter(element -> element.getId().equals(formElementId))
                       .map(FormElementDescriptor::getOwlProperty)
                       .filter(Optional::isPresent)
                       .map(Optional::get)
                       .findFirst();
    }

    @Nonnull
    public Optional<EntityFormSubjectFactoryDescriptor> getSubjectFactoryDescriptor() {
        return Optional.ofNullable(subjectFactoryDescriptor);
    }

    public List<FormElementDescriptor> getElements() {
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

    public static class Builder {

        private final FormId formId;

        private LanguageMap label = LanguageMap.empty();

        private final List<FormElementDescriptor> builder_elementDescriptors = new ArrayList<>();


        public Builder(FormId formId) {
            this.formId = checkNotNull(formId);
        }

        public Builder setLabel(LanguageMap label) {
            this.label = checkNotNull(label);
            return this;
        }

        public Builder addDescriptor(FormElementDescriptor descriptor) {
            builder_elementDescriptors.add(descriptor);
            return this;
        }


        public FormDescriptor build() {
            return new FormDescriptor(formId, label, builder_elementDescriptors, Optional.empty());
        }
    }
}
