package edu.stanford.bmir.protege.web.server.bioportal;



import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
public class BioPortalReleaseDate implements Serializable {

    private static final DateFormat REST_SERVICE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    private long timestamp;

    /**
     * Empty constructor for serialization purposes.
     */
    private BioPortalReleaseDate() {

    }

    private BioPortalReleaseDate(long timestamp) {
        this.timestamp = timestamp;
    }

    public static BioPortalReleaseDate createWithToday() {
        return new BioPortalReleaseDate(System.currentTimeMillis());
    }

    public static BioPortalReleaseDate createFromTimestamp(long timestamp) {
        return new BioPortalReleaseDate(timestamp);
    }
    
    public String formatForRestService() {
        return REST_SERVICE_FORMAT.format(getDate());
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Date getDate() {
        return new Date(timestamp);
    }


    @Override
    public int hashCode() {
        return "BioPortalReleaseDate".hashCode() + (int) timestamp;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof BioPortalReleaseDate)) {
            return false;
        }
        BioPortalReleaseDate other = (BioPortalReleaseDate) obj;
        return this.timestamp == other.timestamp;
    }
}
