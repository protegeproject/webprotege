package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.AbstractWebProtegeAsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerService;
import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectType;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeOKCancelDialogController;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.projectsettings.GetProjectSettingsAction;
import edu.stanford.bmir.protege.web.shared.projectsettings.GetProjectSettingsResult;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettings;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/07/2012
 */
public class ProjectSettingsDialogController extends WebProtegeOKCancelDialogController<ProjectSettings> {

    public static final String DIALOG_TITLE = "Project settings";

    private ProjectSettingsView view;

    public ProjectSettingsDialogController(ProjectSettingsView view) {
        super(DIALOG_TITLE);
        this.view = view;
    }

    
    public void showProjectSettings(ProjectSettings projectSettings) {
        view.setValue(projectSettings);
    }


    /**
     * Gets the widget that is displayed in the dialog and allows information to be entered into the dialog.
     * @return The widget that the user interacts with.  Not <code>null</code>.
     */
    @Override
    public Widget getWidget() {
        return view.asWidget();
    }

    /**
     * Gets the focusable that should receive the focus when the dialog is shown.
     * @return The focusable that will receive the focus. Not <code>null</code>
     */
    @Override
    public Optional<Focusable> getInitialFocusable() {
        return view.getInitialFocusable();
    }

    @Override
    public ProjectSettings getData() {
        return view.getValue().get();
    }
}
