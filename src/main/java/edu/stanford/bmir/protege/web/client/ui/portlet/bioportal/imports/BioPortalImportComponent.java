package edu.stanford.bmir.protege.web.client.ui.portlet.bioportal.imports;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.ExtElement;
import com.gwtext.client.data.*;
import com.gwtext.client.widgets.*;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.grid.*;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;

import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.BioPortalReferenceData;
import edu.stanford.bmir.protege.web.client.rpc.data.BioPortalSearchData;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.notes.editor.NoteContentEditorHandler;
import edu.stanford.bmir.protege.web.client.ui.notes.editor.NoteContentEditorMode;
import edu.stanford.bmir.protege.web.client.ui.notes.editor.NoteEditorDialogController;
import edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm.ReferenceFieldWidget;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.notes.NoteContent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.HashMap;
import java.util.Map;

public class BioPortalImportComponent extends GridPanel {

    private Project project;
    private EntityData currentEntity;
    private EntityData property;

    private Map<String, Object> configPropertiesMap;
    private Store store;
    private RecordDef recordDef;

    private TextField searchStringTextField;
    private ToolbarButton searchButton;
    //private ToolbarButton searchAllButton;
    //private ToolbarButton createDNFRefButton; 
    private ToolbarButton previousButton;
    private ToolbarButton nextButton;
    private ToolbarTextItem searchCountText;
    private ToolbarTextItem pageCountText;
    private int currentPage;
    private int pageToLoad;   

    //private boolean ignoreSearchAllPressed = false;
    //private boolean searchAll = false;
    private boolean replaceExisting = false;
    private final boolean isSingleValued;

    private String currentValue; //TODO: logic is inverted - should not be here but in the widget; import should call a callback


    public BioPortalImportComponent(Project project, boolean isSingleValued) {
        this(project, null, new PropertyEntityData(null), isSingleValued);
    }

    public BioPortalImportComponent(Project project, ReferenceFieldWidget referenceFieldWidget,
            PropertyEntityData referenceProperty, boolean isSingleValued) {
        this.project = project;
        this.isSingleValued = isSingleValued;
        createGrid();
    }

    public void setConfigProperties(Map<String, Object> configPropertiesMap) {
        if (configPropertiesMap != null) {
            this.configPropertiesMap = configPropertiesMap;
        } else {
            GWT.log("The argument passed to setConfigurationProperties should not be null!");
            this.configPropertiesMap = new HashMap<String, Object>();
        }
    }

    private void createGrid() {
    	
    	recordDef = new RecordDef(new FieldDef[] {
				new StringFieldDef("id"), new StringFieldDef("shortId"),
				new StringFieldDef("idSelf"),
				new StringFieldDef("preferredName"),
				new StringFieldDef("uriBioPortal"),
				new StringFieldDef("ontologyId"),
				new StringFieldDef("ontologyAcronym"),
				new StringFieldDef("ontologyName"),							
				new StringFieldDef("ontologyUriBioPortal"),
				new StringFieldDef("matchingField") });
		
		ArrayReader reader = new ArrayReader(recordDef);
	    MemoryProxy dataProxy = new MemoryProxy(new Object[][] {});
	    store = new Store(dataProxy, reader);
	    
        ColumnConfig conceptIdShortCol = new ColumnConfig("Id", "shortId");
        ColumnConfig preferredNameCol = new ColumnConfig("Preferred Name", "preferredName");
        preferredNameCol.setId("preferredName");
        //ColumnConfig contentsCol = new ColumnConfig("Matched content", "contents");
        ColumnConfig recordTypeCol = new ColumnConfig("Found in", "matchingField");
        ColumnConfig ontologyCol = new ColumnConfig("Ontology", "ontologyName");
        ColumnConfig detailsCol = new ColumnConfig(" ", "viewDetails");
        ColumnConfig graphCol = new ColumnConfig(" ", "viewGraph");
        ColumnConfig importCol = new ColumnConfig(" ", "importLink");
        
        conceptIdShortCol.setWidth(120);
        recordTypeCol.setWidth(100);
        ontologyCol.setWidth(200);
        detailsCol.setWidth(25);
        graphCol.setWidth(30);
        importCol.setWidth(60);

        preferredNameCol.setRenderer(new Renderer() {
            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum,
                    Store store) {
                String text = record.getAsString("preferredName");
                return "<span class=\"bp-search-pref-name\">" + text +"</span>";
            }
        });

//        contentsCol.setRenderer(new Renderer() {
//            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum,
//                    Store store) {
//                String text = record.getAsString("contents");
//                return "<span class=\"bp-search-contents\">" + text +"</span>";
//            }
//        });

        conceptIdShortCol.setRenderer(new Renderer() {
            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum,
                    Store store) {
            	return "<a href= \"" + record.getAsString("uriBioPortal") + "\" target=\"_blank\""
                		+ " title=\"Go to BioPortal page\">"
                		+ record.getAsString("shortId") + "</a>";
//                return "<a href= \"" + getBioPortalVisualizeURL() + record.getAsString("ontologyVersionId") + "/"
//                + "?conceptid=" + URL.encodeComponent(record.getAsString("conceptIdShort")) + "\" target=\"_blank\">"
//                + UIUtil.getShortName(record.getAsString("conceptIdShort")) + "</a>";
            }
        });


        recordTypeCol.setRenderer(new Renderer() {
            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum,
                    Store store) {
            	String type = BioPortalConstants.getRecordTypePrintText(record.getAsString("matchingField"));
                //String type = BioPortalConstants.getRecordTypePrintText(record.getAsString("recordType"));
                return "<span class=\"bp-search-rec-type\">" + type +"</span>";
            }
        });

        ontologyCol.setRenderer(new Renderer() {
            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum,
                    Store store) {
//                return "<a href= \"" + getBioPortalOntologyURL() + record.getAsString("ontologyVersionId")
//                        + "\" target=\"_blank\">" + record.getAsString("ontologyDisplayLabel") + "</a>";
                return "<a href= \"" + record.getAsString("ontologyUriBioPortal") + "\" target=\"_blank\""
                        + " title=\"See details in BioPortal\">" + record.getAsString("ontologyName") + "</a>";
            }
        });

        detailsCol.setRenderer(new Renderer() {
            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum,
                    Store store) {
                return "<img src=\"images/details.png\"></img>";
            }

        });

        graphCol.setRenderer(new Renderer() {
            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum,
                    Store store) {
                return "<img src=\"images/graph.png\"></img>";
            }

        });

        importCol.setRenderer(new Renderer() {
            public String render(Object value, CellMetadata cellMetadata, final Record record, int rowIndex,
                    int colNum, Store store) {
                // the return string may contain ONLY ONE HTML TAG before the text,
                // otherwise GridCellListener would not receive the onClick event!
                return "<DIV style=\"color:#1542bb;text-decoration:underline;font-weight:bold\">Import</DIV>";
            }
        });
        importCol.setSortable(false);

        ColumnConfig[] columnConfigs = { conceptIdShortCol, preferredNameCol, recordTypeCol, /*contentsCol,*/ ontologyCol, detailsCol, graphCol, importCol };

        ColumnModel columnModel = new ColumnModel(columnConfigs);
        columnModel.setDefaultSortable(true);

        setHeight(200);
        setStore(store);
        setColumnModel(columnModel);
        setAutoWidth(true);
        stripeRows(true);
        setAutoExpandColumn("preferredName");

        addGridCellListener(new GridCellListenerAdapter() {
            @Override
            public void onCellClick(GridPanel grid, int rowIndex, int colindex, EventObject e) {
                if (grid.getColumnModel().getDataIndex(colindex).equals("importLink")) {
                    if (UIUtil.confirmOperationAllowed(project.getProjectId())) {
                        Record record = grid.getStore().getAt(rowIndex);
                        onImportReference(record);
                    }
                } else if (grid.getColumnModel().getDataIndex(colindex).equals("viewDetails")) {
                    Record record = grid.getStore().getAt(rowIndex);
                    onViewDetails(record);
                } else if (grid.getColumnModel().getDataIndex(colindex).equals("viewGraph")) {
                    Record record = grid.getStore().getAt(rowIndex);
                    onViewGraph(record);
                }
            }
        });

        searchStringTextField = new TextField();
        searchStringTextField.addListener(new TextFieldListenerAdapter() {
            @Override
            public void onSpecialKey(Field field, EventObject e) {
                if (e.getKey() == EventObject.ENTER) {
                	pageToLoad = 1;
                    reload();
                }
            }
        });
        searchStringTextField.setWidth(250);

        searchButton = new ToolbarButton(createLinkFont("Search in BioPortal", false));
        searchButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
            	pageToLoad = 1;
                reload();
            }
        });

        Toolbar topToolbar = new Toolbar();
        topToolbar.addText("&nbsp<i>Search for class</i>:&nbsp&nbsp");
        topToolbar.addElement(searchStringTextField.getElement());
        topToolbar.addSpacer();
        topToolbar.addButton(searchButton);
        setTopToolbar(topToolbar);
        
        previousButton = new ToolbarButton(createLinkFont(BioPortalConstants.PREVIOUS_BUTTON_TEXT, false));
        previousButton.setVisible(false);
        previousButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				pageToLoad = currentPage - 1;
				reload();				
			}
		});
        
        nextButton = new ToolbarButton(createLinkFont(BioPortalConstants.NEXT_BUTTON_TEXT, false));
        nextButton.setVisible(false);
        nextButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				pageToLoad = currentPage + 1;
				reload();				
			}
		});		

//        searchAllButton = new ToolbarButton(createLinkFont(BioPortalConstants.SHOW_ALL_BUTTON_TEXT, false));
//        searchAllButton.setEnableToggle(true);
//        searchAllButton.addListener(new ButtonListenerAdapter() {
//            @Override
//            public void onToggle(Button button, boolean pressed) {
//                searchAll = pressed;
//                if (!ignoreSearchAllPressed) {
//                    reload();
//                }
//            }
//        });
//
//        createDNFRefButton = new ToolbarButton(createLinkFont(BioPortalConstants.DNF_BUTTON_TEXT, false));
//        createDNFRefButton.addListener(new ButtonListenerAdapter() {
//            @Override
//            public void onClick(Button button, EventObject e) {
//                onLeaveAComment();
//            }
//        });

        searchCountText = new ToolbarTextItem("No results");
        pageCountText = new ToolbarTextItem("");

        Toolbar toolbar = new Toolbar();
        toolbar.addItem(searchCountText);
        toolbar.addButton(previousButton);
        toolbar.addItem(pageCountText);
        toolbar.addButton(nextButton);
        //toolbar.addButton(searchAllButton);
        toolbar.addFill();
        //toolbar.addButton(createDNFRefButton);
        setBottomToolbar(toolbar);

    }

    private String createLinkFont(String text, boolean alert) {
        final String blue_link = "#1542bb";
        final String red_link = "#bb4215";

        return "<font color='" + (alert ? red_link : blue_link) + "'><b><u>" + text + "</u></b></font>";
    }

    protected void onImportReference(Record record) {
        BioPortalReferenceData bpRefData = createBioPortalReferenceDataFromRecord(record);
        if(replaceExisting && isSingleValued){
            EntityData oldValueEntityData = new EntityData(currentValue);
            OntologyServiceManager.getInstance().replaceExternalReference(project.getProjectId(), currentEntity.getName(), bpRefData,
                    oldValueEntityData, Application.get().getUserId(),
                    getReplaceReferenceApplyToString(bpRefData, oldValueEntityData),
                    getImportBioPortalConceptHandler());
        } else {
            OntologyServiceManager.getInstance().createExternalReference(project.getProjectId(), currentEntity.getName(), bpRefData,
                    Application.get().getUserId(),
                    getImportReferenceApplyToString(bpRefData),
                    getImportBioPortalConceptHandler());
            replaceExisting = true;

        }
    }

    protected String getImportReferenceApplyToString(BioPortalReferenceData bpRefData) {
        return UIUtil.getAppliedToTransactionString("Imported reference for " + UIUtil.getDisplayText(currentEntity) +
                " and property " + UIUtil.getDisplayText(getProperty()) +". Reference: " + bpRefData.getPreferredName() + ", code: " + bpRefData.getConceptId(),
                getEntity().getName());
    }

    /*
     * TODO: The apply to string has to be fixed, when we refactor his class.
     * It should show the old value, but currently we only have the old value full name which is not user friendly.
     * This code should not be here, but in the external reference grid, where we have access to all the info.
     */
    protected String getReplaceReferenceApplyToString(BioPortalReferenceData bpRefData, EntityData oldValue) {
        return UIUtil.getAppliedToTransactionString("Replaced reference for " + getEntity().getBrowserText() +
                 " New reference: " + bpRefData.getPreferredName() + ", code: " + bpRefData.getConceptId(),
                getEntity().getName());
    }

    private void onViewDetails(Record record) {    	
        GWT.log("onViewDetails", null);
        final Window window = new Window();
        window.setWidth(650);
        window.setHeight(600);
        window.setLayout(new FitLayout());
        final Panel panel = new Panel();
        panel.setAutoScroll(true);
        window.add(panel);
        window.show();
        doMask(panel);

        BioPortalSearchData bpSearchData = new BioPortalSearchData();
        initBioPortalSearchData(bpSearchData);
        BioPortalReferenceData bpRefData = createBioPortalReferenceDataFromRecord(record);
       
        OntologyServiceManager.getInstance().getBioPortalSearchContentDetails(project.getProjectId(), bpSearchData,
                bpRefData, new AsyncCallback<String>() {
                    public void onFailure(Throwable caught) {                    	
                        doUnmask(panel);
                        window.close();
                        MessageBox.alert("Getting details from server failed. Please try it later.<BR>"
                                + "Reason for failure: " + caught.getMessage());
                    }

                    public void onSuccess(String result) {                    	
                        doUnmask(panel);
                        panel.setHtml(result);
                    }
                });
    }

    private void doUnmask(Panel panel) {
        ExtElement el = panel.getEl();
        if (el != null) {
            el.unmask();
        }
    }

    private void doMask(Panel panel) {
        ExtElement el = panel.getEl();
        if (el != null) {
            el.mask("Loading details...");
        }
    }

    private void onViewGraph(Record record) {
        GWT.log("onViewGraph", null);
        Window window = new Window();
        window.setWidth(800);
        window.setHeight(550);
        String classId = URL.encode(record.getAsString("id"));
        String ontologyAcronym = record.getAsString("ontologyAcronym");       
        window.add(getViewGraphContent(ontologyAcronym, classId));
        window.show();
    }

    private Component getViewGraphContent(String ontologyAcronym, String classId) {
        Panel html = new Panel();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<html><body>");
        String apikey = BioPortalConstants.DEFAULT_API_KEY;        
        stringBuilder.append("<iframe " + "width=\"770\" " + "height=\"520\" " + "frameborder=\"0\" "
                + "scrolling=\"no\" " + "marginwidth=\"0\" " + "marginheight=\"0\" "
                + "src='http://bioportal.bioontology.org/widgets/visualization/?ontology=" + ontologyAcronym 
                + "&class=" + classId + "&apikey=" + apikey + "'");
        stringBuilder.append("\">" + "</iframe></body></html>");
        html.setHtml(stringBuilder.toString());
        return html;
    }

//    public static String replaceSpaces(String text) {
//        return text.replaceAll(" ", "%20");
//    }

//    private void onLeaveAComment() {
//        createReferenceIfUserComments();
//    }

    public EntityData getEntity() {
        return currentEntity;
    }
    
    public void setEntity(EntityData newEntity) {
        setEntity(newEntity, false);
    }

	public void setEntity(EntityData newEntity, boolean refreshUI) {
		if (currentEntity != null && currentEntity.equals(newEntity)) {
			return;
		}
		currentEntity = newEntity;
		
		String defSearchString = (String) configPropertiesMap
				.get(BioPortalConstants.CONFIG_PROPERTY_DEFAULT_SEARCH_STRING);				
		if (defSearchString == null) {
			// Do nothing
		} else if (defSearchString.equals(DefaultSearchStringTypeEnum.None
				.toString())) {
			// Do nothing
		} else if (defSearchString.equals(DefaultSearchStringTypeEnum.Entity
				.toString())) {
			refreshUI = true;
			searchStringTextField.setValue(currentEntity.getBrowserText());			
		} else if (defSearchString
				.startsWith(DefaultSearchStringTypeEnum.Property.toString())) {
			refreshUI = true;
			/* TODO get the name of the property of the  entity that we wish to display
		    String propName = defSearchString.substring(DefaultSearchStringType.Property.toString().length());
		    if (propName.equals("BrowserText")) {
		    	searchStringTextField.setValue(_entity.getBrowserText());
		    } else if () {
		     	....
		    }
		    */
		} else {
			searchStringTextField.setValue(defSearchString);
		}
		if (refreshUI) {
			pageToLoad = 1;
			reload();
		}
	}

    public EntityData getProperty() {
        return property;
    }

    public void setProperty(EntityData property) {
        this.property = property;
    }

    public void setReplaceExisting(boolean replace) {
        replaceExisting = replace;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    protected void reload() {
        store.removeAll();

        String searchString = searchStringTextField.getText();
        if (searchString != null && searchString.length() > 0) {
            final ExtElement el = getEl();
            if (el != null) {
                el.mask("Loading search results", true);
            }
            if (configPropertiesMap != null) {
                BioPortalSearchData bpSearchData = new BioPortalSearchData();
                initBioPortalSearchData(bpSearchData);
                OntologyServiceManager.getInstance().getBioPortalSearchContent(project.getProjectId(), searchString,
                        bpSearchData, new GetSearchURLContentHandler());   
            } else {
                GWT.log("configPropertiesMap should have been initialized!", new Exception(
                        "reload() method called before configPropertiesMap has been initialized."));
            }
        }
    }

    private void initBioPortalSearchData(BioPortalSearchData bpSearchData) {
    	bpSearchData.setPageToLoad(pageToLoad);
		bpSearchData.setPageSize(BioPortalConstants.PAGE_SIZE);
    }

//    private String getBioPortalRestBaseURL() {
//        String res = BioPortalConstants.DEFAULT_BIOPORTAL_REST_BASE_URL;
//        if (configPropertiesMap != null) {
//            res = (String) configPropertiesMap.get(BioPortalConstants.CONFIG_PROPERTY_BIOPORTAL_REST_BASE_URL);
//            if (res == null) {
//                res = BioPortalConstants.DEFAULT_BIOPORTAL_REST_BASE_URL;
//            }
//        }
//
//        return res;
//    }
//
//    private String getBioPortalRestCallSuffix() {
//        String res = BioPortalConstants.DEFAULT_BIOPORTAL_REST_CALL_SUFFIX;
//        if (configPropertiesMap != null) {
//            res = (String) configPropertiesMap.get(BioPortalConstants.CONFIG_PROPERTY_BIOPORTAL_REST_CALL_SUFFIX);
//            if (res == null) {
//                res = BioPortalConstants.DEFAULT_BIOPORTAL_REST_CALL_SUFFIX;
//            }
//        }
//
//        return res;
//    }
//
//    private String getBioPortalOntologyURL() {
//        String res = BioPortalConstants.DEFAULT_BIOPORTAL_ONTOLOGY_URL;
//        if (configPropertiesMap != null) {
//            res = (String) configPropertiesMap.get(BioPortalConstants.CONFIG_PROPERTY_BIOPORTAL_BASE_URL);
//            if (res != null) {
//                res = res + BioPortalConstants.BP_ONTOLOGY_STR + "/";
//            } else {
//                res = BioPortalConstants.DEFAULT_BIOPORTAL_ONTOLOGY_URL;
//            }
//        }
//
//        return res;
//    }

//    private String getBioPortalVisualizeURL() {
//        String res = BioPortalConstants.DEFAULT_BIOPORTAL_VISUALIZE_URL;
//        if (configPropertiesMap != null) {
//            res = (String) configPropertiesMap.get(BioPortalConstants.CONFIG_PROPERTY_BIOPORTAL_BASE_URL);
//            if (res != null) {
//                res = res + BioPortalConstants.BP_VISUALIZE_STR + "/";
//            } else {
//                res = BioPortalConstants.DEFAULT_BIOPORTAL_VISUALIZE_URL;
//            }
//        }
//
//        return res;
//    }

//    private String getBioPortalSearchOptions() {
//        String res = "";
//        if (configPropertiesMap != null) {
//            res = (String) configPropertiesMap.get(BioPortalConstants.CONFIG_PROPERTY_SEARCH_OPTIONS);
//            if (res == null) {
//                res = "";
//            }
//        }
//
//        return res;
//    }
//
//    private String getBioPortalSearchPageOption(boolean all) {
//        if (all) {
//            //do not restrict search pages
//            return null;
//        }
//        String res = BioPortalConstants.DEFAULT_BIOPORTAL_SEARCH_ONE_PAGE_OPTION;
//        if (configPropertiesMap != null) {
//            res = (String) configPropertiesMap.get(BioPortalConstants.CONFIG_PROPERTY_SEARCH_ONE_PAGE_OPTION);
//            if (res == null) {
//                res = BioPortalConstants.DEFAULT_BIOPORTAL_SEARCH_ONE_PAGE_OPTION;
//            }
//        }
//
//        return res;
//    }

//    public void createReferenceIfUserComments() {
//        NoteEditorDialogController controller = new NoteEditorDialogController(new NoteContentEditorHandler() {
//            @Override
//            public void handleAccept(Optional<NoteContent> noteContent) {
//                if (noteContent.isPresent()) {
//                    createDNFReference(noteContent.get());
//                }
//            }
//        });
//        controller.setMode(NoteContentEditorMode.NEW_TOPIC);
//        WebProtegeDialog.showDialog(controller);
//    }

//    public void createDNFReference(NoteContent noteContent) {
//        GWT.log("onCreateDNFReference", null);
//        BioPortalReferenceData bpRefData = new BioPortalReferenceData();
//
//        initBioPortalReferenceData(bpRefData);
//
//        bpRefData.setBpUrl(null);
//        bpRefData.setConceptId(BioPortalConstants.DNF_CONCEPT_ID);
//        bpRefData.setConceptIdShort(BioPortalConstants.DNF_CONCEPT_ID_SHORT);
//        bpRefData.setOntologyVersionId(null);
//        bpRefData.setOntologyName(null);
//        bpRefData.setPreferredName(BioPortalConstants.DNF_CONCEPT_LABEL);
//        bpRefData.setBpUrl(null);//do not use the BP rest URL to find out more information about this concept
//        OntologyServiceManager.getInstance().createExternalReference(
//                projectId,
//                currentEntity.getName(),
//                bpRefData,
//                Application.get().getUserId(),
//                UIUtil.getAppliedToTransactionString("Create a 'Did not find' reference on "
//                        + getEntity().getBrowserText() + " "
//                        + (String) configPropertiesMap.get(BioPortalConstants.CONFIG_PROPERTY_REFERENCE_PROPERTY), getEntity().getName()),
//                getCreateDNFConceptHandler(noteContent));
//    }

    public void createReference(String ontologyVersionId, String conceptId, String conceptIdShort, String preferredName, String url) {
        GWT.log("onCreateReference", null);
        BioPortalReferenceData bpRefData = new BioPortalReferenceData();

        initBioPortalReferenceData(bpRefData);

        bpRefData.setBpUrl(url);
        bpRefData.setConceptId(conceptId);
        bpRefData.setConceptIdShort(conceptIdShort);
        bpRefData.setOntologyVersionId(ontologyVersionId);
        bpRefData.setOntologyName(null);
        bpRefData.setPreferredName(preferredName);
        bpRefData.setBpUrl(null);//do not use the BP rest URL to find out more information about this concept
        OntologyServiceManager.getInstance().createExternalReference(
                project.getProjectId(),
                currentEntity.getName(),
                bpRefData,
                Application.get().getUserId(),
                UIUtil.getAppliedToTransactionString("Created reference on " + getEntity().getBrowserText() + " "
                        + (String) configPropertiesMap.get(BioPortalConstants.CONFIG_PROPERTY_REFERENCE_PROPERTY), getEntity().getName()),
                getCreateManualreferenceHandler());
    }

    public void replaceReference(String ontologyVersionId, String conceptId, String conceptIdShort, String preferredName, String url, String oldInstanceName) {
        GWT.log("onCreateReference", null);
        BioPortalReferenceData bpRefData = new BioPortalReferenceData();

        initBioPortalReferenceData(bpRefData);

        bpRefData.setBpUrl(url);
        bpRefData.setConceptId(conceptId);
        bpRefData.setConceptIdShort(conceptIdShort);
        bpRefData.setOntologyVersionId(ontologyVersionId);
        bpRefData.setOntologyName(null);
        bpRefData.setPreferredName(preferredName);
        bpRefData.setBpUrl(null);//do not use the BP rest URL to find out more information about this concept

        EntityData oldValueEntityData = new EntityData(oldInstanceName);
        OntologyServiceManager.getInstance().replaceExternalReference(
                project.getProjectId(),
                currentEntity. getName(),
                bpRefData,
                oldValueEntityData,
                Application.get().getUserId(),
                UIUtil.getAppliedToTransactionString("Created reference on " + getEntity().getBrowserText() + " "
                        + (String) configPropertiesMap.get(BioPortalConstants.CONFIG_PROPERTY_REFERENCE_PROPERTY), getEntity().getName()),
                getCreateManualreferenceHandler());
    }

    private BioPortalReferenceData createBioPortalReferenceDataFromRecord(Record record) {
    	String ontologyId = record.getAsString("ontologyId");
        String conceptId = record.getAsString("idSelf");
        String conceptIdShort = record.getAsString("shortId");      
        String ontologyName = record.getAsString("ontologyName");
        String preferredName = record.getAsString("preferredName");
        String url = record.getAsString("uriBioPortal");

        BioPortalReferenceData bpRefData = new BioPortalReferenceData();

        initBioPortalReferenceData(bpRefData);

        bpRefData.setBpUrl(url);
        bpRefData.setConceptId(conceptId);
        bpRefData.setConceptIdShort(conceptIdShort);
        bpRefData.setOntologyVersionId(ontologyId);
        bpRefData.setOntologyName(ontologyName);
        bpRefData.setPreferredName(preferredName);       
        
//        String ontologyVersionId = record.getAsString("ontologyVersionId");
//        String conceptId = record.getAsString("conceptId");
//        String conceptIdShort = record.getAsString("conceptIdShort");
//        String ontologyName = record.getAsString("ontologyDisplayLabel");
//        String preferredName = record.getAsString("preferredName");
//
//        BioPortalReferenceData bpRefData = new BioPortalReferenceData();
//
//        initBioPortalReferenceData(bpRefData);
//
//        String url = getBioPortalVisualizeURL() + ontologyVersionId + "/?conceptid=" + URL.encodeComponent(conceptId);
//        bpRefData.setBpUrl(url);
//
//        bpRefData.setConceptId(conceptId);
//        bpRefData.setConceptIdShort(conceptIdShort);
//        bpRefData.setOntologyVersionId(ontologyVersionId);
//        bpRefData.setOntologyName(ontologyName);
//        bpRefData.setPreferredName(preferredName);
//        bpRefData.setBpRestBaseUrl(getBioPortalRestBaseURL());
//        bpRefData.setBpRestCallSuffix(getBioPortalRestCallSuffix());

        return bpRefData;
    }

    private void initBioPortalReferenceData(BioPortalReferenceData bpRefData) {
        bpRefData.setReferenceClassName((String) configPropertiesMap.get(BioPortalConstants.CONFIG_PROPERTY_REFERENCE_CLASS));
        bpRefData.setReferencePropertyName((String) configPropertiesMap.get(BioPortalConstants.CONFIG_PROPERTY_REFERENCE_PROPERTY));
        bpRefData.setUrlPropertyName((String) configPropertiesMap.get(BioPortalConstants.CONFIG_PROPERTY_URL_PROPERTY));
        bpRefData.setOntologyNamePropertyName((String) configPropertiesMap.get(BioPortalConstants.CONFIG_PROPERTY_ONTOLOGY_NAME_PROPERTY));
        bpRefData.setOntologyNameAltPropertyName((String) configPropertiesMap
                .get(BioPortalConstants.CONFIG_PROPERTY_ONTOLOGY_NAME_ALT_PROPERTY));
        bpRefData.setOntologyIdPropertyName((String) configPropertiesMap.get(BioPortalConstants.CONFIG_PROPERTY_ONTOLGY_ID_PROPERTY));
        bpRefData.setConceptIdPropertyName((String) configPropertiesMap.get(BioPortalConstants.CONFIG_PROPERTY_CONCEPT_ID_PROPERTY));
        bpRefData
                .setConceptIdAltPropertyName((String) configPropertiesMap.get(BioPortalConstants.CONFIG_PROPERTY_CONCEPT_ID_ALT_PROPERTY));
        bpRefData.setPreferredLabelPropertyName((String) configPropertiesMap
                .get(BioPortalConstants.CONFIG_PROPERTY_PREFERRED_LABEL_PROPERTY));

        bpRefData.setImportFromOriginalOntology(getImportFromOriginalOntology());
    }

    public boolean getImportFromOriginalOntology() {
        Boolean b = (Boolean) configPropertiesMap.get(BioPortalConstants.CONFIG_PROPERTY_IMPORT_FROM_ORIGINAL_ONTOLOGY);
        return b == null ? BioPortalConstants.DEFAULT_IMPORT_FROM_ORIGINAL_ONTOLOGIES : b.booleanValue();
    }

    class GetSearchURLContentHandler extends AbstractAsyncHandler<BioPortalSearchResultsBean> {
        @Override
        public void handleFailure(Throwable caught) {
            if (getEl() != null) { //how can it be null?
                doUnmask(BioPortalImportComponent.this);
            }
            GWT.log("Could not retrive BioPortal search results for " + currentEntity, null);
        }

        @Override
        public void handleSuccess(BioPortalSearchResultsBean searchResults) {
            if (getEl() != null) { //how can it be null?
                doUnmask(BioPortalImportComponent.this);
            }
            if (searchResults != null) {
				for (BioPortalResultBean r : searchResults.getResults()) {
					Record record = recordDef.createRecord(new Object[] {
							r.getId(), r.getShortId(), r.getIdSelf(),
							r.getPreferredName(), r.getUriBioPortal(),
							r.getOntologyId(), r.getOntologyAcronym(),
							r.getOntologyName(), r.getOntologyUriBioPortal(),
							r.getMatchingField() });
					store.add(record);
				}				
				currentPage = searchResults.getPageNum();
				int totalPages = searchResults.getTotalPages();

				if (currentPage == 1) {
					previousButton.setVisible(false);
					if (searchResults.getTotalPages() > 1)
						nextButton.setVisible(true);
					else
						nextButton.setVisible(false);
				} else if (currentPage == totalPages) {
					previousButton.setVisible(true);
					nextButton.setVisible(false);
				} else {
					previousButton.setVisible(true);
					nextButton.setVisible(true);
				}
				int startIndex = ((currentPage-1) * BioPortalConstants.PAGE_SIZE) + 1;
				int endIndex = startIndex + searchResults.getResults().size() - 1;                 				                	
                if (totalPages > 1) {                	
                	searchCountText.setText("Results " + startIndex + " to " + endIndex + " of " + searchResults.getNumResults() + "  |  ");
                	pageCountText.setText("Page " + currentPage + " of " + totalPages);
                	pageCountText.setVisible(true);
                }
                else {
                	if (searchResults.getNumResults()>1)
                		searchCountText.setText(searchResults.getNumResults() + " results");
                	else if (searchResults.getNumResults()==1)         
                		searchCountText.setText("1 result");
                	else 
                		searchCountText.setText("No results");                	
                	pageCountText.setVisible(false);
                }
            }
//            store.loadXmlData(searchXml, true);
//            searchCountText.setText(store.getTotalCount() + " / " + extractNumResultsTotal(searchXml) + " results shown.");
//            searchAllButton.setText(createLinkFont(BioPortalConstants.SHOW_ALL_BUTTON_TEXT,
//                    !searchAllButton.isPressed() && extractNumPages(searchXml)>1));
//            createDNFRefButton.setText(createLinkFont(BioPortalConstants.DNF_BUTTON_TEXT, store.getTotalCount() == 0));
                
        }

//        private int extractNumPages(final String searchXml) {
//        	return extractAttributeValue(searchXml, BioPortalConstants.XML_ELEMENT_NUM_PAGES);
//        }
//
//        private int extractNumResultsTotal(final String searchXml) {
//        	return extractAttributeValue(searchXml, BioPortalConstants.XML_ELEMENT_NUM_RESULTS_TOTAL);
//        }

//        private int extractAttributeValue(final String searchXml, final String attrName) {
//            final String start_el = "<" + attrName + ">";
//            final String end_el = "</" + attrName + ">";
//            int res = 0;
//            //This is a hacky solution in order to avoid the overhead of using the XML parser.
//            int start_idx = searchXml.indexOf(start_el);
//            if (start_idx >= 0) {
//                int val_idx = start_idx + start_el.length();
//                int end_idx = searchXml.indexOf(end_el, val_idx);
//                if (end_idx >= 0) {
//                    res = Integer.parseInt(searchXml.substring(val_idx, end_idx));
//                }
//            }
//            return res;
//        }
    }

    protected AbstractAsyncHandler<EntityData> getImportBioPortalConceptHandler() {
        return new ImportBioPortalConceptHandler();
    }

    class ImportBioPortalConceptHandler extends AbstractAsyncHandler<EntityData> {
        @Override
        public void handleFailure(Throwable caught) {
            doUnmask(BioPortalImportComponent.this);
            GWT.log("Could not import BioPortal concept for " + currentEntity, null);
            MessageBox.alert("Import operation failed!");
        }

        @Override
        public void handleSuccess(EntityData refInstance) {
            doUnmask(BioPortalImportComponent.this);
            MessageBox.alert(refInstance != null ? "Import successful: " + refInstance + 
            		" was created as a subclass of " + currentEntity.getBrowserText()
                    : "Import operation did not succeed");
            project.forceGetEvents();
        }
    }

    protected AbstractAsyncHandler<EntityData> getCreateManualreferenceHandler() {
        return new CreateManualreferenceHandler();
    }

    class CreateManualreferenceHandler extends AbstractAsyncHandler<EntityData> {
        @Override
        public void handleFailure(Throwable caught) {
            doUnmask(BioPortalImportComponent.this);
            GWT.log("Could not create manual reference for " + currentEntity, null);
            MessageBox.alert("Reference creation failed!");
        }

        @Override
        public void handleSuccess(EntityData refInstance) {
            doUnmask(BioPortalImportComponent.this);
            MessageBox.alert(refInstance != null ? "Reference creation SUCCEDED! Reference instance: " + refInstance
                    : "Reference creation DID NOT SUCCEDED!");
        }
    }

//    protected AbstractAsyncHandler<EntityData> getCreateDNFConceptHandler(NoteContent noteContent) {
//        return new CreateDNFConceptHandler(noteContent);
//    }
//
//    class CreateDNFConceptHandler extends AbstractAsyncHandler<EntityData> {
//        private NoteContent noteContent;
//
//        public CreateDNFConceptHandler(NoteContent noteContent) {
//            this.noteContent = noteContent;
//        }
//
//        @Override
//        public void handleFailure(Throwable caught) {
//            doUnmask(BioPortalImportComponent.this);
//            GWT.log("Could not create DNF reference for " + currentEntity, null);
//            MessageBox.alert("Reference creation failed!");
//        }
//
//        @Override
//        public void handleSuccess(EntityData refInstance) {
//            doUnmask(BioPortalImportComponent.this);
//            MessageBox.alert(refInstance != null ? "Reference creation SUCCEDED! Reference instance: " + refInstance : "Reference creation DID NOT SUCCEDED!");
//            ReferenceFieldWidget.addUserComment(project.getProjectId(), noteContent, DataFactory.getOWLClass(refInstance.getName()));
//        }
//    }
}
