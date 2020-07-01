package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.match.MatchingEngine;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlDataDto;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptorDto;
import edu.stanford.bmir.protege.web.shared.form.field.DynamicChoiceListSourceDescriptor;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableMap.toImmutableMap;

@FormDataBuilderSession
public class DynamicListChoiceDescriptorDtoSupplier {

    public static final int CHOICE_LIMIT = 100;
    @Nonnull
    private final MatchingEngine matchingEngine;

    @Nonnull
    private final FormDataBuilderSessionRenderer sessionRenderer;

    @Inject
    public DynamicListChoiceDescriptorDtoSupplier(@Nonnull MatchingEngine matchingEngine,
                                                  @Nonnull FormDataBuilderSessionRenderer sessionRenderer) {
        this.matchingEngine = checkNotNull(matchingEngine);
        this.sessionRenderer = checkNotNull(sessionRenderer);
    }

    @Nonnull
    public ImmutableList<ChoiceDescriptorDto> getChoices(@Nonnull DynamicChoiceListSourceDescriptor descriptor) {
        var matchCriteria = descriptor.getCriteria();
        return matchingEngine.match(matchCriteria)
                             .map(sessionRenderer::getEntityRendering)
                             .sorted()
                             .limit(CHOICE_LIMIT)
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
