package edu.stanford.bmir.protege.web.client.ui.icd;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.ConfirmCallback;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.AnchorLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.ChAOServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.ICDServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.ChangeData;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

public class ICDExportImportPortlet extends AbstractEntityPortlet{

   private final static String EXPORT_LABEL = "Click here to export";

   private Anchor exportAnchor;

    public ICDExportImportPortlet(final Project project) {
        super(project);
    }

    @Override
    public void initialize() {
        setTitle("Export and Import to/from spreadsheets");

        setLayout(new AnchorLayout());

        add(createIntroPanel());
        add(createSeparator());
        add(createExportPanel());
        add(createSeparator());
        add(createImportPanel());

        updateExportLink();
    }


    protected Panel createIntroPanel() {
        Panel introPanel = new Panel();
        introPanel.add(new HTML(getIntroMessage()));

        return introPanel;
    }

    protected Panel createExportPanel() {
        Panel exportPanel = new Panel();
        exportPanel.setLayout(new AnchorLayout());
        exportPanel.add(new HTML(getExportMessage()));

        Panel anchorPanel = new Panel();
        anchorPanel.setLayout(new HorizontalLayout(5));
        anchorPanel.add(new HTML("<img src=\"images/excel_icon.png\" style=\"padding-left:15px;\" />"));
        anchorPanel.add(exportAnchor = createExportAnchor());

        exportPanel.add(anchorPanel);

        return exportPanel;
    }

    protected Anchor createExportAnchor() {
        Anchor anchor = new Anchor(EXPORT_LABEL, true);
        anchor.setStylePrimaryName("export_import_anchor");

        anchor.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                onExport();
            }
        });
        return anchor;
    }

    protected void onExport() {
        final EntityData currentEntity = getEntity();

        if (currentEntity == null) {
            MessageBox.alert("No selection", "Please select a class in the tree to export.");
            return;
        }

        MessageBox.confirm("Export?", "<b>The entire branch for " + UIUtil.getDisplayText(currentEntity) + " will be exported to a spreadsheet. </b><br /><br />" +
        		"The export may take several minutes to complete.<br /><br />" +
        		"When the export is finished, you will be prompted to dowload the file. <br />" +
        		"Depending on the number of classes in the branch, the spreadsheet file size might be significant (over 10MB). <br /> <br />" +
        		"<b>Are you sure you want to export the entire branch for " + UIUtil.getDisplayText(currentEntity) + " ?<b>",

                new ConfirmCallback() {
                        public void execute(String btnID) {
                           if (btnID.equalsIgnoreCase("yes")) {
                               getEl().mask("Exporting spreadsheet for " + UIUtil.getDisplayText(currentEntity), true);
                               ICDServiceManager.getInstance().exportICDBranch(getProject().getProjectName(), currentEntity.getName(),
                                       GlobalSettings.getGlobalSettings().getUserName(),  new ExportHandler(currentEntity));
                           }
                        }
                    });
    }

    protected Panel createImportPanel() {
        Panel importPanel = new Panel();
        importPanel.add(new HTML(getImportMessage()));

        return importPanel;
    }

    protected HTML createSeparator() {
        String getSeparatorHtml = getSeparatorText();
        HTML html = new HTML(getSeparatorHtml == null ? "" : getSeparatorHtml);
        html.setStylePrimaryName("export-import-separator");
        return html;
    }

    @Override
    public void reload() {
        updateExportLink();
    }

    protected void updateExportLink() {
        EntityData entity = getEntity();
        exportAnchor.setHTML(entity == null ? EXPORT_LABEL : EXPORT_LABEL + " <i>" + UIUtil.getDisplayText(entity) + "</i>");
    }

    public Collection<EntityData> getSelection() {
        return UIUtil.createCollection(getEntity());
    }

  //FIXME: styles to be outsourced later in a css

    protected String getIntroMessage() {
        String text = "<div style=\"padding:10px 15px;\">" +
            "<div style=\"color:#003399; font-weight: bold; font-size: 120%;\">INTRODUCTION</div> <br />" +
            "You may export and import branches of ICD in this tab. Please make sure you are aware of " +
            "the export/import workflow available <a href=\"http://tinyurl.com/2arkmlv\" target=\"_blank\">here</a>. <br /><br />" +
            "You need to be a member of the icat-users group to access the workflow document." +
            "</div>";

            return text;
    }

    protected String getExportMessage() {
        String text = "<div style=\"padding:10px 15px;\">" +
            "<div style=\"color:#003399; font-weight: bold; font-size: 120%;\">EXPORT</div> <br />" +
            "You may export the selected ICD branch in the left tree to a spreadsheet by clicking on the export link below.  To export: <br /><br />" +
            "1. Select the top level parent for the branch to export in the tree on the left.<br />" +
            "2. Click on the Export link from below.<br /><br />" +
            "The export might take several minutes to complete. When the spreadsheet is available, the download will start automatically. If the download is blocked by your browser," +
            " iCAT will provide you with a Web link, so that you can download the file manually." +
            "<br />" +
            "</div>";

            return text;
    }


    protected String getImportMessage() {
        String text = "<div style=\"padding:10px 15px;\">" +
        "<div style=\"color:#003399; font-weight: bold; font-size: 120%;\">IMPORT (optional)</div> <br />" +
        "To import a spreadsheet in iCAT, the managing editor has to make sure that the content of the spreadsheet is valid with respect to the spreadsheet authoring instructions " +
        "and should understand the import workflow as it is documented <a href=\"http://tinyurl.com/2arkmlv\" target=\"_blank\">here</a >.<br /><br />" +
        "Please submit the spreadsheet to import by email. Please send the email to: tudorache_at_stanford.edu." +
        "</div>";


            return text;
    }

    protected String getSeparatorText() {
        return "<hr />";
    }

    /*
     * Remote methods
     */

    private class ExportHandler implements AsyncCallback<String> {
        private EntityData exportedParentClass;

        private ExportHandler(EntityData exportedParentClass) {
            this.exportedParentClass = exportedParentClass;
        }

        public void onFailure(Throwable caught) {
            getEl().unmask();
            MessageBox.alert("Error", "There was an error exporting the spreadsheet. Please try again later.");
        }

        public void onSuccess(String fileName) {
            getEl().unmask();
            if (fileName == null) {
                MessageBox.alert("Error", "There was an error exporting the spreadsheet. Please try again later.");
                return;
            }

            String downloadLink = GWT.getHostPageBaseURL() + "downloads/" + fileName; //TODO "downloads" should not be hard coded
            //TODO: we should figure out how to make the real HTML enconding for the URL, this is super simplified
            downloadLink = downloadLink.replaceAll(" ", "%20");

            recordChange(downloadLink);

            MessageBox.alert("Export successful", "The export to spreadsheet completed successfully. <br /> <br />" +
            		"If the download does not start automatically in a few seconds, " +
            		"you may download the file manually, by clicking on this link: <br /><br />" +
            		"<a href=\"" + downloadLink + "\" target=\"_blank\">" + downloadLink + "</a>");

            String link = GWT.getModuleBaseURL() + "fileserver?FILE_NAME=" + fileName; //FIXME: this should not be hardcoded
            DOM.setElementAttribute(RootPanel.get("__download").getElement(), "src", link);
        }

        private void recordChange(String downloadLink) {
            final String context = "Exported branch " + UIUtil.getDisplayText(exportedParentClass) + " to a spreadsheet. " +
            		"Download spreadsheet from: " + downloadLink;
            ChAOServiceManager.getInstance().createChange(project.getProjectName(),
                    GlobalSettings.getGlobalSettings().getUserName(), exportedParentClass.getName(), "Export", context,
                    new AbstractAsyncHandler<ChangeData>() {
                            @Override
                            public void handleFailure(Throwable caught) {
                                GWT.log("Error at creating change for export of " + exportedParentClass.getBrowserText());

                            }
                            @Override
                            public void handleSuccess(ChangeData result) {
                                GWT.log("Change for export of " + exportedParentClass + " saved successfully.");
                            }
                        });
        }
    }

}
