package edu.stanford.bmir.protege.web.client.ui.ontology.classes;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Node;
import com.gwtext.client.dd.DragData;
import com.gwtext.client.dd.DragDrop;
import com.gwtext.client.widgets.*;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.CheckItem;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtext.client.widgets.menu.event.BaseItemListener;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import com.gwtext.client.widgets.menu.event.CheckItemListener;
import com.gwtext.client.widgets.menu.event.CheckItemListenerAdapter;
import com.gwtext.client.widgets.tree.*;
import com.gwtext.client.widgets.tree.event.DefaultSelectionModelListenerAdapter;
import com.gwtext.client.widgets.tree.event.MultiSelectionModelListener;
import com.gwtext.client.widgets.tree.event.TreeNodeListenerAdapter;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.csv.CSVImportDialogController;
import edu.stanford.bmir.protege.web.client.csv.CSVImportViewImpl;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.actions.*;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.permissions.PermissionChecker;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.*;
import edu.stanford.bmir.protege.web.client.rpc.data.*;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.YesNoHandler;
import edu.stanford.bmir.protege.web.client.ui.library.progress.ProgressMonitor;
import edu.stanford.bmir.protege.web.client.ui.notes.editor.DiscussionThreadDialog;
import edu.stanford.bmir.protege.web.client.ui.ontology.entity.CreateEntityDialogController;
import edu.stanford.bmir.protege.web.client.ui.ontology.entity.CreateEntityInfo;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.search.SearchUtil;
import edu.stanford.bmir.protege.web.client.ui.upload.UploadFileDialogController;
import edu.stanford.bmir.protege.web.client.ui.upload.UploadFileResultHandler;
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

    private static final String PLACE_HOLDER_PANEL = "placeHolderPanel";

    private final String linkPattern = "{0}?ontology={1}&tab={2}&id={3}";

    private TreePanel treePanel;

    private final ToolbarButton createButton = new ToolbarButton("Create");

    private final ToolbarButton deleteButton = new ToolbarButton("Delete");

    protected CycleButton watchButton;

    private CheckItem lastSelectedWatchType;

    private CheckItem watchBranchItem;

    private CheckItem unwatchBranchItem;

    private boolean expandDisabled = false;

    private boolean showToolbar = true;

    private boolean showTitle = true;

    private boolean showTools = true;

    private boolean allowsMultiSelection = true;

    private String hierarchyProperty = null;

    private TreeNodeListenerAdapter nodeListener;

    private boolean registeredEventHandlers = false;
    
    private final DispatchServiceManager dispatchServiceManager;

    /*
    * Configuration constants and defaults
    */
    private static Set<EntityData> nodesWithNotesOpen = new HashSet<EntityData>();

    private final LoggedInUserProvider loggedInUserProvider;

    private final Provider<DiscussionThreadDialog> discussionThreadDialogProvider;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    @Inject
    public ClassTreePortlet(SelectionModel selectionModel,
                            EventBus eventBus,
                            DispatchServiceManager dispatchServiceManager,
                            final ProjectId projectId,
                            LoggedInUserProvider loggedInUserProvider,
                            Provider<DiscussionThreadDialog> discussionThreadDialogProvider,
                            LoggedInUserProjectPermissionChecker permissionChecker) {
        this(selectionModel, eventBus, dispatchServiceManager, loggedInUserProvider, projectId, true, true, true, true, null, discussionThreadDialogProvider, permissionChecker);
    }

    public ClassTreePortlet(SelectionModel selectionModel, EventBus eventBus, DispatchServiceManager dispatchServiceManager, LoggedInUserProvider loggedInUserProvider, final ProjectId projectId, final boolean showToolbar, final boolean showTitle, final boolean showTools, final boolean allowsMultiSelection, final String topClass, Provider<DiscussionThreadDialog> discussionThreadDialogProvider, LoggedInUserProjectPermissionChecker loggedInUserProjectPermissionChecker) {
        super(selectionModel, eventBus, projectId, loggedInUserProvider);
        this.showToolbar = showToolbar;
        this.dispatchServiceManager = dispatchServiceManager;
        this.loggedInUserProvider = loggedInUserProvider;
        this.showTitle = showTitle;
        this.showTools = showTools;
        this.allowsMultiSelection = allowsMultiSelection;
        this.discussionThreadDialogProvider = discussionThreadDialogProvider;
        this.permissionChecker = loggedInUserProjectPermissionChecker;
        registerEventHandlers();
        BUNDLE.style().ensureInjected();

        setLayout(new FitLayout());

        setTools(getTools());

        if (showTitle) {
            setTitle("Classes");
        }

        if (showToolbar) {
            addToolbarButtons();
        }

        final Panel bogusPanel = new Panel();
        bogusPanel.setId(PLACE_HOLDER_PANEL);
        bogusPanel.setHeight(560);
        add(bogusPanel);

        updateButtonStates();
        if (nodeListener == null) {
            //listener for click on the comment icon to display notes
            nodeListener = new TreeNodeListenerAdapter() {

                //TODO: this functionality is similar to that found in AbstractFieldWidget and
                @Override
                public boolean doBeforeClick(final Node node, final EventObject e) {

                    if (!nodesWithNotesOpen.contains(node.getUserObject())) { //not the second click in a double click
                        onCellClickOrDblClick(node, e);
                    }
                    return true;
                }

                private void onCellClickOrDblClick(Node node, EventObject e) {
                    final Element target = e.getTarget();
                    if (target != null) {
                        final String targetId = target.getId();
                        if (targetId.endsWith(SUFFIX_ID_LOCAL_ANNOTATION_IMG) || targetId.endsWith(SUFFIX_ID_LOCAL_ANNOTATION_COUNT)) {
                            showClassNotes(node);
                        }
                    }
                }

                @Override
                public void onContextMenu(final Node node, EventObject e) {
                    treePanel.getSelectionModel().select((TreeNode) node);
                    Menu contextMenu = new Menu();
                    contextMenu.setWidth("140px");

                    MenuItem menuShowInternalID = new MenuItem();
                    menuShowInternalID.setText("Show internal ID");
                    menuShowInternalID.addListener(new BaseItemListenerAdapter() {
                        @Override
                        public void onClick(BaseItem item, EventObject event) {
                            super.onClick(item, event);
                            showInternalID((EntityData) node.getUserObject());
                        }
                    });
                    contextMenu.addItem(menuShowInternalID);

                    MenuItem menuShowDirectLink = new MenuItem();
                    menuShowDirectLink.setText("Show direct link");
                    menuShowDirectLink.addListener(new BaseItemListenerAdapter() {
                        @Override
                        public void onClick(BaseItem item, EventObject event) {
                            super.onClick(item, event);
                            showDirectLink((EntityData) node.getUserObject());
                        }
                    });
                    contextMenu.addItem(menuShowDirectLink);

                    contextMenu.showAt(e.getXY()[0] + 5, e.getXY()[1] + 5);
                }
            };
        }
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
                    onWatchRemoved(event);
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
            TreeNode parentTn = findTreeNode(event.getParent());
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
                            if(getSelectedTreeNodeClass().equals(Optional.of(child))) {
                                Node parentNode = childTn.getParentNode();
                                treePanel.getSelectionModel().select((TreeNode) parentNode);
                            }
                            childTn.remove();
                            return;
                        }
                    }
                }
            }

    }

    protected String getMoveClsDescription() {
        return "Move class";
    }

    protected String getCreateClsLabel() {
        return "Create";
    }

    protected String getDeleteClsLabel() {
        return "Delete";
    }

    protected boolean getDeleteEnabled() {
        // TODO: These should be injected
        return true;
    }

    protected String getWatchClsLabel() {
        return "Watch class";
    }

    protected String getWatchBranchClsLabel() {
        return "Watch branch";
    }

    protected String getUnwatchClsLabel() {
        return "Remove watch";
    }

    private void onNotesChanged(EntityNotesChangedEvent event) {
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

    private void onWatchRemoved(WatchRemovedEvent event) {
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

    private void showInternalID(EntityData entity) {
        String className = entity.getName();
        MessageBox.showMessage(entity.getBrowserText() + " Internal Id", className);
    }

    private void showDirectLink(EntityData entity) {

        String url = "";
        try {
            String applicationURL = Window.Location.getHref();
            if (applicationURL.contains("?")) {
                applicationURL = applicationURL.substring(0, applicationURL.indexOf("?"));
            }
            if (applicationURL.contains("#")) {
                applicationURL = applicationURL.substring(0, applicationURL.indexOf("#"));
            }
            String tabName = getTab().getLabel();
            if (tabName.contains(".")) {
                tabName = tabName.substring(tabName.lastIndexOf(".") + 1);
            }
            String className = entity.getName();
            url = linkPattern.replace("{0}", applicationURL).
                    replace("{1}", URL.encodeQueryString(getProjectId().getId())).
                    replace("{2}", tabName).
                    replace("{3}", className == null ? "" : URL.encodeQueryString(className));
        } catch (Exception e) {
            e.printStackTrace();
        }

        MessageBox.showMessage("Direct link to " + entity.getBrowserText(), url);

    }

    private TreePanel createTreePanel() {
        treePanel = new TreePanel();
        treePanel.setHeight(560);
        treePanel.setAutoWidth(true);
        treePanel.setAnimate(true);
        treePanel.setAutoScroll(true);
        treePanel.setEnableDD(true);

        if (allowsMultiSelection) {
            treePanel.setSelectionModel(new MultiSelectionModel());
        }

        treePanel.addListener(new TreePanelListenerAdapter() {
            @Override
            public void onExpandNode(final TreeNode node) {
                // if (!expandDisabled && !treePanel.getRootNode().equals(node)) {
                if (!expandDisabled) {
                    getSubclasses(((EntityData) node.getUserObject()).getName(), node);
                }
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
                    Optional<OWLClassData> selectedClassDataFromTree = getSelectedTreeNodeClassData();
                    if (selectedClassDataFromTree.isPresent()) {
                        getSelectionModel().setSelection(selectedClassDataFromTree.get());
                    }

                }
            });
        }
        else if (selModel instanceof MultiSelectionModel) {
            ((MultiSelectionModel) selModel).addSelectionModelListener(new MultiSelectionModelListener() {
                public void onSelectionChange(final MultiSelectionModel sm, final TreeNode[] nodes) {
                    Optional<OWLClassData> selectedClassDataFromTree = getSelectedTreeNodeClassData();
                    if (selectedClassDataFromTree.isPresent()) {
                        getSelectionModel().setSelection(selectedClassDataFromTree.get());
                    }
                }
            });
        }
        else {
            GWT.log("Unknown tree selection model for class tree: " + selModel, null);
        }
    }

    @Override
    protected Tool[] getTools() {
        return showTools ? super.getTools() : new Tool[]{};
    }

    protected void addToolbarButtons() {
        setTopToolbar(new Toolbar());
        final Toolbar toolbar = getTopToolbar();

        setupCreateButton();
        toolbar.addButton(createButton);


        setupDeleteButton();
        toolbar.addButton(deleteButton);

        watchButton = createWatchButton();
        if (watchButton != null) {
            toolbar.addElement(watchButton.getElement());
        }

        final Component searchField = createSearchField();
        if (searchField != null) {
            toolbar.addSpacer();
            toolbar.addSeparator();
            toolbar.addText("&nbsp<i>Search</i>:&nbsp&nbsp");
            toolbar.addElement(searchField.getElement());
        }
    }

    protected void setupCreateButton() {
        createButton.setCls("toolbar-button");
        createButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(final Button button, final EventObject e) {
                onCreateCls(e.isShiftKey() ? CreateClassesMode.IMPORT_CSV : CreateClassesMode.CREATE_SUBCLASSES);
            }
        });
        createButton.setDisabled(true);
    }

    protected void setupDeleteButton() {
        deleteButton.setCls("toolbar-button");
        deleteButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(final Button button, final EventObject e) {
                onDeleteCls();
            }
        });
        deleteButton.setDisabled(true);
    }

    protected CycleButton createWatchButton() {
        watchButton = new CycleButton();
        watchButton.setShowText(true);
        watchButton.setCls("toolbar-button");

        final CheckItem watchItem = new CheckItem();
        watchItem.setText(getWatchClsLabel());
        watchItem.setCls("toolbar-button");
        watchButton.addItem(watchItem);

        watchBranchItem = new CheckItem();
        watchBranchItem.setText(getWatchBranchClsLabel());
        watchBranchItem.setCls("toolbar-button");
        watchBranchItem.setChecked(true);
        watchButton.addItem(watchBranchItem);

        unwatchBranchItem = new CheckItem();
        unwatchBranchItem.setText(getUnwatchClsLabel());
        unwatchBranchItem.setCls("toolbar-button");
        watchButton.addItem(unwatchBranchItem);

        //the listener is needed to override behavior of cycle button
        CheckItemListener checkItemListener = new CheckItemListenerAdapter() {
            @Override
            public boolean doBeforeCheckChange(CheckItem item, boolean checked) {
                return false;
            }
        };

        BaseItemListener baseItemListener = new BaseItemListenerAdapter() {
            @Override
            public void onClick(BaseItem item, EventObject e) {
                CheckItem activeItem = (CheckItem) item;
                watchButton.setActiveItem(activeItem);
                if (activeItem != unwatchBranchItem) {
                    lastSelectedWatchType = activeItem;
                }

                //this is optional, and can be removed if it is confusing to the user:
                //if a user clicks in the watch menu on an item, we perform the operation right away to avoid one more click.
                if (activeItem.equals(watchItem)) {
                    onWatchCls();
                }
                else if (activeItem.equals(watchBranchItem)) {
                    onWatchBranchCls();
                }
                else if (activeItem.equals(unwatchBranchItem)) {
                    onUnwatchCls();
                }
            }
        };

        watchItem.addListener(baseItemListener);
        watchBranchItem.addListener(baseItemListener);
        unwatchBranchItem.addListener(baseItemListener);

        watchItem.addListener(checkItemListener);
        watchBranchItem.addListener(checkItemListener);
        unwatchBranchItem.addListener(checkItemListener);

        //listener for performing the specific watch action
        watchButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                CheckItem activeItem = watchButton.getActiveItem();
                if (activeItem.equals(watchItem)) {
                    onWatchCls();
                }
                else if (activeItem.equals(watchBranchItem)) {
                    onWatchBranchCls();
                }
                else if (activeItem.equals(unwatchBranchItem)) {
                    onUnwatchCls();
                }
            }
        });

        return watchButton;
    }

    protected void updateWatchedMenuState() {
        List<EntityData> entities = getSelectedTreeNodeEntityData();
        if (entities == null || entities.size() == 0) {
            return;
        }
        EntityData entityData = entities.iterator().next();
        Set<Watch<?>> watches = entityData.getWatches();
        if (!watches.isEmpty()) {
            watchButton.setActiveItem(unwatchBranchItem);
        }
        else {
            watchButton.setActiveItem(lastSelectedWatchType == null ? watchBranchItem : lastSelectedWatchType);
        }
    }

    protected Component createSearchField() {
        final TextField searchField = new TextField("Search: ", "search");
        searchField.setSelectOnFocus(true);
        searchField.setAutoWidth(true);
        searchField.setEmptyText("Type search string");
        searchField.addListener(new TextFieldListenerAdapter() {
            @Override
            public void onSpecialKey(final Field field, final EventObject e) {
                if (e.getKey() == EventObject.ENTER) {
                    SearchUtil searchUtil = new SearchUtil(getProjectId(), getSelectionModel(), getSearchAsyncCallback());
                    searchUtil.setBusyComponent(searchField);
                    searchUtil.setSearchedValueType(ValueType.Cls);
                    searchUtil.search(searchField.getText());
                }
            }
        });
        return searchField;
    }

    protected AsyncCallback<Boolean> getSearchAsyncCallback() {
        return new AsyncCallback<Boolean>() {
            public void onFailure(Throwable caught) {
                MessageBox.showAlert("Search error", "An error occurred during the search. Please try again.");
            }

            public void onSuccess(Boolean result) {
            }
        };
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
        final TreeNode node = findTreeNode(root, id, new ArrayList<TreeNode>());
        return node;
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

//    protected void onSubclassRemoved(final EntityData entity, final Collection<EntityData> subclasses) {
//        if (subclasses == null || subclasses.size() == 0) {
//            return;
//        }
//
//        final EntityData subclass = ((List<EntityData>) subclasses).get(0);
//        final TreeNode parentNode = findTreeNode(entity.getName());
//
//        if (parentNode == null) {
//            return;
//        }
//
//        // final TreeNode subclassNode = findTreeNode(parentNode, subclass.getName(), new ArrayList<TreeNode>());
//        final TreeNode subclassNode = getDirectChild(parentNode, subclass.getName());
//        if (subclassNode == null) {
//            return;
//        }
//
//        //if (subclassNode.getParentNode().equals(parentNode)) {
//        parentNode.removeChild(subclassNode);
//        if (parentNode.getChildNodes().length < 1) {
//            parentNode.setExpandable(false);
//        }
//        //}
//    }

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
        MessageBox.showAlert("No class selected", "Please select a class to delete.");
    }

    private void deleteCls(final OWLClass cls) {
        dispatchServiceManager.execute(new DeleteEntityAction(cls, getProjectId()), new DeleteClassHandler());
    }

    protected void onWatchCls() {
        final Optional<OWLClass> sel = getSelectedTreeNodeClass();
        if (!sel.isPresent()) {
            return;
        }
        EntityFrameWatch entityWatch = new EntityFrameWatch(sel.get());
        dispatchServiceManager.execute(new AddWatchAction(entityWatch, getProjectId(), getUserId()), new DispatchServiceCallback<AddWatchResult>() {
            @Override
            public void handleSuccess(AddWatchResult result) {

            }
        });
    }

    protected void onWatchBranchCls() {
        final Optional<OWLClass> sel = getSelectedTreeNodeClass();
        if (!sel.isPresent()) {
            return;
        }
        Watch<?> watch = new HierarchyBranchWatch(sel.get());
        dispatchServiceManager.execute(new AddWatchAction(watch, getProjectId(), getUserId()), new DispatchServiceCallback<AddWatchResult>() {

            @Override
            public void handleSuccess(AddWatchResult result) {

            }
        });
    }


    protected void onUnwatchCls() {
        for (TreeNode selTreeNode : getSelectedTreeNodes()) {
            Object userObject = selTreeNode.getUserObject();
            if (userObject instanceof EntityData) {
                Set<Watch<?>> watches = ((EntityData) userObject).getWatches();
                dispatchServiceManager.execute(new RemoveWatchesAction(watches, getProjectId(), getUserId()), new DispatchServiceCallback<RemoveWatchesResult>() {

                    @Override
                    public void handleSuccess(RemoveWatchesResult result) {
                    }
                });
            }
        }

    }

    @Override
    protected void afterRender() {
        getRootCls();
    }

    private void updateTreeNodeIcon(TreeNode treeNode) {
        setTreeNodeIcon(treeNode, (EntityData) treeNode.getUserObject());
    }

    public void setTreeNodeIcon(final TreeNode node, EntityData entityData) {
        if (entityData instanceof SubclassEntityData && ((SubclassEntityData) entityData).isDeprecated()) {
            node.setIconCls(BUNDLE.style().deprecatedClassIcon());
        }
        else {
            node.setIconCls(BUNDLE.style().classIcon());
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

    protected String getStoredSubclassCount(final TreeNode node) {
        return node.getAttribute("scc");
    }

    public boolean isSubclassesLoaded(final TreeNode node) {
        final String val = node.getAttribute("subclassesLoaded");
        return val != null && val.equals("true");
    }

    public void setSubclassesLoaded(final TreeNode node, final boolean loaded) {
        node.setAttribute("subclassesLoaded", loaded ? "true" : "false");
    }

    public void getRootCls() {
        OntologyServiceManager.getInstance().getRootEntity(getProjectId(), new GetRootClassHandler());
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
        return getMoveClsDescription() + ": " + getDisplayText(cls) + ". Old parent: " + getDisplayText(oldParent) + ", New parent: " + getDisplayText(newParent);
    }

    public void getPathToRoot(final OWLEntityData entity) {
        OntologyServiceManager.getInstance().getPathToRoot(getProjectId(), entity.getEntity().getIRI().toString(), new GetPathToRootHandler());
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


    private void setSelectionInTree(Optional<OWLEntityData> selection) {
        if(!selection.isPresent()) {
            return;
        }
        //happens only at initialization
        if (!isRendered() || treePanel == null || treePanel.getRootNode() == null) {
            return;
        }
        getPathToRoot(selection.get());
    }

    public void selectPathInTree(List<EntityData> path) {
        selectPathInTree(path, treePanel.getRootNode(), 0);
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
            text = text + "<span style=\"padding-left: 2px;\"><img id=\"" + idLocalAnnotationImg + "\" src=\"" + BUNDLE.commentSmallFilledIcon().getSafeUri().asString() + "\" title=\"" + getNiceNoteCountText(localAnnotationsCount) + " on this category. \nClick on the icon to see and edit the notes\" /></span>" + "<span id=\"" + idLocalAnnotationCnt + "\" style=\"font-size:95%;color:#15428B;font-weight:bold;\">" + localAnnotationsCount + "</span>";
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

    private void showClassNotes(final Node node) {
        SubclassEntityData subClassData = (SubclassEntityData) node.getUserObject();
        String name = subClassData.getName();
        OWLClass cls = DataFactory.getOWLClass(name);

        DiscussionThreadDialog dlg = discussionThreadDialogProvider.get();
        dlg.showDialog(cls);
    }

    private boolean hasChild(final TreeNode parentNode, final String childId) {
        return getDirectChild(parentNode, childId) != null;
    }

    protected void createRoot(EntityData rootEnitity) {
        if (rootEnitity == null) {
            rootEnitity = new EntityData("Root", "Root node is not defined");
        }
        remove(PLACE_HOLDER_PANEL);

        treePanel = createTreePanel();
        final TreeNode root = createTreeNode(rootEnitity);
        treePanel.setRootNode(root);
        add(treePanel);
        createSelectionListener();

        try {
            // FIXME: could not figure out why it throws exceptions sometimes, not elegant but it works
            doLayout();
        } catch (final Exception e) {
            GWT.log("Error at doLayout in class tree", e);
        }

        root.select();
        // MH: createTreeNode calls get subclasses, so it was being called twice
//        getSubclasses(rootEnitity.getName(), root);
        root.expand();
        setSelectionInTree(getSelectedEntityData());
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntityData> entityData) {
        if(getSelectedTreeNodeClassData().equals(entityData)) {
            return;
        }
        setSelectionInTree(entityData);
        updateWatchedMenuState();
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

        Optional<OWLEntityData> selection = getSelectedEntityData();
        if(selection.isPresent() && !selection.get().getEntity().isTopEntity()) {
            setSelectionInTree(selection);
        }
        else {
            root.expand();
        }
    }

    public void updateButtonStates() {
        createButton.setDisabled(true);
        deleteButton.setDisabled(true);
        permissionChecker.hasWritePermission(new DispatchServiceCallback<Boolean>() {
            @Override
            public void handleSuccess(Boolean result) {
                createButton.setDisabled(!result);
                deleteButton.setDisabled(!result);
            }
        });

        if (watchButton != null) {
            // This used to disable the button.  However, the buttons seem to be laid out only when the containing
            // tab is selected and they appear over other components before this.
            watchButton.setVisible(!loggedInUserProvider.getCurrentUserId().isGuest());
        }
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

    /*
     * ************ Remote procedure calls *****************
     */

    class GetRootClassHandler implements AsyncCallback<EntityData> {

        @Override
        public void onFailure(final Throwable caught) {
            if (getEl() != null) {
                // getEl().unmask();
            }
            GWT.log("RPC error at getting classes root ", caught);
        }

        @Override
        public void onSuccess(final EntityData rootEnitity) {
            if (getEl() != null) {
                //   getEl().unmask();
            }
            createRoot(rootEnitity);
        }
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
                    getSelectionModel().setSelection(owlEntityData);
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
