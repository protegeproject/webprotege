package edu.stanford.bmir.protege.web.shared.project;


import com.google.common.base.Optional;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
public class ProjectId implements Serializable {

    private String projectName = "";

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
     * @throws NullPointerException if the projectName parameter is <code>null</code>.
     */
    private ProjectId(String id) throws ProjectIdFormatException{
        this.projectName = checkFormat(checkNotNull(id));
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


    public static Optional<ProjectId> getFromNullable(String uuid) {
        if(uuid == null || uuid.isEmpty()) {
            return Optional.absent();
        }
        else {
            return Optional.of(get(uuid));
        }
    }

//    public String getProjectName() {
//        return projectName;
//    }

    public String getId() {
        return projectName;
    }
    
    public String getSuggestedAcronym() {

        // Split into camel case words - filter out "of"
        List<String> significantWords = getSignificantWords();
        
        if(significantWords.size() == 1) {
            String firstWord = significantWords.get(0);
            if(isAllUpperCaseWord(firstWord)) {
                return firstWord;
            }
            else {
                // Only one word, pretend "Ontology" is a second word
                return firstWord.charAt(0) + "O";
            }
        }
        else {
            // Build an acronym from the words
            return getAcronym(significantWords);
        }

    }

    private List<String> getSignificantWords() {
        List<String> allWords = getProjectNameWords();

        List<String> significantWords = new ArrayList<String>();
        for(String word : allWords) {
            if(!isStopWord(word)) {
                significantWords.add(word);
            }
        }
        return significantWords;
    }

    private List<String> getProjectNameWords() {
        RegExp regExp = RegExp.compile("\\p{Upper}+\\b|(\\p{Upper}((\\p{Upper}\\p{Upper})+|\\p{Lower}+))|\\p{Lower}+", "g");

        MatchResult matchResult;

        List<String> words = new ArrayList<String>();
        while((matchResult = regExp.exec(projectName)) != null)  {
            String word = matchResult.getGroup(0);
            words.add(word);
        }
        return words;
    }

    public String getSuggestedURLPathElementName() {
        StringBuilder sb = new StringBuilder();
        for(Iterator<String> it = getProjectNameWords().iterator(); it.hasNext(); ) {
            String word = it.next().toLowerCase();
            sb.append(word);
            if(it.hasNext()) {
                sb.append("-");
            }
        }
        return sb.toString();
//        return URL.encodePathSegment(underscored);
    }

    private boolean isStopWord(String word) {
        return "of".equalsIgnoreCase(word) || "for".equalsIgnoreCase(word) || "test".equalsIgnoreCase(word) || "the".equalsIgnoreCase(word) || "in".equalsIgnoreCase(word) || "and".equalsIgnoreCase(word);
    }

    private static String getAcronym(List<String> words) {
        StringBuilder sb = new StringBuilder();
        for(String word : words) {
            sb.append(Character.toUpperCase(word.charAt(0)));
        }
        return sb.toString();
    }

    private static boolean isAllUpperCaseWord(String word) {
        RegExp uppercaseRegExp = RegExp.compile("\\p{Upper}+");
        MatchResult result = uppercaseRegExp.exec(word);
        // All upper case one word is assumed to be an acronym, so just return that.
        return result != null && result.getGroup(0).equals(word);
    }

    @Override
    public String toString() {
        return "ProjectId(" + projectName + ")";
    }

    @Override
    public int hashCode() {
        return projectName == null ? 0 : projectName.hashCode();
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
        if(this.projectName == null) {
            return other.projectName == null;
        }
        return other.projectName != null && other.projectName.equals(this.projectName);
    }


}
