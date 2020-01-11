package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;

import javax.annotation.Nonnull;
import javax.naming.directory.DirContext;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-11
 */
public class GridRowPresenterAdapter implements ValueEditor<GridRowPresenter> {

    @Nonnull
    private final SimplePanel holder = new SimplePanel();

    private final HandlerManager handlerManager = new HandlerManager(this);

    @Nonnull
    private Optional<GridRowPresenter> presenter = Optional.empty();

    @Override
    public void setValue(GridRowPresenter presenter) {
        this.presenter = Optional.of(presenter);
        presenter.start(holder);
    }

    @Override
    public void clearValue() {
        presenter.ifPresent(GridRowPresenter::clear);
    }

    @Override
    public Optional<GridRowPresenter> getValue() {
        return presenter;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<GridRowPresenter>> handler) {
        return handlerManager.addHandler(ValueChangeEvent.getType(), handler);
    }

    @Override
    public Widget asWidget() {
        return holder;
    }

    @Override
    public boolean isDirty() {
        return presenter.map(GridRowPresenter::isDirty).orElse(false);
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
        return presenter.isPresent();
    }
}
