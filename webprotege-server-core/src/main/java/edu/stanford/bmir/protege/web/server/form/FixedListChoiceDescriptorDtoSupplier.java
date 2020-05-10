package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.frame.FrameComponentSessionRenderer;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptorDto;
import edu.stanford.bmir.protege.web.shared.form.field.FixedChoiceListSourceDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

public class FixedListChoiceDescriptorDtoSupplier {

    @Nonnull
    private final PrimitiveFormControlDataDtoRenderer renderer;

    @Inject
    public FixedListChoiceDescriptorDtoSupplier(@Nonnull PrimitiveFormControlDataDtoRenderer renderer) {
        this.renderer = checkNotNull(renderer);
    }

    @Nonnull
    public ImmutableList<ChoiceDescriptorDto> getChoices(@Nonnull FixedChoiceListSourceDescriptor descriptor,
                                                         @Nonnull FrameComponentSessionRenderer sessionRenderer) {
        return descriptor.getChoices()
                  .stream()
                  .flatMap(choiceDescriptor -> descriptor.getChoices().stream())
                  .map(choiceDescriptor -> toChoiceDescriptorDto(choiceDescriptor, sessionRenderer))
                  .collect(toImmutableList());
    }

    @Nonnull
    private ChoiceDescriptorDto toChoiceDescriptorDto(ChoiceDescriptor choiceDescriptor,
                                                      @Nonnull FrameComponentSessionRenderer sessionRenderer) {
        var dataDto = toPrimitiveFormControlDataDto(choiceDescriptor.getValue(), sessionRenderer);
        return ChoiceDescriptorDto.get(dataDto, choiceDescriptor.getLabel());
    }

    @Nonnull
    private PrimitiveFormControlDataDto toPrimitiveFormControlDataDto(@Nonnull PrimitiveFormControlData data,
                                                                      @Nonnull FrameComponentSessionRenderer sessionRenderer) {
        var primitive = data.getPrimitive();
        return renderer.toFormControlDataDto(primitive, sessionRenderer);
    }
}
