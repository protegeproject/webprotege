package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.server.frame.FrameComponentSessionRenderer;
import edu.stanford.bmir.protege.web.shared.entity.IRIData;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import edu.stanford.bmir.protege.web.shared.form.data.IriFormControlDataDto;
import edu.stanford.bmir.protege.web.shared.form.data.LiteralFormControlDataDto;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlDataDto;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLPrimitive;

import javax.annotation.Nonnull;
import javax.inject.Inject;

public class PrimitiveFormControlDataDtoRenderer {

    @Inject
    public PrimitiveFormControlDataDtoRenderer() {
    }

    @Nonnull
    public PrimitiveFormControlDataDto toFormControlDataDto(@Nonnull OWLPrimitive primitive,
                                                             @Nonnull FrameComponentSessionRenderer sessionRenderer) {
        if(primitive instanceof IRI) {
            return IriFormControlDataDto.get(IRIData.get((IRI) primitive, ImmutableMap.of()));
        }
        else if(primitive instanceof OWLLiteral) {
            return LiteralFormControlDataDto.get(OWLLiteralData.get((OWLLiteral) primitive));
        }
        else if(primitive instanceof OWLEntity) {
            var entityRendering = sessionRenderer.getEntityRendering((OWLEntity) primitive);
            return PrimitiveFormControlDataDto.get(entityRendering);
        }
        else {
            throw new RuntimeException("Cannot handle primitive " + primitive);
        }
    }
}
