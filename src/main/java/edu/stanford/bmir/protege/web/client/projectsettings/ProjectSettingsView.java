package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettings;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
public interface ProjectSettingsView extends IsWidget, HasInitialFocusable, ValueEditor<ProjectSettings> {


    interface ApplyChangesHandler {
        void handleApplyChanges();
    }

    interface CancelChangedHandler {
        void handleCancelChanges();
    }

    void setApplyChangesHandler(@Nonnull ApplyChangesHandler applyChangesHandler);

    void setCancelChangesHandler(@Nonnull CancelChangedHandler cancelChangesHandler);

    void setCancelButtonVisible(boolean visible);
}
