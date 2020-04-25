package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-09
 *
 * Adapts a form presenter to a {@link FormControl}.
 */
public class SubFormControl implements FormControl {

    @Nonnull
    private final SimplePanel container = new SimplePanel();


    @Nonnull
    private final FormPresenter formPresenter;

    private final HandlerManager handlerManager = new HandlerManager(this);

    @Inject
    public SubFormControl(@Nonnull FormPresenter formPresenter) {
        this.formPresenter = checkNotNull(formPresenter);
    }

    public void start() {
        formPresenter.start(container);
        formPresenter.setFormDataChangedHandler(this::handleFormDataChanged);
        formPresenter.clear();
    }

    @Override
    public void requestFocus() {
        formPresenter.requestFocus();
    }

    private void handleFormDataChanged() {
        ValueChangeEvent.fire(this, getValue());
    }

    @Override
    public void setValue(FormControlData object) {
        clearValue();
        if(!(object instanceof FormData)) {
            return;
        }
        FormData formData = (FormData) object;
        formPresenter.displayForm(formData);
    }

    @Override
    public void clearValue() {
        formPresenter.clearData();
    }

    @Override
    public Optional<FormControlData> getValue() {
        return formPresenter.getFormData().map(fd -> fd);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<FormControlData>> handler) {
        return handlerManager.addHandler(ValueChangeEvent.getType(), handler);
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    @Override
    public boolean isDirty() {
        return formPresenter.isDirty();
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return handlerManager.addHandler(DirtyChangedEvent.TYPE, handler);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        handlerManager.fireEvent(event);
    }

    @Override
    public boolean isWellFormed() {
        return true;
    }

    @Override
    public void setEnabled(boolean enabled) {
        formPresenter.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return formPresenter.isEnabled();
    }
}
