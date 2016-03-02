package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.ui.UIAction;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 01/03/16
 */
public interface ProjectMenuView extends IsWidget {

    void addMenuAction(UIAction uiAction);
}
