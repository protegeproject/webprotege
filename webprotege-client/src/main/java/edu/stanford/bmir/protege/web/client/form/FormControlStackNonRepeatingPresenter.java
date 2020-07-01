package edu.stanford.bmir.protege.web.client.form;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.ImmutableList;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.RegionPageChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageRequest;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlDataDto;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.List;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

public class FormControlStackNonRepeatingPresenter implements FormControlStackPresenter {

    @Nonnull
    private final FormControl formControl;
    @Nonnull
    private final FormRegionPosition position;

    @Nonnull
    private final HandlerManager handlerManager = new HandlerManager(this);

    @Inject
    @AutoFactory
    public FormControlStackNonRepeatingPresenter(@Nonnull FormControl formControl,
                                                 @Nonnull FormRegionPosition position) {
        this.formControl = checkNotNull(formControl);
        this.position = checkNotNull(position);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        formControl.setPosition(position);
        container.setWidget(formControl);
    }

    public void clearValue() {
        formControl.clearValue();
    }

    public void setValue(@Nonnull Page<FormControlDataDto> page) {
        List<FormControlDataDto> value = page.getPageElements();
        if(value.size() == 1) {
            formControl.setValue(value.get(0));
        }
        else if(value.size() == 0) {
            formControl.clearValue();
        }
        else {
            throw new RuntimeException("Too many values set (" + value.size() + ") in Non-Repeating Form Control");
        }
    }

    @Nonnull
    public ImmutableList<FormControlData> getValue() {
        return formControl.getValue().map(ImmutableList::of).orElse(ImmutableList.of());
    }

    @Override
    public boolean isNonEmpty() {
        return formControl.getValue().isPresent();
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<FormControlData>> handler) {
        return handlerManager.addHandler(ValueChangeEvent.getType(), handler);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        handlerManager.fireEvent(event);
    }

    @Override
    public boolean isEnabled() {
        return formControl.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        formControl.setEnabled(enabled);
    }

    @Override
    public void requestFocus() {
        formControl.requestFocus();
    }

    @Override
    public void setPageCount(int pageCount) {
        // Nothing to do.  We don't have any pages
    }

    @Override
    public void setPageNumber(int pageNumber) {
        // Nothing to do.  We don't have any pages
    }

    @Override
    public int getPageNumber() {
        // Nothing to do.  We don't have any pages
        return 1;
    }

    @Override
    public void setPageNumberChangedHandler(PageNumberChangedHandler handler) {
        // Nothing to do.  We don't have any pages
    }

    @Nonnull
    @Override
    public ImmutableList<FormRegionPageRequest> getPageRequests(@Nonnull FormSubject formSubject, @Nonnull FormRegionId formRegionId) {
        return formControl.getPageRequests(formSubject, formRegionId);
    }

    @Override
    public void setRegionPageChangedHandler(RegionPageChangedHandler regionPageChangedHandler) {
        formControl.setRegionPageChangedHandler(regionPageChangedHandler);
    }

    @Override
    public int getPageSize() {
        return 1;
    }

    @Override
    public void forEachFormControl(@Nonnull Consumer<FormControl> formControlConsumer) {
        formControlConsumer.accept(formControl);
    }

    @Override
    public void setFormRegionFilterChangedHandler(@Nonnull FormRegionFilterChangedHandler handler) {
        formControl.setFormRegionFilterChangedHandler(handler);
    }
}
