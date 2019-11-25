package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-24
 */
@AutoValue
public abstract class GridColumnDescriptor {

    @JsonCreator
    @Nonnull
    public static GridColumnDescriptor get(@Nonnull @JsonProperty("label") LanguageMap columnLabel,
                                           @Nonnull @JsonProperty("placeholder") LanguageMap placeholder,
                                           @Nonnull @JsonProperty("formFieldDescriptor") FormFieldDescriptor formFieldDescriptor) {
        return new AutoValue_GridColumnDescriptor(columnLabel, placeholder, formFieldDescriptor);
    }

    @Nonnull
    public abstract LanguageMap getLabel();

    @Nonnull
    public abstract LanguageMap getPlaceholder();

    @Nonnull
    public abstract FormFieldDescriptor getFieldDescriptor();

}
