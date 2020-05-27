package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
    private final FormFieldDescriptorObjectListPresenter elementDescriptorListPresenter;

    @Nullable
    private FormId formId;

    @Inject
    public FormDescriptorPresenter(@Nonnull FormDescriptorView view,
                                   @Nonnull FormFieldDescriptorObjectListPresenter elementDescriptorListPresenter) {
        this.view = checkNotNull(view);
        this.elementDescriptorListPresenter = checkNotNull(elementDescriptorListPresenter);
    }

    public void clear() {
        formId = null;
        elementDescriptorListPresenter.clear();
    }

    @Nonnull
    public LanguageMap getFormLabel() {
        return view.getLabel();
    }

    public void setFormId(@Nonnull FormId formId) {
        this.formId = checkNotNull(formId);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        container.setWidget(view);
        elementDescriptorListPresenter.start(
                view.getFieldDescriptorListContainer(),
                eventBus);
        elementDescriptorListPresenter.setDefaultStateCollapsed();
        // TODO: Resource bundle
        elementDescriptorListPresenter.setAddObjectText("Add field");
        view.setAddFormFieldHandler(this::handleAddFormElement);

    }

    private void handleAddFormElement() {
        elementDescriptorListPresenter.addElement();
    }

    public void setFormDescriptor(@Nonnull FormDescriptor formDescriptor) {
        this.formId = formDescriptor.getFormId();
        view.setLabel(formDescriptor.getLabel());
        elementDescriptorListPresenter.setValues(formDescriptor.getFields());
    }

    @Nonnull
    public FormDescriptor getFormDescriptor() {
        LanguageMap label = view.getLabel();
        List<FormFieldDescriptor> elementDescriptors = elementDescriptorListPresenter.getValues();

        return new FormDescriptor(this.formId, label, elementDescriptors, Optional.empty());
    }
}
