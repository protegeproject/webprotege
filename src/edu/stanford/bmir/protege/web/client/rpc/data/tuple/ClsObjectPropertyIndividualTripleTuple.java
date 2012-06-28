package edu.stanford.bmir.protege.web.client.rpc.data.tuple;

import edu.stanford.bmir.protege.web.client.rpc.data.primitive.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class ClsObjectPropertyIndividualTripleTuple extends TripleTuple<Cls, ObjectProperty, NamedIndividual> {

    private ClsObjectPropertyIndividualTripleTuple() {
    }

    public ClsObjectPropertyIndividualTripleTuple(VisualCls subject, VisualObjectProperty property, VisualNamedIndividual object) {
        super(subject, property, object);
    }
}
