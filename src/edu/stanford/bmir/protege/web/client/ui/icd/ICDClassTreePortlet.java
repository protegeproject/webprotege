package edu.stanford.bmir.protege.web.client.ui.icd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.ObjectFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.event.ComboBoxCallback;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.tree.TreeNode;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.ICDServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.SubclassEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;
import edu.stanford.bmir.protege.web.client.ui.ontology.classes.ClassTreePortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.hierarchy.CreateClassPanel;
import edu.stanford.bmir.protege.web.client.ui.search.SearchResultsProxyImpl;
import edu.stanford.bmir.protege.web.client.ui.search.SearchUtil;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

public class ICDClassTreePortlet extends ClassTreePortlet {
    public static final String CREATE_ICD_SPECIFIC_ENTITES_PROP = "create_icd_specific_entities";
    public static final boolean CREATE_ICD_SPECIFIC_ENTITES_DEFAULT = true;
    private ComboBox searchCb;

    public ICDClassTreePortlet(Project project) {
        super(project);
    }

    public ICDClassTreePortlet(final Project project, final boolean showToolbar, final boolean showTitle,
            final boolean showTools, final boolean allowsMultiSelection, final String topClass) {
        super(project, showToolbar, showTitle, showTools, allowsMultiSelection, topClass);
    }

    @Override
    public void initialize() {
        super.initialize();
        addSearchInWindowButton();
    }


    @Override
    protected void onCreateCls() {
        final com.gwtext.client.widgets.Window selectWindow = new com.gwtext.client.widgets.Window();

        selectWindow.setBorder(false);
        selectWindow.setWidth(500);
        selectWindow.setBodyBorder(false);

        final CreateClassPanel createClassPanel = new CreateClassPanel(project, getCreateICDSpecificEntities(), null, getRootClsName(), ICDClassTreePortlet.this);

        createClassPanel.setAsyncCallback(new AsyncCallback<EntityData>() {

            public void onFailure(Throwable caught) {
                selectWindow.hide();
                MessageBox.alert("Error", "There was a problem at creating class. Please try again later.");
            }

            public void onSuccess(EntityData newClass) {
                selectWindow.hide();

                if (newClass == null) { //probably the user just pressed cancel
                    return;
                }

                MessageBox.alert("Success", "Created class successfully.");

                EntityData supercls = UIUtil.getFirstItem(createClassPanel.getParentsClses());

                if (supercls != null) {
                    TreeNode parentNode = findTreeNode(supercls.getName());
                    if (parentNode != null) {
                        parentNode.expand();

                        TreeNode newNode = getDirectChild(parentNode, newClass.getName());
                        if (newNode != null) {
                            newNode.select();
                        } else {
                            newNode = createTreeNode(newClass);
                            parentNode.appendChild(newNode);
                            newNode.select();
                        }
                    }
                }
            }
        });

        EntityData currentSelection = getSingleSelection();

        createClassPanel.setParentsClses(UIUtil.createCollection(currentSelection));
        selectWindow.add(createClassPanel);
        selectWindow.show();
        selectWindow.center();
    }

    @Override
    protected void moveClass(final EntityData cls, final EntityData oldParent, final EntityData newParent) {
        if (oldParent.equals(newParent)) {
            return;
        }
        OntologyServiceManager.getInstance().moveCls(
                project.getProjectName(),
                cls.getName(),
                oldParent.getName(),
                newParent.getName(),
                true,
                GlobalSettings.getGlobalSettings().getUserName(),
                UIUtil.getAppliedToTransactionString(getMoveClsOperationDescription(cls, oldParent, newParent),
                        cls.getName()), new MoveClassHandler(cls.getName(), oldParent.getName(), newParent.getName()));
    }

    @Override
    protected ToolbarButton createDeleteButton() {
        return null;
    }

    private boolean getCreateICDSpecificEntities() {
        return UIUtil.getBooleanConfigurationProperty(getPortletConfiguration(), CREATE_ICD_SPECIFIC_ENTITES_PROP, CREATE_ICD_SPECIFIC_ENTITES_DEFAULT);
    }

    @Override
    protected Component createSearchField() {
        RecordDef recordDef = new RecordDef(new FieldDef[] { new ObjectFieldDef("entity"), new StringFieldDef("browserText") });

        ArrayReader reader = new ArrayReader(recordDef);
        final SearchResultsProxyImpl proxy = new SearchResultsProxyImpl();
        proxy.setProjectName(getProject().getProjectName());
        proxy.setValueType(ValueType.Cls);
        final Store store = new Store(proxy, reader);

        searchCb = new ComboBox();
        searchCb.setStore(store);
        searchCb.setDisplayField("browserText");
        searchCb.setTypeAhead(false);
        searchCb.setLoadingText("Searching...");
        searchCb.setListWidth(400);
        searchCb.setWidth(150);
        searchCb.setPageSize(10);
        searchCb.setMinChars(3);
        searchCb.setQueryDelay(1);
        searchCb.setHideTrigger(true);
        searchCb.setHideLabel(true);
        searchCb.setResizable(true);
        searchCb.setEmptyText("Type search string");
        searchCb.setValueNotFoundText("No results");

        searchCb.addListener(new ComboBoxListenerAdapter() {
            @Override
            public boolean doBeforeQuery(ComboBox comboBox, ComboBoxCallback cb) {
                proxy.setSearchText(searchCb.getValueAsString());
                return true;
            }
            @Override
            public void onSelect(ComboBox comboBox, Record record, int index) {
                EntityData selection = (EntityData)record.getAsObject("entity");
                comboBox.setValue(UIUtil.getDisplayText(selection).trim());
                Collection<EntityData> selectionCollection = new ArrayList<EntityData>();
                selectionCollection.add(selection);
                setSelection(selectionCollection);
            }
            @Override
            public void onSpecialKey(Field field, EventObject e) {
                if (e.getKey() == EventObject.ENTER) {
                    String searchText = searchCb.getValueAsString();
                    if (searchText != null) {
                        searchText = searchText.trim();
                        if (searchText.length() > 0) {
                            proxy.setSearchText(searchText);
                            store.load(0, 10);
                        } else {
                            store.removeAll();
                        }
                    } else {
                        store.removeAll();
                    }
                }
            }

            @Override
            public void onValid(Field field) {
                String searchText = searchCb.getValueAsString();
                if (searchText == null || searchText.length() < 3) {
                    store.removeAll();
                    searchCb.collapse();
                }
            }
        });

        return searchCb;
    }

    //TODO: this will eventually be moved to the superclass
    protected void addSearchInWindowButton() {
        ToolbarButton searchInWindowButton = new ToolbarButton();
        searchInWindowButton.setCls("toolbar-button");
        searchInWindowButton.setIcon("images/magnifier.png");
        searchInWindowButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                onShowSearchWindow();
            }
        });
        getTopToolbar().addButton(searchInWindowButton);
    }

    private void onShowSearchWindow() {
        SearchUtil searchUtil = new SearchUtil(project, ICDClassTreePortlet.this, getSearchAsyncCallback());
        searchUtil.setBusyComponent(searchCb);
        searchUtil.setSearchedValueType(ValueType.Cls);
        searchUtil.search(searchCb.getText());
    }

    @Override
    public void setTreeNodeIcon(TreeNode node, EntityData entityData) {
        String status = entityData.getProperty("status");
        if (status == null) {
            node.setIconCls(null);
        } else if (status.equals(DisplayStatus.B.toString())) {
            node.setIconCls( "blue-display-status-icon");
        } else if (status.equals(DisplayStatus.Y.toString())) {
            node.setIconCls( "yellow-display-status-icon");
        }else if (status.equals(DisplayStatus.R.toString())) {
            node.setIconCls( "red-display-status-icon");
        }
    }

    @Override
    protected String createNodeText(EntityData entityData) {
        String browserText = super.createNodeText(entityData);
        if ("true".equals(entityData.getProperty("obsolete"))) {
           browserText = "<span style=\"font-style:italic; text-decoration: line-through;\">" + browserText + "</span>";
        }
        return browserText;
    }

    @Override
    public void setTreeNodeTooltip(TreeNode node, EntityData entityData) {
        String tooltip = UIUtil.getDisplayText(entityData);
        if ("true".equals(entityData.getProperty("obsolete"))) {
            tooltip = tooltip + " is obsolete.";
        }
        node.setTooltip(tooltip);
    }

    @Override
    protected void invokeGetSubclassesRemoteCall(String parentClsName, AsyncCallback<List<SubclassEntityData>> callback) {
        ICDServiceManager.getInstance().getSubclasses(project.getProjectName(), parentClsName, callback);
    }

}
