package edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.BioPortalReferenceData;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import edu.stanford.bmir.protege.web.client.ui.ontology.classes.ClassTreePortlet;
import edu.stanford.bmir.protege.web.client.ui.portlet.bioportal.imports.BioPortalConstants;
import edu.stanford.bmir.protege.web.client.ui.selection.Selectable;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

import java.util.Collection;
import java.util.Map;

/**
 * Exposes an 'internal' reference to another class so that classes can refer to each other as property values.
 *
 * For example, Class A can have a property field P, which we wish to constrain to use only values that inherit from Class B.
 * This widget will display only values that inherit from Class B for population in property P.
 */
public class ClassInstanceReferenceFieldWidget extends ReferenceFieldWidget {
    private Window selectWindow;
    private Selectable selectable;
    private String topClass;
    private String objectType;
    private String ontologyIdPropertyName;
    private String conceptIdPropertyName;

    public ClassInstanceReferenceFieldWidget(Project project) {
        super(project);
    }

    @Override
    public Anchor createAddNewValueHyperlink() {
        final Anchor addNewValueHyperlink = super.createAddNewValueHyperlink();
        
        final Map<String, Object> widgetConfiguration = getWidgetConfiguration();
        final ProjectLayoutConfiguration projectLayoutConfiguration = getProject().getProjectLayoutConfiguration();
        addNewValueHyperlink.setHTML(
                InstanceGridWidgetConstants.getIconLink(
                        InstanceGridWidgetConstants.getAddNewValueActionDesc(widgetConfiguration, projectLayoutConfiguration, "Add value"),
                        InstanceGridWidgetConstants.getAddIcon(widgetConfiguration, projectLayoutConfiguration)));
        return addNewValueHyperlink;
    }

    @Override
    protected Anchor createReplaceNewValueHyperlink() {
        final Anchor replaceNewValueHyperlink = super.createReplaceNewValueHyperlink();
        
        final Map<String, Object> widgetConfiguration = getWidgetConfiguration();
        final ProjectLayoutConfiguration projectLayoutConfiguration = getProject().getProjectLayoutConfiguration();
        replaceNewValueHyperlink.setHTML(
                InstanceGridWidgetConstants.getIconLink(
                        InstanceGridWidgetConstants.getReplaceNewValueActionDesc(widgetConfiguration, projectLayoutConfiguration, "Replace value"),
                        InstanceGridWidgetConstants.getReplaceIcon(widgetConfiguration, projectLayoutConfiguration)));
        return replaceNewValueHyperlink;
    }

    @Override
    protected Anchor createAddExistingHyperlink() {
        final Map<String, Object> widgetConfiguration = getWidgetConfiguration();
        final ProjectLayoutConfiguration projectLayoutConfiguration = getProject().getProjectLayoutConfiguration();
        Anchor addNewLink = new Anchor(
                InstanceGridWidgetConstants.getIconLink(
                        InstanceGridWidgetConstants.getAddExistingValueActionDesc(widgetConfiguration, projectLayoutConfiguration, "Find value"),
                        InstanceGridWidgetConstants.getAddIcon(widgetConfiguration, projectLayoutConfiguration)), true);
        addNewLink.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (isWriteOperationAllowed()){
                //TODO fix this
                //onAddNewReference((String) ReferenceFieldWidget.this.getWidgetConfiguration().get(FormConstants.LABEL));
                PropertyEntityData prop = getProperty();
                onAddNewReference(prop == null ? "" : UIUtil.getDisplayText(prop));
                }
            }
        });

        return addNewLink;
    }

    @Override
    protected Anchor createReplaceExistingHyperlink() {
        final Map<String, Object> widgetConfiguration = getWidgetConfiguration();
        final ProjectLayoutConfiguration projectLayoutConfiguration = getProject().getProjectLayoutConfiguration();
        Anchor addNewLink = new Anchor(
                InstanceGridWidgetConstants.getIconLink(
                        InstanceGridWidgetConstants.getReplaceExistingValueActionDesc(widgetConfiguration, projectLayoutConfiguration, "Find & Replace <br/>value"),
                        InstanceGridWidgetConstants.getReplaceIcon(widgetConfiguration, projectLayoutConfiguration)), true);
        addNewLink.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (isWriteOperationAllowed()){
                //TODO fix this
                //onAddNewReference((String) ReferenceFieldWidget.this.getWidgetConfiguration().get(FormConstants.LABEL));
                PropertyEntityData prop = getProperty();
                onReplaceReference(prop == null ? "" : UIUtil.getDisplayText(prop));
                }
            }
        });

        return addNewLink;
    }

    @Override
    public void setup(Map<String, Object> widgetConfiguration, PropertyEntityData propertyEntityData) {
        super.setup(widgetConfiguration, propertyEntityData);
        this.topClass = (String) widgetConfiguration.get(FormConstants.TOP_CLASS);
        this.objectType = (String)widgetConfiguration.get(FormConstants.ONT_TYPE);
        this.ontologyIdPropertyName =  (String)bpSearchProperties.get(BioPortalConstants.CONFIG_PROPERTY_ONTOLGY_ID_PROPERTY);
        this.conceptIdPropertyName =  (String)bpSearchProperties.get(BioPortalConstants.CONFIG_PROPERTY_CONCEPT_ID_PROPERTY);
    }

    private void onAddNewReference(String string) {
        Window window = getSelectionWindow();
        window.show();
    }

    private void onReplaceReference(String string) {
        Window window = getSelectionWindow();
        window.show();
    }

    protected Window getSelectionWindow() {
        if (selectWindow == null) {
            selectWindow = new Window();
            selectWindow.setTitle("Select values");
            selectWindow.setWidth(600);
            selectWindow.setHeight(480);
            selectWindow.setMinWidth(300);
            selectWindow.setMinHeight(350);
            selectWindow.setLayout(new FitLayout());
            selectWindow.setPaddings(5);
            selectWindow.setButtonAlign(Position.CENTER);

            selectWindow.setCloseAction(Window.HIDE);
            selectWindow.setPlain(true);

            Button cancelButton = new Button("Cancel");
            cancelButton.addListener(new ButtonListenerAdapter() {
                @Override
                public void onClick(Button button, EventObject e) {
                    selectWindow.hide();
                }
            });

            Button selectButton = new Button("Select");
            selectButton.addListener(new ButtonListenerAdapter() {
                @Override
                public void onClick(Button button, EventObject e) {
                    Collection<EntityData> selection = getSelectable().getSelection();
                    if (selection == null || selection.size() == 0) {
                        MessageBox.alert("No selection", "No class selected. Please select a class from the tree.");
                        return;
                    }
                    final Collection<EntityData> selected = selectable.getSelection();

                    for (EntityData data : selected) {
                        final BioPortalReferenceData bpRefData = createBioPortalReferenceData(data);
                        if (isReplace()){
                           EntityData oldValueEntityData = new EntityData(store.getAt(0).getAsString(INSTANCE_FIELD_NAME));
                            OntologyServiceManager.getInstance().replaceExternalReference(
                                getProjectId(),
                                getSubject().getName(),
                                bpRefData,
                                    oldValueEntityData,
                                Application.get().getUserId(),
                                    getTransactionString(bpRefData), new ImportInternalReferenceHandler(selectWindow));
                        } else {
                            OntologyServiceManager.getInstance().createExternalReference(
                                getProjectId(),
                                getSubject().getName(),
                                bpRefData,
                                Application.get().getUserId(),
                                    getTransactionString(bpRefData), new ImportInternalReferenceHandler(selectWindow));
                        }
                    }

//                    updatePanel();
                    selectWindow.hide();
                }
            });

            selectWindow.add((Component) getSelectable());
            selectWindow.addButton(selectButton);
            selectWindow.addButton(cancelButton);
        }
        return selectWindow;
    }

    protected String getTransactionString(BioPortalReferenceData bpRefData) {
        return UIUtil.getAppliedToTransactionString("Added internal reference " + bpRefData.getPreferredName()
                + ", for property: " + getWidgetConfiguration().get(FormConstants.LABEL) + " and class: " +getSubject().getBrowserText(),
                getSubject().getName());
    }

    class ImportInternalReferenceHandler extends AbstractAsyncHandler<EntityData> {
        private Window selectWindow;

        public ImportInternalReferenceHandler(Window selectWindow) {
            this.selectWindow = selectWindow;
        }

        @Override
        public void handleFailure(Throwable caught) {
            selectWindow.getEl().unmask();
            GWT.log("Could not import Internal reference ", null);
            MessageBox.alert("Internal reference generation failed!");
        }

        @Override
        public void handleSuccess(EntityData refInstance) {
            selectWindow.getEl().unmask();
            getProject().forceGetEvents();
            if (refInstance != null) {
                //activate this code if we need it in the future
                //addUserCommentOnReference(getProject(), refInstance);
                refresh();
            } else {
                MessageBox.alert("Internal reference operation DID NOT SUCCEED!");
            }
        }
    }


    private BioPortalReferenceData createBioPortalReferenceData(EntityData entityData) {
        BioPortalReferenceData bpRefData = new BioPortalReferenceData();
        bpRefData.setCreateAsClass(false);
        bpRefData.setReferenceClassName(objectType);
        //name of the property, eg #action
        bpRefData.setReferencePropertyName(getProperty().getName());
        bpRefData.setPreferredName(entityData.getBrowserText());

        bpRefData.setBpUrl(null);

        bpRefData.setConceptId(entityData.getName());
        bpRefData.setConceptIdPropertyName(conceptIdPropertyName);
        bpRefData.setConceptIdShort(entityData.getName());
        //not sure about this one ...
        bpRefData.setOntologyVersionId(null);

        bpRefData.setImportFromOriginalOntology(true);
        bpRefData.setConceptIdAltPropertyName("http://who.int/icps#termId");
        final String url = entityData.getName();
        bpRefData.setOntologyName(null);
        if (url != null && url.indexOf('#') > -1){
            bpRefData.setOntologyName(url.substring(0, url.indexOf('#')));
        }
        bpRefData.setOntologyNamePropertyName(ontologyIdPropertyName);


        bpRefData.setBpRestBaseUrl(BioPortalConstants.DEFAULT_BIOPORTAL_REST_BASE_URL);
        bpRefData.setBpRestCallSuffix("");      //TODO This may cause BP REST calls to fail. We should re-think this solution entirely, because creating BioPortal references to internal classes is not a good idea!!!!!!

        return bpRefData;
    }


    public Selectable getSelectable() {
        if (selectable == null) {
            ClassTreePortlet selectableTree = new ClassTreePortlet(getProject(), false, false, false, true, topClass);
            selectableTree.setDraggable(false);
            selectableTree.setClosable(false);
            selectableTree.setCollapsible(false);
            selectableTree.setHeight(300);
            selectableTree.setWidth(450);
            selectable = selectableTree;
        }
        return selectable;
    }
}
