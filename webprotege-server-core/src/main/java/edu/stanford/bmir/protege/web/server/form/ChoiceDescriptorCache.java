package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.frame.FrameComponentSessionRenderer;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptorDto;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceListSourceDescriptor;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChoiceDescriptorCache {


    @Nonnull
    private final Map<ChoiceListSourceDescriptor, ImmutableList<ChoiceDescriptorDto>> descriptorCache = new HashMap<>();

    @Nonnull
    private final ChoiceDescriptorDtoSupplier choiceDescriptorDtoSupplier;

    @Nonnull
    private final FrameComponentSessionRenderer sessionRenderer;

    public ChoiceDescriptorCache(@Nonnull ChoiceDescriptorDtoSupplier choiceDescriptorDtoSupplier,
                                 @Nonnull FrameComponentSessionRenderer sessionRenderer) {
        this.choiceDescriptorDtoSupplier = checkNotNull(choiceDescriptorDtoSupplier);
        this.sessionRenderer = checkNotNull(sessionRenderer);
    }

    @Nonnull
    public ImmutableList<ChoiceDescriptorDto> getChoices(@Nonnull ChoiceListSourceDescriptor sourceDescriptor) {
        var cachedChoices = descriptorCache.get(sourceDescriptor);
        if (cachedChoices == null) {
            cachedChoices = choiceDescriptorDtoSupplier.getChoices(sourceDescriptor, sessionRenderer);
            descriptorCache.put(sourceDescriptor, cachedChoices);
        }
        return cachedChoices;
    }
}