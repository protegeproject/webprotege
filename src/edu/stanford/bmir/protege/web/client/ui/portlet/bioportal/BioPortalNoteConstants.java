package edu.stanford.bmir.protege.web.client.ui.portlet.bioportal;

import java.util.LinkedHashMap;
import java.util.Map;

import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;



public class BioPortalNoteConstants {

    static final String ID = "id";
    static final String VALUES = "values";
    static final String STATUS = "status";
    static final String BODY = "body";
    static final String SUBJECT = "subject";
    static final String CREATED = "created";
    static final String AUTHOR = "author";
    static final String TYPE = "type";
    static final String ONTOLOGY_ID = "ontologyId";
    static final String ASSOCIATED = "associated"; //threading
    static final String ARCHIVED = "archived";

    public final static String NOTE_TYPE_COMMENT = "Comment";

    /*
     * Note status
     */

    static final String STATUS_NOT_SET = "Not set";
    static final String STATUS_SUBMITTED = "Submitted";
    static final String STATUS_UNDER_DISCUSSION = "Under Discussion";
    static final String STATUS_UNDER_REVIEW = "Under Review";
    static final String STATUS_APPROVED = "Approved";
    static final String STATUS_REJECTED = "Rejected";
    static final String STATUS_IMPLEMENTED = "Implemented";
    static final String STATUS_PUBLISHED = "Published";
    static final String STATUS_OTHER = "Other ...";

    static final Map<String, Integer> STATUS_2_INDEX_MAP = new LinkedHashMap<String, Integer>() {
        {
            put(STATUS_NOT_SET, 0);
            put(STATUS_SUBMITTED, 1);
            put(STATUS_UNDER_DISCUSSION, 2);
            put(STATUS_UNDER_REVIEW, 3);
            put(STATUS_APPROVED, 4);
            put(STATUS_REJECTED, 5);
            put(STATUS_IMPLEMENTED, 6);
            put(STATUS_PUBLISHED, 7);
           //put(STATUS_OTHER, 8); //TODO: support later
        }
    };

    // Entity types for notes

    static final Map<ValueType, String> VALUE_TYPE_2_ENTITY_TYPE = new LinkedHashMap<ValueType, String>() {
        {
            put(ValueType.Cls, "Class");
            put(ValueType.Property, "Property");
            put(ValueType.Instance, "Individual");
        }
    };

    static final String ENTITY_TYPE_ONTOLOGY = "Ontology";
    static final String ENTITY_TYPE_NOTE = "Note";


    /*
     * Users
     */
    static final String FIRSTNAME = "firstname";
    static final String LASTNAME = "lastname";
    static final String USERNAME = "username";

}
