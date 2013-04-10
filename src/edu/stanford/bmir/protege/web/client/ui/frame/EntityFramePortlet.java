package edu.stanford.bmir.protege.web.client.ui.frame;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/01/2013
 */
public class EntityFramePortlet  {
//
//    private Optional<? extends LabelledFrame<?, ?>> lastEditedObject = Optional.absent();
//
//    private ValueEditor<?> editor;
//
//    private EditorUpdateStrategy<?> updateStrategy;
//
//    private UpdateObjectRequestFactory<?> updateObjectRequestFactory;
//
//    private GetObjectRequestFactory<?> getObjectRequestFactory;
//
//    private FramePresenterStrategy<?, ?, ?> presenter;
//
//
//    public EntityFramePortlet(Project project) {
//        super(project);
//        ProjectId projectId = new ProjectId(project.getProjectName());
//        this.editor = factory.getEditor(projectId);
//        this.updateStrategy = factory.getUpdateStrategy(projectId);
//        this.updateObjectRequestFactory = factory.getUpdateObjectRequestFactory(projectId);
//        this.getObjectRequestFactory = factory.getGetObjectRequestFactory(projectId);
//        add(editor.getWidget());
//        updateState();
//    }
//
//    private EntityData selectedEntity;
//
//    @Override
//    public void reload() {
//        // Reload is called by setEntity.
////        handleStateChange();
//    }
//
//    @Override
//    public void initialize() {
//
//    }
//
//    @Override
//    public Collection<EntityData> getSelection() {
//        if(selectedEntity == null) {
//            return Collections.emptySet();
//        }
//        else {
//            return Collections.singleton(selectedEntity);
//        }
//    }
//
//    private EditorContext<?> lastEditorContext;
//
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
//
//    private boolean isEditorContextChanged() {
//        EditorContext<?> editorContext = getEditorContext();
//        return !editorContext.equals(lastEditorContext);
//    }
//
//    private void processEditorContextChange() {
//        lastEditorContext = getEditorContext();
//        handleStateChange();
//    }
//
//    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//
//    private void handleStateChange() {
//        final EditorContext<?> editorContext = getEditorContext();
//        if(editor.isDirty() && lastEditedObject.isPresent()) {
//            Optional<?> editedObject = editor.getValue();
//            if (editedObject.isPresent()) {
//                final LabelledFrame<?, ?> from = lastEditedObject.get();
//                UpdateObjectRequest<?> req = presenter.createUpdateObjectRequest(getProjectId(), from, )
//                UpdateObjectRequest<?> updateObjectRequest = updateObjectRequestFactory.createUpdateObjectRequest(editorContext, from, editedObject.get());
//                // Fire request off to service
//                ObjectEditorServiceManager.get().updateObject(updateObjectRequest, new AsyncCallback<UpdateObjectResponse<?>>() {
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        // TODO:
//                    }
//
//                    @Override
//                    public void onSuccess(UpdateObjectResponse<?> result) {
//                        handleUpdateObjectResponse(result);
//                    }
//                });
//            }
//
//        }
//
//        if(updateStrategy.shouldUpdateEditor(editorContext)) {
//            GetObjectRequest<?> getObjectRequest = getObjectRequestFactory.createGetObjectRequest(editorContext);
//            // Fire request off to service
//            ObjectEditorServiceManager.get().getObject(getObjectRequest, new AsyncCallback<GetObjectResponse<?>>() {
//                @Override
//                public void onFailure(Throwable caught) {
//                    GWT.log(caught.getPermissionDeniedMessage(), caught);
//                    // TODO:
//                }
//
//
//                @Override
//                public void onSuccess(GetObjectResponse<?> result) {
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
//    private EditorContext<?> getEditorContext() {
//        final Optional<OWLEntityData> sel = getSelectedEntityData();
//        return new EditorContext(getProjectId(), sel);
//    }
//
//    private void handleUpdateObjectResponse(UpdateObjectResponse<?> response) {
////        List<Event<?>> events = response.getUpdateEvents();
////        EventBusManager.getManager().postEvents(events);
//        List<OntologyEvent> eventList = response.getEvents();
//        project.getEventManager().dispatchEvents(eventList);
//    }
//
//    private void handleGetObjectResponse(GetObjectResponse<?> response) {
//        T object = response.getObject();
//        lastEditedObject = Optional.of(object);
//        editor.setValue(object);
//    }
//
//    @Override
//    public void onPermissionsChanged(Collection<String> permissions) {
//        updateState();
//    }
//
//    @Override
//    public void onLogin(String userName) {
//        updateState();
//    }
//
//    @Override
//    public void onLogout(String userName) {
//        updateState();
//    }
//
//    private void updateState() {
//        if(editor instanceof HasEnabled) {
//            Project project = getProject();
//            boolean canWrite = project.hasWritePermission();
//            ((HasEnabled) editor).setEnabled(canWrite);
//        }
//    }
    
}
