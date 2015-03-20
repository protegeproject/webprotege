package edu.stanford.bmir.protege.web.client.ui.ontology.home;

import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectType;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.ValidationState;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogForm;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogValidator;

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

    public NewProjectInfoWidget() {
        projectNameTextBox = new TextBox();
        projectNameTextBox.setWidth(FIELD_WIDTH);
        projectNameTextBox.setName(PROJECT_NAME_FIELD_NAME);
        addWidget(PROJECT_NAME_LABEL, projectNameTextBox);


        projectDescriptionTextArea = new TextArea();
        projectDescriptionTextArea.setName(PROJECT_DESCRIPTION_FIELD_NAME);
        projectDescriptionTextArea.setVisibleLines(PROJECT_DESCRIPTION_LINE_COUNT);
        projectDescriptionTextArea.setWidth(FIELD_WIDTH);
        addWidget(PROJECT_DESCRIPTION_LABEL, projectDescriptionTextArea);

        addDialogValidator(new EmptyProjectNameValidator());
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
    }




    private class EmptyProjectNameValidator implements WebProtegeDialogValidator {

        public ValidationState getValidationState() {
            return getProjectName().isEmpty() ? ValidationState.INVALID : ValidationState.VALID;
        }

        public String getValidationMessage() {
            return "Please specify a project name";
        }
    }
}
