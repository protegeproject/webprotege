package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptor;

import javax.annotation.Nonnull;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-26
 */
public class GridColumnDescriptorPresenter {

    @Nonnull
    private final GridColumnDescriptorView view;

    private Optional<GridColumnDescriptor> descriptor = Optional.empty();

    public GridColumnDescriptorPresenter(@Nonnull GridColumnDescriptorView view) {
        this.view = checkNotNull(view);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    public void setValue(@Nonnull GridColumnDescriptor descriptor) {
        this.descriptor = Optional.of(descriptor);
        view.setId(descriptor.getId());

        view.setLabel(descriptor.getLabel());
    }

    @Nonnull
    public GridColumnDescriptor getValue(@Nonnull GridColumnDescriptor descriptor) {
        return null;
    }
}
