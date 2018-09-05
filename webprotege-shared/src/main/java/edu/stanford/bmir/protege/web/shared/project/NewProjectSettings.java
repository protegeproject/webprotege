package edu.stanford.bmir.protege.web.shared.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/01/2012
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class NewProjectSettings implements IsSerializable {


    /**
     * Creates a NewProjectSettings object that describes the basic settings for a new project and also specifies a
     * set of source documents (via a set of {@link DocumentId} objects) from which to create the project.
     *
     * @param projectOwner       The desired owner of the project.  Not null.
     * @param displayName        The desired project name for the new project.  Not null.
     * @param projectDescription The desired project description for the new project.  Not null.
     * @param sourceDocumentId   A {@link DocumentId} object that should be used to identify the source document with
     *                           which to initialise a project.  May be null.
     * @throws NullPointerException if either projectOwner, displayName, projectDescription or sourceDocumentId are
     *                              null.
     */
    public static NewProjectSettings get(@Nonnull UserId projectOwner,
                                         @Nonnull String displayName,
                                         @Nonnull String projectDescription,
                                         @Nonnull DocumentId sourceDocumentId) {
        return new AutoValue_NewProjectSettings(projectOwner,
                                                displayName,
                                                projectDescription,
                                                Optional.of(sourceDocumentId));
    }

    /**
     * Creates a NewProjectSettings object that describes the basic settings for a new project.
     *
     * @param projectOwner       The desired owner of the project.  Not null.
     * @param displayName        The desired project name for the new project.  Not {@code null}.
     * @param projectDescription The desired project description for the new project.  Not {@code null}.
     * @throws NullPointerException if either projectOwner, displayName or projectDescription are null.
     */
    @JsonCreator
    public static NewProjectSettings get(@JsonProperty("projectOwner") UserId projectOwner,
                                         @JsonProperty("displayName") String displayName,
                                         @JsonProperty("description") String projectDescription) {
        return new AutoValue_NewProjectSettings(projectOwner,
                                                displayName,
                                                projectDescription,
                                                Optional.empty());
    }

    /**
     * Gets the desired owner of the project.
     *
     * @return The {@link UserId} representing the desired ownner of the project.  Not null.
     */
    public abstract UserId getProjectOwner();

    /**
     * Gets the desired name of the project.
     *
     * @return A string representing the project name.  Not null.
     */
    public abstract String getDisplayName();


    /**
     * Gets the desired project description.
     *
     * @return A string representing the project description.  Not null.
     */
    public abstract String getProjectDescription();

    /**
     * Determines whether of not this new project settings object has a source document associated with it.
     *
     * @return <code>true</code> if there is a source documents associated with this {@link NewProjectSettings} object,
     * otherwise <code>false</code>.
     */
    @JsonIgnore
    public boolean hasSourceDocument() {
        return getSourceDocumentId() != null;
    }

    /**
     * Gets a set of {@link DocumentId}s that identify source documents that should be used to create a new project.
     *
     * @return A {@link DocumentId} object identifying a source document.
     */
    public abstract Optional<DocumentId> getSourceDocumentId();
}
