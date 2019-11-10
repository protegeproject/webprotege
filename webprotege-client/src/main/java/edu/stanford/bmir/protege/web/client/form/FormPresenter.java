package edu.stanford.bmir.protege.web.client.form;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;
import edu.stanford.bmir.protege.web.shared.form.field.SubFormFieldDescriptor;

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

    @Nonnull
    private final FormView formView;

    @Nonnull
    private final NoFormView noFormView;

    @Nonnull
    private final Provider<FormElementView> formElementViewProvider;

    @Nonnull
    private final FormEditorFactory formEditorFactory;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    private final FormPresenterFactory formPresenterFactory;

    @Nonnull
    private Optional<FormDescriptor> currentFormDescriptor = Optional.empty();

    private boolean dirty;

    private Optional<AcceptsOneWidget> container = Optional.empty();


    @AutoFactory
    @Inject
    public FormPresenter(@Nonnull @Provided FormView formView,
                         @Nonnull @Provided NoFormView noFormView,
                         @Nonnull @Provided FormEditorFactory formEditorFactory,
                         @Nonnull @Provided Provider<FormElementView> formElementViewProvider,
                         @Nonnull @Provided DispatchServiceManager dispatchServiceManager,
                         @Nonnull FormPresenterFactory formPresenterFactory) {
        this.formView = checkNotNull(formView);
        this.noFormView = checkNotNull(noFormView);
        this.formElementViewProvider = checkNotNull(formElementViewProvider);
        this.formEditorFactory = checkNotNull(formEditorFactory);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.formPresenterFactory = formPresenterFactory;
    }

    public FormDescriptor getFormDescriptor() {
        return currentFormDescriptor.orElse(FormDescriptor.empty());
    }

    /**
     * Starts the form presenter.  The form will be placed into the specified container.
     * @param container The container.
     */
    public void start(@Nonnull AcceptsOneWidget container) {
        GWT.log("[FormPresenter] start");
        this.container = Optional.of(container);
    }


    /**
     * Displays the specified form and the specified form data.
     * @param formDescriptor The form to be displayed.
     * @param formData The form data to be shown in the form.
     */
    public void displayForm(@Nonnull FormDescriptor formDescriptor,
                            @Nonnull FormData formData) {
        GWT.log("[FormPresenter] Form data: " + formData);
        checkNotNull(formDescriptor);
        checkNotNull(formData);
        clearDirty();
        if (!currentFormDescriptor.equals(Optional.of(formDescriptor))) {
            GWT.log("[FormPresenter] New form descriptor.  Creating new form and setting data.");
            createFormAndSetFormData(formDescriptor, formData);
        }
        else {
            GWT.log("[FormPresenter] Existing form descriptor.  Setting data.");
            setFormData(formData);
        }
        if(formDescriptor.getElements().isEmpty()) {
            GWT.log("[FormPresenter] Form is empty");
            container.ifPresent(c -> c.setWidget(noFormView));
        }
        else {
            GWT.log("[FormPresenter] Container " + container.map(c -> c.getClass().getName()).orElse(""));
            GWT.log("[FormPresenter] FormView.elementViews " + formView.getElementViews().size());
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
    public FormData getFormData() {
        Map<FormElementId, FormDataValue> dataMap = new HashMap<>();
        for (FormElementView view : formView.getElementViews()) {
            view.getId().ifPresent(id -> view.getEditor().getValue().ifPresent(
                    v -> dataMap.put(id, v)
            ));
        }
        return new FormData(dataMap);
    }

    public void clearData() {
        clearDirty();
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
        GWT.log("[FormPresenter] setFormData: " + formData);
        dispatchServiceManager.beginBatch();
        formView.getElementViews().forEach(view -> {
            Optional<FormElementId> theId = view.getId();
            GWT.log("    [FormPresenter] FormElement: " + theId);
            if (theId.isPresent()) {
                FormElementId id = theId.get();
                Optional<FormDataValue> formElementData = formData.getFormElementData(id);
                GWT.log("    [FormPresenter] FormElementData: " + formElementData);
                if (formElementData.isPresent()) {
                    GWT.log("    [FormPresenter] Editor: " + view.getEditor().getClass().getName());
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
        FormElementEditor formElementEditor;
        if(elementDescriptor.isComposite()) {
            GWT.log("[FormPresenter] SubForm: " + elementDescriptor);
            SubFormFieldDescriptor subFormFieldDescriptor = (SubFormFieldDescriptor) elementDescriptor.getFieldDescriptor();
            GWT.log("[FormPresenter] Creating presenter for subform");
            FormPresenter subFormPresenter = formPresenterFactory.create(formPresenterFactory);
            FormDescriptor subFormDescriptor = subFormFieldDescriptor.getFormDescriptor();
            subFormPresenter.displayForm(subFormDescriptor,
                                         new FormData(new HashMap<>()));;
            FormPresenterAdapter subFormPresenterAdapter = new FormPresenterAdapter(subFormDescriptor, subFormPresenter);
            subFormPresenterAdapter.start();
            formElementEditor = subFormPresenterAdapter;
        }
        else {
            Optional<FormElementEditor> elementEditor = createFormElementEditor(elementDescriptor);
            if (!elementEditor.isPresent()) {
                return;
            }
            formElementEditor = elementEditor.get();
        }


        LocaleInfo localeInfo = LocaleInfo.getCurrentLocale();
        String langTag = localeInfo.getLocaleName();
        FormElementView elementView = formElementViewProvider.get();
        elementView.setId(elementDescriptor.getId());
        elementView.setFormLabel(elementDescriptor.getLabel().get(langTag));
        elementView.setEditor(formElementEditor);
        elementView.setRequired(elementDescriptor.getOptionality());
        Map<String, String> style = elementDescriptor.getStyle();
        style.forEach(elementView::addStylePropertyValue);
        // Update the required value missing display when the value changes
        formElementEditor.addValueChangeHandler(event -> {
            this.dirty = true;
            updateRequiredValuePresent(elementView);
        });
        if (formDataValue.isPresent()) {
            formElementEditor.setValue(formDataValue.get());
        }
        else {
            formElementEditor.clearValue();
        }
        updateRequiredValuePresent(elementView);
        formView.addFormElementView(elementView, elementDescriptor.getElementRun());
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
    private Optional<FormElementEditor> createFormElementEditor(@Nonnull FormElementDescriptor descriptor) {
        Optional<ValueEditorFactory<FormDataValue>> editorFactory = formEditorFactory.getValueEditorFactory(descriptor.getFieldDescriptor());
        return editorFactory.map(valueEditorFactory -> new FormElementEditorImpl(
                valueEditorFactory,
                descriptor.getRepeatability()
        ));
    }

    public void clear() {
        GWT.log("[FormPresenter] Clearing form view");
        formView.clear();
        currentFormDescriptor = Optional.empty();
        container.ifPresent(c -> c.setWidget(noFormView));
    }

    public IsWidget getView() {
        return formView;
    }
}
