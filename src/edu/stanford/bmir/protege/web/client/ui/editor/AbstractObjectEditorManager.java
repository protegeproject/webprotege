package edu.stanford.bmir.protege.web.client.ui.editor;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasEnabled;
import edu.stanford.bmir.protege.web.client.ui.portlet.objecteditor.*;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/01/2013
 * <p>
 *     An object which communicates with the server to retrieve and update object and pass them on to an editor.
 * </p>
 */
public class AbstractObjectEditorManager<T extends Serializable> extends FlowPanel implements HasEnabled, HasEditorContext<T> {

    @Override
    public void setEditorContext(EditorContext<T> editorContext) {
    }

    @Override
    public EditorContext<T> getEditorContext() {
        return null;
    }

    /**
     * Returns true if the widget is enabled, false if not.
     */
    @Override
    public boolean isEnabled() {
        return false;
    }

    /**
     * Sets whether this widget is enabled.
     * @param enabled <code>true</code> to enable the widget, <code>false</code>
     * to disable it
     */
    @Override
    public void setEnabled(boolean enabled) {
    }

    //
//    private EventDispatcher eventDispatcher;
//
//    public AbstractObjectEditorManager(ProjectId projectId, EditorConfigurationFactory<T> factory) {
//        this.editor = factory.getEditor(projectId);
//        this.updateStrategy = factory.getUpdateStrategy(projectId);
//        this.updateObjectRequestFactory = factory.getUpdateObjectRequestFactory(projectId);
//        this.getObjectRequestFactory = factory.getGetObjectRequestFactory(projectId);
//        add(editor.getWidget());
////        updateState();
//    }
//
////    private EntityData selectedEntity;
////
////    @Override
////    public Collection<EntityData> getSelection() {
////        if(selectedEntity == null) {
////            return Collections.emptySet();
////        }
////        else {
////            return Collections.singleton(selectedEntity);
////        }
////    }
//
//    private EditorContext<T> lastEditorContext;
//
////    @Override
////    public void setEntity(EntityData newEntity) {
////        super.setEntity(newEntity);
////        selectedEntity = newEntity;
////        if (isEditorContextChanged()) {
////            GWT.log("setEntity: Editor context changed.  Processing change.");
////            processEditorContextChange();
////        }
////        else {
////            GWT.log("setEntity: Editor context has NOT changed. Ignoring call to setEntity.");
////        }
////
////    }
//
//    private boolean isEditorContextChanged() {
//        EditorContext<T> editorContext = getEditorContext();
//        return !editorContext.equals(lastEditorContext);
//    }
//
//    private void processEditorContextChange() {
//        lastEditorContext = getEditorContext();
//        handleStateChange();
//    }
//
//
//
//    @Override
//    public void setEditorContext(EditorContext<T> editorContext) {
//
//    }
//
//    public EditorContext<T> getEditorContext() {
//        final Optional<OWLEntityData> sel = getSelectedEntityData();
//        return new EditorContext<T>(getProjectId(), sel);
//    }
//
//    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//    private Optional<T> lastEditedObject = Optional.absent();
//
//    private ValueEditor<T> editor;
//
//    private EditorUpdateStrategy<T> updateStrategy;
//
//    private UpdateObjectRequestFactory<T> updateObjectRequestFactory;
//
//    private GetObjectRequestFactory<T> getObjectRequestFactory;
//
//    private void handleStateChange() {
////        final EditorContext<T> editorContext = getEditorContext();
//        if(editor.isDirty() && lastEditedObject.isPresent()) {
//            Optional<T> editedObject = editor.getValue();
//            if (editedObject.isPresent()) {
//                UpdateObjectRequest<T> updateObjectRequest = updateObjectRequestFactory.createUpdateObjectRequest(editorContext, lastEditedObject.get(), editedObject.get());
//                // Fire request off to service
//                ObjectEditorServiceManager.get().updateObject(updateObjectRequest, new AsyncCallback<UpdateObjectResponse<T>>() {
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        // TODO:
//                    }
//
//                    @Override
//                    public void onSuccess(UpdateObjectResponse<T> result) {
//                        handleUpdateObjectResponse(result);
//                    }
//                });
//            }
//
//        }
//
//        if(updateStrategy.shouldUpdateEditor(editorContext)) {
//            GetObjectRequest<T> getObjectRequest = getObjectRequestFactory.createGetObjectRequest(editorContext);
//            // Fire request off to service
//            ObjectEditorServiceManager.get().getObject(getObjectRequest, new AsyncCallback<GetObjectResponse<T>>() {
//                @Override
//                public void onFailure(Throwable caught) {
//                    GWT.log(caught.getPermissionDeniedMessage(), caught);
//                    // TODO:
//                }
//
//
//                @Override
//                public void onSuccess(GetObjectResponse<T> result) {
//                    handleGetObjectResponse(result);
//                }
//            });
//        }
//        else {
//            editor.clearValue();
//        }
//        setTitle(updateStrategy.getEditorTitle(editorContext));
//    }
//
//
//
//    private void handleUpdateObjectResponse(UpdateObjectResponse<T> response) {
//        List<OntologyEvent> eventList = response.getEvents();
//        eventDispatcher.dispatchOntologyEvents(eventList);
//    }
//
//    private void handleGetObjectResponse(GetObjectResponse<T> response) {
//        T object = response.getObject();
//        lastEditedObject = Optional.of(object);
//        editor.setValue(object);
//    }
//
//
//
////    private void updateState() {
////        if(editor instanceof HasEnabled) {
////            Project project = getProject();
////            boolean canWrite = project.hasWritePermission();
////            ((HasEnabled) editor).setEnabled(canWrite);
////        }
////    }
//
//    /**
//     * Returns true if the widget is enabled, false if not.
//     */
//    @Override
//    public boolean isEnabled() {
//        if(editor instanceof HasEnabled) {
//            return ((HasEnabled) editor).isEnabled();
//        }
//        else {
//            return true;
//        }
//    }
//
//    /**
//     * Sets whether this widget is enabled.
//     * @param enabled <code>true</code> to enable the widget, <code>false</code>
//     * to disable it
//     */
//    @Override
//    public void setEnabled(boolean enabled) {
//        if(editor instanceof HasEnabled) {
//            ((HasEnabled) editor).setEnabled(true);
//        }
//    }
}
