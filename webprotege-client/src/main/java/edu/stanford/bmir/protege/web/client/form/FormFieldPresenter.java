package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageRequest;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.FormFieldData;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionPresenter;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.form.field.Optionality.REQUIRED;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-08
 */
public class FormFieldPresenter implements FormRegionPresenter {

    private boolean enabled = true;

    private void handleFormControlValueChanged(ValueChangeEvent<Optional<List<FormControlData>>> event) {
        updateRequiredValuePresent();
    }

    enum ExpansionState {
        EXPANDED,
        COLLAPSED
    }

    @Nonnull
    private final FormFieldView view;

    @Nonnull
    private final FormFieldDescriptor formFieldDescriptor;

    private FormControlStack controlStack;

    private ExpansionState expansionState = ExpansionState.EXPANDED;

    private Optional<FormFieldData> currentValue = Optional.empty();

    private final FormFieldControlStackFactory formFieldControlStackFactory;

    @Nonnull
    private final LanguageMapCurrentLocaleMapper languageMapCurrentLocaleMapper;

    public FormFieldPresenter(@Nonnull FormFieldView view,
                              @Nonnull FormFieldDescriptor formFieldDescriptor,
                              @Nonnull FormFieldControlStackFactory formFieldControlStackFactory,
                              @Nonnull LanguageMapCurrentLocaleMapper languageMapCurrentLocaleMapper) {
        this.view = checkNotNull(view);
        this.formFieldDescriptor = checkNotNull(formFieldDescriptor);
        this.formFieldControlStackFactory = checkNotNull(formFieldControlStackFactory);
        this.languageMapCurrentLocaleMapper = checkNotNull(languageMapCurrentLocaleMapper);
    }

    @Nonnull
    @Override
    public FormRegionId getFormRegionId() {
        return formFieldDescriptor.getId();
    }

    @Nonnull
    public FormFieldView getFormFieldView() {
        return view;
    }

    public boolean isDirty() {
        return controlStack.isDirty();
    }


    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        controlStack.setEnabled(enabled);
    }

    protected FormFieldView start() {
        FormControlDescriptor formControlDescriptor = formFieldDescriptor.getFormControlDescriptor();
        controlStack = formFieldControlStackFactory.create(formControlDescriptor,
                                                           formFieldDescriptor.getRepeatability(),
                                                           FormRegionPosition.TOP_LEVEL);
        controlStack.setEnabled(enabled);
        view.setId(formFieldDescriptor.getId());
        view.setFormLabel(languageMapCurrentLocaleMapper.getValueForCurrentLocale(formFieldDescriptor.getLabel()));
        view.setEditor(controlStack);
        view.setRequired(formFieldDescriptor.getOptionality());
        view.setHelpText(languageMapCurrentLocaleMapper.getValueForCurrentLocale(formFieldDescriptor.getHelp()));
        Map<String, String> style = formFieldDescriptor.getStyle();
        style.forEach(view::addStylePropertyValue);
        view.setHeaderClickedHandler(this::toggleExpansionState);

        // Update the required value missing display when the value changes
        controlStack.addValueChangeHandler(this::handleFormControlValueChanged);


        updateRequiredValuePresent();
        return view;
    }

    @Nonnull
    public ExpansionState getExpansionState() {
        return expansionState;
    }

    public void toggleExpansionState() {
        if(expansionState == ExpansionState.EXPANDED) {
            setExpansionState(ExpansionState.COLLAPSED);
        }
        else {
            setExpansionState(ExpansionState.EXPANDED);
        }
    }

    public void setExpansionState(ExpansionState expansionState) {
        this.expansionState = expansionState;
        if(expansionState == ExpansionState.EXPANDED) {
            view.expand();
        }
        else {
            view.collapse();
        }

    }

    @Nonnull
    public ImmutableList<FormRegionPageRequest> getPageRequests(@Nonnull FormSubject formSubject) {
        return controlStack.getPageRequests(formSubject, formFieldDescriptor.getId());
    }

    public FormFieldData getValue() {
        if(controlStack == null) {
            return FormFieldData.get(formFieldDescriptor, Page.emptyPage());
        }
        ImmutableList<FormControlData> formControlData = controlStack.getEditorValue()
                                                                     .map(ImmutableList::copyOf)
                                                                     .orElse(ImmutableList.of());

        Page<FormControlData> controlDataPage = new Page<>(1, 1, formControlData, formControlData.size());
        return FormFieldData.get(formFieldDescriptor, controlDataPage);
    }

    public void setValue(@Nonnull FormFieldData formFieldData) {
        checkNotNull(formFieldData);
        if(currentValue.equals(Optional.of(formFieldData))) {
            GWT.log("[FormFieldPresenter] (setValue) "+formFieldData.getFormFieldDescriptor().getId()+" Skipping setValue because current data is the same");
            return;
        }
        else {
            GWT.log("[FormFieldPresenter] (setValue) "+formFieldData.getFormFieldDescriptor().getId()+" Value is new");
        }
        currentValue = Optional.of(formFieldData);
        if(controlStack == null) {
            return;
        }
        if(!formFieldData.getFormFieldDescriptor().equals(formFieldDescriptor)) {
            throw new RuntimeException("FormFieldDescriptor mismatch for field: " + formFieldDescriptor.getId());
        }
        Page<FormControlData> page = formFieldData.getFormControlData();
        controlStack.setValue(page.getPageElements());
        controlStack.setPageCount(page.getPageCount());
        controlStack.setPageNumber(page.getPageNumber());
        updateRequiredValuePresent();
    }

    public void clearValue() {
        currentValue = Optional.empty();
        if(controlStack == null) {
            return;
        }
        controlStack.clearValue();
        updateRequiredValuePresent();
    }

    /**
     * Updates the specified view so that there is a visual indication if the value is required but not present.
     */
    private void updateRequiredValuePresent() {
        if(controlStack == null) {
            view.setRequiredValueNotPresentVisible(false);
            return;
        }
        if (formFieldDescriptor.getOptionality() == REQUIRED) {
            boolean requiredValueNotPresent = !controlStack.getValue().isPresent();
            view.setRequiredValueNotPresentVisible(requiredValueNotPresent);
        }
        else {
            view.setRequiredValueNotPresentVisible(false);
        }
    }

    public void setFormRegionPageChangedHandler(FormRegionPageChangedHandler formRegionPageChangedHandler) {
        controlStack.setFormRegionPageChangedHandler(formRegionPageChangedHandler);
    }

}
