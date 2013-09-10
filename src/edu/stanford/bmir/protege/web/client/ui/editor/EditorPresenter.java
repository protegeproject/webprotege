package edu.stanford.bmir.protege.web.client.ui.editor;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInHandler;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutHandler;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.project.ProjectManager;
import edu.stanford.bmir.protege.web.client.rpc.AbstractWebProtegeAsyncCallback;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectAction;
import edu.stanford.bmir.protege.web.shared.event.HandlerRegistrationManager;
import edu.stanford.bmir.protege.web.shared.event.PermissionsChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.PermissionsChangedHandler;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class EditorPresenter implements HasDispose {

    private static final Label NOTHING_SELECTED_WIDGET = new Label("Nothing selected");

    private static final Label LOADING_INDICATOR_WIDGET = new Label("Loading...");

    private static final int VALUE_CHANGED_COMMIT_DELAY_MS = 1000;

    private EditorContextMapper editorContextMapper;



    private SimplePanel editorHolder = new SimplePanel();

    private Optional<EditorState<?, ?>> editorState = Optional.absent();

    private HandlerRegistration valueChangedReg;


    private HandlerManager handlerManager = new HandlerManager(this);


    private int counter = 0;

    private HandlerRegistrationManager handlerRegistrationManager = new HandlerRegistrationManager();

    private Timer commitOnValueChangedTimer = new Timer() {
        @Override
        public void run() {
            commitCurrentValue(editorState.get());
        }
    };


    public EditorPresenter(ProjectId projectId, EditorContextMapper editorContextMapper) {
        this.editorContextMapper = editorContextMapper;
        handlerRegistrationManager.registerHandler(UserLoggedInEvent.TYPE, new UserLoggedInHandler() {
            @Override
            public void handleUserLoggedIn(UserLoggedInEvent event) {
                updatePermissionBasedItems();
            }
        });

        handlerRegistrationManager.registerHandler(UserLoggedOutEvent.TYPE, new UserLoggedOutHandler() {
            @Override
            public void handleUserLoggedOut(UserLoggedOutEvent event) {
                updatePermissionBasedItems();
            }
        });

        handlerRegistrationManager.registerHandlerToProject(projectId, PermissionsChangedEvent.TYPE, new PermissionsChangedHandler() {
            @Override
            public void handlePersmissionsChanged(PermissionsChangedEvent event) {
                updatePermissionBasedItems();
            }
        });
        
        handlerRegistrationManager.registerHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
            @Override
            public void onPlaceChange(PlaceChangeEvent event) {
                updatePermissionBasedItems();
            }
        });
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
        commitOnValueChangedTimer.cancel();
        commitCurrentValue(editorState);
        clearEditorState();
    }

    private <C extends EditorCtx, O extends Serializable> void commitCurrentValue(final EditorState<C, O> editorState) {
        final EditorView<O> view = editorState.getEditorView();
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
        GWT.log("Updating object");
        DispatchServiceManager.get().execute(updateAction, new AsyncCallback<Result>() {
            @Override
            public void onFailure(Throwable caught) {
                GWT.log("Updating object failed", caught);
            }

            @Override
            public void onSuccess(Result result) {
                GWT.log("Object successfully updated");
                Optional<EditorManager<C, O>> editorManager = editorContextMapper.getEditorManager(editorCtx);
                if (editorManager.isPresent()) {
                    setEditorState(editedValue, editorCtx, view, editorManager.get());
                }
            }
        });
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
//                            commitCurrentValue(editorState.get());
                            rescheduleCommit();
                        }
                    });
                    final Widget editorWidget = editorView.asWidget();

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

    private void rescheduleCommit() {
        commitOnValueChangedTimer.cancel();
        commitOnValueChangedTimer.schedule(VALUE_CHANGED_COMMIT_DELAY_MS);
    }

    @Override
    public void dispose() {
        handlerRegistrationManager.removeHandlers();
    }


    private void updatePermissionBasedItems() {
        if(editorHolder.getWidget() instanceof HasEnabled) {
            boolean enabled = isEditingEnabled();
            ((HasEnabled) editorHolder.getWidget()).setEnabled(enabled);
        }
    }

    private boolean isEditingEnabled() {
        final Optional<ProjectId> activeProjectId = Application.get().getActiveProject();
        if(!activeProjectId.isPresent()) {
            return false;
        }
        ProjectId projectId = activeProjectId.get();
        final Optional<Project> activeProject = ProjectManager.get().getProject(projectId);
        if(!activeProject.isPresent()) {
            return false;
        }
        Project project = activeProject.get();
        return project.hasWritePermission();
    }


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
