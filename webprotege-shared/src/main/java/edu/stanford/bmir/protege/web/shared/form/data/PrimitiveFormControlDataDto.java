package edu.stanford.bmir.protege.web.shared.form.data;

import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.IRIData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import java.util.Optional;

public abstract class PrimitiveFormControlDataDto {

    public static EntityFormControlDataDto get(OWLEntityData entity) {
        return new AutoValue_EntityFormControlDataDto(entity);
    }

    public static PrimitiveFormControlDataDto get(IRIData iri) {
        return new AutoValue_IriFormControlDataDto(iri);
    }

    public static LiteralFormControlDataDto get(OWLLiteralData literal) {
        return new AutoValue_LiteralFormControlDataDto(literal);
    }

    public static LiteralFormControlDataDto get(String text) {
        return get(OWLLiteralData.get(DataFactory.getOWLLiteral(text)));
    }

    public static LiteralFormControlDataDto get(double value) {
        return LiteralFormControlDataDto.get(OWLLiteralData.get(DataFactory.getOWLLiteral(value)));
    }

    public static LiteralFormControlDataDto get(boolean value) {
        return LiteralFormControlDataDto.get(OWLLiteralData.get(DataFactory.getOWLLiteral(value)));
    }

    @Nonnull
    public abstract PrimitiveFormControlData toPrimitiveFormControlData();

    @Nonnull
    public abstract Optional<OWLLiteral> asLiteral();
}
