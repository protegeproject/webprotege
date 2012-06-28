package edu.stanford.bmir.protege.web.server.owlapi.notes;

import java.util.Date;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/04/2012
 */
public class NoteTimeStamp {

    private long timestamp;

    public NoteTimeStamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int hashCode() {
        return (int) timestamp * 37;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof NoteTimeStamp)) {
            return false;
        }
        NoteTimeStamp other = (NoteTimeStamp) obj;
        return other.timestamp == this.timestamp;
    }
    
    public Date getDate() {
        return new Date(timestamp);
    }

    @Override
    public String toString() {
        return new Date(timestamp).toString();
    }
}
