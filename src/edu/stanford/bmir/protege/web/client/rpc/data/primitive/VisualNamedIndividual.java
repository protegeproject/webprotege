package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class VisualNamedIndividual extends VisualEntity<NamedIndividual> implements Serializable {

    public VisualNamedIndividual() {
    }

    public VisualNamedIndividual(NamedIndividual entity, String browserText) {
        super(entity, browserText);
    }

}
