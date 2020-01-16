package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.form.field.ImageControlDescriptor;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-08
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class ImageControlData implements FormControlData {

    @JsonCreator
    public static ImageControlData get(@JsonProperty("descriptor") @Nonnull ImageControlDescriptor descriptor,
                                       @JsonProperty("iri") @Nullable IRI iri) {
        return new AutoValue_ImageControlData(descriptor, iri);
    }

    @Override
    public <R> R accept(@Nonnull FormControlDataVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public void accept(@Nonnull FormControlDataVisitor visitor) {
        visitor.visit(this);
    }

    @JsonProperty("descriptor")
    @Nonnull
    public abstract ImageControlDescriptor getDescriptor();

    @JsonProperty("iri")
    @Nullable
    protected abstract IRI getIriInternal();

    @Nonnull
    public Optional<IRI> getIri() {
        return Optional.ofNullable(getIriInternal());
    }
}
