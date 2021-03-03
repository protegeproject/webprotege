package edu.stanford.bmir.protege.web.client.crud;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.crud.gen.GeneratedAnnotationDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-02
 */
public class GeneratedAnnotationDescriptorValueEditor implements ValueEditor<GeneratedAnnotationDescriptor> {

    @Nonnull
    private final GeneratedAnnotationDescriptorPresenter presenter;

    @Nonnull
    private final HandlerManager handlerManager = new HandlerManager(this);

    private SimplePanel container = new SimplePanel();

    @Inject
    public GeneratedAnnotationDescriptorValueEditor(@Nonnull GeneratedAnnotationDescriptorPresenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    public void start() {
        presenter.start(container);
    }

    @Override
    public void setValue(GeneratedAnnotationDescriptor object) {
        presenter.setValue(object);
    }

    @Override
    public void clearValue() {

    }

    @Override
    public Optional<GeneratedAnnotationDescriptor> getValue() {
        return presenter.getValue();
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<GeneratedAnnotationDescriptor>> handler) {
        return handlerManager.addHandler(ValueChangeEvent.getType(), handler);
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    @Override
    public boolean isDirty() {
        return false;
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
        return getValue().isPresent();
    }
}
