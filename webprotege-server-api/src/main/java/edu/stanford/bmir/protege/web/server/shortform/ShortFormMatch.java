package edu.stanford.bmir.protege.web.server.shortform;

import com.google.auto.value.AutoValue;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.ImmutableIntArray;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Comparator;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Comparator.comparing;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Apr 2018
 */
@AutoValue
public abstract class ShortFormMatch {

    @Nonnull
    public static ShortFormMatch get(@Nonnull OWLEntity entity,
                                     @Nonnull String shortForm,
                                     @Nonnull DictionaryLanguage language,
                                     @Nonnull ImmutableList<ShortFormMatchPosition> shortFormMatchPositions) {
        for(var shortFormMatchPosition : shortFormMatchPositions) {
            if(!(shortFormMatchPosition.getStart() < shortForm.length())) {
                throw new IllegalArgumentException("Short form match start must be less than short from length");
            }
            if(!(shortFormMatchPosition.getEnd() <= shortForm.length())) {
                throw new IllegalArgumentException("Short form match end must be less than or equal to the short from length");
            }
        }
        return new AutoValue_ShortFormMatch(entity, shortForm, shortFormMatchPositions, language);
    }

    @Nonnull
    public abstract OWLEntity getEntity();

    @Nonnull
    public abstract String getShortForm();

    @Nonnull
    public abstract ImmutableList<ShortFormMatchPosition> getMatchPositions();

    @Nonnull
    public abstract DictionaryLanguage getLanguage();
}
