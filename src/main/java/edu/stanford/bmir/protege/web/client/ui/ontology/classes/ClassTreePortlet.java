package edu.stanford.bmir.protege.web.client.ui.ontology.classes;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Node;
import com.gwtext.client.data.Tree;
import com.gwtext.client.dd.DragData;
import com.gwtext.client.dd.DragDrop;
import com.gwtext.client.widgets.tree.*;
import com.gwtext.client.widgets.tree.event.DefaultSelectionModelListenerAdapter;
import com.gwtext.client.widgets.tree.event.MultiSelectionModelListener;
import com.gwtext.client.widgets.tree.event.TreeNodeListenerAdapter;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.csv.CSVImportDialogController;
import edu.stanford.bmir.protege.web.client.csv.CSVImportViewImpl;
import edu.stanford.bmir.protege.web.client.csv.DocumentId;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.actions.*;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.place.WebProtegePlaceHistoryMapper;
import edu.stanford.bmir.protege.web.client.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.client.portlet.PortletActionHandler;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.*;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.InputBox;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.InputBoxHandler;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.YesNoHandler;
import edu.stanford.bmir.protege.web.client.ui.library.popupmenu.PopupMenu;
import edu.stanford.bmir.protege.web.client.ui.library.progress.ProgressMonitor;
import edu.stanford.bmir.protege.web.client.ui.notes.editor.DiscussionThreadDialog;
import edu.stanford.bmir.protege.web.client.ui.ontology.entity.CreateEntityDialogController;
import edu.stanford.bmir.protege.web.client.ui.ontology.entity.CreateEntityInfo;
import edu.stanford.bmir.protege.web.client.upload.UploadFileDialogController;
import edu.stanford.bmir.protege.web.client.upload.UploadFileResultHandler;
import edu.stanford.bmir.protege.web.client.watches.WatchPresenter;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.ObjectPath;
import edu.stanford.bmir.protege.web.shared.csv.CSVImportDescriptor;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.hierarchy.ClassHierarchyParentAddedEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.ClassHierarchyParentAddedHandler;
import edu.stanford.bmir.protege.web.shared.hierarchy.ClassHierarchyParentRemovedEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.ClassHierarchyParentRemovedHandler;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityDataAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityDataResult;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.watches.*;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;

/**
 * Portlet for displaying class trees. It can be configured to show only a
 * subtree of an ontology, by setting the portlet property <code>topClass</code>
 * to the name of the top class to show. <br>
 * Also supports creating and editing classes.
 *
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class ClassTreePortlet extends AbstractOWLEntityPortlet {

    private static final String SUFFIX_ID_LOCAL_ANNOTATION_COUNT = "_locAnnCnt";

    private static final String SUFFIX_ID_LOCAL_ANNOTATION_IMG = "_locAnnImg";

    protected static final String WATCH_ICON_STYLE_STRING = "style=\"position:relative; top:3px; left:2px;\"";

    private TreePanel treePanel;

    private boolean expandDisabled = false;

    private String hierarchyProperty = null;

    private TreeNodeListenerAdapter nodeListener;

    private boolean registeredEventHandlers = false;
    
    private final DispatchServiceManager dispatchServiceManager;

    private final LoggedInUserProvider loggedInUserProvider;

    private final Provider<DiscussionThreadDialog> discussionThreadDialogProvider;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    private final WatchPresenter watchPresenter;

    private final PortletAction createClassAction = new PortletAction("Create", new PortletActionHandler() {
        @Override
        public void handleActionInvoked(PortletAction action, ClickEvent event) {
            onCreateCls(event.isShiftKeyDown() ? CreateClassesMode.IMPORT_CSV : CreateClassesMode.CREATE_SUBCLASSES);
        }
    });

    private final PortletAction deleteClassAction = new PortletAction("Delete", new PortletActionHandler() {
        @Override
        public void handleActionInvoked(PortletAction action, ClickEvent event) {
            onDeleteCls();
        }
    });

    private final PortletAction watchClassAction = new PortletAction("Watch", new PortletActionHandler() {
        @Override
        public void handleActionInvoked(PortletAction action, ClickEvent event) {
            editWatches();
        }
    });

    @Inject
    public ClassTreePortlet(SelectionModel selectionModel,
                            WebProtegePlaceHistoryMapper placeHistoryMapper,
                            WatchPresenter watchPresenter,
                            EventBus eventBus,
                            DispatchServiceManager dispatchServiceManager,
                            final ProjectId projectId,
                            LoggedInUserProvider loggedInUserProvider,
                            Provider<DiscussionThreadDialog> discussionThreadDialogProvider,
                            LoggedInUserProjectPermissionChecker permissionChecker) {
        this(selectionModel, watchPresenter, eventBus, dispatchServiceManager, loggedInUserProvider, projectId, null, discussionThreadDialogProvider, permissionChecker);
    }

    private ClassTreePortlet(SelectionModel selectionModel,
                             WatchPresenter watchPresenter, EventBus eventBus, DispatchServiceManager dispatchServiceManager, LoggedInUserProvider loggedInUserProvider, final ProjectId projectId, final String topClass, Provider<DiscussionThreadDialog> discussionThreadDialogProvider, LoggedInUserProjectPermissionChecker loggedInUserProjectPermissionChecker) {
        super(selectionModel, eventBus, projectId, loggedInUserProvider);
        this.dispatchServiceManager = dispatchServiceManager;
        this.loggedInUserProvider = loggedInUserProvider;
        this.discussionThreadDialogProvider = discussionThreadDialogProvider;
        this.permissionChecker = loggedInUserProjectPermissionChecker;
        this.watchPresenter = watchPresenter;
        addPortletAction(createClassAction);
        addPortletAction(deleteClassAction);
        addPortletAction(watchClassAction);

        setTitle("Classes");


        registerEventHandlers();

        updateButtonStates();
        if (nodeListener == null) {
            //listener for click on the comment icon to display notes
            nodeListener = new TreeNodeListenerAdapter() {

                @Override
                public void onContextMenu(final Node node, EventObject e) {
                    treePanel.getSelectionModel().select((TreeNode) node);
                    PopupMenu contextMenu = new PopupMenu();
                    contextMenu.addItem("Show IRI", new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            Optional<OWLEntity> selectedEntity = getSelectedEntity();
                            if (selectedEntity.isPresent()) {
                                String iri = selectedEntity.get().getIRI().toQuotedString();
                                InputBox.showOkDialog("Class IRI", true, iri, new InputBoxHandler() {
                                    @Override
                                    public void handleAcceptInput(String input) {

                                    }
                                });
                            }
                        }
                    });
                    contextMenu.addItem("Show direct link", new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            String location = Window.Location.getHref();
                            InputBox.showOkDialog("Direct link", true, location, new InputBoxHandler() {
                                @Override
                                public void handleAcceptInput(String input) {

                                }
                            });
                        }
                    });
                    contextMenu.addSeparator();
                    contextMenu.addItem("Refresh tree", new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            onRefresh();
                        }
                    });
                    contextMenu.show(e.getXY()[0], e.getXY()[1] + 5);
                }
            };
        }
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                EntityData root = new EntityData(OWLRDFVocabulary.OWL_THING.getIRI().toString(), "owl:Thing");
                createRoot(root);
            }
        });
    }

    private void registerEventHandlers() {
        if (registeredEventHandlers) {
            return;
        }
        registeredEventHandlers = true;
        ///////////////////////////////////////////////////////////////////////////
        //
        // Registration of event handlers that we are interested in
        //
        ///////////////////////////////////////////////////////////////////////////

        addProjectEventHandler(BrowserTextChangedEvent.TYPE, new BrowserTextChangedHandler() {
            @Override
            public void browserTextChanged(BrowserTextChangedEvent event) {
//                if (isEventForThisProject(event)) {
                onEntityBrowserTextChanged(event);
//                }
            }
        });

        addProjectEventHandler(EntityNotesChangedEvent.TYPE, new EntityNotesChangedHandler() {
            @Override
            public void entityNotesChanged(EntityNotesChangedEvent event) {
                if (isEventForThisProject(event)) {
                    onNotesChanged(event);
                }
            }
        });

        addProjectEventHandler(WatchAddedEvent.TYPE, new WatchAddedHandler() {
            @Override
            public void handleWatchAdded(WatchAddedEvent event) {
                if (isEventForThisProject(event)) {
                    onWatchAdded(event);
                }
            }
        });

        addProjectEventHandler(WatchRemovedEvent.TYPE, new WatchRemovedHandler() {
            @Override
            public void handleWatchRemoved(WatchRemovedEvent event) {
                if (isEventForThisProject(event)) {
                    ClassTreePortlet.this.handleWatchRemoved(event);
                }
            }
        });

        addProjectEventHandler(EntityDeprecatedChangedEvent.TYPE, new EntityDeprecatedChangedHandler() {
            @Override
            public void handleEntityDeprecatedChangedEvent(EntityDeprecatedChangedEvent evt) {
                if (isEventForThisProject(evt)) {
                    onEntityDeprecatedChanged(evt.getEntity(), evt.isDeprecated());
                }
            }
        });

        addProjectEventHandler(ClassHierarchyParentAddedEvent.TYPE, new ClassHierarchyParentAddedHandler() {
            @Override
            public void handleClassHierarchyParentAdded(final ClassHierarchyParentAddedEvent event) {
                if (isEventForThisProject(event)) {
                    handleParentAddedEvent(event);
                }
            }
        });


        addProjectEventHandler(ClassHierarchyParentRemovedEvent.TYPE, new ClassHierarchyParentRemovedHandler() {
            @Override
            public void handleClassHierarchyParentRemoved(ClassHierarchyParentRemovedEvent event) {
                if (isEventForThisProject(event)) {
                    handleParentRemovedEvent(event);
                }
            }
        });

    }

    private void handleParentAddedEvent(final ClassHierarchyParentAddedEvent event) {
        final TreeNode tn = findTreeNode(event.getParent());
        if (tn != null) {
            GetEntityDataAction action = new GetEntityDataAction(getProjectId(), ImmutableSet.copyOf(event.getSignature()));
            dispatchServiceManager.execute(action, new DispatchServiceCallback<GetEntityDataResult>() {
                @Override
                public void handleSuccess(GetEntityDataResult result) {
                    SubclassEntityData subClassData = new SubclassEntityData(event.getChild().toStringID(), result.getEntityDataMap().get(event.getChild()).getBrowserText(), Collections.<EntityData>emptyList(), 0);
                    subClassData.setValueType(ValueType.Cls);
                    onSubclassAdded((EntityData) tn.getUserObject(), Arrays.<EntityData>asList(subClassData), false);
                }
            });

        }
    }

    private void handleParentRemovedEvent(ClassHierarchyParentRemovedEvent event) {
        try {
            GWT.log("[ClassTreePortlet] Handling parent removed: " + event);
            inRemove = true;
            TreeNode parentTn = findTreeNode(event.getParent());
            GWT.log("[ClassTreePortlet] Couldn't find parent: " + event);
            if (parentTn != null) {
                // We should check
                TreeNode childTn = findTreeNode(event.getChild());
                if (childTn != null) {
                    for (Node existingChild : parentTn.getChildNodes()) {
//                    String parentId = parentTn.getId();
                        String childId = childTn.getId();
                        String existingChildId = existingChild.getId();
                        if (childId != null && existingChildId != null && childId.equals(existingChildId)) {
                            OWLClass child = event.getChild();
                            childTn.remove();
                            if (parentTn.getChildNodes().length < 1) {
                                parentTn.setExpandable(false);
                            }
                            setSelectionInTree(getSelectedEntity());
                            return;
                        }
                    }
                }
            }
        } finally {
            inRemove = false;
        }


    }

    private void onNotesChanged(EntityNotesChangedEvent event) {
        GWT.log("[ClassTreePortlet] Notes Changed: " + event);
        String name = event.getEntity().getIRI().toString();
        TreeNode node = findTreeNode(name);
        if (node != null) {
            final Object userObject = node.getUserObject();
            if (userObject instanceof EntityData) {
                EntityData subclassEntityData = (EntityData) userObject;
                subclassEntityData.setLocalAnnotationsCount(event.getTotalNotesCount());
                String nodeText = createNodeRenderText(node);
                node.setText(nodeText);
            }
        }
    }

    private void onWatchAdded(WatchAddedEvent event) {
        if (!event.getUserId().equals(getUserId())) {
            return;
        }
        Watch<?> watch = event.getWatch();
        if (watch instanceof EntityBasedWatch) {
            TreeNode tn = findTreeNode(((EntityBasedWatch) watch).getEntity().getIRI().toString());
            if (tn != null) {
                SubclassEntityData data = (SubclassEntityData) tn.getUserObject();
                data.addWatch(watch);
                updateTreeNodeRendering(tn);
            }
        }
    }

    private void updateTreeNodeRendering(TreeNode tn) {
        tn.setText(createNodeRenderText(tn));
        updateTreeNodeIcon(tn);
    }

    private void handleWatchRemoved(WatchRemovedEvent event) {
        if (!event.getUserId().equals(getUserId())) {
            return;
        }
        Watch<?> watch = event.getWatch();
        if (watch instanceof EntityBasedWatch) {
            OWLEntity entity = ((EntityBasedWatch) watch).getEntity();
            TreeNode tn = findTreeNode(entity.getIRI().toString());
            if (tn != null) {
                SubclassEntityData data = (SubclassEntityData) tn.getUserObject();
                data.clearWatches();
                updateTreeNodeRendering(tn);
            }
        }
    }

    private boolean inRemove = false;

    private TreePanel createTreePanel() {
        treePanel = new TreePanel();
        treePanel.setAnimate(true);
        treePanel.setEnableDD(true);

        treePanel.addListener(new TreePanelListenerAdapter() {
            @Override
            public void onExpandNode(final TreeNode node) {
                // if (!expandDisabled && !treePanel.getRootNode().equals(node)) {
                if (!expandDisabled) {
                    getSubclasses(((EntityData) node.getUserObject()).getName(), node);
                }
            }

            @Override
            public boolean doBeforeRemove(Tree tree, TreeNode parent, TreeNode node) {
                return super.doBeforeRemove(tree, parent, node);
            }

            @Override
            public void onRemove(Tree tree, TreeNode parent, TreeNode node) {
                super.onRemove(tree, parent, node);
            }
        });

        addDragAndDropSupport();
        return treePanel;
    }

    private void createSelectionListener() {
        final TreeSelectionModel selModel = treePanel.getSelectionModel();
        if (selModel instanceof DefaultSelectionModel) {
            ((DefaultSelectionModel) selModel).addSelectionModelListener(new DefaultSelectionModelListenerAdapter() {
                @Override
                public void onSelectionChange(final DefaultSelectionModel sm, final TreeNode node) {
                    transmitSelectionFromTree();
                }
            });
        }
        else if (selModel instanceof MultiSelectionModel) {
            ((MultiSelectionModel) selModel).addSelectionModelListener(new MultiSelectionModelListener() {
                public void onSelectionChange(final MultiSelectionModel sm, final TreeNode[] nodes) {
                    transmitSelectionFromTree();
                }
            });
        }
        else {
            GWT.log("Unknown tree selection model for class tree: " + selModel, null);
        }
    }

    private void transmitSelectionFromTree() {
        if(inRemove) {
            GWT.log("[ClassTreePortlet] In Remove.  Not updating selection.");
            return;
        }
        Optional<OWLClassData> selectedClassDataFromTree = getSelectedTreeNodeClassData();
        if (selectedClassDataFromTree.isPresent()) {
            getSelectionModel().setSelection(selectedClassDataFromTree.get().getEntity());
        }
    }

    protected void addDragAndDropSupport() {
        treePanel.addListener(new TreePanelListenerAdapter() {
            @Override
            public boolean doBeforeNodeDrop(final TreePanel treePanel, final TreeNode target, final DragData dragData, final String point, final DragDrop source, final TreeNode dropNode, final DropNodeCallback dropNodeCallback) {
                    final boolean success = Window.confirm("Are you sure you want to move " + getNodeBrowserText(dropNode) + " from parent " + getNodeBrowserText(dropNode.getParentNode()) + " to parent " + getNodeBrowserText(target) + " ?");
                    if (success) {
                        moveClass((EntityData) dropNode.getUserObject(), (EntityData) dropNode.getParentNode().getUserObject(), (EntityData) target.getUserObject());
                        return true;
                    }
                    else {
                        return false;
                    }

            }
        });
    }

    protected void onSubclassAdded(final EntityData parent, final Collection<EntityData> subclasses, final boolean selectNewNode) {
        if (subclasses == null || subclasses.size() == 0) {
            return;
        }

        final EntityData subclassEntity = ((List<EntityData>) subclasses).get(0); //there is always just one
        final TreeNode parentNode = findTreeNode(parent.getName());

        if (parentNode == null) {
            return; // nothing to be done
        }

        TreeNode subclassNode = findTreeNode(subclassEntity.getName());
        if (subclassNode == null) {
            subclassNode = createTreeNode(subclassEntity);
            parentNode.appendChild(subclassNode);
            getSubclasses(parent.getName(), parentNode);
        }
        else { // tricky if it already exists
            if (!hasChild(parentNode, subclassEntity.getName())) { //multiple parents
                subclassNode = createTreeNode(subclassEntity);
                if (subclassEntity instanceof SubclassEntityData) {
                    final int childrenCount = ((SubclassEntityData) subclassEntity).getSubclassCount();
                    if (childrenCount > 0) {
                        subclassNode.setExpandable(true);
                    }
                }
                parentNode.appendChild(subclassNode);
            }
        }

    }

    protected TreeNode findTreeNode(OWLClass cls) {
        return findTreeNode(cls.getIRI().toString());
    }

    protected TreeNode findTreeNode(final String id) {
        if (treePanel == null) {
            return null;
        }
        final TreeNode root = treePanel.getRootNode();
        if(id.equals(OWLRDFVocabulary.OWL_THING.getIRI().toString())) {
            return root;
        }
        return findTreeNode(root, id, new ArrayList<TreeNode>());
    }

    protected TreeNode findTreeNode(final TreeNode node, final String id, final ArrayList<TreeNode> visited) {
        if (getNodeClsName(node).equals(id)) {
            return node;
        }
        else {
            visited.add(node);
            final Node[] children = node.getChildNodes();
            for (final Node element2 : children) {
                final TreeNode n = findTreeNode((TreeNode) element2, id, visited);
                if (n != null) {
                    return n;
                }
            }
            return null;
        }
    }

    protected void onSubclassRemoved(final EntityData entity, final Collection<EntityData> subclasses) {
        if (subclasses == null || subclasses.size() == 0) {
            return;
        }

        final EntityData subclass = ((List<EntityData>) subclasses).get(0);
        final TreeNode parentNode = findTreeNode(entity.getName());

        if (parentNode == null) {
            return;
        }

        // final TreeNode subclassNode = findTreeNode(parentNode, subclass.getName(), new ArrayList<TreeNode>());
        final TreeNode subclassNode = getDirectChild(parentNode, subclass.getName());
        if (subclassNode == null) {
            return;
        }

        //if (subclassNode.getParentNode().equals(parentNode)) {
        parentNode.removeChild(subclassNode);
        if (parentNode.getChildNodes().length < 1) {
            parentNode.setExpandable(false);
        }
        //}
    }

    /**
     * Called to update the browser text in the tree
     *
     * @param event The event that describes the browser text change that happened.
     */
    protected void onEntityBrowserTextChanged(BrowserTextChangedEvent event) {
        OWLEntity entity = event.getEntity();
        TreeNode tn = findTreeNode(entity.getIRI().toString());
        if (tn == null) {
            return;
        }
        EntityData ed = (EntityData) tn.getUserObject();
        ed.setBrowserText(event.getNewBrowserText());
        updateTreeNodeRendering(tn);
    }


    protected void onEntityDeprecatedChanged(OWLEntity entity, boolean deprecated) {
        TreeNode tn = findTreeNode(entity.getIRI().toString());
        if (tn == null) {
            return;
        }
        if (tn.getUserObject() instanceof SubclassEntityData) {
            SubclassEntityData entityData = (SubclassEntityData) tn.getUserObject();
            entityData.setDeprecated(deprecated);
            updateTreeNodeRendering(tn);
        }
    }

    protected void onClassCreated(final OWLClass freshClass, final List<OWLClass> superClasses) {

    }


    protected void insertNodeInTree(final TreeNode parentNode, final EntityData child) {
        if (!hasChild(parentNode, child.getName())) {
            parentNode.appendChild(createTreeNode(child));
        }
    }

    private enum CreateClassesMode {

        CREATE_SUBCLASSES,
        IMPORT_CSV
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
        if(!getSelectedTreeNodeClass().isPresent()) {
            showClassNotSelectedMessage();
            return;
        }
        WebProtegeDialog.showDialog(new CreateEntityDialogController(EntityType.CLASS, new CreateEntityDialogController.CreateEntityHandler() {
            @Override
            public void handleCreateEntity(CreateEntityInfo createEntityInfo) {
                final Optional<OWLClass> superCls = getSelectedTreeNodeClass();
                if(!superCls.isPresent()) {
                    return;
                }
                final Set<String> browserTexts = new HashSet<String>(createEntityInfo.getBrowserTexts());
                if (browserTexts.size() > 1) {
                    dispatchServiceManager.execute(new CreateClassesAction(getProjectId(), superCls.get(), browserTexts), getCreateClassesActionAsyncHandler());
                }
                else {
                    dispatchServiceManager.execute(new CreateClassAction(getProjectId(), browserTexts.iterator().next(), superCls.get()), getCreateClassAsyncHandler());
                }
            }
        }));
    }

    private void createSubClassesByImportingCSVDocument() {
        final Optional<OWLClass> selCls = getSelectedTreeNodeClass();
        if(!selCls.isPresent()) {
            return;
        }
        UploadFileDialogController controller = new UploadFileDialogController("Upload CSV", new UploadFileResultHandler() {
            @Override
            public void handleFileUploaded(final DocumentId fileDocumentId) {
                WebProtegeDialog<CSVImportDescriptor> csvImportDialog = new WebProtegeDialog<CSVImportDescriptor>(
                        new CSVImportDialogController(getProjectId(), fileDocumentId, selCls.get(), dispatchServiceManager, new CSVImportViewImpl(getProjectId())));
                csvImportDialog.setVisible(true);

            }

            @Override
            public void handleFileUploadFailed(String errorMessage) {
                ProgressMonitor.get().hideProgressMonitor();
                MessageBox.showAlert("Error uploading CSV file", errorMessage);
            }
        });

        WebProtegeDialog.showDialog(controller);
    }

    private DispatchServiceCallback<CreateClassesResult> getCreateClassesActionAsyncHandler() {
        return new DispatchServiceCallback<CreateClassesResult>() {

            @Override
            public void handleSuccess(CreateClassesResult result) {
                Set<OWLClass> createdClasses = result.getCreatedClasses();
                for (TreeNode node : getSelectedTreeNodes()) {
                    Set<OWLClass> existingClasses = new HashSet<OWLClass>();
                    for (Node childNode : node.getChildNodes()) {
                        OWLClass childCls = DataFactory.getOWLClass(getNodeClsName(childNode));
                        existingClasses.add(childCls);
                    }
                    for (OWLClass createdCls : createdClasses) {
                        if (!existingClasses.contains(createdCls)) {
                            final SubclassEntityData entityData = new SubclassEntityData(createdCls.getIRI().toString(), result.getBrowserText(createdCls).or(""), Collections.<EntityData>emptySet(), 0);
                            entityData.setValueType(ValueType.Cls);
                            Node n = createTreeNode(entityData);
                            node.appendChild(n);
                        }
                    }
                }

            }
        };
    }




    protected DispatchServiceCallback<CreateClassResult> getCreateClassAsyncHandler() {
        return new CreateClassHandler();
    }

    protected void onDeleteCls() {
        final Optional<OWLClassData> currentSelection = getSelectedTreeNodeClassData();
        if (!currentSelection.isPresent()) {
            showClassNotSelectedMessage();
            return;
        }

        final OWLClassData theClassData = currentSelection.get();
        final String displayName = theClassData.getBrowserText();
        MessageBox.showYesNoConfirmBox("Delete class?", "Are you sure you want to delete class \"" + displayName + "\"?", new YesNoHandler() {
            @Override
            public void handleYes() {
                deleteCls(theClassData.getEntity());
            }

            @Override
            public void handleNo() {
            }
        });
    }

    private void showClassNotSelectedMessage() {
        MessageBox.showAlert("No class selected", "Please select a class.");
    }

    private void deleteCls(final OWLClass cls) {
        dispatchServiceManager.execute(new DeleteEntityAction(cls, getProjectId()), new DeleteClassHandler());
    }

    protected void editWatches() {
        final Optional<OWLClass> sel = getSelectedTreeNodeClass();
        if (!sel.isPresent()) {
            return;
        }
        watchPresenter.showDialog(sel.get());
    }

    private void updateTreeNodeIcon(TreeNode treeNode) {
        setTreeNodeIcon(treeNode, (EntityData) treeNode.getUserObject());
    }

    public void setTreeNodeIcon(final TreeNode node, EntityData entityData) {
        if (entityData instanceof SubclassEntityData && ((SubclassEntityData) entityData).isDeprecated()) {
            node.setIconCls(BUNDLE.style().deprecatedClassIcon());
        }
        else {
            node.setIcon(BUNDLE.svgClassIcon().getSafeUri().asString());
        }

    }

    public void setTreeNodeTooltip(final TreeNode node, EntityData entityData) {
        //node.setTooltip(entityData.getBrowserText());
    }

    public void getSubclasses(final String parentClsName, final TreeNode parentNode) {
        if (isSubclassesLoaded(parentNode)) {
            return;
        }
        if (hierarchyProperty == null) {
            invokeGetSubclassesRemoteCall(parentClsName, getSubclassesCallback(parentClsName, parentNode));
        }
    }

    protected void invokeGetSubclassesRemoteCall(final String parentClsName, AsyncCallback<List<SubclassEntityData>> callback) {
        OntologyServiceManager.getInstance().getSubclasses(getProjectId(), parentClsName, callback);
    }

    protected AsyncCallback<List<SubclassEntityData>> getSubclassesCallback(final String parentClsName, final TreeNode parentNode) {
        return new GetSubclassesOfClassHandler(parentClsName, parentNode, null);
    }

    public boolean isSubclassesLoaded(final TreeNode node) {
        final String val = node.getAttribute("subclassesLoaded");
        return val != null && val.equals("true");
    }

    public void setSubclassesLoaded(final TreeNode node, final boolean loaded) {
        node.setAttribute("subclassesLoaded", loaded ? "true" : "false");
    }

    protected void moveClass(final EntityData cls, final EntityData oldParent, final EntityData newParent) {
        if (oldParent.equals(newParent)) {
            return;
        }
        permissionChecker.hasWritePermission(new DispatchServiceCallback<Boolean>() {
            @Override
            public void handleSuccess(Boolean hasPermission) {
                if(hasPermission) {
                    OntologyServiceManager.getInstance().moveCls(getProjectId(), cls.getName(), oldParent.getName(), newParent.getName(), false, loggedInUserProvider.getCurrentUserId(), getMoveClsOperationDescription(cls, oldParent, newParent), new MoveClassHandler(cls.getName()));
                }
            }
        });

    }

    protected String getMoveClsOperationDescription(final EntityData cls, final EntityData oldParent, final EntityData newParent) {
        return "Move class: " + getDisplayText(cls) + ". Old parent: " + getDisplayText(oldParent) + ", New parent: " + getDisplayText(newParent);
    }

    public void getPathToRoot(final OWLEntity entity) {
        OntologyServiceManager.getInstance().getPathToRoot(getProjectId(), entity.getIRI().toString(), new GetPathToRootHandler());
    }

    private static String getDisplayText(Object object) {
        if (object == null) {
            return "";
        }
        if (object instanceof EntityData) {
            String browserText = ((EntityData) object).getBrowserText();
            if (browserText == null) {
                browserText = ((EntityData) object).getName();
            }
            return browserText == null ? "" : browserText;
        }
        else {
            return object.toString();
        }
    }

    /**
     * Gets the selected class.
     *
     * @return The selected class, or {@code null} if there is not selection.
     */
    private Optional<OWLClass> getSelectedTreeNodeClass() {
        Optional<OWLClassData> currentSelection = getSelectedTreeNodeClassData();
        if (currentSelection.isPresent()) {
            return Optional.of(currentSelection.get().getEntity());
        }
        else {
            return Optional.absent();
        }
    }

    private Optional<OWLClassData> getSelectedTreeNodeClassData() {
        List<EntityData> sel = getSelectedTreeNodeEntityData();
        if (sel == null) {
            return com.google.common.base.Optional.absent();
        }
        if (sel.isEmpty()) {
            return com.google.common.base.Optional.absent();
        }
        EntityData firstSel = sel.get(0);
        com.google.common.base.Optional<OWLClass> cls = toOWLClass(firstSel);
        if (!cls.isPresent()) {
            return com.google.common.base.Optional.absent();
        }
        return com.google.common.base.Optional.of(new OWLClassData(cls.get(), firstSel.getBrowserText()));
    }

    public List<EntityData> getSelectedTreeNodeEntityData() {
        if (treePanel == null) {
            return null;
        }
        final List<EntityData> selections = new ArrayList<EntityData>();
        final TreeSelectionModel selectionModel = treePanel.getSelectionModel();
        if (selectionModel instanceof MultiSelectionModel) {
            final TreeNode[] selection = ((MultiSelectionModel) selectionModel).getSelectedNodes();
            for (final TreeNode node : selection) {
                final EntityData ed = (EntityData) node.getUserObject();
                selections.add(ed);
            }
        }
        else if (selectionModel instanceof DefaultSelectionModel) {
            final TreeNode node = ((DefaultSelectionModel) selectionModel).getSelectedNode();
            if (node != null) {
                selections.add((EntityData) node.getUserObject());
            }
        }
        return selections;
    }

    public List<TreeNode> getSelectedTreeNodes() {
        if (treePanel == null) {
            return null;
        }
        final List<TreeNode> selections = new ArrayList<TreeNode>();
        final TreeSelectionModel selectionModel = treePanel.getSelectionModel();
        if (selectionModel instanceof MultiSelectionModel) {
            final TreeNode[] selection = ((MultiSelectionModel) selectionModel).getSelectedNodes();
            for (final TreeNode node : selection) {
                selections.add(node);
            }
        }
        else if (selectionModel instanceof DefaultSelectionModel) {
            final TreeNode node = ((DefaultSelectionModel) selectionModel).getSelectedNode();
            selections.add(node);
        }
        return selections;
    }


    private void setSelectionInTree(Optional<OWLEntity> selection) {
        if(!selection.isPresent()) {
            return;
        }
        getPathToRoot(selection.get());
    }

    public void selectPathInTree(List<EntityData> path) {
        if (treePanel != null) {
            selectPathInTree(path, treePanel.getRootNode(), 0);
        }
    }

    private void selectPathInTree(ObjectPath<OWLClass> path) {
        List<EntityData> entityDataPath = new ArrayList<EntityData>();
        for (OWLClass cls : path) {
            entityDataPath.add(new EntityData(cls.getIRI().toString()));
        }
        selectPathInTree(entityDataPath);
    }

    private void selectPathInTree(final List<EntityData> path, TreeNode parentNode, final int index) {
        for (int i = index; i < path.size(); i++) {
            final String clsName = path.get(i).getName();
            final TreeNode node = findTreeNode(clsName);
            if (node == null) {
                final EntityData parentEntityData = (EntityData) parentNode.getUserObject();
                invokeGetSubclassesRemoteCall(parentEntityData.getName(), getSelectInTreeCallback(parentNode, path, i));
                return;
            }
            else {
                parentNode = node;
                if (i == path.size() - 1) {
                    node.select();
                    if (!node.equals(treePanel.getRootNode())) {
                        node.ensureVisible();
                    }
                }
                else {
                    expandDisabled = true;
                    node.expand();
                    expandDisabled = false;
                }
            }
        }
    }

    protected AsyncCallback<List<SubclassEntityData>> getSelectInTreeCallback(TreeNode parentNode, List<EntityData> path, int index) {
        return new SelectInTreeHandler(parentNode, path, index);
    }

    protected TreeNode createTreeNode(final EntityData entityData) {
        final TreeNode node = new TreeNode(getDisplayText(entityData));
        node.setHref(null);
        node.setUserObject(entityData);
        node.setAllowDrag(true);
        node.setAllowDrop(true);
        setTreeNodeIcon(node, entityData);
        setTreeNodeTooltip(node, entityData);

        updateTreeNodeRendering(node);

        node.addListener(nodeListener);

        return node;
    }

    protected String createNodeRenderText(TreeNode node) {
        EntityData entityData = (EntityData) node.getUserObject();
        return createNodeText(entityData) + createNodeNoteCount(entityData, node) + createNodeWatchLabel(entityData);
    }

    protected String createNodeText(EntityData entityData) {
        boolean deprecated = false;
        if (entityData instanceof SubclassEntityData) {
            deprecated = ((SubclassEntityData) entityData).isDeprecated();
        }
        if (deprecated) {
            return "<span style=\"opacity: 0.5;\"><del>" + entityData.getBrowserText() + "</del></span>";
        }
        else {
            return entityData.getBrowserText();
        }
    }

    protected String createNodeNoteCount(EntityData entityData, TreeNode node) {
        String text = "";

        final int localAnnotationsCount = entityData.getLocalAnnotationsCount();
        if (localAnnotationsCount > 0) {
            final String idLocalAnnotationImg = node.getId() + SUFFIX_ID_LOCAL_ANNOTATION_IMG;
            final String idLocalAnnotationCnt = node.getId() + SUFFIX_ID_LOCAL_ANNOTATION_COUNT;

            // TODO: add a css for this
            text = text + "<span style=\"padding-left: 2px;\"><img id=\"" + idLocalAnnotationImg + "\" src=\"" + BUNDLE.svgCommentSmallFilledIcon().getSafeUri().asString() + "\" title=\"" + getNiceNoteCountText(localAnnotationsCount) + " on this category.\" /></span>" + "<span id=\"" + idLocalAnnotationCnt + "\" style=\"font-size:95%;color:#15428B;font-weight:bold;\">" + localAnnotationsCount + "</span>";
        }

        final int childrenAnnotationsCount = entityData.getChildrenAnnotationsCount();
        if (childrenAnnotationsCount > 0) {
            text = text + " <span style=\"padding-left: 2px;\"><img src=\"" + BUNDLE.commentSmallIcon().getSafeUri().asString() + "\" title=\"" + getNiceNoteCountText(childrenAnnotationsCount) + " on the children of this category\" /></span>" + "<span style=\"font-size:90%;color:#999999;\">" + childrenAnnotationsCount + "</span>";
        }

        return text;
    }

    protected String createNodeWatchLabel(EntityData cls) {
        Set<Watch<?>> w = cls.getWatches();
        if (w.isEmpty()) {
            return "";
        }
        if (w.iterator().next() instanceof EntityFrameWatch) {
            return "<img src=\"" + BUNDLE.eyeIcon().getSafeUri().asString() + "\" " + ClassTreePortlet.WATCH_ICON_STYLE_STRING + " title=\"" + " Watched\"></img>";
        }
        else {
            return "<img src=\"" + BUNDLE.eyeDownIcon().getSafeUri().asString() + "\" " + ClassTreePortlet.WATCH_ICON_STYLE_STRING + " title=\"" + " Watched branch\"></img>";
        }
    }

    private boolean hasChild(final TreeNode parentNode, final String childId) {
        return getDirectChild(parentNode, childId) != null;
    }

    private void createRoot(EntityData rootEnitity) {
        if (rootEnitity == null) {
            rootEnitity = new EntityData("Root", "Root node is not defined");
        }
        treePanel = createTreePanel();
        final TreeNode root = createTreeNode(rootEnitity);
        treePanel.setRootNode(root);
        treePanel.setSize("100%", "100%");
        ScrollPanel sp = new ScrollPanel(treePanel);
        getContentHolder().setWidget(sp);
        createSelectionListener();

        // MH: createTreeNode calls get subclasses, so it was being called twice
//        getSubclasses(rootEnitity.getName(), root);
        root.expand();
        setSelectionInTree(getSelectedEntity());
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entity) {
        if(getSelectedTreeNodeClass().equals(entity)) {
            return;
        }
        setSelectionInTree(entity);
    }

    protected TreeNode getDirectChild(final TreeNode parentNode, final String childId) {
        final Node[] children = parentNode.getChildNodes();
        for (final Node child : children) {
            if (getNodeClsName(child).equals(childId)) {
                return (TreeNode) child;
            }
        }
        return null;
    }

    @Override
    protected void onRefresh() {
        if (treePanel == null) {
            return;
        }
        // TODO: not ideal
        final TreeNode root = treePanel.getRootNode();

        treePanel.setVisible(false);
        root.collapse();

        final Node[] children = root.getChildNodes();
        if (children != null) {
            for (final Node element2 : children) {
                final TreeNode child = (TreeNode) element2;
                root.removeChild(child);
                setSubclassesLoaded(child, false);
            }
        }

        treePanel.setVisible(true);
        setSubclassesLoaded(root, false);

        Optional<OWLEntity> selection = getSelectedEntity();
        if(selection.isPresent() && !selection.get().isTopEntity()) {
            setSelectionInTree(selection);
        }
        else {
            root.expand();
        }
    }

    public void updateButtonStates() {
        createClassAction.setEnabled(false);
        deleteClassAction.setEnabled(false);
        permissionChecker.hasWritePermission(new DispatchServiceCallback<Boolean>() {
            @Override
            public void handleSuccess(Boolean result) {
                createClassAction.setEnabled(result);
                deleteClassAction.setEnabled(result);
            }
        });
        watchClassAction.setEnabled(!loggedInUserProvider.getCurrentUserId().isGuest());
    }

    public String getNodeClsName(final Node node) {
        final EntityData data = (EntityData) node.getUserObject();
        return data.getName();
    }

    public String getNodeBrowserText(final Node node) {
        final EntityData data = (EntityData) node.getUserObject();
        return data.getBrowserText();
    }

    @Override
    public void onPermissionsChanged() {
        updateButtonStates();
        onRefresh();
    }

    class GetSubclassesOfClassHandler implements AsyncCallback<List<SubclassEntityData>> {

        private final String clsName;

        private final TreeNode parentNode;

        private final AsyncCallback<Object> endCallback;

        public GetSubclassesOfClassHandler(final String className, final TreeNode parentNode, final AsyncCallback<Object> endCallback) {
            super();
            this.clsName = className;
            this.parentNode = parentNode;
            this.endCallback = endCallback;
        }

        @Override
        public void onFailure(final Throwable caught) {
            //getEl().unmask();
            GWT.log("RPC error at getting subclasses of " + clsName, caught);
            if (endCallback != null) {
                endCallback.onFailure(caught);
            }
        }

        @Override
        public void onSuccess(final List<SubclassEntityData> children) {
//            boolean isFresh = !isSubclassesLoaded(parentNode);
            Set<OWLClass> existingSubclasses = new HashSet<OWLClass>();
            for (Node childNode : parentNode.getChildNodes()) {
                existingSubclasses.add(DataFactory.getOWLClass(getNodeClsName(childNode)));
            }

            for (final SubclassEntityData subclassEntityData : children) {
                OWLClass currentCls = DataFactory.getOWLClass(subclassEntityData.getName());
                if (!existingSubclasses.contains(currentCls)) {
                    final TreeNode childNode = createTreeNode(subclassEntityData);
                    if (subclassEntityData.getSubclassCount() > 0) {
                        childNode.setExpandable(true);
                    }
                    parentNode.appendChild(childNode);
//                    updateAncestorNoteCounts(subclassEntityData.getLocalAnnotationsCount(), childNode);
                }
            }

            setSubclassesLoaded(parentNode, true);
            if (endCallback != null) {
                endCallback.onSuccess(children);
            }
        }
    }

    class GetPropertyHierarchySubclassesOfClassHandler implements AsyncCallback<List<Triple>> {

        private final String clsName;

        private final TreeNode parentNode;

        public GetPropertyHierarchySubclassesOfClassHandler(final String className, final TreeNode parentNode) {
            super();
            this.clsName = className;
            this.parentNode = parentNode;
        }

        @Override
        public void onFailure(final Throwable caught) {
            // getEl().unmask();
            GWT.log("RPC error at getting subproperties of " + clsName, caught);
        }

        @Override
        public void onSuccess(final List<Triple> childTriples) {
            // getEl().unmask();
            if (childTriples != null) {
                for (final Triple childTriple : childTriples) {
                    final EntityData childData = childTriple.getValue();
                    if (!hasChild(parentNode, childData.getName())) {
                        final TreeNode childNode = createTreeNode(childData);
                        childNode.setExpandable(true); // TODO - we need to get the
                        // own slot values count
                        parentNode.appendChild(childNode);
                    }
                }
            }

            setSubclassesLoaded(parentNode, true);
        }
    }

    class CreateClassHandler extends DispatchServiceCallback<CreateClassResult> {


        public CreateClassHandler() {
        }

        @Override
        public void handleSuccess(final CreateClassResult result) {
            onClassCreated(result.getObject(), result.getSuperClasses());
            SubclassEntityData subClassData = new SubclassEntityData(result.getObject().getIRI().toString(), result.getBrowserText(result.getObject()).or(""), Collections.<EntityData>emptyList(), 0);
            ObjectPath<OWLClass> pathToRoot = result.getPathToRoot();
            if(pathToRoot.isEmpty()) {
                return;
            }
            onSubclassAdded(new EntityData(pathToRoot.getSecondToLastElement().getIRI().toString()), Arrays.<EntityData>asList(subClassData), false);
            selectPathInTree(pathToRoot);
        }
    }

    class DeleteClassHandler extends DispatchServiceCallback<DeleteEntityResult> {



        @Override
        public void handleSuccess(final DeleteEntityResult result) {
        }
    }

    public class MoveClassHandler implements AsyncCallback<List<EntityData>> {

        private final String clsName;

        public MoveClassHandler(final String clsName) {
            this.clsName = clsName;
        }

        @Override
        public void onFailure(final Throwable caught) {
            MessageBox.showErrorMessage("Class not moved", caught);
            // TODO: refresh oldParent and newParent
        }

        @Override
        public void onSuccess(final List<EntityData> result) {
            GWT.log("Moved successfully class " + clsName, null);
            if (result == null) {
                //MessageBox.alert("Success", "Class moved successfully.");
            }
            else {
                GWT.log("Cycle warning after moving class " + clsName + ": " + result, null);


                String warningMsg = "<B>WARNING! There is a cycle in the hierarchy: </B><BR><BR>";
                for (EntityData p : result) {
                    warningMsg += "&nbsp;&nbsp;&nbsp;&nbsp;" + p.getBrowserText() + "<BR>";
                }
                warningMsg += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...";
                MessageBox.showAlert("Cycles introduced during class move", "Class moved successfully.<BR>" +
                        "<BR>" +
                        warningMsg);
            }

        }
    }

    class GetPathToRootHandler implements AsyncCallback<List<EntityData>> {

        @Override
        public void onFailure(final Throwable caught) {
        }

        @Override
        public void onSuccess(final List<EntityData> result) {
            if (result == null || result.size() == 0) {
                return;
            }
            String path = "";
            for (final EntityData entity : result) {
                path = path + entity.getName() + " --> <br/>";
            }
            path = path.substring(0, path.length() - 10);
            selectPathInTree(result);
        }
    }

    class SelectInTreeHandler implements AsyncCallback<List<SubclassEntityData>> {

        private final TreeNode parentNode;

        private final List<EntityData> path;

        private final int index;

        public SelectInTreeHandler(final TreeNode parentNode, final List<EntityData> path, final int index) {
            super();
            this.parentNode = parentNode;
            this.index = index;
            this.path = path;
        }

        @Override
        public void onFailure(final Throwable caught) {
            //getEl().unmask();
            GWT.log("RPC error at select in tree for " + parentNode.getUserObject(), caught);
        }

        @Override
        public void onSuccess(final List<SubclassEntityData> children) {
            // getEl().unmask();

            TreeNode pathTreeNode = null;

            final EntityData nextParent = path.get(index);

            for (final SubclassEntityData subclassEntityData : children) {
                final SubclassEntityData childData = subclassEntityData;
                if (!hasChild(parentNode, childData.getName())) {
                    final TreeNode childNode = createTreeNode(childData);
                    if (childData.getSubclassCount() > 0) {
                        childNode.setExpandable(true);
                    }
                    parentNode.appendChild(childNode);
                }
                if (childData.equals(nextParent)) {
                    pathTreeNode = getDirectChild(parentNode, childData.getName());
                }
            }

            setSubclassesLoaded(parentNode, true);

            if (pathTreeNode != null) {
                expandDisabled = true;
                parentNode.expand();
                expandDisabled = false;
                if (path.size() - 1 == index) {
                    pathTreeNode.select();
                    final EntityData entityData = (EntityData) pathTreeNode.getUserObject();
                    OWLEntityData owlEntityData = DataFactory.getOWLEntityData(
                            DataFactory.getOWLClass(entityData.getName()),
                            entityData.getBrowserText());
                    getSelectionModel().setSelection(owlEntityData.getEntity());
                }
                else {
                    selectPathInTree(path, pathTreeNode, index + 1);
                }
            }
            else {
                GWT.log("Error at select in tree: could not find child " + nextParent + " of " + parentNode.getUserObject(), null);
            }
        }
    }

    private static String getNiceNoteCountText(final int noteCount) {
        return noteCount == 1 ? "There is 1 note" : "There are " + noteCount + " notes";
    }

}
