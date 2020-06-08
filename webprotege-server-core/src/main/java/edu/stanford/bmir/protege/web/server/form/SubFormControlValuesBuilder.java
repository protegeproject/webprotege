package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlDataDto;
import edu.stanford.bmir.protege.web.shared.form.field.OwlBinding;
import edu.stanford.bmir.protege.web.shared.form.field.SubFormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.lang.LangTagFilter;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

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
    public ImmutableList<FormControlDataDto> getSubFormControlDataDtoValues(SubFormControlDescriptor subFormControlDescriptor,
                                                                     @Nonnull OWLEntityData subject,
                                                                     @Nonnull OwlBinding theBinding,
                                                                            int depth) {
        var values = bindingValuesExtractor.getBindingValues(subject.getEntity(), theBinding);
        FormDescriptor subFormDescriptor = subFormControlDescriptor.getFormDescriptor();
        return values.stream()
                     .filter(p -> p instanceof OWLEntity)
                     .map(p -> (OWLEntity) p)
                     .map(entity -> formDataDtoBuilderProvider.get().getFormDataDto(entity,
                                                                                subFormDescriptor,
                                                                                depth))
                     .collect(ImmutableList.toImmutableList());
    }
}