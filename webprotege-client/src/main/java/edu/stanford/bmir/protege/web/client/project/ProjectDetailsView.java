package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-13
 */
public interface ProjectDetailsView extends IsWidget {

    void setDisplayName(@Nonnull String displayName);

    void setDescription(@Nonnull String description);

    void setOwner(@Nonnull UserId owner);

}
