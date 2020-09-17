package edu.stanford.bmir.protege.web.shared.form.data;

import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.IRIData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import java.util.Optional;

public abstract class PrimitiveFormControlDataDto implements Comparable<PrimitiveFormControlDataDto> {

    public static final int BEFORE = -1;
    public static final int AFTER = 1;

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

    @Override
    public int compareTo(@Nonnull PrimitiveFormControlDataDto other) {
        // Literals then Entities then IRIs
        if(this instanceof LiteralFormControlDataDto) {
            if(other instanceof LiteralFormControlDataDto) {
                return ((LiteralFormControlDataDto) this).getLiteral().compareTo(((LiteralFormControlDataDto) other).getLiteral());
            }
            else {
                // Always before any other types
                return BEFORE;
            }
        }
        else if(this instanceof EntityFormControlDataDto) {
            if(other instanceof EntityFormControlDataDto) {
                return ((EntityFormControlDataDto) this).getEntity().compareTo(((EntityFormControlDataDto) other).getEntity());
            }
            else if(other instanceof LiteralFormControlDataDto) {
                return AFTER;
            }
            else {
                // IRI
                return BEFORE;
            }
        }
        else {
            // We are and IRI
            if(other instanceof IriFormControlDataDto) {
                return ((IriFormControlDataDto) this).getIri().compareTo(((IriFormControlDataDto) other).getIri());
            }
            else {
                return AFTER;
            }
        }
    }

    public boolean isDeprecated() {
        return false;
    }

    @Nonnull
    public abstract OWLPrimitiveData getPrimitiveData();
}
