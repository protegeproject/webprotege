package edu.stanford.bmir.protege.web.shared.project;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/04/2013
 */
public class ImportedOntologyMetadata implements Serializable {

    private OWLOntologyID ontologyId;

    private IRI originalDocumentLocation;

    private long accessTimestamp;



    private ImportedOntologyMetadata() {
    }

    public ImportedOntologyMetadata(OWLOntologyID ontologyId, IRI originalDocumentLocation, long accessTimestamp) {
        this.ontologyId = ontologyId;
        this.originalDocumentLocation = originalDocumentLocation;
        this.accessTimestamp = accessTimestamp;
    }


    public OWLOntologyID getOntologyId() {
        return ontologyId;
    }

    public IRI getOriginalDocumentLocation() {
        return originalDocumentLocation;
    }

    public long getAccessTimestamp() {
        return accessTimestamp;
    }

    @Override
    public int hashCode() {
        return "ImportedOntologyMetadata".hashCode() + ontologyId.hashCode() + originalDocumentLocation.hashCode() + (int) accessTimestamp;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ImportedOntologyMetadata)) {
            return false;
        }
        ImportedOntologyMetadata other = (ImportedOntologyMetadata) obj;
        return this.ontologyId.equals(other.ontologyId) && this.originalDocumentLocation.equals(other.originalDocumentLocation) && this.accessTimestamp == other.accessTimestamp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ImportedOntologyMetadata");
        sb.append("(");
        sb.append(ontologyId);
        sb.append(" OriginalDocumentLocation(");
        sb.append(originalDocumentLocation);
        sb.append(") TimeStamp(");
        sb.append(accessTimestamp);
        sb.append("))");
        return sb.toString();
    }
}
