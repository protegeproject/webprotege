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
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.watches.WatchPresenter;
import edu.stanford.bmir.protege.web.shared.csv.CSVImportDescriptor;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.event.BrowserTextChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyModel;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.bmir.protege.web.shared.issues.CommentPostedEvent;
import edu.stanford.bmir.protege.web.shared.issues.DiscussionThreadStatusChangedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.watches.WatchAddedEvent;
import edu.stanford.bmir.protege.web.shared.watches.WatchRemovedEvent;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import edu.stanford.protege.gwt.graphtree.shared.tree.impl.GraphTreeNodeModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent.ON_USER_LOGGED_IN;
import static edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent.ON_USER_LOGGED_OUT;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.CANCEL;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.DELETE;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.*;
import static edu.stanford.bmir.protege.web.shared.event.BrowserTextChangedEvent.ON_BROWSER_TEXT_CHANGED;
import static edu.stanford.bmir.protege.web.shared.event.EntityDeprecatedChangedEvent.ON_ENTITY_DEPRECATED;
import static edu.stanford.bmir.protege.web.shared.issues.CommentPostedEvent.ON_COMMENT_POSTED;
import static edu.stanford.bmir.protege.web.shared.issues.DiscussionThreadStatusChangedEvent.ON_STATUS_CHANGED;
import static edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent.ON_PERMISSIONS_CHANGED;
import static edu.stanford.bmir.protege.web.shared.watches.WatchAddedEvent.ON_WATCH_ADDED;
import static edu.stanford.bmir.protege.web.shared.watches.WatchRemovedEvent.ON_WATCH_REMOVED;
import static edu.stanford.protege.gwt.graphtree.shared.tree.RevealMode.REVEAL_FIRST;
import static org.semanticweb.owlapi.model.EntityType.CLASS;


@Portlet(id = "portlets.ClassHierarchy",
        title = "Class Hierarchy",
        tooltip = "Displays the class hierarchy as a tree.")
public class ClassTreePortletPresenter extends AbstractWebProtegePortletPresenter {

    protected static final String WATCH_ICON_STYLE_STRING = "style=\"position:relative; top:2px; left:2px;\"";
    private static final Messages MESSAGES = GWT.create(Messages.class);
    private static final String SUFFIX_ID_LOCAL_ANNOTATION_COUNT = "_locAnnCnt";
    private static final String SUFFIX_ID_LOCAL_ANNOTATION_IMG = "_locAnnImg";
    private final DispatchServiceManager dispatchServiceManager;
    private final LoggedInUserProvider loggedInUserProvider;
    private final LoggedInUserProjectPermissionChecker permissionChecker;
    private final WatchPresenter watchPresenter;
    private final Provider<PrimitiveDataEditor> primitiveDataEditorProvider;
    private final SearchDialogController searchDialogController;
    private final Messages messages;
    private final PortletAction createClassAction = new PortletAction(MESSAGES.create(),
                                                                      (action, event) -> onCreateCls(event.isShiftKeyDown() ? CreateClassesMode.IMPORT_CSV : CreateClassesMode.CREATE_SUBCLASSES));
    private final EntityHierarchyNodeUpdater nodeUpdater;
    private boolean expandDisabled = false;
    private String hierarchyProperty = null;
    private boolean inRemove = false;
    private TreeWidget<EntityHierarchyNode, OWLEntity> treeWidget;
    private final PortletAction watchClassAction = new PortletAction(MESSAGES.watch(),
                                                                     (action, event) -> editWatches());
    private final PortletAction deleteClassAction = new PortletAction(MESSAGES.delete(),
                                                                      (action, event) -> onDeleteCls());
    private final EntityHierarchyModel hierarchyModel;

    private boolean transmittingSelectionFromTree = false;

    @Inject
    public ClassTreePortletPresenter(SelectionModel selectionModel,
                                     WatchPresenter watchPresenter,
                                     DispatchServiceManager dispatchServiceManager,
                                     final ProjectId projectId,
                                     LoggedInUserProvider loggedInUserProvider,
                                     LoggedInUserProjectPermissionChecker permissionChecker,
                                     Provider<PrimitiveDataEditor> primitiveDataEditorProvider,
                                     SearchDialogController searchDialogController,
                                     Messages messages) {
        this(selectionModel,
             primitiveDataEditorProvider,
             watchPresenter,
             dispatchServiceManager,
             loggedInUserProvider,
             projectId,
             permissionChecker,
             searchDialogController,
             messages);
    }

    private ClassTreePortletPresenter(SelectionModel selectionModel,
                                      Provider<PrimitiveDataEditor> primitiveDataEditorProvider,
                                      WatchPresenter watchPresenter,
                                      DispatchServiceManager dispatchServiceManager,
                                      LoggedInUserProvider loggedInUserProvider,
                                      final ProjectId projectId,
                                      LoggedInUserProjectPermissionChecker loggedInUserProjectPermissionChecker,
                                      SearchDialogController searchDialogController,
                                      Messages messages) {
        super(selectionModel, projectId);
        this.dispatchServiceManager = dispatchServiceManager;
        this.loggedInUserProvider = loggedInUserProvider;
        this.permissionChecker = loggedInUserProjectPermissionChecker;
        this.watchPresenter = watchPresenter;
        this.primitiveDataEditorProvider = primitiveDataEditorProvider;
        this.searchDialogController = searchDialogController;
        this.messages = messages;

        hierarchyModel = new EntityHierarchyModel(dispatchServiceManager, projectId);
        GraphTreeNodeModel<EntityHierarchyNode, OWLEntity> treeModel = GraphTreeNodeModel.create(hierarchyModel,
                                                                                                 EntityHierarchyNode::getEntity);
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

    private void displayContextMenu(ContextMenuEvent event) {
        PopupMenu contextMenu = new PopupMenu();
        contextMenu.addItem(messages.showIri(), () -> {
            Optional<OWLEntity> selectedEntity = getSelectedEntity();
            if (selectedEntity.isPresent()) {
                String iri = selectedEntity.get().getIRI().toQuotedString();
                InputBox.showOkDialog(messages.classIri(), true, iri, input -> {
                });
            }
        });
        contextMenu.addItem(messages.showDirectLink(), () -> {
            String location = Window.Location.getHref();
            InputBox.showOkDialog(messages.directLink(), true, location, input -> {
            });
        });
        contextMenu.addItem("Prune to root", this::pruneSelectedNodesToRoot);
        contextMenu.addItem("Clear pruning", this::clearPruning);
        contextMenu.addSeparator();
        contextMenu.addItem(messages.refreshTree(), this::handleRefresh);
        contextMenu.show(event.getNativeEvent().getClientX(), event.getNativeEvent().getClientY() + 5);
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

    private void handleRefresh() {
        GWT.log("[ClassTreePortlet] Refreshing tree");
        treeWidget.reload();
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
            GWT.log("[ClassTreePortlet] Transmitting selection from tree");
            treeWidget.getSelectedSet().stream()
                      .findFirst()
                      .ifPresent(tn -> getSelectionModel().setSelection(tn.getUserObject().getEntity()));
        } finally {
            transmittingSelectionFromTree = false;
        }
    }

    protected void onCreateCls(CreateClassesMode mode) {

        if (mode == CreateClassesMode.CREATE_SUBCLASSES) {
            createSubClasses();
        }
        else {
            createSubClassesByImportingCSVDocument();
        }
    }

    private void createSubClasses() {
        if (!getSelectedTreeNodeClass().isPresent()) {
            showClassNotSelectedMessage();
            return;
        }
        WebProtegeDialog.showDialog(new CreateEntityDialogController(CLASS,
                                                                     createEntityInfo -> {
                                                                         final Optional<OWLClass> superCls = getSelectedTreeNodeClass();
                                                                         if (!superCls.isPresent()) {
                                                                             return;
                                                                         }
                                                                         final Set<String> browserTexts = new HashSet<String>(
                                                                                 createEntityInfo.getBrowserTexts());
                                                                         if (browserTexts.size() > 1) {
                                                                             dispatchServiceManager.execute(new CreateClassesAction(
                                                                                                                    getProjectId(),
                                                                                                                    superCls.get(),
                                                                                                                    browserTexts),
                                                                                                            createClassesResult -> {
                                                                                                            });
                                                                         }
                                                                         else {
                                                                             dispatchServiceManager.execute(new CreateClassAction(
                                                                                                                    getProjectId(),
                                                                                                                    browserTexts.iterator().next(),
                                                                                                                    superCls.get()),
                                                                                                            createClassResult -> {
                                                                                                            });
                                                                         }
                                                                     }, messages));
    }

    private void createSubClassesByImportingCSVDocument() {
        final Optional<OWLClass> selCls = getSelectedTreeNodeClass();
        if (!selCls.isPresent()) {
            return;
        }
        UploadFileDialogController controller = new UploadFileDialogController("Upload CSV",
                                                                               new UploadFileResultHandler() {
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
                                                                                       ProgressMonitor.get()
                                                                                                      .hideProgressMonitor();
                                                                                       MessageBox.showAlert(
                                                                                               "Error uploading CSV file",
                                                                                               errorMessage);
                                                                                   }
                                                                               });

        WebProtegeDialog.showDialog(controller);
    }


    private Optional<OWLClassData> getSelectedTreeNodeClassData() {
        return treeWidget.getSelectedSet().stream()
                         .map(tn -> (OWLClassData) tn.getUserObject().getEntityData())
                         .findFirst();
    }

    protected void onDeleteCls() {
        final Optional<OWLClassData> currentSelection = getSelectedTreeNodeClassData();
        if (!currentSelection.isPresent()) {
            showClassNotSelectedMessage();
            return;
        }

        final OWLClassData theClassData = currentSelection.get();
        final String displayName = theClassData.getBrowserText();
        String subMessage = "Are you sure you want to delete class \"" + displayName + "\"?";
        MessageBox.showConfirmBox("Delete class",
                                  subMessage,
                                  CANCEL, DELETE,
                                  () -> deleteCls(theClassData.getEntity()), CANCEL);
    }

    private void showClassNotSelectedMessage() {
        MessageBox.showAlert("No class selected", "Please select a class.");
    }

    private void deleteCls(final OWLClass cls) {
        dispatchServiceManager.execute(new DeleteEntityAction(cls, getProjectId()), deleteEntityResult -> {
        });
    }

    protected void editWatches() {
        final Optional<OWLClass> sel = getSelectedTreeNodeClass();
        if (!sel.isPresent()) {
            return;
        }
        watchPresenter.showDialog(sel.get());
    }


    /**
     * Gets the selected class.
     *
     * @return The selected class, or {@code null} if there is not selection.
     */
    private Optional<OWLClass> getSelectedTreeNodeClass() {
        Optional<OWLClassData> currentSelection = getSelectedTreeNodeClassData();
        return currentSelection.map(OWLClassData::getEntity);
    }

    private void setSelectionInTree(Optional<OWLEntity> selection) {
        if(transmittingSelectionFromTree) {
            return;
        }
        selection.ifPresent(sel -> {
            GWT.log("[ClassTreePortlet] Setting selection in tree");
            treeWidget.revealTreeNodesForUserObjectKey(sel, REVEAL_FIRST);

        });
    }

    private enum CreateClassesMode {

        CREATE_SUBCLASSES,
        IMPORT_CSV
    }

}
