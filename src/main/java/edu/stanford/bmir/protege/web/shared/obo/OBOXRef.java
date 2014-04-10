package edu.stanford.bmir.protege.web.shared.obo;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class OBOXRef implements Serializable {

    /**
     * The name of the external source (historically database I suppose, but this could be an ontology, vocabulary etc.)
     */
    private String databaseName;

    /**
     * The ID of the term in the database
     */
    private String databaseId;

    /**
     * The description of the xref
     */
    private String description;


    public OBOXRef() {
        databaseName = "";
        databaseId = "";
        description = "";
    }

    /**
     * Constructs an OBOXref.  The parameters are documented in <a href="http://oboedit.org/docs/html/Text_Editing.htm#dbxref">the OBOEdit Guide</a>.
     * @param databaseName The name of the external source where the xref comes from.  Not null.
     * @param databaseId  The name of the term in the external source.
     * @param description
     */
    public OBOXRef(String databaseName, String databaseId, String description) {
        this.databaseName = databaseName;
        this.databaseId = databaseId;
        this.description = description;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getDatabaseId() {
        return databaseId;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Determines in this OBOXref is empty.
     * @return <code>true</code> if this xref is empty i.e. if the database name, database id and description are all
     * empty strings. <code>false</code> if at least one of database name, database id, description are not empty.
     */
    public boolean isEmpty() {
        return databaseName.isEmpty() && databaseId.isEmpty() && description.isEmpty();
    }
    
    public String toOBOId() {
        StringBuilder sb = new StringBuilder();
        sb.append(databaseName);
        sb.append(":");
        sb.append(databaseId);
        return sb.toString();
    }
}
