package edu.stanford.bmir.protege.web.server.owlapi;

import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/04/2012
 */
public class OWLEntityCreator<E extends OWLEntity> {

    private E entity;

    private List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();

    public OWLEntityCreator(E entity, List<OWLOntologyChange> changes) {
        this.entity = entity;
        this.changes.addAll(changes);
    }


    public E getEntity() {
        return entity;
    }

    public List<OWLOntologyChange> getChanges() {
        return new ArrayList<OWLOntologyChange>(changes);
    }
}
