package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementDescriptor;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public class FormDescriptorPresenter implements Presenter {

    @Nonnull
    private final FormDescriptorView view;

    @Nonnull
    private final ObjectListPresenter<FormElementDescriptor> elementDescriptorListPresenter;

    @Inject
    public FormDescriptorPresenter(@Nonnull FormDescriptorView view,
                                   @Nonnull ObjectListPresenter<FormElementDescriptor> elementDescriptorListPresenter) {
        this.view = checkNotNull(view);
        this.elementDescriptorListPresenter = checkNotNull(elementDescriptorListPresenter);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        container.setWidget(view);
        elementDescriptorListPresenter.start(
                view.getElementDescriptorListContainer(),
                eventBus);
        view.setAddFormElementHandler(this::handleAddFormElement);
    }

    private void handleAddFormElement() {
        elementDescriptorListPresenter.addElement();
    }

    public void setFormDescriptor(@Nonnull FormDescriptor formDescriptor) {
        FormId formId = formDescriptor.getFormId();
        view.setFormId(formId.getId());
        view.setLabel(formDescriptor.getLabel());
        elementDescriptorListPresenter.setValues(formDescriptor.getElements());
    }

    @Nonnull
    public FormDescriptor getFormDescriptor() {
        FormId formId = FormId.get(view.getFormId().trim());
        LanguageMap label = view.getLabel();
        List<FormElementDescriptor> elementDescriptors = elementDescriptorListPresenter.getValues();

        return new FormDescriptor(formId, label, elementDescriptors, Optional.empty());
    }
}
