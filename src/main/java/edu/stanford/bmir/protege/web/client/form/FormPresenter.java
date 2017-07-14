package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.form.field.Required.REQUIRED;

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
    private final Provider<FormElementView> formElementViewProvider;

    @Nonnull
    private final FormEditorFactory formEditorFactory;

    @Nonnull
    private Optional<FormDescriptor> currentFormDescriptor = Optional.empty();


    @Inject
    public FormPresenter(@Nonnull FormView formView,
                         @Nonnull FormEditorFactory formEditorFactory,
                         @Nonnull Provider<FormElementView> formElementViewProvider) {
        this.formView = checkNotNull(formView);
        this.formElementViewProvider = checkNotNull(formElementViewProvider);
        this.formEditorFactory = checkNotNull(formEditorFactory);
    }

    /**
     * Starts the form presenter.  The form will be placed into the specified container.
     * @param container The container.
     */
    public void start(@Nonnull AcceptsOneWidget container) {
        ScrollPanel sp = new ScrollPanel(formView.asWidget());
        container.setWidget(sp);
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
        if (!currentFormDescriptor.equals(Optional.of(formDescriptor))) {
            createFormAndSetFormData(formDescriptor, formData);
        }
        else {
            setFormData(formData);
        }
        currentFormDescriptor = Optional.of(formDescriptor);
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

    /**
     * Creates the form from scratch and fills in the specified form data.
     *
     * @param formDescriptor The descriptor that describes the form.
     * @param formData       The form data to be filled into the form.
     */
    private void createFormAndSetFormData(@Nonnull FormDescriptor formDescriptor,
                                          @Nonnull FormData formData) {
        formView.clear();
        for (FormElementDescriptor elementDescriptor : formDescriptor.getElements()) {
            Optional<FormDataValue> dataValue = formData.getFormElementData(elementDescriptor.getId());
            createFormEditor(elementDescriptor, dataValue);
        }
    }

    private void setFormData(@Nonnull FormData formData) {
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
    }

    private void createFormEditor(@Nonnull FormElementDescriptor elementDescriptor,
                                  @Nonnull Optional<FormDataValue> formData) {
        Optional<FormElementEditor> elementEditor = createFormElementEditor(elementDescriptor);
        if (!elementEditor.isPresent()) {
            return;
        }
        FormElementEditor editor = elementEditor.get();

        FormElementView elementView = formElementViewProvider.get();
        elementView.setId(elementDescriptor.getId());
        elementView.setFormLabel(elementDescriptor.getLabel());
        elementView.setEditor(editor);
        elementView.setRequired(elementDescriptor.getRequired());
        // Update the required value missing display when the value changes
        editor.addValueChangeHandler(event -> {
            updateRequiredValuePresent(elementView);
        });
        if (formData.isPresent()) {
            editor.setValue(formData.get());
        }
        else {
            editor.clearValue();
        }
        updateRequiredValuePresent(elementView);
        formView.addFormElementView(elementView);
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
        formView.clear();
        currentFormDescriptor = Optional.empty();
    }

    public IsWidget getView() {
        return formView;
    }
}
