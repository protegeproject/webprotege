package edu.stanford.bmir.protege.web.client.ui.portlet.bioportal;

import java.util.Collection;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.ObjectFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.XmlReader;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.RowSelectionModel;
import com.gwtext.client.widgets.grid.event.RowSelectionListenerAdapter;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.RowLayoutData;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.bioportal.BioportalProposalsManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;
import edu.stanford.bmir.protege.web.client.ui.ontology.search.BioPortalConstants;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.util.AbstractValidatableTab;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

public class BioPortalProposalsPortlet extends AbstractEntityPortlet {

    private GridPanel grid;
    private Store store;
    private BioPortalNotePanel bpNotePanel;
    private Widget actionsBar;

    private String bpRestBase;
    private String bpOntologyId;

    public BioPortalProposalsPortlet(Project project) {
        super(project);
    }

    @Override
    public void initialize() {
        BioportalProposalsManager.getBioportalProposalsManager();  // init manager
        initUsersCache();

        buildUI();
    }

    private void initUsersCache() {
        BioPortalUsersCache.initBioPortalUsersMap(new AbstractAsyncHandler<Void>() {
            @Override
            public void handleFailure(Throwable caught) { }
            @Override
            public void handleSuccess(Void result) {
                if (grid.isRendered()) {
                    grid.getView().refresh();
                }
            }
        }, false);
    }

    protected void buildUI() {
        //setLayout(new AnchorLayout());
        setLayout(new RowLayout());
        setAutoScroll(true);

        bpNotePanel = createNotePanel();
        actionsBar = createBpActionsBar();
        grid = createNotesGrid();

        add(grid, new RowLayoutData("30%"));
        add(actionsBar, new RowLayoutData(30));
        add(new Panel(), new RowLayoutData(10)); //hack to fix scorlling problem
        add(bpNotePanel, new RowLayoutData("60%"));
    }

    private Widget createBpActionsBar() {
        HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.getElement().getStyle().setProperty("padding", "5px");
        hPanel.getElement().getStyle().setProperty("width", "100%");
        //hPanel.getElement().getStyle().setMarginBottom(10, Unit.PX);
        hPanel.getElement().getStyle().setBackgroundColor("#E2EBF0");

        Anchor replyAnchor = new Anchor("<span style=\"font-weight:bold;\">Add new note</span>", true);
        replyAnchor.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                onAddNewNote();
            }
        });

        hPanel.add(replyAnchor);

        return hPanel;
    }

    protected BioPortalNotePanel createNotePanel() {
        BioPortalNotePanel bpNotePanel = new BioPortalNotePanel();
        bpNotePanel.setEntityData(getEntity());

        bpNotePanel.setUpdateNotesCallback(new AbstractAsyncHandler<Void>() {
            @Override
            public void handleFailure(Throwable caught) {
                GWT.log("Failed to retrieve the BP user id", caught);
            }
            @Override
            public void handleSuccess(Void result) {
                Record sel = grid.getSelectionModel().getSelected(); //FIXME
                reload(sel.getAsString(BioPortalNoteConstants.ID));
            }
        });
        return bpNotePanel;
    }

    protected GridPanel createNotesGrid() {
        GridPanel gridPanel = new GridPanel();
        XmlReader reader = new XmlReader("noteBean", new RecordDef(new FieldDef[]
              { new StringFieldDef(BioPortalNoteConstants.ID),
                new StringFieldDef(BioPortalNoteConstants.ONTOLOGY_ID),
                new StringFieldDef(BioPortalNoteConstants.TYPE),
                new StringFieldDef(BioPortalNoteConstants.AUTHOR),
                new StringFieldDef(BioPortalNoteConstants.CREATED),
                new StringFieldDef(BioPortalNoteConstants.SUBJECT),
                new StringFieldDef(BioPortalNoteConstants.BODY),
                new BooleanFieldDef(BioPortalNoteConstants.ARCHIVED),
                new StringFieldDef(BioPortalNoteConstants.STATUS),
                new ObjectFieldDef(BioPortalNoteConstants.ASSOCIATED)}));

        store = new Store(reader);

        gridPanel.setColumnModel(createColumnModel());

        gridPanel.setStore(store);
        gridPanel.setAutoWidth(true);
        gridPanel.stripeRows(true);
        gridPanel.setAutoExpandColumn(BioPortalNoteConstants.SUBJECT);
        gridPanel.setAutoScroll(true);
        gridPanel.setFrame(true);

        final RowSelectionModel sm = new RowSelectionModel(true);
        sm.addListener(new RowSelectionListenerAdapter() {
            @Override
            public void onRowSelect(RowSelectionModel sm, int rowIndex, Record record) {
                bpNotePanel.setRecord(record);
            }
        });
        gridPanel.setSelectionModel(sm);
        return gridPanel;
    }

    protected ColumnModel createColumnModel() {
        ColumnConfig createdCol = new ColumnConfig("Date", BioPortalNoteConstants.CREATED);
        createdCol.setWidth(200);
        createdCol.setRenderer(new Renderer() {
            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
                String created = record.getAsString(BioPortalNoteConstants.CREATED);
                if (created == null) { return "";}
                long createdLong = Long.parseLong(created);
                Date createdDate = new Date(createdLong);
                return createdDate.toString();
            }
        });

        ColumnConfig subjectCol = new ColumnConfig("Subject", BioPortalNoteConstants.SUBJECT);
        subjectCol.setId(BioPortalNoteConstants.SUBJECT);
        subjectCol.setRenderer(new Renderer() {
            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
                boolean archived = record.getAsBoolean(BioPortalNoteConstants.ARCHIVED);
                String subj = value == null ? "(no subject)" : value.toString();
                return archived ? "<span style=\"color:gray;\">" + subj + " (archived)" +"</span>" : subj;
            }
        });

        ColumnConfig authorCol = new ColumnConfig("Author", BioPortalNoteConstants.AUTHOR);
        authorCol.setRenderer(new Renderer() {
            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
                if (value == null) { return ""; } ;
                return BioPortalUsersCache.getBioPortalUserName(value.toString());
            }
        });

        ColumnConfig[] columnConfigs = {subjectCol, authorCol, createdCol};

        ColumnModel columnModel = new ColumnModel(columnConfigs);
        columnModel.setDefaultSortable(true);

        return columnModel;
    }

    @Override
    public void reload() {
        reload(null);
    }

    public void reload(final String selectedNoteId) {
        store.removeAll();
        bpNotePanel.setRecord(null);

        final EntityData entity = getEntity();
        if ( entity == null){
            setTitle("BioPortal notes and proposals (nothing selected)");
            return;
        }

        if ( bpOntologyId == null) {
            setTitle("BioPortal notes and proposals. * Linked BioPortal ontology not configured * Configure using the portlet configuration icon.");
            return;
        }

        setTitle("BioPortal notes and proposals for " + UIUtil.getDisplayText(getEntity()) + "  (loading...)");
        BioportalProposalsManager.getBioportalProposalsManager().getBioportalProposals(getProject().getProjectName(), bpRestBase, bpOntologyId, entity.getName(),
                new AbstractAsyncHandler<String>() {
            @Override
            public void handleFailure(Throwable caught) {
                GWT.log("Error at retrieving proposals for " +getEntity(), caught);
                setTitle("Could not retrieve proposals for " + UIUtil.getDisplayText(getEntity()));
            }

            @Override
            public void handleSuccess(String notesXml) {
                if (!entity.equals(getEntity())) {
                    store.removeAll();
                    return;
                }

                setTitle("BioPortal notes and proposals for " + UIUtil.getDisplayText(getEntity()));
                store.loadXmlData(notesXml, false);

                if (selectedNoteId != null) {
                    int index = store.find(BioPortalNoteConstants.ID, selectedNoteId, 0, false, true);
                    if (index != -1) {
                        grid.getSelectionModel().selectRow(index);
                    }
                }
            };
        });
    }

    protected void onAddNewNote() {
        if (!UIUtil.confirmIsLoggedIn()) {
            return;
        }

        NewNotePanel newNotePanel = new NewNotePanel();
        newNotePanel.setEntityData(getEntity());
        newNotePanel.setOntologyVersionId(bpOntologyId);

        newNotePanel.showPopup("New note on " + UIUtil.getDisplayText(getEntity()));
        newNotePanel.setNoteSentCallback(new AbstractAsyncHandler<Void>() {

            @Override
            public void handleFailure(Throwable caught) {
                // do nothing
            }

            @Override
            public void handleSuccess(Void result) {
                reload();
            }
        });
    }

    public Collection<EntityData> getSelection() {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void setPortletConfiguration(PortletConfiguration portletConfiguration) {
        super.setPortletConfiguration(portletConfiguration);
        initBioPortalParameters();
    }

    private void initBioPortalParameters() {
        bpRestBase = UIUtil.getStringConfigurationProperty(getPortletConfiguration(), BioPortalConstants.CONFIG_PROPERTY_BIOPORTAL_REST_BASE_URL, null);
        bpOntologyId = UIUtil.getStringConfigurationProperty(getPortletConfiguration(), BioPortalConstants.CONFIG_PROPERTY_ONTOLGY_ID_PROPERTY, null);
        //bpUserId = UIUtil.getStringConfigurationProperty(getPortletConfiguration(), BioPortalConstants.BP_USER_ID, BioPortalUsersCache.getWebProtegeBpUserId() ); //TODO: should be stored in the metaproject
    }


    @Override
    protected TabPanel getConfigurationPanel() {
        TabPanel configPanel =  super.getConfigurationPanel();
        configPanel.add(createBioPortalConfigurationPanel());
        return configPanel;
    }

    protected Panel createBioPortalConfigurationPanel() {
        FormPanel formPanel = new FormPanel();
        formPanel.setLabelWidth(100);
        formPanel.setPaddings(10);

        final TextField bpRestBase = new TextField("BioPortal REST Base", "bpRestBase");
        bpRestBase.setValue(UIUtil.getStringConfigurationProperty(getPortletConfiguration(), BioPortalConstants.CONFIG_PROPERTY_BIOPORTAL_REST_BASE_URL, null));
        final TextField ontIdField = new TextField("Linked BioPortal Ontology Id", "bpOntId");
        ontIdField.setValue(UIUtil.getStringConfigurationProperty(getPortletConfiguration(), BioPortalConstants.CONFIG_PROPERTY_ONTOLGY_ID_PROPERTY, null));

        formPanel.add(bpRestBase, new AnchorLayoutData("100%"));
        formPanel.add(ontIdField, new AnchorLayoutData("100%"));

        Panel bpConfigPanel = new AbstractValidatableTab() {
            @Override
            public void onSave() {
                UIUtil.setConfigurationPropertyValue(getPortletConfiguration(), BioPortalConstants.CONFIG_PROPERTY_BIOPORTAL_BASE_URL, bpRestBase.getValueAsString());
                UIUtil.setConfigurationPropertyValue(getPortletConfiguration(), BioPortalConstants.CONFIG_PROPERTY_ONTOLGY_ID_PROPERTY,ontIdField.getValueAsString());
                initBioPortalParameters();
            }

            @Override
            public boolean isValid() {
                return true; //TODO: make extra checks
            }
        };

        bpConfigPanel.setTitle("BioPortal");
        bpConfigPanel.add(formPanel);
        return bpConfigPanel;
    }

}
