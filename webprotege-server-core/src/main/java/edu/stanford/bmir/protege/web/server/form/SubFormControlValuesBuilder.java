package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.OwlBinding;
import edu.stanford.bmir.protege.web.shared.form.field.SubFormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.SubFormControlDescriptorDto;
import edu.stanford.bmir.protege.web.shared.lang.LangTagFilter;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLPrimitive;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@FormDataBuilderSession
public class SubFormControlValuesBuilder {

    @Nonnull
    private final BindingValuesExtractor bindingValuesExtractor;

    @Nonnull
    private final Provider<EntityFrameFormDataDtoBuilder> formDataDtoBuilderProvider;

    @Inject
    public SubFormControlValuesBuilder(@Nonnull BindingValuesExtractor bindingValuesExtractor,
                                       @Nonnull Provider<EntityFrameFormDataDtoBuilder> formDataDtoBuilderProvider) {
        this.bindingValuesExtractor = checkNotNull(bindingValuesExtractor);
        this.formDataDtoBuilderProvider = checkNotNull(formDataDtoBuilderProvider);
    }

    @Nonnull
    public ImmutableList<FormControlDataDto> getSubFormControlDataDtoValues(@Nonnull SubFormControlDescriptor subFormControlDescriptor,
                                                                            @Nonnull Optional<FormSubject> subject,
                                                                            @Nonnull OwlBinding theBinding,
                                                                            int depth) {
        var subFormDescriptor = subFormControlDescriptor.getFormDescriptor();
        var subjectFactoryDescriptor = subFormDescriptor.getSubjectFactoryDescriptor().orElseThrow();
        var valuesEntityType = subjectFactoryDescriptor.getEntityType();
        var values = bindingValuesExtractor.getBindingValuesOfType(subject, theBinding, valuesEntityType);
        if(values.isEmpty()) {
            return ImmutableList.of(
                    formDataDtoBuilderProvider.get()
                                              .getFormDataDto(Optional.empty(), subFormDescriptor, depth + 1)
            );
        }
        return values.stream()
                     .map(this::toFormSubject)
                     .map(subSubject -> formDataDtoBuilderProvider.get()
                                                                  .getFormDataDto(subSubject, subFormDescriptor, depth))
                     .collect(ImmutableList.toImmutableList());
    }

    private Optional<FormSubject> toFormSubject(@Nonnull OWLPrimitive primitive) {
        if (primitive instanceof OWLEntity) {
            return Optional.of(FormEntitySubject.get((OWLEntity) primitive));
        }
        else if (primitive instanceof IRI) {
            return Optional.of(FormIriSubject.get(((IRI) primitive)));
        }
        else {
            return Optional.empty();
        }
    }
}