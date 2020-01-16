package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.form.FormSubjectFactoryDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.SubFormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-21
 */
public class SubFormControlDescriptorPresenter implements FormControlDescriptorPresenter {

    @Nonnull
    private final FormDescriptorPresenter subFormPresenter;

    @Nonnull
    private final SubFormControlDescriptorView view;

    @Nonnull
    private final FormSubjectFactoryDescriptorPresenter formSubjectFactoryDescriptorPresenter;

    @Inject
    public SubFormControlDescriptorPresenter(@Nonnull FormDescriptorPresenter subFormPresenter,
                                             @Nonnull SubFormControlDescriptorView view,
                                             @Nonnull FormSubjectFactoryDescriptorPresenter formSubjectFactoryDescriptorPresenter) {
        this.subFormPresenter = checkNotNull(subFormPresenter);
        this.view = checkNotNull(view);
        this.formSubjectFactoryDescriptorPresenter = checkNotNull(formSubjectFactoryDescriptorPresenter);
    }

    @Override
    public void clear() {
        view.clear();
    }

    @Nonnull
    @Override
    public FormControlDescriptor getFormFieldDescriptor() {
        FormDescriptor subFormDescriptor = subFormPresenter.getFormDescriptor();
        Optional<FormSubjectFactoryDescriptor> formSubjectFactoryDescriptor = formSubjectFactoryDescriptorPresenter.getDescriptor();
        FormDescriptor aug = new FormDescriptor(
                subFormDescriptor.getFormId(),
                subFormDescriptor.getLabel(),
                subFormDescriptor.getFields(),
                formSubjectFactoryDescriptor
        );
        return new SubFormControlDescriptor(aug);
    }

    @Override
    public void setFormFieldDescriptor(@Nonnull FormControlDescriptor formControlDescriptor) {
        view.clear();
        if(!(formControlDescriptor instanceof SubFormControlDescriptor)) {
            return;
        }
        SubFormControlDescriptor subFormFieldDescriptor = (SubFormControlDescriptor) formControlDescriptor;
        FormDescriptor subFormDescriptor = subFormFieldDescriptor.getFormDescriptor();
        subFormPresenter.setFormDescriptor(subFormDescriptor);
        subFormDescriptor.getSubjectFactoryDescriptor().ifPresent(formSubjectFactoryDescriptorPresenter::setDescriptor);
    }


    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        subFormPresenter.start(view.getSubFormContainer(), new SimpleEventBus());
        formSubjectFactoryDescriptorPresenter.start(view.getFormSubjectDescriptorViewContainr());
    }
}
