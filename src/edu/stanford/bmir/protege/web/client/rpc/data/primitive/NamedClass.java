package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class NamedClass extends Entity implements ObjectPropertyEdgeSubject, ObjectPropertyEdgeValue, DataPropertyEdgeSubject {

    private static final NamedClass OWL_THING = new NamedClass(IRI.create(Prefix.OWL + "Thing"));

    private static final NamedClass OWL_NOTHING = new NamedClass(IRI.create(Prefix.OWL + "Nothing"));

    private NamedClass() {
    }

    public NamedClass(IRI iri) {
        super(iri);
    }

    @Override
    public <R, E extends Exception> R accept(EntityVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public VisualObject<?> toVisualObject(String browserText) {
        return new VisualNamedClass(this, browserText);
    }
    
    public static NamedClass getOWLThing() {
        return OWL_THING;
    }
    
    public static NamedClass getOWLNothing() {
        return OWL_NOTHING;
    }
}
