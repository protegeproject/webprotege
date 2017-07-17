package edu.stanford.bmir.protege.web.shared.project;


import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.util.UUIDUtil;

import java.io.Serializable;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2012
 * <p>
 *      An identifier for a project.  Project identifiers are essentially UUID strings.
 * </p>
 */
public class ProjectId implements Serializable, IsSerializable {

    private String id = "";

    /**
     * Default no-args constructor for GWT serialization purposes.
     */
    private ProjectId() {
    }

    /**
     * Constructs a ProjectId.
     * @param id The lexical Id of the project.  Not <code>null</code>.  The specified id must be formatted according
     * to the regular expression for UUIDs.  See {@link UUIDUtil#getIdRegExp()}
     * @throws NullPointerException if the id parameter is <code>null</code>.
     */
    private ProjectId(String id) throws ProjectIdFormatException{
        this.id = checkFormat(checkNotNull(id));
    }


    public static boolean isWelFormedProjectId(String candidateId) {
        return UUIDUtil.checkFormat(candidateId);
    }

    /**
     * Checks that the specified string matches the UUID pattern.
     * @param id The string to check.
     * @return The specified string.
     * @throws ProjectIdFormatException if the specified string does not match the UUID pattern.
     */
    private static String checkFormat(String id) throws ProjectIdFormatException {
        if(!UUIDUtil.checkFormat(id)) {
            throw new ProjectIdFormatException(id);
        }
        return id;
    }

    /**
     * Gets a {@link ProjectId} based on the specified UUID string.  The string must be formatted as a UUID string
     * according to the regular expression returned by {@link UUIDUtil#getIdRegExp()}.  The pattern is specified by the
     * {@link UUIDUtil#UUID_PATTERN} constant.
     * @param uuid The UUID lexical form that the project id will be based on.   The specified
     * {@code uuid} must match the pattern specified by the {@link UUIDUtil#UUID_PATTERN} pattern. Not {@code null}.
     * @return The {@link ProjectId} having the specified UUID.  Not {@code null}.
     * @throws  NullPointerException if {@code uuid} is {@code null}.
     * @throws ProjectIdFormatException if {@code uuid} does not match the UUID pattern specified by {@link UUIDUtil#UUID_PATTERN}.
     */
    public static ProjectId get(String uuid) throws ProjectIdFormatException {
        return new ProjectId(uuid);
    }


    public static Optional<ProjectId> getFromNullable(String uuid) throws ProjectIdFormatException {
        if(uuid == null || uuid.isEmpty()) {
            return Optional.empty();
        }
        else {
            return Optional.of(get(uuid));
        }
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return toStringHelper("ProjectId").addValue(id).toString();
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ProjectId)) {
            return false;
        }
        ProjectId other = (ProjectId) obj;
        if(this.id == null) {
            return other.id == null;
        }
        return other.id != null && other.id.equals(this.id);
    }


}
