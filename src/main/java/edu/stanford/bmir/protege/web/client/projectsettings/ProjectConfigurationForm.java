package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.TextArea;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectType;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.HasFormData;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogForm;
import edu.stanford.bmir.protege.web.client.ui.library.dropdown.DropDown;
import edu.stanford.bmir.protege.web.client.ui.library.dropdown.DropDownModel;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/07/2012
 */
public class ProjectConfigurationForm extends FlowPanel implements HasFormData<ProjectConfigurationInfo>, HasInitialFocusable {

    public static final String PROJECT_TYPE_DROPDOWN_FIELD_NAME = "Type";

    public static final String PROJECT_DESCRIPTION_FIELD_NAME = "Description";

    private DropDown<ProjectType> projectTypeDropDown;

    private ProjectId projectId;

    private final TextArea projectDescriptionTextBox;

//    private final TextBox defaultLanguageBox;

//    private final TextBox bioPortalRestBaseTextBox;

    public ProjectConfigurationForm(ProjectId id) {
        this.projectId = id;
        projectTypeDropDown = new DropDown<ProjectType>(new ProjectTypeDropDownModel());

        WebProtegeDialogForm form = new WebProtegeDialogForm();
        form.addWidget(PROJECT_TYPE_DROPDOWN_FIELD_NAME, projectTypeDropDown);
        form.addVerticalSpacer();

//        defaultLanguageBox = form.addTextBox("Default language", "Enter a default language e.g. en", "en", new NullWebProtegeDialogTextFieldValidator());
//        form.addVerticalSpacer();

        projectDescriptionTextBox = new TextArea();
        projectDescriptionTextBox.setVisibleLines(3);
        projectDescriptionTextBox.setCharacterWidth(60);
        form.addWidget(PROJECT_DESCRIPTION_FIELD_NAME, projectDescriptionTextBox);

        form.addVerticalSpacer();

//        BioPortalConfigurationManager manager = BioPortalConfigurationManager.getManager();
//        bioPortalRestBaseTextBox = form.addTextBox("BioPortal Rest API Base", "The URL of the BioPortal Rest API Service", manager.getRestBase());


//        form.addVerticalSpacer();
//
//        form.addWidget("", new Button("Entity Id Generator Settings ...", new ClickHandler() {
//            public void onClick(ClickEvent event) {
//                EntityIdGeneratorSettingsDialog dlg = new EntityIdGeneratorSettingsDialog(projectId);
//                dlg.show();
//            }
//        }));

        add(form);


    }
    
    public void setAllowedProjectTypes(List<ProjectType> projectTypes) {
        projectTypeDropDown.setModel(new ProjectTypeDropDownModel(projectTypes));
    }

    public void setData(ProjectConfigurationInfo info) {
        projectTypeDropDown.setSelectedItem(info.getProjectType());
        projectDescriptionTextBox.setText(info.getProjectDescription());
//        defaultLanguageBox.setText(info.getDefaultLanguage());
    }



    public ProjectConfigurationInfo getData() {
        // TODO: DEF LANG
        return new ProjectConfigurationInfo(projectId, getProjectType(), "en", getProjectDescription());
    }

//    private String getDefaultLanguage() {
//        return defaultLanguageBox.getText().trim();
//    }

    public ProjectType getProjectType() {
        return projectTypeDropDown.getSelectedItem();
    }
    
    public String getProjectDescription() {
        return projectDescriptionTextBox.getText().trim();
    }

    
    
    private class ProjectTypeDropDownModel implements DropDownModel<ProjectType> {

        private List<ProjectType> projectTypeList = new ArrayList<ProjectType>();

        private ProjectTypeDropDownModel() {
        }

        private ProjectTypeDropDownModel(List<ProjectType> projectTypes) {
            this.projectTypeList.addAll(projectTypes);
        }

        public int getSize() {
            return projectTypeList.size();
        }

        public ProjectType getItemAt(int index) {
            return projectTypeList.get(index);
        }

        public String getRendering(int index) {
            return projectTypeList.get(index).getName();
        }


    }
    
    public Optional<Focusable> getInitialFocusable() {
        return Optional.<Focusable>of(projectTypeDropDown);
    }
}
