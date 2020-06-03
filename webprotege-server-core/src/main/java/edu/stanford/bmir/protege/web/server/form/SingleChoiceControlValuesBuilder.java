package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.frame.FrameComponentSessionRenderer;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;
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

public class SingleChoiceControlValuesBuilder {

    @Nonnull
    private final BindingValuesExtractor bindingValuesExtractor;

    @Nonnull
    private final PrimitiveFormControlDataDtoRenderer renderer;

    @Nonnull
    private final FrameComponentSessionRenderer sessionRenderer;

    @Nonnull
    private final ChoiceDescriptorCache choiceDescriptorCache;

    @Inject
    public SingleChoiceControlValuesBuilder(@Nonnull BindingValuesExtractor bindingValuesExtractor, @Nonnull PrimitiveFormControlDataDtoRenderer renderer, @Nonnull FrameComponentSessionRenderer sessionRenderer, @Nonnull ChoiceDescriptorCache choiceDescriptorCache) {
        this.bindingValuesExtractor = checkNotNull(bindingValuesExtractor);
        this.renderer = checkNotNull(renderer);
        this.sessionRenderer = checkNotNull(sessionRenderer);
        this.choiceDescriptorCache = checkNotNull(choiceDescriptorCache);
    }

    @Nonnull
    public ImmutableList<FormControlDataDto> getSingleChoiceControlDataDtoValues(@Nonnull SingleChoiceControlDescriptor singleChoiceControlDescriptor,
                                                                                 @Nonnull OWLEntityData subject,
                                                                                 @Nonnull OwlBinding theBinding,
                                                                                 @Nonnull LangTagFilter langTagFilter) {
        var values = bindingValuesExtractor.getBindingValues(subject.getEntity(), theBinding);
        var choiceSource = singleChoiceControlDescriptor.getSource();
        var vals = values.stream()
                         .flatMap(v -> renderer.toFormControlDataDto(v, sessionRenderer))
                         .filter(data -> isIncluded(data, langTagFilter))
                         .map(value -> SingleChoiceControlDataDto.get(
                                 singleChoiceControlDescriptor,
                                 choiceDescriptorCache.getChoices(choiceSource),
                                 value))
                         .limit(1)
                         .collect(ImmutableList.<FormControlDataDto>toImmutableList());
        if (vals.isEmpty()) {
            return ImmutableList.of(
                    SingleChoiceControlDataDto.get(singleChoiceControlDescriptor,
                                                   choiceDescriptorCache.getChoices(choiceSource),
                                                   null)
            );
        } else {
            return vals;
        }
    }

    private boolean isIncluded(@Nonnull PrimitiveFormControlDataDto dto, LangTagFilter langTagFilter) {
        var primitive = dto.toPrimitiveFormControlData().getPrimitive();
        if (primitive instanceof OWLLiteral) {
            return langTagFilter.isIncluded(((OWLLiteral) primitive).getLang());
        } else {
            return true;
        }
    }
}