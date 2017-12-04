package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.view.client.SelectionChangeEvent;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.UIAction;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassesAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.DeleteEntityAction;
import edu.stanford.bmir.protege.web.client.entity.CreateEntityDialogController;
import edu.stanford.bmir.protege.web.client.entity.CreateEntityInfo;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.search.SearchDialogController;
import edu.stanford.bmir.protege.web.client.watches.WatchPresenter;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyModel;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import edu.stanford.protege.gwt.graphtree.shared.tree.impl.GraphTreeNodeModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
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
import static edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId.CLASS_HIERARCHY;
import static edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent.ON_PERMISSIONS_CHANGED;
import static edu.stanford.protege.gwt.graphtree.shared.tree.RevealMode.REVEAL_FIRST;
import static org.semanticweb.owlapi.model.EntityType.CLASS;


@SuppressWarnings("Convert2MethodRef")
@Portlet(id = "portlets.ClassHierarchy",
        title = "Class Hierarchy",
        tooltip = "Displays the class hierarchy as a tree.")
public class ClassHierarchyPortletPresenter extends AbstractWebProtegePortletPresenter {

    private final DispatchServiceManager dispatchServiceManager;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    private final WatchPresenter watchPresenter;

    private final SearchDialogController searchDialogController;

    private final Messages messages;

    private final EntityHierarchyModel hierarchyModel;
    @Nonnull
    private final EntityHierarchyTreeNodeRenderer renderer;

    private final UIAction createClassAction;

    private final UIAction watchClassAction;

    private final UIAction deleteClassAction;

    private final UIAction searchAction;

    private final TreeWidget<EntityHierarchyNode, OWLEntity> treeWidget;

    private boolean transmittingSelectionFromTree = false;

    @Inject
    public ClassHierarchyPortletPresenter(@Nonnull final ProjectId projectId,
                                          @Nonnull SelectionModel selectionModel,
                                          @Nonnull DispatchServiceManager dispatchServiceManager,
                                          @Nonnull LoggedInUserProjectPermissionChecker permissionChecker,
                                          @Nonnull WatchPresenter watchPresenter,
                                          @Nonnull SearchDialogController searchDialogController,
                                          @Nonnull Messages messages,
                                          @Nonnull EntityHierarchyModel hierarchyModel,
                                          @Nonnull TreeWidget<EntityHierarchyNode, OWLEntity> treeWidget,
                                          @Nonnull EntityHierarchyTreeNodeRenderer renderer) {
        super(selectionModel, projectId);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.permissionChecker = checkNotNull(permissionChecker);
        this.watchPresenter = checkNotNull(watchPresenter);
        this.searchDialogController = checkNotNull(searchDialogController);
        this.messages = checkNotNull(messages);
        this.hierarchyModel = checkNotNull(hierarchyModel);
        this.treeWidget = checkNotNull(treeWidget);
        this.renderer = checkNotNull(renderer);

        this.createClassAction = new PortletAction(messages.create(),
                                                    this::handleCreateSubClasses);

        this.deleteClassAction = new PortletAction(messages.delete(),
                                                 this::handleDeleteClass);

        this.watchClassAction = new PortletAction(messages.watch(),
                                                  this::handleEditWatches);

        this.searchAction = new PortletAction(messages.search(),
                                              this::handleSearch);

        this.treeWidget.addSelectionChangeHandler(this::transmitSelectionFromTree);
        EntityHierarchyContextMenuPresenter contextMenuPresenter = new EntityHierarchyContextMenuPresenter(messages,
                                                                                                           treeWidget,
                                                                                                           createClassAction,
                                                                                                           deleteClassAction);
        this.treeWidget.addContextMenuHandler(contextMenuPresenter::showContextMenu);


    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entityData) {
        setSelectionInTree(entityData);
    }

    @Override
    public void startPortlet(@Nonnull PortletUi portletUi,
                             @Nonnull WebProtegeEventBus eventBus) {
        portletUi.addAction(createClassAction);
        portletUi.addAction(deleteClassAction);
        portletUi.addAction(watchClassAction);
        portletUi.addAction(searchAction);
        portletUi.setWidget(treeWidget);

        eventBus.addProjectEventHandler(getProjectId(),
                                        ON_PERMISSIONS_CHANGED,
                                        event -> updateButtonStates());

        eventBus.addApplicationEventHandler(ON_USER_LOGGED_OUT,
                                            event -> updateButtonStates());

        eventBus.addApplicationEventHandler(ON_USER_LOGGED_IN,
                                            event -> updateButtonStates());

        updateButtonStates();

        hierarchyModel.start(eventBus, CLASS_HIERARCHY);
        treeWidget.setRenderer(renderer);
        treeWidget.setModel(GraphTreeNodeModel.create(hierarchyModel,
                                                      node -> node.getEntity()));


        setSelectionInTree(getSelectedEntity());
    }


    public void updateButtonStates() {
        createClassAction.setEnabled(false);
        deleteClassAction.setEnabled(false);
        watchClassAction.setEnabled(false);
        // Note that the following action handlers cause GWT compile problems if method references
        // are used for some reason
        permissionChecker.hasPermission(CREATE_CLASS,
                                        enabled -> createClassAction.setEnabled(enabled));
        permissionChecker.hasPermission(DELETE_CLASS,
                                        enabled -> deleteClassAction.setEnabled(enabled));
        permissionChecker.hasPermission(WATCH_CHANGES,
                                        enabled -> watchClassAction.setEnabled(enabled));
    }

    private Optional<OWLClass> getFirstSelectedClass() {
        return treeWidget.getFirstSelectedKey()
                         .filter(sel -> sel instanceof OWLClass)
                         .map(sel -> (OWLClass) sel);
    }

    private void transmitSelectionFromTree(SelectionChangeEvent event) {
        try {
            transmittingSelectionFromTree = true;
            treeWidget.getFirstSelectedKey()
                      .ifPresent(sel -> getSelectionModel().setSelection(sel));
        } finally {
            transmittingSelectionFromTree = false;
        }
    }

    private void handleCreateSubClasses() {
        WebProtegeDialog.showDialog(new CreateEntityDialogController(CLASS,
                                                                     this::createClasses,
                                                                     messages));
    }

    private void createClasses(CreateEntityInfo createEntityInfo) {
        final Optional<OWLClass> selCls = getFirstSelectedClass();
        OWLClass superCls = selCls.orElse(DataFactory.getOWLThing());
        performClassCreation(createEntityInfo, superCls);
    }

    private void performClassCreation(@Nonnull CreateEntityInfo createEntityInfo, @Nonnull OWLClass superCls) {
        final Set<String> browserTexts = createEntityInfo.getBrowserTexts().stream()
                                                         .filter(browserText -> !browserText.trim().isEmpty())
                                                         .collect(Collectors.toSet());
        if (browserTexts.size() > 1) {
            dispatchServiceManager.execute(new CreateClassesAction(
                                                   getProjectId(),
                                                   superCls,
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
                                                   superCls),
                                           result -> {
                                               Path<OWLEntity> path = new Path<>(new ArrayList<>(result.getPathToRoot().getPath()));
                                               selectAndExpandPath(path);
                                           });
        }
    }

    private void handleSearch() {
        searchDialogController.setEntityTypes(CLASS);
        WebProtegeDialog.showDialog(searchDialogController);
    }

    private void selectAndExpandPath(Path<OWLEntity> entityPath) {
        treeWidget.setSelected(entityPath, true, () -> treeWidget.setExpanded(entityPath));
    }

    private void handleDeleteClass() {
        final Optional<EntityHierarchyNode> currentSelection = treeWidget.getFirstSelectedUserObject();
        currentSelection.ifPresent(sel -> {
            OWLEntity entity = sel.getEntity();
            String subMessage = messages.delete_class_msg(sel.getBrowserText());
            MessageBox.showConfirmBox(messages.delete_class_title(),
                                      subMessage,
                                      CANCEL, DELETE,
                                      () -> deleteCls(entity),
                                      CANCEL);
        });
    }

    private void deleteCls(final OWLEntity cls) {
        dispatchServiceManager.execute(new DeleteEntityAction(cls, getProjectId()), deleteEntityResult -> {});
    }

    protected void handleEditWatches() {
        final Optional<OWLEntity> sel = treeWidget.getFirstSelectedKey();
        sel.ifPresent(watchPresenter::showDialog);
    }

    private void setSelectionInTree(Optional<OWLEntity> selection) {
        if (transmittingSelectionFromTree) {
            return;
        }
        selection.ifPresent(sel -> {
            if (sel.isOWLClass()) {
                treeWidget.revealTreeNodesForKey(sel, REVEAL_FIRST);
            }
        });
    }
}
