package edu.stanford.bmir.protege.web.client.form;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.form.ExpansionState;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormPageRequest;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldId;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionOrdering;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Stream;

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
    private Optional<FormSubjectDto> currentSubject = Optional.empty();

    private FormFieldPresenterFactory formFieldPresenterFactory;

    private Set<FormFieldId> collapsedFields = new HashSet<>();

    private FormRegionPageChangedHandler formRegionPageChangedHandler = () -> {};

    private boolean enabled = true;

    @Nonnull
    private GridOrderByChangedHandler orderByChangedHandler = () -> {};

    @AutoFactory
    @Inject
    public FormPresenter(@Nonnull @Provided FormView formView,
                         @Nonnull @Provided NoFormView noFormView,
                         @Nonnull @Provided DispatchServiceManager dispatchServiceManager,
                         @Nonnull FormFieldPresenterFactory formFieldPresenterFactory) {
        this.formView = checkNotNull(formView);
        this.noFormView = checkNotNull(noFormView);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.formFieldPresenterFactory = formFieldPresenterFactory;
    }

    public void clearData() {
        currentSubject = Optional.empty();
        fieldPresenters.forEach(FormFieldPresenter::clearValue);
    }

    public void collapseAll() {
        fieldPresenters.forEach(p -> p.setExpansionState(ExpansionState.COLLAPSED));
    }

    /**
     * Displays the specified form and the specified form data.
     *
     * @param formData The form data to be shown in the form.
     */
    public void displayForm(@Nonnull FormDataDto formData) {
        checkNotNull(formData);
        saveExpansionState();
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

    public boolean isEnabled() {
        return enabled;
    }

    public void saveExpansionState() {
        collapsedFields.clear();
        fieldPresenters.forEach(p -> {
            if(p.getExpansionState() == ExpansionState.COLLAPSED) {
                FormFieldId id = p.getValue()
                                  .getFormFieldDescriptor()
                                  .getId();
                collapsedFields.add(id);
            }
        });
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        fieldPresenters.forEach(formFieldPresenter -> formFieldPresenter.setEnabled(enabled));
    }

    public void setGridOrderByChangedHandler(GridOrderByChangedHandler handler) {
        this.orderByChangedHandler = checkNotNull(handler);
    }

    public void setFormRegionPageChangedHandler(FormRegionPageChangedHandler handler) {
        this.formRegionPageChangedHandler = checkNotNull(handler);
        fieldPresenters.forEach(formFieldPresenter -> formFieldPresenter.setFormRegionPageChangedHandler(handler));
    }

    private void updateFormData(@Nonnull FormDataDto formData) {
        GWT.log("[FormPresenter] Updating form data");
        this.currentSubject = formData.getSubject();
        dispatchServiceManager.beginBatch();
        ImmutableList<FormFieldDataDto> nextFormFieldData = formData.getFormFieldData();
        for(int i = 0; i < nextFormFieldData.size(); i++) {
            FormFieldDataDto fieldData = nextFormFieldData.get(i);
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
    private void createFormAndSetFormData(@Nonnull FormDataDto formData) {
        clear();
        FormDescriptor formDescriptor = formData.getFormDescriptor();
        currentFormDescriptor = Optional.of(formDescriptor);
        dispatchServiceManager.beginBatch();
        for(FormFieldDataDto fieldData : formData.getFormFieldData()) {
            addFormField(fieldData);
        }
        dispatchServiceManager.executeCurrentBatch();
    }

    public void clear() {
        saveExpansionState();
        fieldPresenters.clear();
        formView.clear();
        currentFormDescriptor = Optional.empty();
        container.ifPresent(c -> c.setWidget(noFormView));
    }

    private void addFormField(@Nonnull FormFieldDataDto formFieldData) {
        FormFieldDescriptor formFieldDescriptor = formFieldData.getFormFieldDescriptor();
        if(formFieldDescriptor.getInitialExpansionState().equals(ExpansionState.COLLAPSED)) {
            collapsedFields.add(formFieldData.getFormFieldDescriptor().getId());
        }
        FormFieldPresenter presenter = formFieldPresenterFactory.create(formFieldDescriptor);
        presenter.setEnabled(enabled);
        presenter.setFormRegionPageChangedHandler(formRegionPageChangedHandler);
        presenter.start();
        presenter.setGridOrderByChangedHandler(orderByChangedHandler);
        fieldPresenters.add(presenter);
        if(collapsedFields.contains(formFieldData.getFormFieldDescriptor()
                                                 .getId())) {
            presenter.setExpansionState(ExpansionState.COLLAPSED);
        }
        // TODO : Change handler
        presenter.setValue(formFieldData);
        FormFieldView formFieldView = presenter.getFormFieldView();
        formView.addFormElementView(formFieldView, formFieldDescriptor.getFieldRun());
    }

    public void expandAll() {
        fieldPresenters.forEach(p -> p.setExpansionState(ExpansionState.EXPANDED));
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
            return FormData.get(currentSubject.map(FormSubjectDto::toFormSubject),
                                formDescriptor,
                                formFieldData);
        });
    }

    @Nonnull
    public ImmutableList<FormPageRequest> getPageRequest() {
        return currentFormDescriptor.map(formDescriptor
                                          -> currentSubject.map(subject
                                                                        -> fieldPresenters.stream()
                                                                                                 .map(formFieldPresenter -> formFieldPresenter.getPageRequests(subject.toFormSubject()))
                                                                                                 .flatMap(ImmutableList::stream)
                                                                                                 .map(pr -> FormPageRequest.get(
                                                                                                         formDescriptor.getFormId(),
                                                                                                         subject.toFormSubject(),
                                                                                                         pr.getFieldId(),
                                                                                                         pr.getSourceType(),
                                                                                                         pr.getPageRequest()))
                                                                                                 .collect(toImmutableList())).orElse(ImmutableList.of())).orElse(ImmutableList.of());
    }

    public IsWidget getView() {
        return formView;
    }

    public void requestFocus() {
        formView.requestFocus();
    }

    public void setFormDataChangedHandler(FormDataChangedHandler formDataChangedHandler) {
        //        this.formDataChangedHandler = checkNotNull(formDataChangedHandler);
    }

    /**
     * Starts the form presenter.  The form will be placed into the specified listContainer.
     *
     * @param container The listContainer.
     */
    public void start(@Nonnull AcceptsOneWidget container) {
        this.container = Optional.of(container);
        container.setWidget(noFormView);
    }

    public Stream<FormRegionOrdering> getOrderings() {
        return fieldPresenters.stream()
                .flatMap(FormFieldPresenter::getOrderings);
    }

    interface FormDataChangedHandler {

        void handleFormDataChanged();
    }

}
