package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.form.field.OwlBinding;
import edu.stanford.bmir.protege.web.shared.form.field.OwlClassBinding;
import edu.stanford.bmir.protege.web.shared.form.field.OwlPropertyBinding;
import edu.stanford.bmir.protege.web.shared.frame.*;

import javax.annotation.Nonnull;

import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-06
 */
public class EntityFrameMapper {

    @Nonnull
    private final EntityFrame<?> frame;

    public EntityFrameMapper(@Nonnull EntityFrame<?> frame) {
        this.frame = checkNotNull(frame);
    }

    public ImmutableList<OWLPrimitiveData> getValues(@Nonnull OwlBinding binding) {
        if(binding instanceof OwlClassBinding) {
            if(frame instanceof ClassFrame) {
                return ImmutableList.copyOf(((ClassFrame) frame).getClassEntries());
            }
            else if(frame instanceof NamedIndividualFrame) {
                return ImmutableList.copyOf(((NamedIndividualFrame) frame).getClasses());
            }
            else {
                return ImmutableList.of();
            }
        }
        else {
            var propertyBinding = (OwlPropertyBinding) binding;
            if(frame instanceof HasPropertyValues) {
                return ((HasPropertyValues) frame).getPropertyValues()
                        .stream()
                        .filter(propertyValue -> propertyValue.getProperty().getEntity().equals(propertyBinding.getProperty()))
                        .map(PropertyValue::getValue)
                        .collect(toImmutableList());
            }
            else {
                return ImmutableList.of();
            }
        }
    }
}
