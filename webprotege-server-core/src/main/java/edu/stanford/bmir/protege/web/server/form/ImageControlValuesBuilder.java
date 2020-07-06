package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlDataDto;
import edu.stanford.bmir.protege.web.shared.form.data.ImageControlDataDto;
import edu.stanford.bmir.protege.web.server.form.data.ImageControlDataDtoComparator;
import edu.stanford.bmir.protege.web.shared.form.field.ImageControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.OwlBinding;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

@FormDataBuilderSession
public class ImageControlValuesBuilder {

    @Nonnull
    private final BindingValuesExtractor bindingValuesExtractor;

    @Nonnull
    private final ImageControlDataDtoComparator comparator;

    @Inject
    public ImageControlValuesBuilder(@Nonnull BindingValuesExtractor bindingValuesExtractor, @Nonnull ImageControlDataDtoComparator imageControlDataDtoComparator) {
        this.bindingValuesExtractor = checkNotNull(bindingValuesExtractor);
        this.comparator = checkNotNull(imageControlDataDtoComparator);
    }

    @Nonnull
    public ImmutableList<FormControlDataDto> getImageControlDataDtoValues(ImageControlDescriptor imageControlDescriptor, @Nonnull OWLEntityData subject, OwlBinding theBinding, int depth) {
        var values = bindingValuesExtractor.getBindingValues(subject.getEntity(), theBinding);
        return values.stream()
                     .filter(p -> p instanceof IRI)
                     .map(p -> (IRI) p)
                     .map(iri -> ImageControlDataDto.get(imageControlDescriptor, iri, depth))
                     .sorted(comparator)
                     .collect(toImmutableList());
    }
}