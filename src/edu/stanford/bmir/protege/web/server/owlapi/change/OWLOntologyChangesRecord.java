package edu.stanford.bmir.protege.web.server.owlapi.change;

import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/04/2012
 */
public class OWLOntologyChangesRecord implements Serializable {

    private UserId userId;
    
    private String message;

    private long timestamp;

    private List<OWLOntologyChange> changeList = new ArrayList<OWLOntologyChange>();


    public OWLOntologyChangesRecord(UserId userId, String message, long timestamp, List<? extends OWLOntologyChange> changeList) {
        this.userId = userId;
        this.message = message != null ? message : "";
        this.timestamp = timestamp;
        this.changeList.addAll(changeList);
    }

    public UserId getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public List<OWLOntologyChange> getChangeList() {
        return changeList;
    }
}
