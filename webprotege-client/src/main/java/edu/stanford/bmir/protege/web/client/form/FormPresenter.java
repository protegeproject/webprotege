package edu.stanford.bmir.protege.web.client.form;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;
import edu.stanford.bmir.protege.web.shared.form.field.SubFormControlDescriptor;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.form.field.Optionality.REQUIRED;

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
    private final Provider<FormElementView> formElementViewProvider;

    @Nonnull
    private final FormControlFactory formControlFactory;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    private final FormPresenterFactory formPresenterFactory;

    @Nonnull
    private Optional<FormDescriptor> currentFormDescriptor = Optional.empty();

    private boolean dirty;

    private Optional<AcceptsOneWidget> container = Optional.empty();

    @Nonnull
    private Optional<OWLEntity> currentSubject = Optional.empty();

    private FormDataChangedHandler formDataChangedHandler = () -> {};

    private EntityFormSubjectFactory freshSubjectStrategy = Optional::empty;

    @AutoFactory
    @Inject
    public FormPresenter(@Nonnull @Provided FormView formView,
                         @Nonnull @Provided NoFormView noFormView,
                         @Nonnull @Provided FormControlFactory formControlFactory,
                         @Nonnull @Provided Provider<FormElementView> formElementViewProvider,
                         @Nonnull @Provided DispatchServiceManager dispatchServiceManager,
                         @Nonnull FormPresenterFactory formPresenterFactory) {
        this.formView = checkNotNull(formView);
        this.noFormView = checkNotNull(noFormView);
        this.formElementViewProvider = checkNotNull(formElementViewProvider);
        this.formControlFactory = checkNotNull(formControlFactory);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.formPresenterFactory = formPresenterFactory;
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
     * @param formDescriptor The form to be displayed.
     * @param formData The form data to be shown in the form.
     */
    public void displayForm(@Nonnull FormDescriptor formDescriptor,
                            @Nonnull FormData formData) {
        checkNotNull(formDescriptor);
        checkNotNull(formData);
        clearDirty();
        if (!currentFormDescriptor.equals(Optional.of(formDescriptor))) {
            createFormAndSetFormData(formDescriptor, formData);
        }
        else {
            setFormData(formData);
        }
        if(formDescriptor.getElements().isEmpty()) {
            container.ifPresent(c -> c.setWidget(noFormView));
        }
        else {
            container.ifPresent(c -> c.setWidget(formView));
        }
        currentFormDescriptor = Optional.of(formDescriptor);
    }

    public boolean isDirty() {
        return dirty;
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
            Map<FormElementId, FormDataValue> dataMap = new HashMap<>();
            for (FormElementView view : formView.getElementViews()) {
                view.getId().ifPresent(id -> view.getEditor().getValue().ifPresent(
                        v -> dataMap.put(id, v)
                ));
            }
            return new FormData(currentSubject.orElse(null), dataMap, formDescriptor);
        });
    }

    public void clearData() {
        clearDirty();
        currentSubject = Optional.empty();
        for(FormElementView view : formView.getElementViews()) {
            view.getEditor().clearValue();
            updateRequiredValuePresent(view);
        }
    }

    /**
     * Creates the form from scratch and fills in the specified form data.
     *
     * @param formDescriptor The descriptor that describes the form.
     * @param formData       The form data to be filled into the form.
     */
    private void createFormAndSetFormData(@Nonnull FormDescriptor formDescriptor,
                                          @Nonnull FormData formData) {
        formView.clear();
        dispatchServiceManager.beginBatch();
        for (FormElementDescriptor elementDescriptor : formDescriptor.getElements()) {
            Optional<FormDataValue> dataValue = formData.getFormElementData(elementDescriptor.getId());
            addFormElement(elementDescriptor, dataValue);
        }
        dispatchServiceManager.executeCurrentBatch();
    }

    private void clearDirty() {
        this.dirty = false;
    }

    private void setFormData(@Nonnull FormData formData) {
        this.currentSubject = formData.getSubject();
        dispatchServiceManager.beginBatch();
        formView.getElementViews().forEach(view -> {
            Optional<FormElementId> theId = view.getId();
            if (theId.isPresent()) {
                FormElementId id = theId.get();
                Optional<FormDataValue> formElementData = formData.getFormElementData(id);
                if (formElementData.isPresent()) {
                    view.getEditor().setValue(formElementData.get());
                }
                else {
                    view.getEditor().clearValue();
                }
                updateRequiredValuePresent(view);
            }
            else {
                view.getEditor().clearValue();
            }
        });
        dispatchServiceManager.executeCurrentBatch();
    }

    private void addFormElement(@Nonnull FormElementDescriptor elementDescriptor,
                                @Nonnull Optional<FormDataValue> formDataValue) {
        FormControl formControl;
        if(elementDescriptor.isComposite()) {
            formControl = createSubFormElement(elementDescriptor);
        }
        else {
            Optional<FormControl> elementEditor = createFormElementEditor(elementDescriptor);
            if (!elementEditor.isPresent()) {
                return;
            }
            formControl = elementEditor.get();
        }


        LocaleInfo localeInfo = LocaleInfo.getCurrentLocale();
        String langTag = localeInfo.getLocaleName();
        FormElementView elementView = formElementViewProvider.get();
        elementView.setId(elementDescriptor.getId());
        elementView.setFormLabel(elementDescriptor.getLabel().get(langTag));
        elementView.setEditor(formControl);
        elementView.setRequired(elementDescriptor.getOptionality());
        Map<String, String> style = elementDescriptor.getStyle();
        style.forEach(elementView::addStylePropertyValue);
        // Update the required value missing display when the value changes
        formControl.addValueChangeHandler(event -> {
            this.dirty = true;
            updateRequiredValuePresent(elementView);
            formDataChangedHandler.handleFormDataChanged();
        });
        if (formDataValue.isPresent()) {
            formControl.setValue(formDataValue.get());
        }
        else {
            formControl.clearValue();
        }
        updateRequiredValuePresent(elementView);
        formView.addFormElementView(elementView, elementDescriptor.getElementRun());
    }

    private FormControl createSubFormElement(@Nonnull FormElementDescriptor elementDescriptor) {
        return new FormControlImpl(() -> {
            SubFormControlDescriptor subFormFieldDescriptor = (SubFormControlDescriptor) elementDescriptor.getFormControlDescriptor();

            FormPresenter subFormPresenter = formPresenterFactory.create(formPresenterFactory);
            FormDescriptor subFormDescriptor = subFormFieldDescriptor.getFormDescriptor();
            subFormPresenter.displayForm(subFormDescriptor,
                                         new FormData(null, new HashMap<>(), subFormDescriptor));
            SubFormControl subSubFormControl = new SubFormControl(subFormDescriptor, subFormPresenter);
            subSubFormControl.start();
            return subSubFormControl;
        }, elementDescriptor.getRepeatability());
    }


    /**
     * Updates the specified view so that there is a visual indication if the value is required but not present.
     *
     * @param elementView The element view.
     */
    private void updateRequiredValuePresent(@Nonnull FormElementView elementView) {
        if (elementView.getRequired() == REQUIRED) {
            Optional<FormDataValue> val = elementView.getEditor().getValue();
            if (val.isPresent()) {
                elementView.setRequiredValueNotPresentVisible(false);
            }
            else {
                elementView.setRequiredValueNotPresentVisible(true);
            }
        }
    }

    /**
     * Creates an editor for the form element identified by the specified descriptor.
     *
     * @param descriptor The form element descriptor.
     * @return An editor for the form element described by the descriptor.
     */
    @Nonnull
    private Optional<FormControl> createFormElementEditor(@Nonnull FormElementDescriptor descriptor) {
        Optional<ValueEditorFactory<FormDataValue>> editorFactory = formControlFactory.getValueEditorFactory(descriptor.getFormControlDescriptor());
        return editorFactory.map(valueEditorFactory -> new FormControlImpl(
                valueEditorFactory,
                descriptor.getRepeatability()
        ));
    }

    public void clear() {
        formView.clear();
        currentFormDescriptor = Optional.empty();
        container.ifPresent(c -> c.setWidget(noFormView));
    }

    public IsWidget getView() {
        return formView;
    }
}
