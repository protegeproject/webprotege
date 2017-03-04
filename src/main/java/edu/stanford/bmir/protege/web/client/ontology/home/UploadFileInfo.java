package edu.stanford.bmir.protege.web.client.ontology.home;

import com.google.gwt.user.client.ui.FormPanel;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/01/2012
 */
public class UploadFileInfo {

    private NewProjectInfo newProjectInfo;

    private UploadFileWidget widget;
    

    public UploadFileInfo(NewProjectInfo newProjectInfo, UploadFileWidget widget) {
        this.newProjectInfo = newProjectInfo;
        this.widget = widget;
    }

    public NewProjectInfo getProjectSettings() {
        return newProjectInfo;
    }

    public void submit() {
        widget.submit();
    }

    public void addSubmitCompleteHandler(FormPanel.SubmitCompleteHandler handler) {
        widget.addSubmitCompleteHandler(handler);
    }
}
