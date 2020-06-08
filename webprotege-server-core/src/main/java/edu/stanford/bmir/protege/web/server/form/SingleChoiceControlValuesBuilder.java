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
    private final ChoiceDescriptorCache choiceDescriptorCache;

    @Nonnull
    private final LangTagFilter langTagFilter;

    @Inject
    public SingleChoiceControlValuesBuilder(@Nonnull BindingValuesExtractor bindingValuesExtractor,
                                            @Nonnull PrimitiveFormControlDataDtoRenderer renderer,
                                            @Nonnull ChoiceDescriptorCache choiceDescriptorCache,
                                            @Nonnull LangTagFilter langTagFilter) {
        this.bindingValuesExtractor = checkNotNull(bindingValuesExtractor);
        this.renderer = checkNotNull(renderer);
        this.choiceDescriptorCache = checkNotNull(choiceDescriptorCache);
        this.langTagFilter = checkNotNull(langTagFilter);
    }

    @Nonnull
    public ImmutableList<FormControlDataDto> getSingleChoiceControlDataDtoValues(@Nonnull SingleChoiceControlDescriptor singleChoiceControlDescriptor,
                                                                                 @Nonnull OWLEntityData subject,
                                                                                 @Nonnull OwlBinding theBinding, int depth) {
        var values = bindingValuesExtractor.getBindingValues(subject.getEntity(), theBinding);
        var choiceSource = singleChoiceControlDescriptor.getSource();
        var vals = values.stream()
                         .flatMap(renderer::toFormControlDataDto)
                         .filter(this::isIncluded)
                         .map(value -> SingleChoiceControlDataDto.get(
                                 singleChoiceControlDescriptor,
                                 choiceDescriptorCache.getChoices(choiceSource),
                                 value,
                                 depth))
                         .limit(1)
                         .collect(ImmutableList.<FormControlDataDto>toImmutableList());
        if (vals.isEmpty()) {
            return ImmutableList.of(
                    SingleChoiceControlDataDto.get(singleChoiceControlDescriptor,
                                                   choiceDescriptorCache.getChoices(choiceSource),
                                                   null,
                                                   depth)
            );
        } else {
            return vals;
        }
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