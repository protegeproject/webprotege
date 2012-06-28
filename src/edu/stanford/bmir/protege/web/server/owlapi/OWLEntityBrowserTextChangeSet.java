package edu.stanford.bmir.protege.web.server.owlapi;

import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/05/2012
 */
public class OWLEntityBrowserTextChangeSet {

    private String oldBrowserText;
    
    private String newBrowserText;

    private OWLEntity entity;
    

    public OWLEntityBrowserTextChangeSet(OWLEntity entity, String oldBrowserText, String newBrowserText) {
        this.entity = entity;
        this.oldBrowserText = oldBrowserText;
        this.newBrowserText = newBrowserText;
    }

    public OWLEntity getEntity() {
        return entity;
    }

    public String getOldBrowserText() {
        return oldBrowserText;
    }

    public String getNewBrowserText() {
        return newBrowserText;
    }

    @Override
    public int hashCode() {
        return entity.hashCode() + oldBrowserText.hashCode() + newBrowserText.hashCode() * 37;// + ontologyChanges.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof OWLEntityBrowserTextChangeSet)) {
            return false;
        }
        OWLEntityBrowserTextChangeSet other = (OWLEntityBrowserTextChangeSet) obj;
        return other.entity.equals(this.entity) && other.oldBrowserText.equals(this.oldBrowserText) && other.newBrowserText.equals(this.newBrowserText);// && other.ontologyChanges.equals(this.ontologyChanges);
    }
}
