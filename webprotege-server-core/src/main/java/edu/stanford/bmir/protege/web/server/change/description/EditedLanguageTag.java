package edu.stanford.bmir.protege.web.server.change.description;

import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2018-12-10
 */
@AutoValue
public abstract class EditedLanguageTag implements StructuredChangeDescription {

    @Nonnull
    public static EditedLanguageTag get(@Nonnull IRI subject,
                                         @Nonnull OWLAnnotationProperty property,
                                         @Nonnull OWLAnnotationValue value,
                                         @Nonnull String fromLanguageTag,
                                        @Nonnull String toLanguageTag) {
        return new AutoValue_EditedLanguageTag(subject,
                                                property,
                                                value,
                                                fromLanguageTag,
                                               toLanguageTag);
    }

    public abstract IRI getSubject();

    public abstract OWLAnnotationProperty getProperty();

    public abstract OWLAnnotationValue getValue();

    public abstract String getFromLanguageTag();

    public abstract String getToLanguageTag();

    @Nonnull
    @Override
    public String getTypeName() {
        return "EditedLanguageTag";
    }

    @Nonnull
    @Override
    public String formatDescription(@Nonnull OWLObjectStringFormatter formatter) {
        return formatter.formatString("Changed language tag from %s %s on %s annotation on %s",
                                      getFromLanguageTag(),
                                      getToLanguageTag(),
                                      getProperty(),
                                      getSubject());
    }

}
