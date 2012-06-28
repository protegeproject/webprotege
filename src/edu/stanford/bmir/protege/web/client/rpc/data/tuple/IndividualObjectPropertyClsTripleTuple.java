package edu.stanford.bmir.protege.web.client.rpc.data.tuple;

import edu.stanford.bmir.protege.web.client.rpc.data.primitive.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class IndividualObjectPropertyClsTripleTuple extends TripleTuple<NamedIndividual, ObjectProperty, Cls> {

    public IndividualObjectPropertyClsTripleTuple(VisualNamedIndividual subject, VisualObjectProperty property, VisualCls object) {
        super(subject, property, object);
    }
}
