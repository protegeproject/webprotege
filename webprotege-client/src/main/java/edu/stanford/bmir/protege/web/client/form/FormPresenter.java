package edu.stanford.bmir.protege.web.client.form;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import edu.stanford.bmir.protege.web.shared.form.data.FormFieldData;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 * <p>
 * Presents a form and its associated form data.
 */
public class FormPresenter {

    @Nonnull
    private final FormView formView;

    @Nonnull
    private final NoFormView noFormView;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    private final List<FormFieldPresenter> fieldPresenters = new ArrayList<>();

    @Nonnull
    private Optional<FormDescriptor> currentFormDescriptor = Optional.empty();

    private Optional<AcceptsOneWidget> container = Optional.empty();

    @Nonnull
    private Optional<FormSubject> currentSubject = Optional.empty();

    private EntityFormSubjectFactory freshSubjectStrategy = Optional::empty;

    private FormFieldPresenterFactory formFieldPresenterFactory;

    @AutoFactory
    @Inject
    public FormPresenter(@Nonnull @Provided FormView formView,
                         @Nonnull @Provided NoFormView noFormView,
                         @Nonnull @Provided DispatchServiceManager dispatchServiceManager,
                         FormFieldPresenterFactory formFieldPresenterFactory) {
        this.formView = checkNotNull(formView);
        this.noFormView = checkNotNull(noFormView);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.formFieldPresenterFactory = formFieldPresenterFactory;
    }

    public void clearData() {
        currentSubject = Optional.empty();
        fieldPresenters.forEach(FormFieldPresenter::clearValue);
    }

    /**
     * Displays the specified form and the specified form data.
     *
     * @param formData The form data to be shown in the form.
     */
    public void displayForm(@Nonnull FormData formData) {
        checkNotNull(formData);
        currentSubject = formData.getSubject();
        if(currentFormDescriptor.equals(Optional.of(formData.getFormDescriptor()))) {
            updateFormData(formData);
        }
        else {
            createFormAndSetFormData(formData);
        }
        if(formData.getFormDescriptor()
                   .getFields()
                   .isEmpty()) {
            container.ifPresent(c -> c.setWidget(noFormView));
        }
        else {
            container.ifPresent(c -> c.setWidget(formView));
        }
        currentFormDescriptor = Optional.of(formData.getFormDescriptor());
    }

    private void updateFormData(@Nonnull FormData formData) {
        this.currentSubject = formData.getSubject();
        dispatchServiceManager.beginBatch();
        ImmutableList<FormFieldData> nextFormFieldData = formData.getFormFieldData();
        for(int i = 0; i < nextFormFieldData.size(); i++) {
            FormFieldData fieldData = nextFormFieldData.get(i);
            FormFieldPresenter formFieldPresenter = fieldPresenters.get(i);
            formFieldPresenter.setValue(fieldData);
        }
        dispatchServiceManager.executeCurrentBatch();
    }

    /**
     * Creates the form from scratch and fills in the specified form data.
     *
     * @param formData The form data to be filled into the form.
     */
    private void createFormAndSetFormData(@Nonnull FormData formData) {
        clear();
        currentFormDescriptor = Optional.of(formData.getFormDescriptor());
        dispatchServiceManager.beginBatch();
        for(FormFieldData fieldData : formData.getFormFieldData()) {
            addFormField(fieldData);
        }
        dispatchServiceManager.executeCurrentBatch();
    }

    public void clear() {
        fieldPresenters.clear();
        formView.clear();
        currentFormDescriptor = Optional.empty();
        container.ifPresent(c -> c.setWidget(noFormView));
    }

    private void addFormField(@Nonnull FormFieldData formFieldData) {
        FormFieldDescriptor formFieldDescriptor = formFieldData.getFormFieldDescriptor();
        FormFieldPresenter presenter = formFieldPresenterFactory.create(formFieldDescriptor);
        fieldPresenters.add(presenter);
        // TODO : Change handler
        presenter.setValue(formFieldData);
        FormFieldView formFieldView = presenter.getFormFieldView();
        formView.addFormElementView(formFieldView, formFieldDescriptor.getFieldRun());
    }

    /**
     * Gets the data that is held by the form being presented.  Only present values are
     * returned.
     *
     * @return The {@link FormData} entered into the form.
     */
    @Nonnull
    public Optional<FormData> getFormData() {
        return currentFormDescriptor.map(formDescriptor -> {
            ImmutableList<FormFieldData> formFieldData = fieldPresenters.stream()
                                                                        .map(FormFieldPresenter::getValue)
                                                                        .collect(toImmutableList());
            return FormData.get(currentSubject, formDescriptor, formFieldData);
        });
    }

    public IsWidget getView() {
        return formView;
    }

    public boolean isDirty() {
        return fieldPresenters.stream()
                              .map(FormFieldPresenter::isDirty)
                              .findFirst()
                              .orElse(false);
    }

    public void requestFocus() {
        formView.requestFocus();
    }

    public void setFormDataChangedHandler(FormDataChangedHandler formDataChangedHandler) {
//        this.formDataChangedHandler = checkNotNull(formDataChangedHandler);
    }

    /**
     * Starts the form presenter.  The form will be placed into the specified container.
     *
     * @param container The container.
     */
    public void start(@Nonnull AcceptsOneWidget container) {
        this.container = Optional.of(container);
        container.setWidget(noFormView);
    }

    interface FormDataChangedHandler {

        void handleFormDataChanged();
    }
}
