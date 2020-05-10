package edu.stanford.bmir.protege.web.client.form;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageRequest;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionPresenter;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;

import java.util.List;
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

    private void handleFormControlValueChanged(ValueChangeEvent<List<FormControlData>> event) {
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

    private ExpansionState expansionState = ExpansionState.EXPANDED;

    private Optional<FormFieldDataDto> currentValue = Optional.empty();

    private final FormControlStackPresenter stackPresenter;

    @Nonnull
    private final LanguageMapCurrentLocaleMapper languageMapCurrentLocaleMapper;

    public FormFieldPresenter(@Nonnull FormFieldView view,
                              @Nonnull FormFieldDescriptor formFieldDescriptor,
                              @Nonnull FormControlStackPresenter formControlStackPresenter,
                              @Nonnull LanguageMapCurrentLocaleMapper languageMapCurrentLocaleMapper) {
        this.view = checkNotNull(view);
        this.formFieldDescriptor = checkNotNull(formFieldDescriptor);
        this.stackPresenter = checkNotNull(formControlStackPresenter);
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

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        stackPresenter.setEnabled(enabled);
    }

    protected FormFieldView start() {
        stackPresenter.setEnabled(enabled);
        view.setId(formFieldDescriptor.getId());
        view.setFormLabel(languageMapCurrentLocaleMapper.getValueForCurrentLocale(formFieldDescriptor.getLabel()));
        view.setRequired(formFieldDescriptor.getOptionality());
        view.setHelpText(languageMapCurrentLocaleMapper.getValueForCurrentLocale(formFieldDescriptor.getHelp()));
        stackPresenter.start(view.getFormStackContainer());
        view.setHeaderClickedHandler(this::toggleExpansionState);

        // Update the required value missing display when the value changes
        stackPresenter.addValueChangeHandler(this::handleFormControlValueChanged);

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
        return stackPresenter.getPageRequests(formSubject, formFieldDescriptor.getId());
    }

    public FormFieldData getValue() {
        if(stackPresenter == null) {
            return FormFieldData.get(formFieldDescriptor, Page.emptyPage());
        }
        ImmutableList<FormControlData> formControlData = stackPresenter.getValue();

        Page<FormControlData> controlDataPage = new Page<>(1, 1, formControlData, formControlData.size());
        return FormFieldData.get(formFieldDescriptor, controlDataPage);
    }

    public void setValue(@Nonnull FormFieldDataDto formFieldData) {
        checkNotNull(formFieldData);
        if(currentValue.equals(Optional.of(formFieldData))) {
            GWT.log("[FormFieldPresenter] (setValue) "+formFieldData.getFormFieldDescriptor().getId()+" Skipping setValue because current data is the same");
            return;
        }
        else {
            GWT.log("[FormFieldPresenter] (setValue) "+formFieldData.getFormFieldDescriptor().getId()+" Value is new");
        }
        currentValue = Optional.of(formFieldData);
        if(!formFieldData.getFormFieldDescriptor().equals(formFieldDescriptor)) {
            throw new RuntimeException("FormFieldDescriptor mismatch for field: " + formFieldDescriptor.getId());
        }
        Page<FormControlDataDto> page = formFieldData.getFormControlData();
        stackPresenter.setValue(page.getPageElements());
        stackPresenter.setPageCount(page.getPageCount());
        stackPresenter.setPageNumber(page.getPageNumber());
        updateRequiredValuePresent();
    }

    public void clearValue() {
        currentValue = Optional.empty();
        stackPresenter.clearValue();
        updateRequiredValuePresent();
    }

    /**
     * Updates the specified view so that there is a visual indication if the value is required but not present.
     */
    private void updateRequiredValuePresent() {
        if (formFieldDescriptor.getOptionality() == REQUIRED) {
            boolean requiredValueNotPresent = !stackPresenter.isNonEmpty();
            view.setRequiredValueNotPresentVisible(requiredValueNotPresent);
        }
        else {
            view.setRequiredValueNotPresentVisible(false);
        }
    }

    public void setFormRegionPageChangedHandler(FormRegionPageChangedHandler formRegionPageChangedHandler) {
        stackPresenter.setFormRegionPageChangedHandler(formRegionPageChangedHandler);
    }

}
