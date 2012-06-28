package edu.stanford.bmir.protege.web.client.ui.propertygrid;

import edu.stanford.bmir.protege.web.client.rpc.data.primitive.Entity;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.Property;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.VisualEntity;
import edu.stanford.bmir.protege.web.client.rpc.data.tuple.TripleTuple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class PropertyGrid implements Serializable {

    private Entity subject;

    private List<TripleTuple<?,?,?>> gridEntries = new ArrayList<TripleTuple<?,?,?>>();

    private PropertyGrid() {
    }

    public PropertyGrid(Entity subject) {
        this.subject = subject;
    }

    public PropertyGrid(Entity subject, Collection<TripleTuple<?, ?, ?>> gridEntries) {
        this.subject = subject;
        this.gridEntries.addAll(gridEntries);
    }

    public List<TripleTuple<?, ?, ?>> getGridEntries() {
        return Collections.unmodifiableList(gridEntries);
    }

    public Entity getSubject() {
        return subject;
    }

    public int getSize() {
        return gridEntries.size();
    }

}
