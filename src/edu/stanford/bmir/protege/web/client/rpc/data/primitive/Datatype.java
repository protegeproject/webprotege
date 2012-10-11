package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class Datatype extends Entity implements DataPropertyEdgeValue {
    
    private static final Datatype RDF_PLAIN_LITERAL = new Datatype(Prefix.RDF , "PlainLiteral");

    private static final Datatype XSD_INTEGER = new Datatype(Prefix.XSD , "Integer");

    private static final Datatype XSD_STRING = new Datatype(Prefix.XSD , "String");

    private static final Datatype XSD_BOOLEAN = new Datatype(Prefix.XSD , "Boolean");

    private static final Datatype XSD_FLOAT = new Datatype(Prefix.XSD , "Float");

    private static final Datatype XSD_DOUBLE = new Datatype(Prefix.XSD , "Double");

    public Datatype(IRI iri) {
        super(iri);
    }

    private Datatype(Prefix prefix, String localName) {
        super(prefix, localName);
    }

    public boolean isRDFPlainLiteral() {
        return false;
    }

    @Override
    public <R, E extends Exception> R accept(EntityVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public VisualDatatype toVisualObject() {
        return toVisualObject(getDefaultShortForm());
    }

    public VisualDatatype toVisualObject(String browserText) {
        return new VisualDatatype(this, browserText);
    }

    public static Datatype getRDFPlainLiteral() {
        return RDF_PLAIN_LITERAL;
    }

    public static Datatype getXSDInteger() {
        return XSD_INTEGER;
    }

    public static Datatype getXSDString() {
        return XSD_STRING;
    }
    
    public static Datatype getXSDBoolean() {
        return XSD_BOOLEAN;
    }
    
    public static Datatype getXSDFloat() {
        return XSD_FLOAT;
    }

    public static Datatype getXSDDouble() {
        return XSD_DOUBLE;
    }

}
