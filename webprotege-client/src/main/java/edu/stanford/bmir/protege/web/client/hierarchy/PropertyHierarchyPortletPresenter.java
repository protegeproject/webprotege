package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.view.client.SelectionChangeEvent;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.UIAction;
import edu.stanford.bmir.protege.web.client.filter.FilterView;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.search.SearchDialogController;
import edu.stanford.bmir.protege.web.client.tag.TagVisibilityPresenter;
import edu.stanford.bmir.protege.web.client.watches.WatchPresenter;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateAnnotationPropertiesAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateDataPropertiesAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateObjectPropertiesAction;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import edu.stanford.protege.gwt.graphtree.shared.tree.impl.GraphTreeNodeModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.*;
import static edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId.*;
import static edu.stanford.protege.gwt.graphtree.shared.tree.RevealMode.REVEAL_FIRST;
import static org.semanticweb.owlapi.model.EntityType.*;

@SuppressWarnings("Convert2MethodRef")
@Portlet(id = "portlets.PropertyHierarchy",
        title = "Property Hierarchy",
        tooltip = "Displays the object, data and annotation property hierarchies as a tree.")
public class PropertyHierarchyPortletPresenter extends AbstractWebProtegePortletPresenter {

    @Nonnull
    private final UIAction createAction;

    @Nonnull
    private final UIAction deleteAction;

    @Nonnull
    private final UIAction watchAction;

    @Nonnull
    private final UIAction searchAction;

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
    private final EntityHierarchyTreeNodeRenderer renderer;

    @Nonnull
    private final CreateEntityPresenter createEntityPresenter;

    @Nonnull
    private final DeleteEntityPresenter deleteEntityPresenter;

    @Nonnull
    private final EntityHierarchyContextMenuPresenterFactory contextMenuPresenterFactory;

    @Nonnull
    private final WatchPresenter watchPresenter;

    @Nonnull
    private final SearchDialogController searchDialogController;

    @Nonnull
    private final HierarchyActionStatePresenter actionStatePresenter;

    @Nonnull
    private final Provider<EntityHierarchyDropHandler> entityHierarchyDropHandlerProvider;

    @Nonnull
    private final FilterView filterView;

    @Nonnull
    private final TagVisibilityPresenter tagVisibilityPresenter;

    private boolean transmittingSelectionFromHierarchy = false;

    private boolean settingSelectionInHierarchy = false;

    @Inject
    public PropertyHierarchyPortletPresenter(@Nonnull SelectionModel selectionModel,
                                             @Nonnull ProjectId projectId,
                                             @Nonnull Messages messages,
                                             @Nonnull PropertyHierarchyPortletView view,
                                             @Nonnull EntityHierarchyModel objectPropertyHierarchyModel,
                                             @Nonnull EntityHierarchyModel dataPropertyHierarchyModel,
                                             @Nonnull EntityHierarchyModel annotationPropertyHierarchyModel,
                                             @Nonnull TreeWidget<EntityHierarchyNode, OWLEntity> objectPropertyTree,
                                             @Nonnull TreeWidget<EntityHierarchyNode, OWLEntity> dataPropertyTree,
                                             @Nonnull TreeWidget<EntityHierarchyNode, OWLEntity> annotationPropertyTree,
                                             @Nonnull EntityHierarchyTreeNodeRenderer renderer,
                                             @Nonnull CreateEntityPresenter createEntityPresenter,
                                             @Nonnull DeleteEntityPresenter deleteEntityPresenter,
                                             @Nonnull EntityHierarchyContextMenuPresenterFactory contextMenuPresenterFactory,
                                             @Nonnull WatchPresenter watchPresenter,
                                             @Nonnull SearchDialogController searchDialogController,
                                             @Nonnull HierarchyActionStatePresenter actionStatePresenter,
                                             @Nonnull Provider<EntityHierarchyDropHandler> entityHierarchyDropHandlerProvider,
                                             @Nonnull FilterView filterView,
                                             @Nonnull TagVisibilityPresenter tagVisibilityPresenter) {
        super(selectionModel, projectId);
        this.view = view;
        this.messages = messages;
        this.createAction = new PortletAction(messages.create(), this::handleCreate);
        this.deleteAction = new PortletAction(messages.delete(), this::handleDelete);
        this.watchAction = new PortletAction(messages.watch(), this::handleWatch);
        this.searchAction = new PortletAction(messages.search(), this::handleSearch);
        this.objectPropertyHierarchyModel = objectPropertyHierarchyModel;
        this.dataPropertyHierarchyModel = dataPropertyHierarchyModel;
        this.annotationPropertyHierarchyModel = annotationPropertyHierarchyModel;
        this.objectPropertyTree = objectPropertyTree;
        this.dataPropertyTree = dataPropertyTree;
        this.annotationPropertyTree = annotationPropertyTree;
        this.renderer = renderer;
        this.createEntityPresenter = createEntityPresenter;
        this.deleteEntityPresenter = deleteEntityPresenter;
        this.contextMenuPresenterFactory = contextMenuPresenterFactory;
        this.watchPresenter = watchPresenter;
        this.searchDialogController = searchDialogController;
        this.actionStatePresenter = actionStatePresenter;
        this.entityHierarchyDropHandlerProvider = entityHierarchyDropHandlerProvider;
        this.filterView = filterView;
        this.tagVisibilityPresenter = tagVisibilityPresenter;
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.addAction(createAction);
        portletUi.addAction(deleteAction);
        portletUi.addAction(watchAction);
        portletUi.addAction(searchAction);
        portletUi.setFilterView(filterView);

        actionStatePresenter.registerAction(CREATE_PROPERTY, createAction);
        actionStatePresenter.registerAction(DELETE_PROPERTY, deleteAction);
        actionStatePresenter.registerAction(WATCH_CHANGES, watchAction);

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

        actionStatePresenter.start(eventBus);

        view.setSelectedHierarchy(OBJECT_PROPERTY_HIERARCHY);
        view.setHierarchyIdSelectedHandler(this::handleHierarchySwitched);

        tagVisibilityPresenter.start(filterView, view);

        portletUi.setWidget(view);
        setSelectionInTree(getSelectedEntity());
    }

    /**
     * Starts and setups the specified model and tree using the specified event bus.  The renderer will be set and a
     * context menu will be installed.
     *
     * @param hierarchyId The hierarchy Id
     * @param label       The label for the tree
     * @param eventBus    The event bus
     * @param model       The model
     * @param treeWidget  The tree
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
        contextMenuPresenterFactory.create(model,
                                           treeWidget,
                                           createAction,
                                           deleteAction)
                                   .install();
        EntityHierarchyDropHandler entityHierarchyDropHandler = entityHierarchyDropHandlerProvider.get();
        treeWidget.setDropHandler(entityHierarchyDropHandler);
        entityHierarchyDropHandler.start(hierarchyId);
        view.addHierarchy(hierarchyId,
                          label,
                          treeWidget);
    }

    private void handleSelectionChanged(SelectionChangeEvent selectionChangeEvent) {
        GWT.log("[PropertyHierarchyPortletPresenter] handling selection changed in tree");
        transmitSelection();
    }

    private void handleHierarchySwitched(@Nonnull HierarchyId hierarchyId) {
        GWT.log("[PropertyHierarchyPortletPresenter] handling hierarchy switched");
        transmitSelection();
    }

    private void transmitSelection() {
        if (settingSelectionInHierarchy) {
            GWT.log("[PropertyHierarchyPortletPresenter] Setting selection in hierarchy, returning");
            return;
        }
        try {
            transmittingSelectionFromHierarchy = true;
            view.getSelectedHierarchy().ifPresent(tree -> {
                Optional<OWLEntity> sel = tree.getFirstSelectedKey();
                if (!sel.equals(getSelectedEntity())) {
                    sel.ifPresent(entity -> {
                        GWT.log("[PropertyHierarchyPortletPresenter] Transmitting selection " + entity);
                        getSelectionModel().setSelection(entity);

                    });
                }
                if (!sel.isPresent()) {
                    GWT.log("[PropertyHierarchyPortletPresenter] Transmitting empty selection");
                    getSelectionModel().clearSelection();
                }
            });
        } finally {
            transmittingSelectionFromHierarchy = false;
        }
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entity) {
        setSelectionInTree(entity);
    }

    private void setSelectionInTree(Optional<OWLEntity> entity) {
        if (transmittingSelectionFromHierarchy) {
            return;
        }
        try {
            GWT.log("[PropertyHierarchyPortletPresenter] Setting selection in hierarchy: " + entity);
            settingSelectionInHierarchy = true;
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
        } finally {
            settingSelectionInHierarchy = false;
        }
    }

    private void handleCreate() {
        view.getSelectedHierarchyId().ifPresent(hierarchyId -> {
            if (hierarchyId.equals(OBJECT_PROPERTY_HIERARCHY)) {
                handleCreateObjectProperty();
            }
            else if (hierarchyId.equals(DATA_PROPERTY_HIERARCHY)) {
                handleCreateDataProperty();
            }
            else if (hierarchyId.equals(ANNOTATION_PROPERTY_HIERARCHY)) {
                handleCreateAnnotationProperty();
            }
        });
    }

    private void handleCreateAnnotationProperty() {
        createEntityPresenter.createEntities(ANNOTATION_PROPERTY,
                                             annotationPropertyTree,
                                             (projectId, browserText) ->
                                                     new CreateAnnotationPropertiesAction(projectId,
                                                                                          browserText,
                                                                                          getSelectedAnnotationProperty())
        );
    }

    private void handleCreateDataProperty() {
        createEntityPresenter.createEntities(DATA_PROPERTY,
                                             dataPropertyTree,
                                             (projectId, browserText) ->
                                                     new CreateDataPropertiesAction(projectId,
                                                                                    browserText,
                                                                                    getSelectedDataProperty())
        );
    }

    private void handleCreateObjectProperty() {
        createEntityPresenter.createEntities(OBJECT_PROPERTY,
                                             objectPropertyTree,
                                             (projectId, browserText) ->
                                                     new CreateObjectPropertiesAction(projectId,
                                                                                      browserText,
                                                                                      getSelectedObjectProperty())
        );
    }

    private Optional<OWLObjectProperty> getSelectedObjectProperty() {
        return objectPropertyTree.getFirstSelectedKey()
                                 .filter(sel -> sel instanceof OWLObjectProperty)
                                 .map(sel -> (OWLObjectProperty) sel);
    }


    private Optional<OWLDataProperty> getSelectedDataProperty() {
        return dataPropertyTree.getFirstSelectedKey()
                               .filter(sel -> sel instanceof OWLDataProperty)
                               .map(sel -> (OWLDataProperty) sel);
    }


    private Optional<OWLAnnotationProperty> getSelectedAnnotationProperty() {
        return annotationPropertyTree.getFirstSelectedKey()
                                     .filter(sel -> sel instanceof OWLAnnotationProperty)
                                     .map(sel -> (OWLAnnotationProperty) sel);
    }

    private void handleDelete() {
        view.getSelectedHierarchy().ifPresent(treeWidget -> deleteEntityPresenter.start(treeWidget));
    }

    private void handleWatch() {
        view.getSelectedHierarchy().ifPresent(treeWidget -> {
            treeWidget.getFirstSelectedKey().ifPresent(selection -> {
                watchPresenter.start(selection);
            });
        });
    }

    private void handleSearch() {
        searchDialogController.setEntityTypes(OBJECT_PROPERTY, DATA_PROPERTY, ANNOTATION_PROPERTY);
        WebProtegeDialog.showDialog(searchDialogController);
    }
}
