package edu.stanford.bmir.protege.web.client.form;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableList;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.data.FormFieldData;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    interface FormDataChangedHandler {
        void handleFormDataChanged();
    }

    @Nonnull
    private final FormView formView;

    @Nonnull
    private final NoFormView noFormView;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private Optional<FormDescriptor> currentFormDescriptor = Optional.empty();

    private Optional<AcceptsOneWidget> container = Optional.empty();

    @Nonnull
    private Optional<OWLEntity> currentSubject = Optional.empty();

    private FormDataChangedHandler formDataChangedHandler = () -> {};

    private EntityFormSubjectFactory freshSubjectStrategy = Optional::empty;

    private FormFieldPresenterFactory formFieldPresenterFactory;

    private List<FormFieldPresenter> fieldPresenters = new ArrayList<>();

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

    /**
     * Starts the form presenter.  The form will be placed into the specified container.
     * @param container The container.
     */
    public void start(@Nonnull AcceptsOneWidget container) {
        this.container = Optional.of(container);
        container.setWidget(noFormView);
    }

    public void setFormDataChangedHandler(FormDataChangedHandler formDataChangedHandler) {
        this.formDataChangedHandler = checkNotNull(formDataChangedHandler);
    }

    public void requestFocus() {
        formView.requestFocus();
    }

    /**
     * Displays the specified form and the specified form data.
     * @param formData The form data to be shown in the form.
     */
    public void displayForm(@Nonnull FormData formData) {
        checkNotNull(formData);
//        if (!currentFormDescriptor.equals(Optional.of(formData.getFormDescriptor()))) {
            createFormAndSetFormData(formData);
//        }
//        else {
//            setFormData(formData);
//        }
        if(formData.getFormDescriptor().getFields().isEmpty()) {
            container.ifPresent(c -> c.setWidget(noFormView));
        }
        else {
            container.ifPresent(c -> c.setWidget(formView));
        }
        currentFormDescriptor = Optional.of(formData.getFormDescriptor());
    }

    public boolean isDirty() {
        return fieldPresenters.stream()
                .map(FormFieldPresenter::isDirty)
                .findFirst()
                .orElse(false);
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

    public void clearData() {
        currentSubject = Optional.empty();
        fieldPresenters.forEach(FormFieldPresenter::clearValue);
    }

    /**
     * Creates the form from scratch and fills in the specified form data.
     *
     * @param formData       The form data to be filled into the form.
     */
    private void createFormAndSetFormData(@Nonnull FormData formData) {
        clear();
        currentFormDescriptor = Optional.of(formData.getFormDescriptor());
        dispatchServiceManager.beginBatch();
        for (FormFieldData fieldData : formData.getFormFieldData()) {
            addFormField(fieldData);
        }
        dispatchServiceManager.executeCurrentBatch();
    }


    private void setFormData(@Nonnull FormData formData) {
//        this.currentSubject = formData.getSubject();
//        dispatchServiceManager.beginBatch();
//        formView.getFieldViews().forEach(view -> {
//            Optional<FormFieldId> theId = view.getId();
//            if (theId.isPresent()) {
//                FormFieldId id = theId.get();
//                Optional<FormDataValue> formElementData = formData.getFormElementData(id);
//                if (formElementData.isPresent()) {
//                    view.getEditor().setValue(formElementData.get());
//                }
//                else {
//                    view.getEditor().clearValue();
//                }
//                updateRequiredValuePresent(view);
//            }
//            else {
//                view.getEditor().clearValue();
//            }
//        });
//        dispatchServiceManager.executeCurrentBatch();
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

    private FormControl createSubFormElement(@Nonnull FormFieldDescriptor elementDescriptor) {
//        return new FormFieldControlImpl(() -> {
//            SubFormControlDescriptor subFormFieldDescriptor = (SubFormControlDescriptor) elementDescriptor.getFormControlDescriptor();
//
//            FormPresenter subFormPresenter = formPresenterFactory.create(formPresenterFactory);
//            FormDescriptor subFormDescriptor = subFormFieldDescriptor.getFormDescriptor();
//            subFormPresenter.displayForm(subFormDescriptor,
//                                         new FormData(null, new HashMap<>(), subFormDescriptor));
//            SubFormControl subSubFormControl = new SubFormControl(subFormDescriptor, subFormPresenter);
//            subSubFormControl.start();
//            return subSubFormControl;
//        }, elementDescriptor.getRepeatability());
        throw new RuntimeException();
    }




    public void clear() {
        formView.clear();
        fieldPresenters.clear();
        currentFormDescriptor = Optional.empty();
        container.ifPresent(c -> c.setWidget(noFormView));
    }

    public IsWidget getView() {
        return formView;
    }
}
