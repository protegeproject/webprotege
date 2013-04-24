package edu.stanford.bmir.protege.web.client.ui.editor;


import com.google.gwt.event.shared.GwtEvent;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class EditorContextChangedEvent extends GwtEvent<EditorContextChangedHandler> {

    public static final GwtEvent.Type<EditorContextChangedHandler> TYPE = new GwtEvent.Type<EditorContextChangedHandler>();

    private EditorCtx editorCtx;

    private String contextDescription;

    public EditorContextChangedEvent(EditorCtx editorCtx, String contextDescription) {
        this.editorCtx = editorCtx;
        this.contextDescription = contextDescription;
    }

    public EditorCtx getEditorCtx() {
        return editorCtx;
    }

    public String getContextDescription() {
        return contextDescription;
    }

    @Override
    public Type<EditorContextChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(EditorContextChangedHandler handler) {
        handler.handleEditorContextChanged(this);
    }
}
