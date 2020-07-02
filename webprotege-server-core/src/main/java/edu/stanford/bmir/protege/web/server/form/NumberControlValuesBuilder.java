package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlDataDto;
import edu.stanford.bmir.protege.web.shared.form.data.NumberControlDataDto;
import edu.stanford.bmir.protege.web.server.form.data.NumberControlDataDtoComparator;
import edu.stanford.bmir.protege.web.shared.form.field.NumberControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.OwlBinding;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

@FormDataBuilderSession
public class NumberControlValuesBuilder {

    @Nonnull
    private final BindingValuesExtractor bindingValuesExtractor;

    @Nonnull
    private final NumberControlDataDtoComparator comparator;

    @Inject
    public NumberControlValuesBuilder(@Nonnull BindingValuesExtractor bindingValuesExtractor,
                                      @Nonnull NumberControlDataDtoComparator comparator) {
        this.bindingValuesExtractor = checkNotNull(bindingValuesExtractor);
        this.comparator = checkNotNull(comparator);
    }

    @Nonnull
    public ImmutableList<FormControlDataDto> getNumberControlDataDtoValues(NumberControlDescriptor numberControlDescriptor, @Nonnull OWLEntityData subject, OwlBinding theBinding, int depth) {
        var values = bindingValuesExtractor.getBindingValues(subject.getEntity(), theBinding);
        return values.stream()
                     .filter(p -> p instanceof OWLLiteral)
                     .map(p -> (OWLLiteral) p)
                     .map(value -> NumberControlDataDto.get(numberControlDescriptor, value, depth))
                     .sorted(comparator)
                     .collect(ImmutableList.toImmutableList());
    }
}