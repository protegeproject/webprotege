package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public interface EntityVisitor<R,E extends Exception> {

    R visit(Cls cls) throws E;
    
    R visit(ObjectProperty property) throws E;
    
    R visit(DataProperty property) throws E;
    
    R visit(AnnotationProperty property) throws E;
    
    R visit(Datatype datatype) throws E;
    
    R visit(NamedIndividual individual) throws E;
}
