package edu.stanford.bmir.protege.web.shared.entity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/12/2012
 */
public class OWLPrimitiveDataVisitorAdapter<R, E extends Throwable> implements OWLPrimitiveDataVisitor<R, E> {

    protected R getDefaultReturnValue() {
        return null;
    }

    @Override
    public R visit(OWLClassData data) throws E {
        return getDefaultReturnValue();
    }

    @Override
    public R visit(OWLObjectPropertyData data) throws E {
        return getDefaultReturnValue();
    }

    @Override
    public R visit(OWLDataPropertyData data) throws E {
        return getDefaultReturnValue();
    }

    @Override
    public R visit(OWLAnnotationPropertyData data) throws E {
        return getDefaultReturnValue();
    }

    @Override
    public R visit(OWLNamedIndividualData data) throws E {
        return getDefaultReturnValue();
    }

    @Override
    public R visit(OWLDatatypeData data) throws E {
        return getDefaultReturnValue();
    }

    @Override
    public R visit(OWLLiteralData data) throws E {
        return getDefaultReturnValue();
    }

    @Override
    public R visit(IRIData data) throws E {
        return getDefaultReturnValue();
    }
}
