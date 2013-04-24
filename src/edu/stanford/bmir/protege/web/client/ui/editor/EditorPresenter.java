package edu.stanford.bmir.protege.web.client.ui.editor;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.project.ProjectManager;
import edu.stanford.bmir.protege.web.client.rpc.AbstractWebProtegeAsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.EmptySuccessWebProtegeCallback;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectAction;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class EditorPresenter {

    private static final Label NOTHING_SELECTED_WIDGET = new Label("Nothing selected");

    private static final Label LOADING_INDICATOR_WIDGET = new Label("Loading...");

    private EditorContextMapper editorContextMapper;



    private SimplePanel editorHolder = new SimplePanel();

    private Optional<EditorState<?, ?>> editorState = Optional.absent();

    private HandlerRegistration valueChangedReg;


    private HandlerManager handlerManager = new HandlerManager(this);


    private int counter = 0;




    public EditorPresenter(EditorContextMapper editorContextMapper) {
        this.editorContextMapper = editorContextMapper;
    }

    public HandlerRegistration addEditorContextChangedHandler(EditorContextChangedHandler handler) {
        return handlerManager.addHandler(EditorContextChangedEvent.TYPE, handler);
    }

    public Widget getEditorHolder() {
        return editorHolder;
    }

    public <C extends EditorCtx, O extends Serializable> void setEditorContext(final Optional<C> editorCtx) {
        if (editorState.isPresent()) {
            unbindPrevious(editorState.get());
        }

        if (editorCtx.isPresent()) {
            bindNext(editorCtx.get());
        }
        else {
            editorHolder.setWidget(NOTHING_SELECTED_WIDGET);
        }
    }





    private void clearEditorState() {
        editorState = Optional.absent();
    }

    private <C extends EditorCtx, O extends Serializable> void setEditorState(O pristineObject, C editorContext, EditorView<O> view, EditorManager<C, O> editorManager) {
        editorState = Optional.<EditorState<?,?>>of(new EditorState<C, O>(pristineObject, editorContext, view, editorManager));
        handlerManager.fireEvent(new EditorContextChangedEvent(editorContext, editorManager.getDescription(editorContext)));
    }



    private <C extends EditorCtx, O extends Serializable> void unbindPrevious(final EditorState<C, O> editorState) {
        valueChangedReg.removeHandler();
        commitCurrentValue(editorState);
        clearEditorState();
    }

    private <C extends EditorCtx, O extends Serializable> void commitCurrentValue(final EditorState<C, O> editorState) {
        EditorView<O> view = editorState.getEditorView();
        if(!view.isDirty()) {
            return;
        }
        final Optional<O> value = view.getValue();
        if(!value.isPresent()) {
            return;
        }
        final O pristineValue = editorState.getPristineObject();
        final O editedValue = value.get();
        if(pristineValue.equals(editedValue)) {
            return;
        }
        final C editorCtx = editorState.getEditorContext();
        UpdateObjectAction<O> updateAction = editorState.getEditorManager().createUpdateObjectAction(pristineValue, editedValue, editorCtx);
        DispatchServiceManager.get().execute(updateAction, new EmptySuccessWebProtegeCallback<Result>());
    }


    private <C extends EditorCtx, O extends Serializable> void bindNext(final C editorCtx) {
        counter++;
        final Optional<EditorManager<C, O>> selectedMan = editorContextMapper.getEditorManager(editorCtx);
        if(selectedMan.isPresent()) {
//            editorHolder.setWidget(LOADING_INDICATOR_WIDGET);
            final EditorManager<C, O> editorManager = selectedMan.get();
            GetObjectAction<O> action = editorManager.createGetObjectAction(editorCtx);
            DispatchServiceManager.get().execute(action, new AbstractWebProtegeAsyncCallback<GetObjectResult<O>>() {

                private int executionCounter = counter;

                @Override
                public void onSuccess(GetObjectResult<O> result) {
                    handleGetObjectSuccess(result);
                }

                private void handleGetObjectSuccess(GetObjectResult<O> result) {
                    if(executionCounter != counter) {
                        return;
                    }
                    final O value = result.getObject();
                    final EditorView<O> editorView = editorManager.getView(editorCtx);
                    editorView.setValue(value);
                    valueChangedReg = editorView.addValueChangeHandler(new ValueChangeHandler<Optional<O>>() {
                        @Override
                        public void onValueChange(ValueChangeEvent<Optional<O>> event) {
                            commitCurrentValue(editorState.get());
                        }
                    });
                    final Widget editorWidget = editorView.getWidget();

                    if(editorWidget instanceof HasEnabled) {
                        Optional<Project> project = ProjectManager.get().getProject(editorCtx.getProjectId());
                            ((HasEnabled) editorWidget).setEnabled(project.isPresent() && project.get().hasWritePermission());
                    }

                    editorHolder.setWidget(editorWidget);
                    setEditorState(value, editorCtx, editorView, editorManager);
                }
            });
        }
        else {
            editorHolder.setWidget(new Label("No editor available for selection: " + editorCtx));
        }

    }





//    @Override
//    public void setEntity(EntityData newEntity) {
//        super.setEntity(newEntity);
//        selectedEntity = newEntity;
//        if (isEditorContextChanged()) {
//            GWT.log("setEntity: Editor context changed.  Processing change.");
//            processEditorContextChange();
//        }
//        else {
//            GWT.log("setEntity: Editor context has NOT changed. Ignoring call to setEntity.");
//        }
//
//    }

//    private boolean isEditorContextChanged() {
//        EditorContext<O> editorContext = getEditorContext();
//        return !editorContext.equals(lastEditorContext);
//    }
//
//    private void processEditorContextChange() {
//        lastEditorContext = getEditorContext();
//        handleStateChange();
//    }



//    private void handleValueChanged(Optional<O> value) {
//        commitCurrentValue(getEditorContext());
//    }
//
//    private HandlerRegistration valueChangedReg;
//
//    private Widget currentEditorWidget;
//
//    private void handleContextChanged() {
//
//        EditorState editorState;
//
//        if(valueChangedReg != null) {
//            valueChangedReg.removeHandler();
//        }
//
//        final EditorView<O> editorView = editorManager.getView();
//
//        Optional<O> editedValue = editorView.getValue();
//        O editedObject = editedValue.get();
//
//
//        valueChangedReg = editorView.addValueChangeHandler(new ValueChangeHandler<Optional<O>>() {
//            @Override
//            public void onValueChange(ValueChangeEvent<Optional<O>> event) {
//                handleValueChanged(event.getValue());
//            }
//        });
//
//        // Old one will be detached
//        currentEditorWidget = editorView.getWidget();
//        editorHolder.setWidget(currentEditorWidget);
//
//        // Set value
//        EditorContext<O> editorContext = getEditorContext();
//        GetObjectAction<O> actionGet = editorManager.createGetObjectAction(getEditorState());
//        DispatchServiceManager.get().execute(actionGet, new AbstractWebProtegeAsyncCallback<GetObjectResult<O>>() {
//            @Override
//            public void onSuccess(GetObjectResult<O> result) {
//                O object = result.getObject();
//                editorView.setValue(object);
//            }
//        });
//
//    }

//    private EditorState getEditorState() {
//        return new EditorState(getProjectId());
//    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



//    protected abstract UpdateObjectAction<O> createUpdateAction(EditorContext<O> editorContext, O from, O to);
//
//    protected abstract GetObjectAction<O> createGetObjectAction(EditorContext<O> editorContext);



//    private void handleStateChange() {
//        final EditorContext<O> editorContext = getEditorContext();
//        commitCurrentValue(editorContext);
//        updateFromServer(editorContext);
//        setTitle(updateStrategy.getEditorTitle(editorContext));
//    }
//
//    private long getTime() {
//        return new Date().getTime();
//    }
//
//    private void updateFromServer(EditorContext<O> editorContext) {
//        if(updateStrategy.shouldUpdateEditor(editorContext)) {
//            GWT.log("Getting object to update");
//            final GetObjectAction<O> getObjectAction = createGetObjectAction(getEditorContext());
//            DispatchServiceManager.get().execute(getObjectAction, new AsyncCallback<GetObjectResult<O>>() {
//                @Override
//                public void onFailure(Throwable caught) {
//                }
//
//                @Override
//                public void onSuccess(GetObjectResult<O> result) {
//                    handleGetObjectResponse(result);
//
//                }
//            });
//        }
//        else {
//            editor.clearValue();
//        }
//    }
//
//    private void commitCurrentValue(EditorContext<O> editorContext) {
//        if(editor.isDirty() && lastEditedObject.isPresent()) {
//            final Optional<O> editedObject = editor.getValue();
//            if (editedObject.isPresent() && !serverReferencePoint.equals(editedObject)) {
//                GWT.log("Server reference point and edited object differ.  Committing local changes to edited object.");
//                UpdateObjectAction<O> action = createUpdateAction(getEditorContext(), lastEditedObject.get(), editedObject.get());
//                DispatchServiceManager.get().execute(action, new AsyncCallback<Result>() {
//                    @Override
//                    public void onFailure(Throwable caught) {
//                    }
//
//                    @Override
//                    public void onSuccess(Result result) {
//                        handleUpdateObjectSuccess(result);
//
//                    }
//                });
//            }
//        }
//    }
//
//    private void handleUpdateObjectSuccess(Result result) {
//        serverReferencePoint = editor.getValue();
//        lastEditedObject = editor.getValue();
//    }
//
//    private EditorContext<O> getEditorContext() {
//        final Optional<OWLEntityData> sel = getSelectedEntityData();
//        return new EditorContext<O>(getProjectId(), sel);
//    }
//
//
//    private void handleGetObjectResponse(GetObjectResult<O> result) {
//        O object = result.getObject();
//        lastEditedObject = Optional.of(object);
//        serverReferencePoint = Optional.of(object);
//        editor.setValue(object);
//    }



//    private void updateState() {
//        if(editor instanceof HasEnabled) {
////            Project project = getProject();
////            boolean canWrite = project.hasWritePermission();
////            ((HasEnabled) editor).setEnabled(canWrite);
//        }
//    }




    private static class EditorState<C extends EditorCtx, O extends Serializable> {

        private O pristineObject;

        private C editorCtx;

        private EditorManager<C, O> editorManager;

        private EditorView<O> editorView;

        private EditorState(O pristineObject, C editorCtx, EditorView<O> editorView, EditorManager<C, O> editorManager) {
            this.pristineObject = pristineObject;
            this.editorCtx = editorCtx;
            this.editorManager = editorManager;
            this.editorView = editorView;
        }

        public EditorView<O> getEditorView() {
            return editorView;
        }

        public O getPristineObject() {
            return pristineObject;
        }

        public C getEditorContext() {
            return editorCtx;
        }

        public EditorManager<C, O> getEditorManager() {
            return editorManager;
        }
    }


}
