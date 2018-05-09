package edu.stanford.bmir.protege.web.server.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Apr 2018
 *
 * This is a server side result.  It won't work on the client.
 */
public class AddAxiomsResult implements Result, HasProjectId {

    @Nonnull
    private final ProjectId projectId;

    private final int addedAxiomsCount;

    public AddAxiomsResult(@Nonnull ProjectId projectId,
                           int addedAxiomsCount) {
        this.projectId = checkNotNull(projectId);
        this.addedAxiomsCount = addedAxiomsCount;
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    public int getAddedAxiomsCount() {
        return addedAxiomsCount;
    }
}
