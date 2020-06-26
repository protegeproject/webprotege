package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.frame.FrameComponentSessionRenderer;
import edu.stanford.bmir.protege.web.server.match.MatcherFactory;
import edu.stanford.bmir.protege.web.server.match.MatchingEngine;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlDataDto;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptorDto;
import edu.stanford.bmir.protege.web.shared.form.field.DynamicChoiceListSourceDescriptor;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableMap.toImmutableMap;

public class DynamicListChoiceDescriptorDtoSupplier {

    @Nonnull
    private final MatchingEngine matchingEngine;

    @Inject
    public DynamicListChoiceDescriptorDtoSupplier(@Nonnull MatchingEngine matchingEngine) {
        this.matchingEngine = checkNotNull(matchingEngine);
    }

    @Nonnull
    public ImmutableList<ChoiceDescriptorDto> getChoices(@Nonnull DynamicChoiceListSourceDescriptor descriptor,
                                                         @Nonnull FrameComponentSessionRenderer sessionRenderer) {
        var matchCriteria = descriptor.getCriteria();
        return matchingEngine.match(matchCriteria)
                             .limit(100)
                             .map(sessionRenderer::getEntityRendering)
                             .map(this::toChoiceDescriptorDto)
                             .collect(toImmutableList());
    }

    private ChoiceDescriptorDto toChoiceDescriptorDto(OWLEntityData entityData) {
        var shortForms = entityData.getShortForms();
        var dto = PrimitiveFormControlDataDto.get(entityData);
        var languageMap = LanguageMap.fromDictionaryMap(shortForms);
        return ChoiceDescriptorDto.get(dto, languageMap);
    }
}
