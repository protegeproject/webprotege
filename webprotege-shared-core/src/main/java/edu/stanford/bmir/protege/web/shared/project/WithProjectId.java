package edu.stanford.bmir.protege.web.shared.project;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-24
 */
public interface WithProjectId<T> {

    /**
     * Generate a copy replacing the projectId in this object with the specified id.
     * @param projectId The project id to replace the current project Id
     * @return A copy of this object with the project id replaced with the specified project id
     */
    @JsonIgnore
    T withProjectId(@Nonnull ProjectId projectId);
}
