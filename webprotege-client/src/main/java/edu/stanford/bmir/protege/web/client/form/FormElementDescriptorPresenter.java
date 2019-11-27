package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;
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
public class FormElementDescriptorPresenter implements ObjectPresenter<FormElementDescriptor> {

    @Nonnull
    private ProjectId projectId;

    @Nonnull
    private final FormElementDescriptorView view;

    @Nonnull
    private final FormFieldDescriptorChooserPresenter fieldDescriptorChooserPresenter;

    @Nonnull
    private DispatchServiceManager dispatchServiceManager;

    @Inject
    public FormElementDescriptorPresenter(@Nonnull ProjectId projectId,
                                          @Nonnull FormElementDescriptorView view,
                                          @Nonnull FormFieldDescriptorChooserPresenter fieldChooserPresenter,
                                          @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.projectId = projectId;
        this.view = checkNotNull(view);
        this.fieldDescriptorChooserPresenter = fieldChooserPresenter;
        this.dispatchServiceManager = dispatchServiceManager;
    }

    @Nonnull
    @Override
    public String getHeaderLabel() {
        return view.getFormElementId();
    }

    @Override
    public void setHeaderLabelChangedHandler(Consumer<String> headerLabelHandler) {
        view.setElementIdChangedHandler(elementId -> headerLabelHandler.accept(elementId.getId()));
    }

    @Nonnull
    public Optional<FormElementDescriptor> getValue() {
        Optional<FormFieldDescriptor> formFieldDescriptor = fieldDescriptorChooserPresenter.getFormFieldDescriptor();
        if(!formFieldDescriptor.isPresent()) {
            return Optional.empty();
        }
        FormElementDescriptor descriptor = FormElementDescriptor.get(FormElementId.get(view.getFormElementId()),
                                                                     view.getOwlProperty()
                                                                         .map(OWLPropertyData::getEntity)
                                                                         .map(OwlPropertyBinding::get)
                                                                         .orElse(null),
                                                                     view.getLabel(),
                                                                     view.getElementRun(),
                                                                     formFieldDescriptor.get(),
                                                                     view.getRepeatability(),
                                                                     view.getOptionality(),
                                                                     view.getHelp(),
                                                                     Collections.emptyMap());
        return Optional.of(descriptor);
    }

    public void setValue(@Nonnull FormElementDescriptor descriptor) {

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
        view.setFormElementId(elementId);

        view.setElementRun(descriptor.getElementRun());

        view.setLabel(descriptor.getLabel());

        view.setHelp(descriptor.getHelp());

        view.setRepeatability(descriptor.getRepeatability());

        view.setOptionality(descriptor.getOptionality());



        FormFieldDescriptor formFieldDescriptor = descriptor.getFieldDescriptor();
        fieldDescriptorChooserPresenter.setFormFieldDescriptor(formFieldDescriptor);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        fieldDescriptorChooserPresenter.start(view.getFieldDescriptorViewContainer());
    }
}
