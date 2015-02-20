package edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.*;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import edu.stanford.bmir.protege.web.client.project.Project;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import edu.stanford.bmir.protege.web.client.ui.ontology.search.BioPortalConstants;
import edu.stanford.bmir.protege.web.client.ui.ontology.search.BioPortalSearchComponent;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;
import edu.stanford.bmir.protege.web.shared.notes.NoteContent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.HashMap;
import java.util.Map;

public class ReferenceFieldWidget extends InstanceGridWidget {

    private BioPortalSearchComponent bpSearchComponent = null;

    protected Map<String, Object> bpSearchProperties;

    public ReferenceFieldWidget(Project project) {
        super(project);
    }

    @Override
    public void setup(Map<String, Object> widgetConfiguration, PropertyEntityData propertyEntityData) {
        super.setup(widgetConfiguration, propertyEntityData);

        //since we combine 2 property maps we can't directly reuse them but have to make a copy of their content
        //(otherwise the merging operation would alter the first (i.e. the global) property map)
        bpSearchProperties = new HashMap<String, Object>();

        //read global properties
        Map<String, Object> projectConfiguration = getProject().getProjectLayoutConfiguration().getProperties();
        if (projectConfiguration != null) {
            Map<String, Object> globalBpSearchProperties = (Map<String, Object>) projectConfiguration.get(FormConstants.BP_SEARCH_PROPERTIES);
            if (globalBpSearchProperties != null) {
                bpSearchProperties.putAll(globalBpSearchProperties);
            }
        }

        //read local properties
        Map<String, Object> widgetConfig = getWidgetConfiguration();
        Map<String, Object> localBpSearchProperties = (Map<String, Object>) widgetConfig.get(FormConstants.BP_SEARCH_PROPERTIES);
        if (localBpSearchProperties != null) {
            bpSearchProperties.putAll(localBpSearchProperties);
        }
    }

    @Override
    protected Anchor createReplaceNewValueHyperlink() {
        final Map<String, Object> widgetConfiguration = getWidgetConfiguration();
        final ProjectLayoutConfiguration projectLayoutConfiguration = getProject().getProjectLayoutConfiguration();
        Anchor addNewLink = new Anchor(InstanceGridWidgetConstants.getIconLink(InstanceGridWidgetConstants.getReplaceNewValueActionDesc(widgetConfiguration, projectLayoutConfiguration, "Replace term"), InstanceGridWidgetConstants.getReplaceIcon(widgetConfiguration, projectLayoutConfiguration)), true);
        addNewLink.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (isWriteOperationAllowed()) {
                    onCreateNewReference();
                }
            }
        });
        return addNewLink;
    }

    @Override
    protected Anchor createReplaceExistingHyperlink() {
        final Map<String, Object> widgetConfiguration = getWidgetConfiguration();
        final ProjectLayoutConfiguration projectLayoutConfiguration = getProject().getProjectLayoutConfiguration();
        Anchor addNewLink = new Anchor(InstanceGridWidgetConstants.getIconLink(InstanceGridWidgetConstants.getReplaceExistingValueActionDesc(widgetConfiguration, projectLayoutConfiguration, "Find & Replace <br/>term"), InstanceGridWidgetConstants.getReplaceIcon(widgetConfiguration, projectLayoutConfiguration)), true);
        addNewLink.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (isWriteOperationAllowed()) {
                    //TODO fix this
                    //onAddNewReference((String) ReferenceFieldWidget.this.getWidgetConfiguration().get(FormConstants.LABEL));
                    onAddNewReference(true);
                }
            }
        });

        return addNewLink;
    }

    public String getReferenceProperty() {
        return (String) bpSearchProperties.get(BioPortalConstants.CONFIG_PROPERTY_REFERENCE_PROPERTY);
    }

    @Override
    protected GridEditor getGridEditor(final String fieldType, final Map<String, Object> config) {
        return null;
    }

    @Override
    public String preRenderColumnContent(String content, String fieldType) {
        if (fieldType != null && fieldType.equals("nolink")) {
            //do nothing
        }
        else {
            if (content.startsWith("http://")) {
                content = "<a href= \"" + content + "\" target=\"_blank\">" + content + "</a>";
            }
        }
        return content;
    }

    @Override
    protected Anchor createAddExistingHyperlink() {
        final Map<String, Object> widgetConfiguration = getWidgetConfiguration();
        final ProjectLayoutConfiguration projectLayoutConfiguration = getProject().getProjectLayoutConfiguration();
        Anchor addNewLink = new Anchor(InstanceGridWidgetConstants.getIconLink(InstanceGridWidgetConstants.getAddExistingValueActionDesc(widgetConfiguration, projectLayoutConfiguration, "Find term"), InstanceGridWidgetConstants.getAddIcon(widgetConfiguration, projectLayoutConfiguration)), true);
        addNewLink.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                //TODO fix this
                //onAddNewReference((String) ReferenceFieldWidget.this.getWidgetConfiguration().get(FormConstants.LABEL));
                onAddNewReference(false);
            }
        });

        return addNewLink;
    }

    @Override
    public Anchor createAddNewValueHyperlink() {
        final Map<String, Object> widgetConfiguration = getWidgetConfiguration();
        final ProjectLayoutConfiguration projectLayoutConfiguration = getProject().getProjectLayoutConfiguration();
        Anchor addNewLink = new Anchor(InstanceGridWidgetConstants.getIconLink(InstanceGridWidgetConstants.getAddNewValueActionDesc(widgetConfiguration, projectLayoutConfiguration, "Add term"), InstanceGridWidgetConstants.getAddIcon(widgetConfiguration, projectLayoutConfiguration)), true);
        addNewLink.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (isWriteOperationAllowed()) {
                    onCreateNewReference();
                }
            }
        });

        return addNewLink;
    }

    private void onAddNewReference(boolean replaceExisting) {
        final Window window = new Window();

        window.setTitle("BioPortal Search  for property " + UIUtil.getDisplayText(getProperty()));
        window.setWidth(800);
        window.setHeight(400);
        window.setLayout(new FitLayout());

        bpSearchComponent = new BioPortalSearchComponent(getProjectId(), !multiValue) {
            @Override
            protected AsyncCallback<EntityData> getImportBioPortalConceptHandler() {
                return new ImportBioPortalConceptHandler(this);
            }
        };

        bpSearchComponent.setProperty(getProperty());

        if (store.getRecords() != null && store.getRecords().length > 0) {
            bpSearchComponent.setReplaceExisting(replaceExisting);
            bpSearchComponent.setCurrentValue(store.getAt(0).getAsString(INSTANCE_FIELD_NAME));
        }

        window.add(bpSearchComponent);
        window.show();

        bpSearchComponent.setConfigProperties(bpSearchProperties);
        bpSearchComponent.setEntity(getSubject());
    }

    private void onCreateNewReference() {
        final Window window = new Window();
        window.setTitle("Create new manual reference");
        window.setMinWidth(560);
        window.setWidth(560);
        window.setMinHeight(505);
        window.setHeight(505);

        Panel panel = new Panel();
        panel.setWidth(548);
        panel.setHeight(465);

        Panel wrappingPanel = new Panel();
        wrappingPanel.setHeight(270);
        wrappingPanel.setLayout(new FitLayout());

        final FormPanel formPanel = new FormPanel();
        formPanel.setPaddings(7);

        final TextField txtLabel = new TextField("Label");
        final TextField txtCode = new TextField("Code");
        final TextField txtSource = new TextField("Terminology");
        final TextField txtURL = new TextField("URL");
        HTMLPanel verticalSpacer = new HTMLPanel("<BR /><BR />");
//        final NoteInputPanel nip = new NoteInputPanel(getProjectId(), "Enter a comment about this reference (optional):",
//                false, "", "", null, window);
//        nip.showButtons(false);

        Button btnCreate = new Button("Create reference", new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                String label = txtLabel.getText();
                String code = txtCode.getText();
                String source = txtSource.getText();
                String url = txtURL.getText();
                if (label.length() > 0 || code.length() > 0 || source.length() > 0 || url.length() > 0) {
//                    createNewReference(label, code, source, url, null);
                    createNewReference(label, code, source, url);
                }
                window.close();
            }
        });
        Button btnCancel = new Button("Cancel", new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                window.close();
            }
        });

        formPanel.add(txtLabel, new AnchorLayoutData("100%"));
        formPanel.add(txtCode, new AnchorLayoutData("100%"));
        formPanel.add(txtSource, new AnchorLayoutData("100%"));
        formPanel.add(txtURL, new AnchorLayoutData("100%"));
        formPanel.add(verticalSpacer);

        wrappingPanel.add(null, new AnchorLayoutData("100% 170"));

        FormPanel formPanelBottom = new FormPanel();
        TextField dummyField = new TextField(); //we need to add this to the bottom formPanel
        //because otherwise we are not allowed to add buttons to it
        dummyField.setVisible(false);
        formPanelBottom.add(dummyField);
        formPanelBottom.addButton(btnCreate);
        formPanelBottom.addButton(btnCancel);

        panel.add(formPanel, new AnchorLayoutData("100% 150"));
        panel.add(wrappingPanel, new AnchorLayoutData("100% 170"));
        panel.add(formPanelBottom, new AnchorLayoutData("100% 20"));
        window.add(panel);
        window.show();
    }

    private void createNewReference(String label, String termId, String ontologyId, String url) {
//    private void createNewReference(String label, String termId, String ontologyId, String url, final NoteInputPanel noteInputPanel) {
        bpSearchComponent = new BioPortalSearchComponent(getProjectId(), !multiValue) {
            @Override
            protected AsyncCallback<EntityData> getCreateManualreferenceHandler() {
                return new CreateManualReferenceHandler();
            }
        };
        bpSearchComponent.setConfigProperties(bpSearchProperties);
        bpSearchComponent.setEntity(getSubject(), false);//do not reload because bpSearchProperties is not visible
        if (!isReplace()) {
            bpSearchComponent.createReference(ontologyId, termId, termId, label, url);
        }
        else {
            bpSearchComponent.replaceReference(ontologyId, termId, termId, label, url, store.getAt(0).getAsString(INSTANCE_FIELD_NAME));
        }
    }

    public void addUserCommentOnReference(final Project project, EntityData refInstance) {

        final Window window = new Window();
        window.setTitle("Comment on reference");
        window.setWidth(600);
        window.setHeight(350);
        window.setMinWidth(300);
        window.setMinHeight(250);
        window.setLayout(new FitLayout());
        window.setPaddings(5);
        window.setButtonAlign(Position.CENTER);

        //window.setCloseAction(Window.HIDE);
        window.setPlain(true);

//        final NoteInputPanel nip = new NoteInputPanel(getProjectId(), "Enter a comment about this reference (optional):", false, refInstance, window, new AsyncCallback<NotesData>() {
//            public void onFailure(Throwable caught) {
//                if (caught != null) {
//                    MessageBox.alert(caught.getMessage());
//                }
//                window.close();
//            }
//
//            public void handleSuccess(NotesData note) {
//                addUserComment(getProjectId(), note);
//                window.close();
//            }
//        });
//        window.add(nip);
//
//        window.show();
//        nip.getMainComponentForFocus().focus();
    }

    public static void addUserComment(final ProjectId projectId, NoteContent noteContent, OWLEntity targetEntity) {
        MessageBox.alert("This functionality is not currently available.  We apologise for any inconvenience.");
    }

    /*
     * Remote calls
     */

    class ImportBioPortalConceptHandler implements AsyncCallback<EntityData> {

        private BioPortalSearchComponent bpSearchComponent;

        public ImportBioPortalConceptHandler(BioPortalSearchComponent bioPortalSearchComponent) {
            this.bpSearchComponent = bioPortalSearchComponent;
        }

        @Override
        public void onFailure(Throwable caught) {
            bpSearchComponent.getEl().unmask();
            GWT.log("Could not import BioPortal concept ", null);
            MessageBox.alert("Import operation failed!");
        }

        @Override
        public void onSuccess(EntityData refInstance) {
            bpSearchComponent.getEl().unmask();
            if (refInstance != null) {
                //activate this code if we need it in the future
                //addUserCommentOnReference(getProject(), refInstance);
                refresh();
            }
            else {
                MessageBox.alert("Import operation DID NOT SUCCED!");
            }
        }
    }


    class CreateManualReferenceHandler implements AsyncCallback<EntityData> {

//        private NoteInputPanel noteInputPanel;

        public CreateManualReferenceHandler() {
//        public CreateManualReferenceHandler(NoteInputPanel noteInputPanel) {
//            this.noteInputPanel = noteInputPanel;
        }

        @Override
        public void onFailure(Throwable caught) {
            GWT.log("Could not create manual reference for " + getSubject(), null);
            MessageBox.alert("Reference creation failed!");
        }

        @Override
        public void onSuccess(EntityData refInstance) {
            refresh();
            if (refInstance != null) {
                //addUserCommentOnReference(getProject(), refInstance);
//                noteInputPanel.setAnnotatedEntity(refInstance);
//                noteInputPanel.doSendNote();
            }
            else {
                MessageBox.alert("Reference creation DID NOT SUCCEDED!");
            }
        }
    }

}
