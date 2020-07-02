package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceListSourceDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.DynamicChoiceListSourceDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FixedChoiceListSourceDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-11
 */
public class ChoiceListSourceDescriptorPresenter {

    @Nonnull
    private final ChoiceListSourceDescriptorView view;

    @Nonnull
    private final FixedChoiceListSourceDescriptorPresenter fixedChoiceListSourceDescriptorPresenter;

    @Nonnull
    private final DynamicChoiceListSourceDescriptorPresenter dynamicChoiceListSourceDescriptorPresenter;

    @Inject
    public ChoiceListSourceDescriptorPresenter(@Nonnull ChoiceListSourceDescriptorView view,
                                               @Nonnull FixedChoiceListSourceDescriptorPresenter fixedChoiceListSourceDescriptorPresenter,
                                               @Nonnull DynamicChoiceListSourceDescriptorPresenter dynamicChoiceListSourceDescriptorPresenter) {
        this.view = checkNotNull(view);
        this.fixedChoiceListSourceDescriptorPresenter = checkNotNull(fixedChoiceListSourceDescriptorPresenter);
        this.dynamicChoiceListSourceDescriptorPresenter = checkNotNull(dynamicChoiceListSourceDescriptorPresenter);
    }

    public void clear() {
        view.setFixedList(true);
        fixedChoiceListSourceDescriptorPresenter.clear();
        handleSourceTypeChanged();
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        view.setSourceTypeChangedHandler(this::handleSourceTypeChanged);
    }

    private void handleSourceTypeChanged() {
        if(view.isFixedList()) {
            fixedChoiceListSourceDescriptorPresenter.start(view.getDescriptorContainer());
        }
        else {
            dynamicChoiceListSourceDescriptorPresenter.start(view.getDescriptorContainer());
        }
    }

    public void setDescriptor(ChoiceListSourceDescriptor descriptor) {
        if(descriptor instanceof FixedChoiceListSourceDescriptor) {
            view.setFixedList(true);
            fixedChoiceListSourceDescriptorPresenter.setDescriptor((FixedChoiceListSourceDescriptor) descriptor);
            fixedChoiceListSourceDescriptorPresenter.start(view.getDescriptorContainer());
        }
        else {
            view.setFixedList(false);
            dynamicChoiceListSourceDescriptorPresenter.start(view.getDescriptorContainer());
            dynamicChoiceListSourceDescriptorPresenter.setDescriptor((DynamicChoiceListSourceDescriptor) descriptor);
        }
        handleSourceTypeChanged();
    }

    public ChoiceListSourceDescriptor getDescriptor() {
        if(view.isFixedList()) {
            return fixedChoiceListSourceDescriptorPresenter.getDescriptor();
        }
        else {
            return dynamicChoiceListSourceDescriptorPresenter.getDescriptor();
        }
    }
}
