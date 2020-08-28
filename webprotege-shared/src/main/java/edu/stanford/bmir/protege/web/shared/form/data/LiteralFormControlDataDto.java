package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import java.util.Optional;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class LiteralFormControlDataDto extends PrimitiveFormControlDataDto {

    @Nonnull
    public abstract OWLLiteralData getLiteral();

    @Nonnull
    @Override
    public PrimitiveFormControlData toPrimitiveFormControlData() {
        return LiteralFormControlData.get(getLiteral().getLiteral());
    }

    @Nonnull
    @Override
    public Optional<OWLLiteral> asLiteral() {
        return Optional.of(getLiteral().getLiteral());
    }

    @Nonnull
    @Override
    public OWLPrimitiveData getPrimitiveData() {
        return getLiteral();
    }
}
