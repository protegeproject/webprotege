package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.frame.FrameComponentSessionRenderer;
import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.bmir.protege.web.server.mansyntax.render.ManchesterSyntaxEntityFrameRenderer;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.form.data.EntityNameControlDataDto;
import edu.stanford.bmir.protege.web.shared.form.data.EntityNameControlDataDtoComparator;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlDataDto;
import edu.stanford.bmir.protege.web.shared.form.field.EntityNameControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.OwlBinding;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

public class EntityNameControlValuesBuilder {

    @Nonnull
    private final BindingValuesExtractor bindingValuesExtractor;

    @Nonnull
    private final EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex;

    @Nonnull
    private final FrameComponentSessionRenderer renderer;

    @Nonnull
    private final EntityNameControlDataDtoComparator comparator;

    @Inject
    public EntityNameControlValuesBuilder(@Nonnull BindingValuesExtractor bindingValuesExtractor,
                                          @Nonnull EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex,
                                          @Nonnull FrameComponentSessionRenderer renderer,
                                          @Nonnull EntityNameControlDataDtoComparator comparator) {
        this.bindingValuesExtractor = bindingValuesExtractor;
        this.entitiesInProjectSignatureByIriIndex = entitiesInProjectSignatureByIriIndex;
        this.renderer = renderer;
        this.comparator = comparator;
    }

    @Nonnull
    public ImmutableList<FormControlDataDto> getEntityNameControlDataDtoValues(@Nonnull EntityNameControlDescriptor entityNameControlDescriptor,
                                                                        @Nonnull OWLEntityData subject,
                                                                        @Nonnull OwlBinding theBinding) {
        var values = bindingValuesExtractor.getBindingValues(subject.getEntity(), theBinding);
        return values.stream()
                     // Allow IRIs which correspond to entities
                     .filter(p -> p instanceof OWLEntity || p instanceof IRI)
                     .flatMap(p -> {
                         if (p instanceof OWLEntity) {
                             return Stream.of((OWLEntity) p);
                         } else {
                             var iri = (IRI) p;
                             return entitiesInProjectSignatureByIriIndex.getEntitiesInSignature(iri);
                         }
                     })
                     .map(renderer::getEntityRendering)
                     .map(entity -> EntityNameControlDataDto.get(entityNameControlDescriptor, entity))
                     .sorted(comparator)
                     .collect(ImmutableList.toImmutableList());
    }
}