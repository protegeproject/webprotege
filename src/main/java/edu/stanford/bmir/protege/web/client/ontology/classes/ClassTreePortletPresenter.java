package edu.stanford.bmir.protege.web.client.ontology.classes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.csv.CSVImportDialogController;
import edu.stanford.bmir.protege.web.client.csv.CSVImportViewImpl;
import edu.stanford.bmir.protege.web.client.csv.DocumentId;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassesAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.DeleteEntityAction;
import edu.stanford.bmir.protege.web.client.entity.CreateEntityDialogController;
import edu.stanford.bmir.protege.web.client.entity.CreateEntityInfo;
import edu.stanford.bmir.protege.web.client.hierarchy.EntityHierarchyNodeUpdater;
import edu.stanford.bmir.protege.web.client.hierarchy.EntityHierarchyTreeNodeRenderer;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.library.msgbox.InputBox;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.library.popupmenu.PopupMenu;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.client.progress.ProgressMonitor;
import edu.stanford.bmir.protege.web.client.search.SearchDialogController;
import edu.stanford.bmir.protege.web.client.upload.UploadFileDialogController;
import edu.stanford.bmir.protege.web.client.upload.UploadFileResultHandler;
import edu.stanford.bmir.protege.web.client.watches.WatchPresenter;
import edu.stanford.bmir.protege.web.shared.csv.CSVImportDescriptor;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyModel;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import edu.stanford.protege.gwt.graphtree.shared.UserObjectKeyProvider;
import edu.stanford.protege.gwt.graphtree.shared.tree.impl.GraphTreeNodeModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent.ON_USER_LOGGED_IN;
import static edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent.ON_USER_LOGGED_OUT;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.CANCEL;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.DELETE;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.*;
import static edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent.ON_PERMISSIONS_CHANGED;
import static edu.stanford.protege.gwt.graphtree.shared.tree.RevealMode.REVEAL_FIRST;
import static org.semanticweb.owlapi.model.EntityType.CLASS;


@Portlet(id = "portlets.ClassHierarchy",
        title = "Class Hierarchy",
        tooltip = "Displays the class hierarchy as a tree.")
public class ClassTreePortletPresenter extends AbstractWebProtegePortletPresenter {

    private static final Messages MESSAGES = GWT.create(Messages.class);

    private final DispatchServiceManager dispatchServiceManager;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    private final WatchPresenter watchPresenter;

    private final Provider<PrimitiveDataEditor> primitiveDataEditorProvider;

    private final SearchDialogController searchDialogController;

    private final Messages messages;

    private final EntityHierarchyNodeUpdater nodeUpdater;

    private final EntityHierarchyModel hierarchyModel;

    private final PortletAction createClassAction = new PortletAction(MESSAGES.create(),
                                                                      (action, event) -> handleCreateClass(event.isShiftKeyDown() ? CreateClassesMode.IMPORT_CSV : CreateClassesMode.CREATE_SUBCLASSES));
    private boolean expandDisabled = false;
    private String hierarchyProperty = null;
    private boolean inRemove = false;
    private TreeWidget<EntityHierarchyNode, OWLEntity> treeWidget;
    private final PortletAction watchClassAction = new PortletAction(MESSAGES.watch(),
                                                                     (action, event) -> handleEditWatches());
    private final PortletAction deleteClassAction = new PortletAction(MESSAGES.delete(),
                                                                      (action, event) -> handleDeleteClass());
    private boolean transmittingSelectionFromTree = false;
    private final GraphTreeNodeModel<EntityHierarchyNode, OWLEntity> treeModel;

    @Inject
    public ClassTreePortletPresenter(@Nonnull final ProjectId projectId,
                                     @Nonnull SelectionModel selectionModel,
                                     @Nonnull DispatchServiceManager dispatchServiceManager,
                                     @Nonnull LoggedInUserProjectPermissionChecker permissionChecker,
                                     @Nonnull WatchPresenter watchPresenter,
                                     @Nonnull SearchDialogController searchDialogController,
                                     @Nonnull Provider<PrimitiveDataEditor> primitiveDataEditorProvider,
                                     @Nonnull Messages messages) {
        super(selectionModel, projectId);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.permissionChecker = checkNotNull(permissionChecker);
        this.watchPresenter = checkNotNull(watchPresenter);
        this.primitiveDataEditorProvider = checkNotNull(primitiveDataEditorProvider);
        this.searchDialogController = checkNotNull(searchDialogController);
        this.messages = checkNotNull(messages);

        hierarchyModel = new EntityHierarchyModel(dispatchServiceManager, projectId);
        UserObjectKeyProvider<EntityHierarchyNode, OWLEntity> keyProvider = EntityHierarchyNode::getEntity;
        treeModel = GraphTreeNodeModel.create(hierarchyModel,
                                              keyProvider);
        treeWidget = new TreeWidget<>(
                treeModel,
                new EntityHierarchyTreeNodeRenderer());

        treeWidget.addSelectionChangeHandler(event -> transmitSelectionFromTree());

        nodeUpdater = new EntityHierarchyNodeUpdater(projectId, hierarchyModel);

        treeWidget.addDomHandler(event -> {
            event.preventDefault();
            event.stopPropagation();
            displayContextMenu(event);
        }, ContextMenuEvent.getType());
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entityData) {
        setSelectionInTree(entityData);
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.addPortletAction(createClassAction);
        portletUi.addPortletAction(deleteClassAction);
        portletUi.addPortletAction(watchClassAction);
        portletUi.addPortletAction(new PortletAction(messages.search(), (action, event) -> {
            searchDialogController.setEntityTypes(CLASS);
            WebProtegeDialog.showDialog(searchDialogController);
        }));
        portletUi.setWidget(treeWidget);

        eventBus.addProjectEventHandler(getProjectId(),
                                        ON_PERMISSIONS_CHANGED,
                                        event -> {
                                            updateButtonStates();
                                            handleRefresh();
                                        });

        eventBus.addApplicationEventHandler(ON_USER_LOGGED_OUT,
                                            event -> updateButtonStates());

        eventBus.addApplicationEventHandler(ON_USER_LOGGED_IN,
                                            event -> updateButtonStates());

        updateButtonStates();

        hierarchyModel.start(eventBus);
        nodeUpdater.start(eventBus);

        setSelectionInTree(getSelectedEntity());
    }


    public void updateButtonStates() {
        createClassAction.setEnabled(false);
        deleteClassAction.setEnabled(false);
        watchClassAction.setEnabled(false);
        permissionChecker.hasPermission(CREATE_CLASS,
                                        createClassAction::setEnabled);
        permissionChecker.hasPermission(DELETE_CLASS,
                                        deleteClassAction::setEnabled);
        permissionChecker.hasPermission(WATCH_CHANGES,
                                        watchClassAction::setEnabled);
    }

    private void displayContextMenu(ContextMenuEvent event) {
        PopupMenu contextMenu = new PopupMenu();
        contextMenu.addItem(messages.create(), this::handleCreateSubClasses);
        contextMenu.addItem(messages.delete(), this::handleDeleteClass);
        contextMenu.addSeparator();
        contextMenu.addItem(messages.tree_pruneToRoot(), this::pruneSelectedNodesToRoot);
        contextMenu.addItem(messages.tree_clearPruning(), this::clearPruning);
        contextMenu.addSeparator();
        contextMenu.addItem(messages.showIri(), showIriForSelection());
        contextMenu.addItem(messages.showDirectLink(), this::showDirectLinkForSelection);
        contextMenu.addSeparator();
        contextMenu.addItem(messages.refreshTree(), this::handleRefresh);
        contextMenu.show(event.getNativeEvent().getClientX(), event.getNativeEvent().getClientY() + 5);
    }

    private Runnable showIriForSelection() {
        return () -> {
            Optional<OWLEntity> selectedEntity = getSelectedEntity();
            if (selectedEntity.isPresent()) {
                String iri = selectedEntity.get().getIRI().toQuotedString();
                InputBox.showOkDialog(messages.classIri(), true, iri, input -> {
                });
            }
        };
    }

    private void handleRefresh() {
        treeWidget.setModel(treeModel);
        setSelectionInTree(getSelectedEntity());
    }


    private void showDirectLinkForSelection() {
        String location = Window.Location.getHref();
        InputBox.showOkDialog(messages.directLink(), true, location, input -> {});
    }

    private void pruneSelectedNodesToRoot() {
        treeWidget.pruneToSelectedNodes();
    }

    private void clearPruning() {
        treeWidget.clearPruning();
    }

    private void transmitSelectionFromTree() {
        try {
            transmittingSelectionFromTree = true;
            treeWidget.getSelectedSet().stream()
                      .findFirst()
                      .ifPresent(tn -> getSelectionModel().setSelection(tn.getUserObject().getEntity()));
        } finally {
            transmittingSelectionFromTree = false;
        }
    }

    private void handleCreateClass(CreateClassesMode mode) {
        if (mode == CreateClassesMode.CREATE_SUBCLASSES) {
            handleCreateSubClasses();
        }
        else {
            createSubClassesByImportingCSVDocument();
        }
    }

    private void handleCreateSubClasses() {
        if (!getSelectedTreeNodeClass().isPresent()) {
            showClassNotSelectedMessage();
            return;
        }
        WebProtegeDialog.showDialog(new CreateEntityDialogController(CLASS,
                                                                     this::createClasses,
                                                                     messages));
    }

    private void createClasses(CreateEntityInfo createEntityInfo) {
        final Optional<OWLClass> superCls = getSelectedTreeNodeClass();
        if (!superCls.isPresent()) {
            return;
        }
        final Set<String> browserTexts = createEntityInfo.getBrowserTexts().stream()
                                                         .filter(browserText -> !browserText.trim().isEmpty())
                                                         .collect(Collectors.toSet());
        if (browserTexts.size() > 1) {
            dispatchServiceManager.execute(new CreateClassesAction(
                                                   getProjectId(),
                                                   superCls.get(),
                                                   browserTexts),
                                           result -> {
                                               if (!result.getCreatedClasses().isEmpty()) {
                                                   List<OWLEntity> path = new ArrayList<>();
                                                   path.addAll(result.getSuperClassPathToRoot().getPath());
                                                   path.add(result.getCreatedClasses().get(0).getEntity());
                                                   Path<OWLEntity> entityPath = new Path<>(path);
                                                   selectAndExpandPath(entityPath);
                                               }
                                           });
        }
        else {
            dispatchServiceManager.execute(new CreateClassAction(
                                                   getProjectId(),
                                                   browserTexts.iterator().next(),
                                                   superCls.get()),
                                           result -> {
                                               Path<OWLEntity> path = new Path<>(new ArrayList<>(result.getPathToRoot().getPath()));
                                               selectAndExpandPath(path);
                                           });
        }
    }

    private void selectAndExpandPath(Path<OWLEntity> entityPath) {
        treeWidget.setSelected(entityPath, true, () -> treeWidget.setExpanded(entityPath));
    }

    private void createSubClassesByImportingCSVDocument() {
        final Optional<OWLClass> selCls = getSelectedTreeNodeClass();
        if (!selCls.isPresent()) {
            return;
        }
        UploadFileResultHandler uploadResultHandler = new UploadFileResultHandler() {
            @Override
            public void handleFileUploaded(final DocumentId fileDocumentId) {
                WebProtegeDialog<CSVImportDescriptor> csvImportDialog = new WebProtegeDialog<>(
                        new CSVImportDialogController(
                                getProjectId(),
                                fileDocumentId,
                                selCls.get(),
                                dispatchServiceManager,
                                new CSVImportViewImpl(
                                        primitiveDataEditorProvider)));
                csvImportDialog.setVisible(true);

            }

            @Override
            public void handleFileUploadFailed(
                    String errorMessage) {
                ProgressMonitor.get().hideProgressMonitor();
                MessageBox.showAlert("Error uploading CSV file", errorMessage);
            }
        };
        UploadFileDialogController controller = new UploadFileDialogController("Upload CSV",
                                                                               uploadResultHandler);

        WebProtegeDialog.showDialog(controller);
    }


    private Optional<OWLClassData> getSelectedTreeNodeClassData() {
        return treeWidget.getSelectedSet().stream()
                         .map(tn -> (OWLClassData) tn.getUserObject().getEntityData())
                         .findFirst();
    }

    private void handleDeleteClass() {
        final Optional<OWLClassData> currentSelection = getSelectedTreeNodeClassData();
        if (!currentSelection.isPresent()) {
            showClassNotSelectedMessage();
            return;
        }

        final OWLClassData theClassData = currentSelection.get();
        final String displayName = theClassData.getBrowserText();
        String subMessage = messages.delete_class_msg(displayName);
        MessageBox.showConfirmBox(messages.delete_class_title(),
                                  subMessage,
                                  CANCEL, DELETE,
                                  () -> deleteCls(theClassData.getEntity()),
                                  CANCEL);
    }

    private void showClassNotSelectedMessage() {
        MessageBox.showAlert("No class selected", "Please select a class.");
    }

    private void deleteCls(final OWLClass cls) {
        dispatchServiceManager.execute(new DeleteEntityAction(cls, getProjectId()), deleteEntityResult -> {});
    }

    protected void handleEditWatches() {
        final Optional<OWLClass> sel = getSelectedTreeNodeClass();
        if (!sel.isPresent()) {
            return;
        }
        watchPresenter.showDialog(sel.get());
    }


    /**
     * Gets the selected class.
     * @return The selected class.
     */
    private Optional<OWLClass> getSelectedTreeNodeClass() {
        Optional<OWLClassData> currentSelection = getSelectedTreeNodeClassData();
        return currentSelection.map(OWLClassData::getEntity);
    }

    private void setSelectionInTree(Optional<OWLEntity> selection) {
        if (transmittingSelectionFromTree) {
            return;
        }
        selection.ifPresent(sel -> {
            if (sel.isOWLClass()) {
                treeWidget.revealTreeNodesForUserObjectKey(sel, REVEAL_FIRST);
            }
        });
    }

    private enum CreateClassesMode {

        CREATE_SUBCLASSES,
        IMPORT_CSV
    }

}
