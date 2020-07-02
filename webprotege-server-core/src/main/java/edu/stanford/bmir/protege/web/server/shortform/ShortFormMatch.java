package edu.stanford.bmir.protege.web.server.shortform;

import com.google.auto.value.AutoValue;
import com.google.common.base.Objects;
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
public abstract class ShortFormMatch implements Comparable<ShortFormMatch> {

    private static final int NOT_FOUND = -1;

    private static final int BEFORE = -1;

    private static final int SAME = 0;

    private static final int AFTER = 1;

    private static final Comparator<ShortFormMatch> byMatchCount = comparing(ShortFormMatch::getMatchCount);

    private static final Comparator<ShortFormMatch> byMatchPositions = (o1, o2) -> {
        ImmutableIntArray ps1 = o1.getMatchPositions();
        ImmutableIntArray ps2 = o2.getMatchPositions();
        for (int i = 0; i < ps1.length() && i < ps2.length(); i++) {
            int p1 = ps1.get(i);
            int p2 = ps2.get(i);
            int diff;
            if (p1 == NOT_FOUND) {
                if(p2 == NOT_FOUND) {
                    diff = SAME;
                }
                else {
                    diff = AFTER;
                }
            }
            else if (p2 == NOT_FOUND) {
                return BEFORE;
            }
            else {
                diff = p1 - p2;
            }
            if (diff != 0) {
                return diff;
            }
        }
        return 0;
    };

    private static final Comparator<ShortFormMatch> byShortFormIgnoringCase = (m1, m2) -> m1.getShortForm()
                                                                                            .compareToIgnoreCase(m2.getShortForm());

    private static final Comparator<ShortFormMatch> byEntity = comparing(ShortFormMatch::getEntity);

    private static final Comparator<ShortFormMatch> comparator =
            byMatchCount.reversed()
                        .thenComparing(byMatchPositions
                                               .thenComparing(byShortFormIgnoringCase)
                                               .thenComparing(byEntity));

    @Nonnull
    public static ShortFormMatch get(@Nonnull OWLEntity entity,
                                     @Nonnull String shortForm,
                                     @Nonnull DictionaryLanguage language,
                                     int matchCount,
                                     @Nonnull ImmutableIntArray matchPositions) {
        return new AutoValue_ShortFormMatch(entity, shortForm, matchPositions, matchCount, language);
    }

    @Nonnull
    public abstract OWLEntity getEntity();

    @Nonnull
    public abstract String getShortForm();

    @Nonnull
    public abstract ImmutableIntArray getMatchPositions();

    public abstract int getMatchCount();

    @Nonnull
    public abstract DictionaryLanguage getLanguage();

    @Override
    public int compareTo(@Nonnull ShortFormMatch other) {
        return comparator.compare(this, other);
    }
}
