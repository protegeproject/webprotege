package edu.stanford.bmir.protege.web.shared.project;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/04/2013
 */
public class ProjectIdFormatException extends RuntimeException implements Serializable {

    private String id;

    /**
     * For serialization purposes only
     */
    private ProjectIdFormatException() {
    }

    /**
     * Constructs a {@link ProjectIdFormatException}.
     * @param id The string that was specified for the {@link ProjectId}
     */
    public ProjectIdFormatException(String id) {
        this.id = checkNotNull(id);
    }

    /**
     * Gets the malformed id.
     * @return A string representing the malformed id.  Not {@code null}.
     */
    public String getId() {
        return id;
    }

    @Override
    public String getMessage() {
        return "Invalid Project Id format: " + id;
    }
}
