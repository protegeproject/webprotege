package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.view.client.SelectionChangeEvent;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.UIAction;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyModel;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import edu.stanford.protege.gwt.graphtree.shared.tree.impl.GraphTreeNodeModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.client.hierarchy.EntityHierarchyContextMenuPresenter.createAndInstallContextMenu;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.CREATE_PROPERTY;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.DELETE_PROPERTY;
import static edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId.*;
import static edu.stanford.protege.gwt.graphtree.shared.tree.RevealMode.REVEAL_FIRST;

@SuppressWarnings("Convert2MethodRef")
@Portlet(id = "portlets.PropertyHierarchy",
        title = "Property Hierarchy",
        tooltip = "Displays the object, data and annotation property hierarchies as a tree.")
public class PropertyHierarchyPortletPresenter extends AbstractWebProtegePortletPresenter {

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final UIAction createAction;

    @Nonnull
    private final UIAction deleteAction;

    @Nonnull
    private final LoggedInUserProjectPermissionChecker permissionChecker;

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final PropertyHierarchyPortletView view;

    @Nonnull
    private final EntityHierarchyModel objectPropertyHierarchyModel;

    @Nonnull
    private final EntityHierarchyModel dataPropertyHierarchyModel;

    @Nonnull
    private final EntityHierarchyModel annotationPropertyHierarchyModel;

    @Nonnull
    private final TreeWidget<EntityHierarchyNode, OWLEntity> objectPropertyTree;

    @Nonnull
    private final TreeWidget<EntityHierarchyNode, OWLEntity> dataPropertyTree;

    @Nonnull
    private final TreeWidget<EntityHierarchyNode, OWLEntity> annotationPropertyTree;

    @Nonnull
    private final EntityHierarchyTreeNodeRenderer renderer = new EntityHierarchyTreeNodeRenderer();

    private boolean transmittingSelection = false;

    @Inject
    public PropertyHierarchyPortletPresenter(@Nonnull SelectionModel selectionModel,
                                             @Nonnull DispatchServiceManager dispatchServiceManager,
                                             @Nonnull ProjectId projectId,
                                             @Nonnull LoggedInUserProjectPermissionChecker permissionChecker,
                                             @Nonnull Messages messages,
                                             @Nonnull PropertyHierarchyPortletView view,
                                             @Nonnull EntityHierarchyModel objectPropertyHierarchyModel,
                                             @Nonnull EntityHierarchyModel dataPropertyHierarchyModel,
                                             @Nonnull EntityHierarchyModel annotationPropertyHierarchyModel,
                                             @Nonnull TreeWidget<EntityHierarchyNode, OWLEntity> objectPropertyTree,
                                             @Nonnull TreeWidget<EntityHierarchyNode, OWLEntity> dataPropertyTree,
                                             @Nonnull TreeWidget<EntityHierarchyNode, OWLEntity> annotationPropertyTree) {
        super(selectionModel, projectId);
        this.view = view;
        this.dispatchServiceManager = dispatchServiceManager;
        this.permissionChecker = permissionChecker;
        this.messages = messages;

        createAction = new PortletAction(messages.create(), this::handleCreate);
        deleteAction = new PortletAction(messages.delete(), this::handleDelete);
        this.objectPropertyHierarchyModel = objectPropertyHierarchyModel;
        this.dataPropertyHierarchyModel = dataPropertyHierarchyModel;
        this.annotationPropertyHierarchyModel = annotationPropertyHierarchyModel;
        this.objectPropertyTree = objectPropertyTree;
        this.dataPropertyTree = dataPropertyTree;
        this.annotationPropertyTree = annotationPropertyTree;
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.addAction(createAction);
        portletUi.addAction(deleteAction);

        startTree(OBJECT_PROPERTY_HIERARCHY,
                  messages.hierarchy_objectproperties(),
                  eventBus,
                  objectPropertyHierarchyModel, objectPropertyTree);

        startTree(DATA_PROPERTY_HIERARCHY,
                  messages.hierarchy_dataproperties(),
                  eventBus,
                  dataPropertyHierarchyModel, dataPropertyTree);

        startTree(ANNOTATION_PROPERTY_HIERARCHY,
                  messages.hierarchy_annotationproperties(),
                  eventBus,
                  annotationPropertyHierarchyModel, annotationPropertyTree);

        view.setHierarchyIdSelectedHandler(this::handleHierarchyChanged);

        view.setSelectedHierarchy(OBJECT_PROPERTY_HIERARCHY);

        updateButtonStates();
        setSelectionInTree(getSelectedEntity());
        portletUi.setWidget(view);
    }

    /**
     * Starts and setups the specified model and tree using the specified event bus.  The renderer will be set
     * and a context menu will be installed.
     * @param hierarchyId The hierarchy Id
     * @param label The label for the tree
     * @param eventBus The event bus
     * @param model The model
     * @param treeWidget The tree
     */
    private void startTree(@Nonnull HierarchyId hierarchyId,
                           @Nonnull String label,
                           @Nonnull WebProtegeEventBus eventBus,
                           @Nonnull EntityHierarchyModel model,
                           @Nonnull TreeWidget<EntityHierarchyNode, OWLEntity> treeWidget) {
        model.start(eventBus, hierarchyId);
        treeWidget.setRenderer(renderer);
        treeWidget.setModel(GraphTreeNodeModel.create(model, EntityHierarchyNode::getEntity));
        treeWidget.addSelectionChangeHandler(this::handleSelectionChanged);
        createAndInstallContextMenu(treeWidget,
                                    messages,
                                    createAction,
                                    deleteAction);
        view.addHierarchy(hierarchyId,
                          label,
                          treeWidget);

    }

    private void handleSelectionChanged(SelectionChangeEvent selectionChangeEvent) {
        transmitSelection();
    }

    private void handleHierarchyChanged(@Nonnull HierarchyId hierarchyId) {
        transmitSelection();
    }

    private void transmitSelection() {
        try {
            transmittingSelection = true;
            view.getSelectedHierarchy().ifPresent(tree -> {
                tree.getFirstSelectedKey().ifPresent(entity ->
                                                             getSelectionModel().setSelection(entity)
                );
            });
        } finally {
            transmittingSelection = false;
        }
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entity) {
        setSelectionInTree(entity);
    }

    private void setSelectionInTree(Optional<OWLEntity> entity) {
        if (transmittingSelection) {
            return;
        }
        entity.ifPresent(sel -> {
            if (sel.isOWLObjectProperty()) {
                view.setSelectedHierarchy(OBJECT_PROPERTY_HIERARCHY);
                objectPropertyTree.revealTreeNodesForKey(sel, REVEAL_FIRST);
            }
            else if (sel.isOWLDataProperty()) {
                view.setSelectedHierarchy(DATA_PROPERTY_HIERARCHY);
                dataPropertyTree.revealTreeNodesForKey(sel, REVEAL_FIRST);
            }
            else if (sel.isOWLAnnotationProperty()) {
                view.setSelectedHierarchy(ANNOTATION_PROPERTY_HIERARCHY);
                annotationPropertyTree.revealTreeNodesForKey(sel, REVEAL_FIRST);
            }
        });
    }

    private void handleCreate() {
        
    }

    private void handleDelete() {

    }

    private void updateButtonStates() {
        permissionChecker.hasPermission(CREATE_PROPERTY, enabled -> createAction.setEnabled(enabled));
        permissionChecker.hasPermission(DELETE_PROPERTY, enabled -> deleteAction.setEnabled(enabled));
    }

}
