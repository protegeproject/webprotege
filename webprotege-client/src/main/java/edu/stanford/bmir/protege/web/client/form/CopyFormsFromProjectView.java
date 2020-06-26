package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.project.AvailableProject;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-13
 */
public interface CopyFormsFromProjectView extends IsWidget {

    void setFormDescriptors(@Nonnull List<FormDescriptor> formDescriptors);

    @Nonnull
    List<FormDescriptor> getSelectedFormIds();

    interface SelectedProjectChangedHandler {
        void handleSelectedProjectChanged();
    }

    void setSelectedProjectChangedHandler(@Nonnull SelectedProjectChangedHandler handler);

    @Nonnull
    Optional<HasRequestFocus> getInitialFocusable();

    @Nonnull
    Optional<ProjectId> getSelectedProjectId();

    void setProjects(@Nonnull List<ProjectDetails> projects);
}
