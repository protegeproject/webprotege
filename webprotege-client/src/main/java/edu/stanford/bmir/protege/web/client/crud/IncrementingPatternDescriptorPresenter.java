package edu.stanford.bmir.protege.web.client.crud;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.crud.gen.GeneratedValueDescriptor;
import edu.stanford.bmir.protege.web.shared.crud.gen.IncrementingPatternDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-01
 */
public class IncrementingPatternDescriptorPresenter implements GeneratedValueDescriptorPresenter {

    @Nonnull
    private final IncrementingPatternDescriptorView view;

    @Inject
    public IncrementingPatternDescriptorPresenter(@Nonnull IncrementingPatternDescriptorView view) {
        this.view = checkNotNull(view);
    }

    @Override
    public String getName() {
        return "Incrementing number";
    }

    @Override
    public void setValue(GeneratedValueDescriptor valueDescriptor) {
        if(!(valueDescriptor instanceof IncrementingPatternDescriptor)) {
            view.clear();
            return;
        }
        setValue((IncrementingPatternDescriptor) valueDescriptor);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    private void setValue(@Nonnull IncrementingPatternDescriptor descriptor) {
        view.setStartingValue(descriptor.getStartingValue());
        view.setFormat(descriptor.getFormat());
    }

    public Optional<GeneratedValueDescriptor> getValue() {
        int startingValue = view.getStartingValue();
        String format = view.getFormat();
        return Optional.of(IncrementingPatternDescriptor.get(startingValue, format));
    }

}
