package edu.stanford.bmir.protege.web.client.frame;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.gwtcodemirror.client.AutoCompletionHandler;
import edu.stanford.bmir.gwtcodemirror.client.EditorPosition;
import edu.stanford.bmir.gwtcodemirror.client.GWTCodeMirror;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.frame.ManchesterSyntaxFrameParseError;

import javax.inject.Inject;


/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class ManchesterSyntaxFrameEditorImpl extends Composite implements ManchesterSyntaxFrameEditor {

    interface ManchesterSyntaxFrameEditorImplUiBinder extends UiBinder<HTMLPanel, ManchesterSyntaxFrameEditorImpl> {
    }

    private static ManchesterSyntaxFrameEditorImplUiBinder ourUiBinder = GWT.create(ManchesterSyntaxFrameEditorImplUiBinder.class);

    @UiField(provided = true)
    protected GWTCodeMirror editor;

    @UiField
    protected ManchesterSyntaxFrameEditorErrorMessageViewImpl errorMessageView;

    @UiField
    protected ApplyChangesActionView applyChangesActionView;

    private boolean dirty = false;

    @Inject
    public ManchesterSyntaxFrameEditorImpl() {
        editor = new GWTCodeMirror();
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        errorMessageView.setVisible(false);
        applyChangesActionView.setVisible(false);
        editor.setLineWrapping(true);
    }

    public void setEnabled(boolean enabled) {
        editor.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return editor.isEnabled();
    }

    @UiHandler("editor")
    protected void handleTextChanged(ValueChangeEvent<String> event) {
        dirty = true;
        fireEvent(new DirtyChangedEvent());
        ValueChangeEvent.fire(this, getValue());
    }

    private boolean isArrow(KeyUpEvent event) {
        return event.isDownArrow() || event.isUpArrow() || event.isLeftArrow() || event.isRightArrow();
    }

    @Override
    public void setValue(String object) {
        dirty = false;
        editor.setValue(object);
//        editor.reindentLines();
    }

    @Override
    public void clearValue() {
        dirty = false;
//        editor.setValue("");
    }

    @Override
    public Optional<String> getValue() {
        return Optional.of(editor.getValue());
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<String>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isWellFormed() {
        return true;
    }

    @Override
    public void clearError() {
        hideErrorMessageView();
        editor.clearErrorRange();
    }

    @Override
    public void setError(ManchesterSyntaxFrameParseError error) {
        int line = error.getLine() - 1;
        int col = error.getCol();
        editor.setErrorRange(new EditorPosition(line, col), new EditorPosition(line, col + error.getToken().length()));
        errorMessageView.setErrorMessage(error.getMessage());
        errorMessageView.setCurrentToken(error.getToken());
        errorMessageView.setExpectedEntityTypes(error.getExpectedEntityTypes());
        showErrorMessageView();
    }

    @Override
    public void setCreateAsEntityTypeHandler(CreateAsEntityTypeHandler handler) {
        errorMessageView.setCreateAsEntityTypeHandler(handler);
    }

    @Override
    public void setAutoCompletionHandler(AutoCompletionHandler autoCompletionHandler) {
        editor.setAutoCompletionHandler(autoCompletionHandler);
    }

    @Override
    public void setApplyChangesViewVisible(boolean visible) {
        applyChangesActionView.setVisible(visible);
    }

    @Override
    public void setApplyChangesHandler(ApplyChangesActionHandler handler) {
        applyChangesActionView.setHandler(handler);
    }

    private void showErrorMessageView() {
        errorMessageView.setVisible(true);
        Element errorMessageElement = errorMessageView.getElement();
        Element editorElement = editor.getElement();
        editorElement.getStyle().setBottom(errorMessageElement.getOffsetHeight() + 5, Style.Unit.PX);
    }

    private void hideErrorMessageView() {
        errorMessageView.setVisible(false);
        editor.getElement().getStyle().setBottom(0, Style.Unit.PX);
    }
}
