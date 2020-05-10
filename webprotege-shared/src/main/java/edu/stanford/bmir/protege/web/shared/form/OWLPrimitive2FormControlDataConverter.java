package edu.stanford.bmir.protege.web.shared.form;

import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlDataDto;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-01
 */
public class OWLPrimitive2FormControlDataConverter {

    @Nonnull
    private final OWLEntity2FormControlDataVisitor entityVisitor;

    @Inject
    public OWLPrimitive2FormControlDataConverter(@Nonnull OWLEntity2FormControlDataVisitor entityVisitor) {
        this.entityVisitor = entityVisitor;
    }

    @Nonnull
    public PrimitiveFormControlData toFormControlData(@Nonnull OWLPrimitive primitive) {
        if(primitive instanceof OWLEntity) {
            return ((OWLEntity) primitive).accept(entityVisitor);
        }
        else if(primitive instanceof IRI) {
            return PrimitiveFormControlData.get((IRI) primitive);
        }
        else if(primitive instanceof OWLLiteral) {
            return PrimitiveFormControlData.get((OWLLiteral) primitive);
        }
        else if(primitive instanceof OWLAnonymousIndividual) {
            throw new RuntimeException("Anonymous individuals are not supported");
        }
        else {
            throw new RuntimeException("Missing case for primitive type " + primitive.getClass().getName());
        }
    }
}
