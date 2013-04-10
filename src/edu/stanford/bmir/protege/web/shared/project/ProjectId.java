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
 *     A simple object that identifies a project.
 * </p>
 */
public class ProjectId implements Serializable {

    private String projectName = "";


    /**
     * Default no-args constructor for GWT serialization purposes.
     */
    private ProjectId() {
    }

    /**
     * Constructs a ProjectId.
     * @param projectName The name of the project.  Not <code>null</code>.
     * @throws NullPointerException if the projectName parameter is <code>null</code>.
     */
    private ProjectId(String projectName) {
        if(projectName == null) {
            throw new NullPointerException("The projectName parameter must not be null.");
        }
        this.projectName = projectName;
    }


    public static ProjectId get(String projectName) {
        return new ProjectId(checkNotNull(projectName, "projectName must not be null"));
    }

    public static Optional<ProjectId> getFromNullable(String projectName) {
        if(projectName == null || projectName.isEmpty()) {
            return Optional.absent();
        }
        else {
            return Optional.of(get(projectName));
        }
    }



    public String getProjectName() {
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
