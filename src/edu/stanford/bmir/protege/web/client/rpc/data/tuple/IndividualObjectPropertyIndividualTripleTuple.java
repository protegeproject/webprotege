package edu.stanford.bmir.protege.web.client.rpc.data.tuple;

import edu.stanford.bmir.protege.web.client.rpc.data.primitive.NamedIndividual;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.ObjectProperty;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.VisualNamedIndividual;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.VisualObjectProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class IndividualObjectPropertyIndividualTripleTuple extends TripleTuple<NamedIndividual, ObjectProperty, NamedIndividual> {

    public IndividualObjectPropertyIndividualTripleTuple(VisualNamedIndividual subject, VisualObjectProperty property, VisualNamedIndividual object) {
        super(subject, property, object);
    }
}
