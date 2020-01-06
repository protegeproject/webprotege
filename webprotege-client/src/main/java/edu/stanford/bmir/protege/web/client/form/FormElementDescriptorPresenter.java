package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldId;
import edu.stanford.bmir.protege.web.shared.form.field.OwlPropertyBinding;
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
public class FormElementDescriptorPresenter implements ObjectPresenter<FormFieldDescriptor> {

    @Nonnull
    private ProjectId projectId;

    @Nonnull
    private final FormFieldDescriptorView view;

    @Nonnull
    private final FormControlDescriptorChooserPresenter fieldDescriptorChooserPresenter;

    @Nonnull
    private DispatchServiceManager dispatchServiceManager;

    @Inject
    public FormElementDescriptorPresenter(@Nonnull ProjectId projectId,
                                          @Nonnull FormFieldDescriptorView view,
                                          @Nonnull FormControlDescriptorChooserPresenter fieldChooserPresenter,
                                          @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.projectId = projectId;
        this.view = checkNotNull(view);
        this.fieldDescriptorChooserPresenter = fieldChooserPresenter;
        this.dispatchServiceManager = dispatchServiceManager;
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
                                                                 view.getOwlProperty()
                                                                         .map(OWLPropertyData::getEntity)
                                                                         .map(OwlPropertyBinding::get)
                                                                         .orElse(null),
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

        view.clearOwlProperty();
        Optional<OWLProperty> owlProperty = descriptor.getOwlProperty();
        owlProperty.ifPresent(property -> {
            dispatchServiceManager.execute(new GetEntityRenderingAction(projectId,
                                                                        property),
                                           result -> {
                                               OWLEntityData entityData = result.getEntityData();
                                               if(entityData instanceof OWLPropertyData) {
                                                   view.setOwlProperty((OWLPropertyData) entityData);
                                               }
            });
        });


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
    }
}
