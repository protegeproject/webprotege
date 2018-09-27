package edu.stanford.bmir.protege.web.shared.bulkop;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Sep 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class NewAnnotationData {

    public static NewAnnotationData get(@Nonnull Optional<OWLAnnotationProperty> property,
                                        @Nonnull Optional<String> value,
                                        @Nonnull Optional<String> languageTag) {
        return new AutoValue_NewAnnotationData(property.orElse(null),
                                           value.orElse(null),
                                           languageTag.orElse(null));
    }

    @Nullable
    protected abstract OWLAnnotationProperty property();

    @Nonnull
    public Optional<OWLAnnotationProperty> getProperty() {
        return Optional.ofNullable(property());
    }

    @Nullable
    protected abstract String value();

    @Nonnull
    public Optional<String> getValue() {
        return Optional.ofNullable(value());
    }

    @Nullable
    protected abstract String languageTag();

    @Nonnull
    public Optional<String> getLanguageTag() {
        return Optional.ofNullable(languageTag());
    }
}
