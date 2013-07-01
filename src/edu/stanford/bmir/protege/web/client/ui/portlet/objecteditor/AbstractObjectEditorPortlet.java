package edu.stanford.bmir.protege.web.client.ui.portlet.objecteditor;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.ScrollPanel;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/01/2013
 */
public abstract class AbstractObjectEditorPortlet<T extends Serializable> extends AbstractOWLEntityPortlet {


    private Optional<T> lastEditedObject = Optional.absent();

    private Optional<T> serverReferencePoint = Optional.absent();

    private ValueEditor<T> editor;

    private EditorUpdateStrategy<T> updateStrategy;



    public AbstractObjectEditorPortlet(Project project, EditorConfigurationFactory<T> factory) {
        super(project);
        this.editor = factory.getEditor(getProjectId());
        this.updateStrategy = factory.getUpdateStrategy(getProjectId());
        add(new ScrollPanel(editor.getWidget()));
        editor.addValueChangeHandler(new ValueChangeHandler<Optional<T>>() {
            @Override
            public void onValueChange(ValueChangeEvent<Optional<T>> event) {
                handleValueChanged(event.getValue());
            }
        });
        updateState();

    }


    @Override
    protected void onRefresh() {
        updateFromServer(getEditorContext());
    }

    @Override
    public void commitChanges() {
        commitCurrentValue(getEditorContext());
        updateFromServer(getEditorContext());
    }

    private EntityData selectedEntity;

    @Override
    public void reload() {
        // Reload is called by setEntity.
//        handleStateChange();
    }

    @Override
    public void initialize() {
    }

    @Override
    public Collection<EntityData> getSelection() {
        if(selectedEntity == null) {
            return Collections.emptySet();
        }
        else {
            return Collections.singleton(selectedEntity);
        }
    }

    private EditorContext<T> lastEditorContext;

    @Override
    public void setEntity(EntityData newEntity) {
        super.setEntity(newEntity);
        selectedEntity = newEntity;
        if (isEditorContextChanged()) {
            GWT.log("setEntity: Editor context changed.  Processing change.");
            processEditorContextChange();
        }
        else {
            GWT.log("setEntity: Editor context has NOT changed. Ignoring call to setEntity.");
        }

    }

    private boolean isEditorContextChanged() {
        EditorContext<T> editorContext = getEditorContext();
        return !editorContext.equals(lastEditorContext);
    }

    private void processEditorContextChange() {
        lastEditorContext = getEditorContext();
        handleStateChange();
    }


    private void handleValueChanged(Optional<T> value) {
        GWT.log("VALUE CHANGED IN PORTLET: " + value);
        commitCurrentValue(getEditorContext());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    protected abstract UpdateObjectAction<T> createUpdateAction(EditorContext<T> editorContext, T from, T to);

    protected abstract GetObjectAction<T> createGetObjectAction(EditorContext<T> editorContext);



    private void handleStateChange() {
        final EditorContext<T> editorContext = getEditorContext();
        commitCurrentValue(editorContext);
        updateFromServer(editorContext);
        setTitle(updateStrategy.getEditorTitle(editorContext));
    }

    private long t0;

    private long t1;

    private long t2;

    private long getTime() {
        return new Date().getTime();
    }

    private void updateFromServer(EditorContext<T> editorContext) {
        if(updateStrategy.shouldUpdateEditor(editorContext)) {
            GWT.log("Getting object to update");
            t0 = getTime();
            final GetObjectAction<T> getObjectAction = createGetObjectAction(getEditorContext());
            DispatchServiceManager.get().execute(getObjectAction, new AsyncCallback<GetObjectResult<T>>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(GetObjectResult<T> result) {
                    t1 = getTime();
                    handleGetObjectResponse(result);

                }
            });
        }
        else {
            editor.clearValue();
        }
    }

    private void commitCurrentValue(EditorContext<T> editorContext) {
        if(editor.isDirty() && lastEditedObject.isPresent()) {
            final Optional<T> editedObject = editor.getValue();
            if (editedObject.isPresent() && !serverReferencePoint.equals(editedObject)) {
                GWT.log("Server reference point and edited object differ.  Committing local changes to edited object.");
                UpdateObjectAction<T> action = createUpdateAction(getEditorContext(), lastEditedObject.get(), editedObject.get());
                DispatchServiceManager.get().execute(action, new AsyncCallback<Result>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(Result result) {
                        handleUpdateObjectSuccess(result);

                    }
                });
            }



        }
    }

    private void handleUpdateObjectSuccess(Result result) {
        serverReferencePoint = editor.getValue();
        lastEditedObject = editor.getValue();
    }

    private EditorContext<T> getEditorContext() {
        final Optional<OWLEntityData> sel = getSelectedEntityData();
        return new EditorContext<T>(getProjectId(), sel);
    }


    private void handleGetObjectResponse(GetObjectResult<T> result) {
        T object = result.getObject();
        lastEditedObject = Optional.of(object);
        serverReferencePoint = Optional.of(object);
        editor.setValue(object);
        t2 = getTime();
        GWT.log("Time to update object: ");
        final long totalTime = t2 - t0;
        GWT.log("    Total time:     " + totalTime);
        final long networkTime = t1 - t0;
        GWT.log("    Network time:   " + networkTime + " (" + (networkTime * 100.0 / totalTime) +  "%)");
        final long renderingTime = t2 - t1;
        GWT.log("    Rendering time: " + renderingTime + " (" + (renderingTime * 100.0 / totalTime) + "%)");
    }



    @Override
    public void onPermissionsChanged() {
        updateState();
    }

    @Override
    public void onLogin(UserId userId) {
        updateState();
    }

    @Override
    public void onLogout(UserId userId) {
        updateState();
    }

    private void updateState() {
        if(editor instanceof HasEnabled) {
            Project project = getProject();
            boolean canWrite = project.hasWritePermission();
            ((HasEnabled) editor).setEnabled(canWrite);
        }
    }
}
