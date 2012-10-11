package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class AnnotationProperty extends Property implements HasVisualObject {

    private static final AnnotationProperty RDFS_LABEL = new AnnotationProperty(Prefix.RDFS, "label");

    private static final AnnotationProperty RDFS_COMMENT = new AnnotationProperty(Prefix.RDFS, "comment");

    private static final AnnotationProperty RDFS_SEE_ALSO = new AnnotationProperty(Prefix.RDFS, "seeAlso");

    public AnnotationProperty(IRI iri) {
        super(iri);
    }

    private AnnotationProperty(Prefix prefix, String localName) {
        super(prefix, localName);
    }

    @Override
    public <R, E extends Exception> R accept(EntityVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public VisualAnnotationProperty toVisualObject(String browserText) {
        return new VisualAnnotationProperty(this, browserText);
    }
    
    public static AnnotationProperty getRDFSLabel() {
        return RDFS_LABEL;
    }

    public static AnnotationProperty getRDFSComment() {
        return RDFS_COMMENT;
    }

    public static AnnotationProperty getRDFSSeeAlso() {
        return RDFS_SEE_ALSO;
    }
}
