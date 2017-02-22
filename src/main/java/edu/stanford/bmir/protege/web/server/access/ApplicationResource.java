package edu.stanford.bmir.protege.web.server.access;


import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Jan 2017
 */
public final class ApplicationResource implements Resource {

    private static final ApplicationResource INSTANCE = new ApplicationResource();

    private ApplicationResource() {

    }

    public static ApplicationResource get() {
        return INSTANCE;
    }

    @Override
    public Optional<ProjectId> getProjectId() {
        return Optional.empty();
    }

    @Override
    public boolean isApplicationTarget() {
        return true;
    }

    @Override
    public boolean isProjectTarget(ProjectId projectId) {
        return false;
    }

    @Override
    public int hashCode() {
        return 22;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ApplicationResource)) {
            return false;
        }
        else {
            return true;
        }
    }


    @Override
    public String toString() {
        return toStringHelper("ApplicationActionTarget")
                .toString();
    }
}

