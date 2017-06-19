package edu.stanford.bmir.protege.web.shared.project;


import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.rpc.IsSerializable;

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
     * A regular expression that specifies a pattern for a UUID
     */
    public static final transient String UUID_PATTERN = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

    private static final transient RegExp PROJECT_ID_REG_EXP = RegExp.compile(UUID_PATTERN);


    /**
     * Default no-args constructor for GWT serialization purposes.
     */
    private ProjectId() {
    }

    /**
     * Constructs a ProjectId.
     * @param id The lexical Id of the project.  Not <code>null</code>.  The specified id must be formatted according
     * to the regular expression for project id.  See {@link #getIdRegExp()}
     * @throws NullPointerException if the id parameter is <code>null</code>.
     */
    private ProjectId(String id) throws ProjectIdFormatException{
        this.id = checkFormat(checkNotNull(id));
    }

    /**
     * Get the regular expression that specifies the lexical format of {@link ProjectId}s.  The returned regular expression
     * specifies a UUID format consisting of a series of characters from the range a-z0-9 separated by dashes.  The
     * first block contains 8 characters, the second block 4 characters, the third block 4 characters, the fourth
     * block 4 characters, and the fifth block 12 characters.  For example, cb88785a-bfc5-4299-9b5b-7920451aba06.
     * @return The {@link RegExp} for project id lexical values.  Not {@code null}.
     */
    public static RegExp getIdRegExp() {
        return PROJECT_ID_REG_EXP;
    }

    public static boolean isWelFormedProjectId(String candidateId) {
        return PROJECT_ID_REG_EXP.exec(checkNotNull(candidateId)) != null;
    }

    /**
     * Checks that the specified string matches the UUID pattern {@link #UUID_PATTERN}.
     * @param id The string to check.
     * @return The specified string.
     * @throws ProjectIdFormatException if the specified string does not match the UUID pattern.
     */
    private static String checkFormat(String id) throws ProjectIdFormatException {
        MatchResult result = PROJECT_ID_REG_EXP.exec(id);
        if(result == null) {
            throw new ProjectIdFormatException(id);
        }
        return id;
    }

    /**
     * Gets a {@link ProjectId} based on the specified UUID string.  The string must be formatted as a UUID string
     * according to the regular expression returned by {@link #getIdRegExp()}.  The pattern is specified by the
     * {@link #UUID_PATTERN} constant.
     * @param uuid The UUID lexical form that the project id will be based on.   The specified
     * {@code uuid} must match the pattern specified by the {@link #UUID_PATTERN} pattern. Not {@code null}.
     * @return The {@link ProjectId} having the specified UUID.  Not {@code null}.
     * @throws  NullPointerException if {@code uuid} is {@code null}.
     * @throws ProjectIdFormatException if {@code uuid} does not match the UUID pattern specified by {@link #UUID_PATTERN}.
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
