package edu.stanford.bmir.protege.web.client.ui.ontology.home;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerService;
import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectType;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.ValidationState;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogForm;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogValidator;
import edu.stanford.bmir.protege.web.client.ui.library.dropdown.DropDownModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/01/2012
 */
public class NewProjectInfoWidget extends WebProtegeDialogForm implements HasInitialFocusable {

    public static final String FIELD_WIDTH = "300px";

    public static final int PROJECT_DESCRIPTION_LINE_COUNT = 5;

    public static final String PROJECT_NAME_LABEL = "Project name";

    public static final String PROJECT_DESCRIPTION_LABEL = "Project description";

    public static final String PROJECT_NAME_FIELD_NAME = "projectname";

    public static final String PROJECT_DESCRIPTION_FIELD_NAME = "projectdescription";

    public static final String ERROR_STYLE = "web-protege-error-background";

    private final TextBox projectNameTextBox;

    private final TextArea projectDescriptionTextArea;

    private Set<String> projectNameCache = new HashSet<String>();

    private Set<String> ownedProjectNameCache = new HashSet<String>();

    private static final String PROJECT_TYPE_LABEL = "Project type";

//    private final DropDown<ProjectType> projectTypeDropDown;

    public NewProjectInfoWidget() {
        fillProjectNamesCache();

        projectNameTextBox = new TextBox();
        projectNameTextBox.setWidth(FIELD_WIDTH);
        projectNameTextBox.setName(PROJECT_NAME_FIELD_NAME);
        projectNameTextBox.addKeyUpHandler(new KeyUpHandler() {
            public void onKeyUp(KeyUpEvent event) {
                handleCandidateProjectNameChange();
            }
        });
        addWidget(PROJECT_NAME_LABEL, projectNameTextBox);


        projectDescriptionTextArea = new TextArea();
        projectDescriptionTextArea.setName(PROJECT_DESCRIPTION_FIELD_NAME);
        projectDescriptionTextArea.setVisibleLines(PROJECT_DESCRIPTION_LINE_COUNT);
        projectDescriptionTextArea.setWidth(FIELD_WIDTH);
        addWidget(PROJECT_DESCRIPTION_LABEL, projectDescriptionTextArea);

//        projectTypeDropDown = new DropDown<ProjectType>(new ProjectTypeDropDownModel());
//        addWidget(PROJECT_TYPE_LABEL, projectTypeDropDown);
        
        addDialogValidator(new EmptyProjectNameValidator());
        addDialogValidator(new ExistingProjectNameValidator());
        addDialogValidator(new ProjectDescriptionValidator());
    }


    private void fillProjectNamesCache() {
        getProjectManagerService().getProjectNames(new AsyncCallback<List<String>>() {
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
            }

            public void onSuccess(List<String> result) {
                projectNameCache.clear();
                projectNameCache.addAll(result);
            }
        });
        getProjectManagerService().getOwnedProjectNames(new AsyncCallback<List<String>>() {
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
            }

            public void onSuccess(List<String> result) {
                ownedProjectNameCache.clear();
                ownedProjectNameCache.addAll(result);
            }
        });
    }

    private void handleCandidateProjectNameChange() {
        if (isExistingProjectName()) {
            projectNameTextBox.addStyleName(ERROR_STYLE);
        }
        else {
            projectNameTextBox.removeStyleName(ERROR_STYLE);
        }
    }

    private ProjectManagerServiceAsync getProjectManagerService() {
        return GWT.create(ProjectManagerService.class);

    }

    public boolean isValidProjectName() {
        return !isExistingProjectName();
    }

    public boolean isExistingProjectName() {
        return projectNameCache.contains(getProjectName());
    }

    public boolean isOwnerOfProjectName() {
        return ownedProjectNameCache.contains(getProjectName());
    }

    public NewProjectInfo getNewProjectInfo() {
        return new NewProjectInfo(getProjectName(), getProjectDescription(), getProjectType());
    }

    public String getProjectName() {
        return projectNameTextBox.getText().trim();
    }

    public String getProjectDescription() {
        return projectDescriptionTextArea.getText().trim();
    }

    public ProjectType getProjectType() {
        // TODO: FIX!!!
        return new ProjectType("OWL Project");
//        return projectTypeDropDown.getSelectedItem();
    }




    private class EmptyProjectNameValidator implements WebProtegeDialogValidator {

        public ValidationState getValidationState() {
            return getProjectName().isEmpty() ? ValidationState.INVALID : ValidationState.VALID;
        }

        public String getValidationMessage() {
            return "Please specify a project name";
        }
    }

    private class ExistingProjectNameValidator implements WebProtegeDialogValidator {

        public ValidationState getValidationState() {
            return isExistingProjectName() && !isOwnerOfProjectName() ? ValidationState.INVALID : ValidationState.VALID;
        }

        public String getValidationMessage() {
            return getValidationState() == ValidationState.INVALID ? "A project named " + getProjectName() + " already exists.  Please enter different name." : "";
        }
    }


    private class ProjectDescriptionValidator implements WebProtegeDialogValidator {

        public ValidationState getValidationState() {
            return getProjectDescription().isEmpty() ? ValidationState.INVALID : ValidationState.VALID;
        }

        public String getValidationMessage() {
            return "Please enter a description for the project.";
        }
    }


    private class ProjectTypeDropDownModel implements DropDownModel<ProjectType> {

        private List<ProjectType> projectTypeList = new ArrayList<ProjectType>();

        private ProjectTypeDropDownModel() {
            ProjectManagerServiceAsync managerService = GWT.create(ProjectManagerService.class);
            managerService.getAvailableProjectTypes(new AsyncCallback<List<ProjectType>>() {
                public void onFailure(Throwable caught) {
                    GWT.log("Error retrieving project types", caught);
                }

                public void onSuccess(List<ProjectType> result) {
                    projectTypeList.clear();
                    projectTypeList.addAll(result);
//                    if(projectTypeDropDown != null) {
//                        projectTypeDropDown.setSelectedItem(projectTypeList.get(0));
//                    }
                }
            });
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
}
