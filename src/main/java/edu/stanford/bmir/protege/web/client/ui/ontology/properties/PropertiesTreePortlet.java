package edu.stanford.bmir.protege.web.client.ui.ontology.properties;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Node;
import com.gwtext.client.widgets.tree.DefaultSelectionModel;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.TreeSelectionModel;
import com.gwtext.client.widgets.tree.event.DefaultSelectionModelListenerAdapter;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.actions.*;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.rpc.*;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyType;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.YesNoHandler;
import edu.stanford.bmir.protege.web.client.ui.ontology.entity.CreateEntityDialogController;
import edu.stanford.bmir.protege.web.client.ui.ontology.entity.CreateEntityInfo;
import edu.stanford.bmir.protege.web.client.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.portlet.LegacyCompatUtil;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;
import edu.stanford.bmir.protege.web.shared.event.BrowserTextChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.BrowserTextChangedHandler;
import edu.stanford.bmir.protege.web.shared.hierarchy.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityDataAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityDataResult;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import org.semanticweb.owlapi.model.*;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;

// TODO: add action descriptions and labels in the config similar to the ClassTreePortlet
public class PropertiesTreePortlet extends AbstractOWLEntityPortlet {

    public static final String ANNOTATION_PROPERTIES_ROOT_NAME = "Annotation properties";

    public static final String ANNOTATION_PROPERTIES_TREE_NODE_ID = "Annotation properties";

    private final DispatchServiceManager dispatchServiceManager;

    private final LoggedInUserProvider loggedInUserProvider;

    protected TreePanel treePanel;


    //    private TreeNode lastSelectedTreeNode;

    private final ProjectId projectId;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    @Inject
    public PropertiesTreePortlet(SelectionModel selectionModel, EventBus eventBus, DispatchServiceManager dispatchServiceManager, LoggedInUserProvider loggedInUserProvider, ProjectId projectId, LoggedInUserProjectPermissionChecker permissionChecker) {
        super(selectionModel, eventBus, projectId, loggedInUserProvider);
        this.dispatchServiceManager = dispatchServiceManager;
        this.loggedInUserProvider = loggedInUserProvider;
        this.projectId = projectId;
        this.permissionChecker = permissionChecker;

//        setLayout(new FitLayout());
        setTitle("Properties Tree");

        treePanel = new TreePanel();
        treePanel.setAnimate(true);
        treePanel.setAutoScroll(true);

        treePanel.addListener(new TreePanelListenerAdapter() {
            @Override
            public void onClick(TreeNode node, EventObject e) {
                selectNode(node);
            }

            @Override
            public void onExpandNode(TreeNode node) {
                getSubProperties(node.getId(), true);
            }
        });


        // Temporary - to be replaced when ontology is loaded.
        TreeNode root = new TreeNode(null);
        root.setId("RootPropertyNode");
        root.setHref("");
        root.setUserObject(new PropertyEntityData("RootPropertyNode", "RootPropertyNode", null));
        setTreeNodeIcon(root);

        treePanel.setRootNode(root);
        treePanel.setRootVisible(false);

        addToolbarButtons();
        getContentHolder().setWidget(treePanel.asWidget());


        addProjectEventHandler(ObjectPropertyHierarchyParentAddedEvent.TYPE, new ObjectPropertyHierarchyParentAddedHandler() {
            @Override
            public void handleObjectPropertyHierarchyParentAdded(ObjectPropertyHierarchyParentAddedEvent event) {
                if (isEventForThisProject(event)) {
                    handleRelationshipAdded(event);
                }
            }
        });

        addProjectEventHandler(ObjectPropertyHierarchyParentRemovedEvent.TYPE, new ObjectPropertyHierarchyParentRemovedHandler() {
            @Override
            public void handleObjectPropertyHierarchyParentRemoved(ObjectPropertyHierarchyParentRemovedEvent event) {
                if (isEventForThisProject(event)) {
                    handleRelationshipRemoved(event);
                }
            }
        });

        addProjectEventHandler(DataPropertyHierarchyParentAddedEvent.TYPE, new DataPropertyHierarchyParentAddedHandler() {
            @Override
            public void handleDataPropertyParentAdded(DataPropertyHierarchyParentAddedEvent event) {
                if (isEventForThisProject(event)) {
                    handleRelationshipAdded(event);
                }
            }
        });

        addProjectEventHandler(AnnotationPropertyHierarchyParentAddedEvent.TYPE, new AnnotationPropertyHierarchyParentAddedHandler() {
            @Override
            public void handleAnnotationPropertyHierarchyParentAdded(AnnotationPropertyHierarchyParentAddedEvent event) {
                if (isEventForThisProject(event)) {
                    handleRelationshipAdded(event);
                }
            }
        });

        addProjectEventHandler(HierarchyRootAddedEvent.TYPE, new HierarchyRootAddedHandler<Serializable>() {
            @Override
            public void handleHierarchyRootAdded(HierarchyRootAddedEvent<Serializable> event) {
                if (isEventForThisProject(event)) {
                    handleRootAdded(event);
                }
            }
        });

        addProjectEventHandler(HierarchyRootRemovedEvent.TYPE, new HierarchyRootRemovedHandler<Serializable>() {
            @Override
            public void handleHierarchyRootRemoved(HierarchyRootRemovedEvent<Serializable> event) {
                if (isEventForThisProject(event)) {
                    handleRootRemoved(event);
                }
            }
        });

        addProjectEventHandler(BrowserTextChangedEvent.TYPE, new BrowserTextChangedHandler() {
            @Override
            public void browserTextChanged(BrowserTextChangedEvent event) {
                if (isEventForThisProject(event)) {
                    handleBrowserTextChanged(event);
                }
            }
        });
        DefaultSelectionModel selModel = new DefaultSelectionModel();
        treePanel.setSelectionModel(selModel);
        selModel.addSelectionModelListener(new DefaultSelectionModelListenerAdapter() {
            @Override
            public void onSelectionChange(DefaultSelectionModel defaultSelectionModel, TreeNode treeNode) {
                if (treeNode != null) {
                    Optional<OWLEntityData> sel = getEntityDataFromTreeNode(treeNode);
                    if (sel.isPresent()) {
                        getSelectionModel().setSelection(sel.get().getEntity());
                    }
                }
            }
        });
    }


    private Optional<OWLEntityData> getSelectedTreeNodeEntityData() {
        TreeSelectionModel tsm = treePanel.getSelectionModel();
        if (tsm instanceof DefaultSelectionModel) {
            TreeNode selectedTreeNode = ((DefaultSelectionModel) tsm).getSelectedNode();
            return getEntityDataFromTreeNode(selectedTreeNode);
        }
        else {
            return Optional.absent();
        }
    }

    private static Optional<OWLEntityData> getEntityDataFromTreeNode(TreeNode selectedTreeNode) {
        if(selectedTreeNode == null) {
            return Optional.absent();
        }
        if(ANNOTATION_PROPERTIES_ROOT_NAME.equals(selectedTreeNode.getText())) {
            return Optional.absent();
        }
        Object userObject = selectedTreeNode.getUserObject();
        if (userObject instanceof EntityData) {
            return LegacyCompatUtil.toOWLEntityData((EntityData) userObject);
        }
        else {
            return Optional.absent();
        }
    }

    private void selectNode(TreeNode node) {
//        lastSelectedTreeNode = node;
        Optional<OWLEntityData> sel = getSelectedTreeNodeEntityData();
        if (sel.isPresent()) {
            getSelectionModel().setSelection(sel.get().getEntity());
        }
    }

    private void handleRelationshipAdded(final HierarchyChangedEvent<? extends OWLEntity, ?> event) {
        TreeNode parentTn = findTreeNode(event.getParent());
        if (parentTn == null) {
            return;
        }
        final OWLEntity child = event.getChild();
        handleChildAdded(parentTn, child, false);
    }

    private void handleRelationshipRemoved(final HierarchyChangedEvent<? extends OWLEntity, ?> event) {
        if (event.getChild().isBuiltIn()) {
            // Don't remove built in things from the tree
            return;
        }
        TreeNode tn = findTreeNode(event.getChild());
        if (tn == null) {
            return;
        }
        TreeNode parTn = findTreeNode(event.getParent());
        if (parTn != null) {
            parTn.removeChild(tn);
        }
    }

    private void handleRootAdded(HierarchyRootAddedEvent<?> event) {
        Object root = event.getRoot();
        if (root instanceof OWLAnnotationProperty) {
            TreeNode treeNode = getAnnotationPropertiesRootNode();
            if (treeNode != null) {
                handleChildAdded(treeNode, (OWLAnnotationProperty) root, false);
            }
        }
    }

    private void handleRootRemoved(HierarchyRootRemovedEvent<?> event) {
        Object root = event.getRoot();
        if (root instanceof OWLEntity) {
            if (((OWLEntity) root).isBuiltIn()) {
                // Don't remove built in things!
                return;
            }
        }
        if (root instanceof OWLAnnotationProperty) {
            TreeNode annoRoot = getAnnotationPropertiesRootNode();
            if (annoRoot != null) {
                TreeNode childTn = findTreeNode((OWLAnnotationProperty) root);
                annoRoot.removeChild(childTn);
                childTn.remove();
            }
        }
    }


    private void handleChildAdded(TreeNode parentTn, final OWLEntity child, boolean shouldSelect) {
        final TreeNode childTn = findTreeNode(child.getIRI().toString());
        final TreeNode freshChild;
        if (childTn == null) {
            final PropertyEntityData entityData = createEntityDataPlaceholder(child);
            freshChild = createTreeNode(entityData);
            updateTextAsync(child, freshChild);
            parentTn.appendChild(freshChild);

        }
        else {
            freshChild = childTn;
        }
        if (freshChild != null && shouldSelect) {
            parentTn.expand();
            freshChild.select();
            selectNode(freshChild);
        }
    }


    private boolean isAnnotationPropertiesRootSelected() {
        TreeNode treeNode = ((DefaultSelectionModel) treePanel.getSelectionModel()).getSelectedNode();
        return treeNode != null && ANNOTATION_PROPERTIES_ROOT_NAME.equals(treeNode.getText());
    }


    private void handleBrowserTextChanged(BrowserTextChangedEvent event) {
        OWLEntity entity = event.getEntity();
        TreeNode treeNode = findTreeNode(entity);
        if (treeNode != null) {
            final Object userObject = treeNode.getUserObject();
            if (userObject instanceof EntityData) {
                EntityData entityData = (EntityData) userObject;
                entityData.setBrowserText(event.getNewBrowserText());
                treeNode.setText(getNodeText(entityData));
            }

        }
    }


    protected void insertNodeInTree(TreeNode parentNode, EntityData child) {
        TreeNode treeNode = createTreeNode(child);
        parentNode.appendChild(treeNode);
    }


    protected void onPropertyDeleted(EntityData entity) {
        TreeNode propNode = findTreeNode(entity.getName());
        if (propNode != null) {
            Node parentNode = propNode.getParentNode();
            if (parentNode != null) {
                parentNode.removeChild(propNode);
            }
            else {
                propNode.remove();
            }
        }
    }


    private boolean hasChild(TreeNode parentNode, String childId) {
        return getDirectChild(parentNode, childId) != null;
    }

    protected void addToolbarButtons() {
//        createButton = new Button("Create");
//        createButton.setCls("toolbar-button");
//        createButton.addListener(new ButtonListenerAdapter() {
//            @Override
//            public void onClick(Button button, EventObject e) {
//                onCreateProperty();
//            }
//        });
//
//
//        deleteButton = new Button("Delete");
//        // deleteButton.setIconCls("protege-class-delete-icon");
//        deleteButton.setCls("toolbar-button");
//        deleteButton.addListener(new ButtonListenerAdapter() {
//            @Override
//            public void onClick(Button button, EventObject e) {
//                onDeleteProperty();
//            }
//        });
//        setTopToolbar(new Button[]{createButton, deleteButton});
    }

    /**
     * Gets the {@link org.semanticweb.owlapi.model.EntityType} of the selected entity.
     *
     * @return The {@link org.semanticweb.owlapi.model.EntityType} of the selected entity.  Not {@code null}.
     */
    @Override
    public Optional<EntityType<?>> getSelectedEntityType() {
        if (isAnnotationPropertiesRootSelected()) {
            return Optional.<EntityType<?>>of(EntityType.ANNOTATION_PROPERTY);
        }
        return super.getSelectedEntityType();
    }

    protected void onCreateProperty() {

        Optional<EntityType<?>> selectedEntityType = getSelectedEntityType();
        if (!selectedEntityType.isPresent()) {
            return;
        }
        WebProtegeDialog.showDialog(new CreateEntityDialogController(selectedEntityType.get(), new CreateEntityDialogController.CreateEntityHandler() {
            @Override
            public void handleCreateEntity(CreateEntityInfo createEntityInfo) {
                handleCreateProperty(createEntityInfo);
            }
        }));
    }

    private void handleCreateProperty(final CreateEntityInfo createEntityInfo) {
        Optional<OWLEntity> sel = getSelectedEntity();
        if (!sel.isPresent()) {
            // Temp hack
            if (isAnnotationPropertiesRootSelected()) {
                createSubProperties(Optional.<OWLAnnotationProperty>absent(), createEntityInfo);
            }
            return;
        }

        // What follows is a cut and paste hack.  It will all be replaced by gwt-dispatch.

        OWLEntity selectedEntity = sel.get();
        selectedEntity.accept(new OWLEntityVisitor() {
            @Override
            public void visit(OWLObjectProperty property) {
                createSubProperties(property, createEntityInfo);
            }

            @Override
            public void visit(OWLDataProperty property) {
                createSubProperties(property, createEntityInfo);
            }

            @Override
            public void visit(OWLAnnotationProperty property) {
                createSubProperties(Optional.of(property), createEntityInfo);
            }

            @Override
            public void visit(OWLClass owlClass) {
            }

            @Override
            public void visit(OWLNamedIndividual individual) {
            }

            @Override
            public void visit(OWLDatatype datatype) {
            }
        });
    }

    private TreeNode getAnnotationPropertiesRootNode() {
        return treePanel.getNodeById(ANNOTATION_PROPERTIES_TREE_NODE_ID);
    }

    private void createSubProperties(Optional<OWLAnnotationProperty> parent, CreateEntityInfo createEntityInfo) {
        CreateAnnotationPropertiesAction action = new CreateAnnotationPropertiesAction(getProjectId(), createEntityInfo.getBrowserTexts(), parent);
        createSubProperties(action);
    }

    private void createSubProperties(OWLDataProperty property, CreateEntityInfo createEntityInfo) {
        CreateDataPropertiesAction action = new CreateDataPropertiesAction(getProjectId(), createEntityInfo.getBrowserTexts(), Optional.of(property));
        createSubProperties(action);
    }

    private void createSubProperties(OWLObjectProperty property, CreateEntityInfo createEntityInfo) {
        CreateObjectPropertiesAction action = new CreateObjectPropertiesAction(getProjectId(), createEntityInfo.getBrowserTexts(), Optional.of(property));
        createSubProperties(action);
    }

    private <R extends AbstractCreateEntityInHierarchyResult<E>, E extends OWLEntity> void createSubProperties(AbstractCreateEntityInHierarchyAction<R, E> action) {
        dispatchServiceManager.execute(action, new DispatchServiceCallback<R>() {
            @Override
            protected String getErrorMessage(Throwable throwable) {
                return "There was a problem creating the properties";
            }

            @Override
            public void handleSuccess(R result) {
                handleCreateEntitiesResult(result);
            }
        });
    }

    private void handleCreateEntitiesResult(AbstractCreateEntityInHierarchyResult<? extends OWLEntity> result) {
        TreeNode parentNode;
        if (result.getParent().isPresent()) {
            parentNode = this.findTreeNode(result.getParent().get());
        }
        else {
            parentNode = getAnnotationPropertiesRootNode();
        }
        if (parentNode != null) {
            handleChildAdded(parentNode, result.getEntities().iterator().next(), true);
        }
    }


//    private void doCreateAnnotationProperty(OWLAnnotationProperty property, CreateEntityInfo createEntityInfo) {
//        String projectName = getProject().getDisplayName();
//        final String superPropName;
//        if (property != null) {
//            superPropName = property.getIRI().toString();
//        }
//        else {
//            superPropName = null;
//        }
//        for (String browserText : createEntityInfo.getBrowserTexts()) {
//            OntologyServiceManager.getInstance().createAnnotationProperty(projectName, browserText, superPropName, GlobalSettings.get().getUserName(), "Created object property", new AsyncCallback<EntityData>() {
//                @Override
//                public void onFailure(Throwable caught) {
//                    MessageBox.alert("There was a problem creating the property.  Please try again.");
//                }
//
//                @Override
//                public void handleSuccess(EntityData result) {
//                    //                        refreshFromServer(500);
//                    if (lastSelectedTreeNode != null) {
//                        lastSelectedTreeNode.expand();
//                    }
//                }
//            });
//        }
//    }

//    private void doCreateDataProperty(OWLDataProperty property, CreateEntityInfo createEntityInfo) {
//        String projectName = getProject().getDisplayName();
//        for (String browserText : createEntityInfo.getBrowserTexts()) {
//            OntologyServiceManager.getInstance().createDatatypeProperty(projectName, browserText, property.getIRI().toString(), GlobalSettings.get().getUserName(), "Created object property", new AsyncCallback<EntityData>() {
//                @Override
//                public void onFailure(Throwable caught) {
//                    MessageBox.alert("There was a problem creating the property.  Please try again.");
//                }
//
//                @Override
//                public void handleSuccess(EntityData result) {
//                    //                        refreshFromServer(500);
//                    if (lastSelectedTreeNode != null) {
//                        lastSelectedTreeNode.expand();
//                    }
//                }
//            });
//        }
//    }

//    private void doCreateObjectProperty(OWLObjectProperty property, CreateEntityInfo createEntityInfo) {
//        String projectName = getProject().getDisplayName();
//        for (String browserText : createEntityInfo.getBrowserTexts()) {
//            OntologyServiceManager.getInstance().createObjectProperty(projectName, browserText, property.getIRI().toString(), GlobalSettings.get().getUserName(), "Created object property", new AsyncCallback<EntityData>() {
//                @Override
//                public void onFailure(Throwable caught) {
//                    MessageBox.alert("There was a problem creating the property.  Please try again.");
//                }
//
//                @Override
//                public void handleSuccess(EntityData result) {
//                    //                        refreshFromServer(500);
//                    if (lastSelectedTreeNode != null) {
//                        lastSelectedTreeNode.expand();
//                    }
//                }
//            });
//        }
//    }

    protected void onDeleteProperty() {

        Optional<OWLEntity> selection = getSelectedEntity();

        if (!selection.isPresent()) {
            return;
        }

        final OWLEntity entity = selection.get();

        if (!(entity instanceof OWLProperty)) {
            // Better safe than sorry
            return;
        }

        MessageBox.showYesNoConfirmBox("Delete selected property?", "Are you sure you want to delete the selected property ?", new YesNoHandler() {
            @Override
            public void handleYes() {
                deleteProperty((OWLProperty) entity);
            }

            @Override
            public void handleNo() {

            }
        });
    }


    protected void deleteProperty(OWLProperty propertyEntity) {
        GWT.log("Should delete property with name: " + propertyEntity, null);
        if (propertyEntity == null) {
            return;
        }
        dispatchServiceManager.execute(new DeleteEntityAction(propertyEntity, getProjectId()), new DeletePropertyHandler());
    }

    private TreeNode getDirectChild(TreeNode parentNode, String childId) {
        Node[] children = parentNode.getChildNodes();
        for (Node child : children) {
            if (getNodeClsName(child).equals(childId)) {
                return (TreeNode) child;
            }
        }
        return null;
    }

    public String getNodeClsName(Node node) {
        EntityData data = (EntityData) node.getUserObject();
        return data.getName();
    }

    protected TreeNode findTreeNode(OWLEntity entity) {
        return findTreeNode(entity.getIRI().toString());
    }

    protected TreeNode findTreeNode(String id) {
        TreeNode root = treePanel.getRootNode();
        TreeNode node = findTreeNode(root, id, new ArrayList<TreeNode>());
        return node;
    }

    protected TreeNode findTreeNode(TreeNode node, String id, ArrayList<TreeNode> visited) {
        if (getNodeClsName(node).equals(id)) {
            return node;
        }
        else {
            visited.add(node);
            Node[] children = node.getChildNodes();
            for (Node element2 : children) {
                TreeNode n = findTreeNode((TreeNode) element2, id, visited);
                if (n != null) {
                    return n;
                }
            }
            return null;
        }
    }

    @Override
    public void onPermissionsChanged() {
        updateButtonStates();
    }

    public void updateButtonStates() {
//        createButton.setDisabled(true);
//        deleteButton.setDisabled(true);
//        permissionChecker.hasWritePermission(new DispatchServiceCallback<Boolean>() {
//            @Override
//            public void handleSuccess(Boolean hasPermission) {
//                createButton.setDisabled(!hasPermission);
//                deleteButton.setDisabled(!hasPermission);
//            }
//        });
    }

//    @Override
//    protected void afterRender() {
//        getSubProperties(null, true);
//        super.afterRender();
//    }

    public void setTreeNodeIcon(TreeNode node) {
        PropertyEntityData entityData = (PropertyEntityData) node.getUserObject();
        PropertyType type = entityData.getPropertyType();
        if (type == PropertyType.OBJECT) {
            node.setIconCls(BUNDLE.style().objectPropertyIcon());
        }
        else if (type == PropertyType.DATATYPE) {
            node.setIconCls(BUNDLE.style().dataPropertyIcon());
        }
        else if (type == PropertyType.ANNOTATION) {
            node.setIconCls(BUNDLE.style().annotationPropertyIcon());
        }
        else {
            node.setIconCls(BUNDLE.style().objectPropertyIcon());
        }
    }

    public void getSubProperties(final String propName, final boolean getSubpropertiesOfSubproperties) {
        OntologyServiceManager.getInstance().getSubproperties(getProjectId(), propName, new GetSubproperties(propName, getSubpropertiesOfSubproperties));
    }

    protected TreeNode createTreeNode(EntityData entityData) {
        TreeNode node = new TreeNode(entityData.getBrowserText());
        node.setId(entityData.getName());
        node.setHref(null);
        node.setUserObject(entityData);
        setTreeNodeIcon(node);

        String text = getNodeText(entityData);
        node.setText(text);

        return node;
    }

    private String getNodeText(EntityData entityData) {
        String text = entityData.getBrowserText();
        int localAnnotationsCount = entityData.getLocalAnnotationsCount();
        if (localAnnotationsCount > 0) {
            text = text + "<img src=\"" + WebProtegeClientBundle.BUNDLE.commentSmallFilledIcon().getSafeUri().asString() + "\" />" + " " + localAnnotationsCount;
        }
        int childrenAnnotationsCount = entityData.getChildrenAnnotationsCount();
        if (childrenAnnotationsCount > 0) {
            text = text + " chd: " + childrenAnnotationsCount;
        }
        return text;
    }

    /*
     * Remote procedure calls
     */
    class GetSubproperties implements AsyncCallback<List<EntityData>> {

        private String propName;

        private boolean getSubpropertiesOfSubproperties;

        public GetSubproperties(String className, boolean getSubpropertiesOfSubproperties) {
            super();
            this.propName = className;
            this.getSubpropertiesOfSubproperties = getSubpropertiesOfSubproperties;
        }

        @Override
        public void onFailure(Throwable caught) {
            GWT.log("RPC error at getting subproperties of " + propName, caught);
        }

        @Override
        public void onSuccess(List<EntityData> children) {
            if (propName == null) {
                propName = "RootPropertyNode";
            }
            TreeNode parentNode = treePanel.getNodeById(propName);

            if (parentNode == null) {
                GWT.log("Cannot find node: " + propName, null);
                return;
            }

            // already loaded?
            // TODO - not a very good method
            if (parentNode.getFirstChild() != null) {
                // children are already fetched? Maybe?
                // TODO: think about this situation

                if (getSubpropertiesOfSubproperties) {
                    for (Object element2 : children) {
                        EntityData child = (EntityData) element2;
                        getSubProperties(child.getName(), false);
                    }
                }
                return;
            }

            for (Object element2 : children) {
                EntityData childData = (EntityData) element2;

                // if (!hasChild(clsName, childName)) {
                parentNode.appendChild(createTreeNode(childData));
                // }
            }

            if (getSubpropertiesOfSubproperties) {
                for (Object element2 : children) {
                    EntityData childName = (EntityData) element2;
                    getSubProperties(childName.getName(), false);
                }
            }
        }

        private boolean hasChild(String parentId, String childId) {
            TreeNode parentNode = treePanel.getNodeById(propName);

            Node[] children = parentNode.getChildNodes();

            for (Node child : children) {
                if (child.getId().equals(childId)) {
                    return true;
                }
            }
            return false;
        }
    }

    class DeletePropertyHandler extends DispatchServiceCallback<DeleteEntityResult> {

        @Override
        protected String getErrorMessage(Throwable throwable) {
            return "Error deleting property";
        }

        @Override
        public void handleSuccess(DeleteEntityResult result) {
            GWT.log("Delete successfully property ", null);
        }
    }

    private void updateTextAsync(final OWLEntity entity, final TreeNode node) {
        dispatchServiceManager.execute(new GetEntityDataAction(getProjectId(), ImmutableSet.<OWLEntity>of(entity)), new DispatchServiceCallback<GetEntityDataResult>() {
            @Override
            public void handleSuccess(GetEntityDataResult result) {
                node.setText(result.getEntityDataMap().get(entity).getBrowserText());
            }
        });
    }

    private PropertyEntityData createEntityDataPlaceholder(OWLEntity child) {
        String browserText = "";
        final PropertyEntityData entityData = new PropertyEntityData(child.getIRI().toString(), browserText, Collections.<EntityData>emptySet());
        final EntityType<?> entityType = child.getEntityType();
        if (entityType == EntityType.OBJECT_PROPERTY) {
            entityData.setPropertyType(PropertyType.OBJECT);
        }
        else if (entityType == EntityType.DATA_PROPERTY) {
            entityData.setPropertyType(PropertyType.DATATYPE);
        }
        else {
            entityData.setPropertyType(PropertyType.ANNOTATION);
        }
        return entityData;
    }


}
