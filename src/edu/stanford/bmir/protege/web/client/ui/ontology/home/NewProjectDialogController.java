package edu.stanford.bmir.protege.web.client.ui.ontology.home;

import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogValidator;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeOKCancelDialogController;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/01/2012
 */
public class NewProjectDialogController extends WebProtegeOKCancelDialogController<NewProjectInfo> {

    public static final String TITLE = "Create project";

    private NewProjectInfoWidget widget = new NewProjectInfoWidget();

    public NewProjectDialogController() {
        super(TITLE);
        this.widget = new NewProjectInfoWidget();
        for(WebProtegeDialogValidator validator : widget.getDialogValidators()) {
            addDialogValidator(validator);
        }
    }

    public Widget getWidget() {
        return widget;
    }

    public Focusable getInitialFocusable() {
        return widget.getDefaultWidget();
    }

    public NewProjectInfo getData() {
        return widget.getNewProjectInfo();
    }
}
