package edu.stanford.bmir.protege.web.server.search;

import edu.stanford.bmir.protege.web.shared.search.SearchSubject;
import edu.stanford.bmir.protege.web.shared.search.SearchType;
import org.semanticweb.owlapi.model.OWLObject;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/06/2013
 * <p>
 * A SearchMetadata object contains a (partial) string representation of some object that can be searched.  There
 * are various categories of SearchMetadata objects as defined by the {@link edu.stanford.bmir.protege.web.shared.search.SearchType} enum.  Further
 * more, each SearchMetadata object can belong to a subgroup.
 * </p>
 */
public class SearchMetadata implements Comparable<SearchMetadata>, Serializable {

    private SearchType type;

    private String groupDescription;


    private SearchSubject subject;

    private String subjectRendering;

    private String searchString;

    /**
     * For serialization purposes only.
     */
    private SearchMetadata() {
    }

    /**
     * Records search metadata for a given object.
     * @param type The category which the search metadata falls into.
     * @param groupDescription The description (human readable name) of the subgroup which the metadata falls into.
     * @param subject The subject to which the search string pertains to.  This is usually an entity or an ontology
     * i.e.
     * something which can be selected in Protege.
     * @param subjectRendering A rendering of the subject.  This rendering is used to compare search metadata objects.
     * @param searchString The string that should be searched.
     */
    public SearchMetadata(SearchType type, String groupDescription, SearchSubject subject, String subjectRendering, String searchString) {
        this.type = checkNotNull(type);
        this.groupDescription = checkNotNull(groupDescription);
        this.subject = checkNotNull(subject);
        this.subjectRendering = checkNotNull(subjectRendering);
        this.searchString = checkNotNull(searchString);
    }




    /**
     * Gets the category which this SearchMetadata belongs to.
     * @return The SearchMetadataCategory.  Not <code>null</code>.
     */
    public SearchType getType() {
        return type;
    }

    /**
     * Gets the group description of this SearchMetadata object.
     * @return The group description.  Not <code>null</code>.
     */
    public String getGroupDescription() {
        return groupDescription;
    }

    /**
     * Gets the subject of this SearchMetadata object.  The subject is the main object that can be selected in protege
     * to which the search string pertains.  For example, the subject of a data property assertion would be the subject
     * individual.
     * @return The subject.  Not <code>null</code>.
     */
    public SearchSubject getSubject() {
        return subject;
    }

    /**
     * Gets the rendering of the subject of this SearchMetadata.
     * @return The subject rendering.  Not <code>null</code>.
     */
    public String getSubjectRendering() {
        return subjectRendering;
    }

    /**
     * Determines whether the search string provided by this SearchMetadata object is in fact the display name
     * rendering
     * of the subject of this SearchMetadata object.
     * @return <code>true</code> if the search string is a display name, otherwise <code>false</code>.
     */
    public boolean isSearchStringEntityRendering() {
        return type == SearchType.DISPLAY_NAME;
    }

    /**
     * Gets the search string which this SearchMetadata object records.
     * @return The search string.  Not <code>null</code>.
     */
    public String getSearchString() {
        return searchString;
    }

//    /**
//     * Gets a stylised version of the search string.  This stylised version contains exactly the same underlying string
//     * as returned by {@link #getSearchString()}.
//     * @return A {@link StyledString} rendering of the search string (for display in a UI).  Not <code>null</code>.
//     */
//    public StyledString getStyledSearchSearchString() {
//        return new StyledString(searchString);
//    }


    public int compareTo(SearchMetadata o) {
        int catDiff = this.type.compareTo(o.type);
        if (catDiff != 0) {
            return catDiff;
        }
        int typeDiff = this.groupDescription.compareTo(o.groupDescription);
        if (typeDiff != 0) {
            return typeDiff;
        }
        int subjectRenderingDiff = this.subjectRendering.compareTo(o.subjectRendering);
        if (subjectRenderingDiff != 0) {
            return subjectRenderingDiff;
        }
//        int subjectDiff = this.subject.compareTo(o.subject);
//        if (subjectDiff != 0) {
//            return subjectDiff;
//        }
        return searchString.compareTo(o.searchString);
    }
}
