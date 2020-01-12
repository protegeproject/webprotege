package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.field.FixedChoiceListSourceDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Collections;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-11
 */
public class FixedChoiceListSourceDescriptorPresenter {

    @Nonnull
    private final FixedChoiceListSourceDescriptorView view;

    @Inject
    public FixedChoiceListSourceDescriptorPresenter(@Nonnull FixedChoiceListSourceDescriptorView view) {
        this.view = checkNotNull(view);

    }

    public void clear() {
        view.setChoiceDescriptors(Collections.emptyList());
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    public void setDescriptor(@Nonnull FixedChoiceListSourceDescriptor descriptor) {
        view.setChoiceDescriptors(descriptor.getChoices());
    }

    public FixedChoiceListSourceDescriptor getDescriptor() {
        return FixedChoiceListSourceDescriptor.get(view.getChoiceDescriptors());
    }
}
