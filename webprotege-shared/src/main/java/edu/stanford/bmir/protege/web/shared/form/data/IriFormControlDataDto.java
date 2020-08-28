package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.entity.IRIData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import java.util.Optional;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class IriFormControlDataDto extends PrimitiveFormControlDataDto {

    public static IriFormControlDataDto get(@Nonnull IRIData iriData) {
        return new AutoValue_IriFormControlDataDto(iriData);
    }

    @Nonnull
    public abstract IRIData getIri();

    @Nonnull
    @Override
    public PrimitiveFormControlData toPrimitiveFormControlData() {
        return IriFormControlData.get(getIri().getObject());
    }

    @Nonnull
    @Override
    public Optional<OWLLiteral> asLiteral() {
        return Optional.empty();
    }

    @Nonnull
    @Override
    public OWLPrimitiveData getPrimitiveData() {
        return getIri();
    }
}
