package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-16
 */
public class FormFieldDescriptorPresenter implements ObjectPresenter<FormFieldDescriptor> {

    @Nonnull
    private ProjectId projectId;

    @Nonnull
    private final FormFieldDescriptorView view;

    @Nonnull
    private final OwlBindingPresenter bindingPresenter;

    @Nonnull
    private final FormControlDescriptorChooserPresenter fieldDescriptorChooserPresenter;

    @Nonnull
    private DispatchServiceManager dispatchServiceManager;

    @Inject
    public FormFieldDescriptorPresenter(@Nonnull ProjectId projectId,
                                        @Nonnull FormFieldDescriptorView view,
                                        @Nonnull OwlBindingPresenter bindingPresenter,
                                        @Nonnull FormControlDescriptorChooserPresenter fieldChooserPresenter,
                                        @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.bindingPresenter = checkNotNull(bindingPresenter);
        this.fieldDescriptorChooserPresenter = checkNotNull(fieldChooserPresenter);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
    }

    @Nonnull
    @Override
    public String getHeaderLabel() {
        return view.getFormFieldId();
    }

    @Override
    public void setHeaderLabelChangedHandler(Consumer<String> headerLabelHandler) {
        view.setElementIdChangedHandler(elementId -> headerLabelHandler.accept(elementId.getId()));
    }

    @Nonnull
    public Optional<FormFieldDescriptor> getValue() {
        Optional<FormControlDescriptor> formFieldDescriptor = fieldDescriptorChooserPresenter.getFormFieldDescriptor();
        if(!formFieldDescriptor.isPresent()) {
            return Optional.empty();
        }
        FormFieldDescriptor descriptor = FormFieldDescriptor.get(FormFieldId.get(view.getFormFieldId()),
                                                                 bindingPresenter.getBinding().orElse(null),
                                                                 view.getLabel(),
                                                                 view.getFieldRun(),
                                                                 formFieldDescriptor.get(),
                                                                 view.getRepeatability(),
                                                                 view.getOptionality(),
                                                                 view.getHelp(),
                                                                 Collections.emptyMap());
        return Optional.of(descriptor);
    }

    public void setValue(@Nonnull FormFieldDescriptor descriptor) {
        bindingPresenter.clear();
        descriptor.getOwlBinding().ifPresent(bindingPresenter::setBinding);

        String elementId = descriptor.getId()
                                     .getId();
        view.setFormFieldId(elementId);

        view.setFieldRun(descriptor.getFieldRun());

        view.setLabel(descriptor.getLabel());

        view.setHelp(descriptor.getHelp());

        view.setRepeatability(descriptor.getRepeatability());

        view.setOptionality(descriptor.getOptionality());



        FormControlDescriptor formControlDescriptor = descriptor.getFormControlDescriptor();
        fieldDescriptorChooserPresenter.setFormFieldDescriptor(formControlDescriptor);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        fieldDescriptorChooserPresenter.start(view.getFieldDescriptorViewContainer());
        bindingPresenter.start(view.getOwlBindingViewContainer());
    }
}
