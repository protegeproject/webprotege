package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlDataDto;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlDataDto;
import edu.stanford.bmir.protege.web.shared.form.data.SingleChoiceControlDataDto;
import edu.stanford.bmir.protege.web.shared.form.field.OwlBinding;
import edu.stanford.bmir.protege.web.shared.form.field.SingleChoiceControlDescriptor;
import edu.stanford.bmir.protege.web.shared.lang.LangTagFilter;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

@FormDataBuilderSession
public class SingleChoiceControlValuesBuilder {

    @Nonnull
    private final BindingValuesExtractor bindingValuesExtractor;

    @Nonnull
    private final PrimitiveFormControlDataDtoRenderer renderer;

    @Nonnull
    private final LangTagFilter langTagFilter;

    @Inject
    public SingleChoiceControlValuesBuilder(@Nonnull BindingValuesExtractor bindingValuesExtractor,
                                            @Nonnull PrimitiveFormControlDataDtoRenderer renderer,
                                            @Nonnull LangTagFilter langTagFilter) {
        this.bindingValuesExtractor = checkNotNull(bindingValuesExtractor);
        this.renderer = checkNotNull(renderer);
        this.langTagFilter = checkNotNull(langTagFilter);
    }

    @Nonnull
    public ImmutableList<FormControlDataDto> getSingleChoiceControlDataDtoValues(@Nonnull SingleChoiceControlDescriptor singleChoiceControlDescriptor,
                                                                                 @Nonnull OWLEntityData subject,
                                                                                 @Nonnull OwlBinding theBinding, int depth) {
        var values = bindingValuesExtractor.getBindingValues(subject.getEntity(), theBinding);
        return values.stream()
                         .flatMap(renderer::toFormControlDataDto)
                         .filter(this::isIncluded)
                         .map(value -> SingleChoiceControlDataDto.get(
                                 singleChoiceControlDescriptor,
                                 value,
                                 depth))
                         .collect(ImmutableList.<FormControlDataDto>toImmutableList());
    }

    private boolean isIncluded(@Nonnull PrimitiveFormControlDataDto dto) {
        var primitive = dto.toPrimitiveFormControlData().getPrimitive();
        if (primitive instanceof OWLLiteral) {
            return langTagFilter.isIncluded(((OWLLiteral) primitive).getLang());
        } else {
            return true;
        }
    }
}