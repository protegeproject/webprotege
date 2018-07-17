package edu.stanford.bmir.protege.web.server.shortform;

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
public class ShortFormMatch implements Comparable<ShortFormMatch> {

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
    private final OWLEntity entity;

    @Nonnull
    private final String shortForm;

    @Nonnull
    private final DictionaryLanguage language;

    private final int matchCount;

    private final ImmutableIntArray matchPositions;

    public ShortFormMatch(@Nonnull OWLEntity entity,
                          @Nonnull String shortForm,
                          @Nonnull DictionaryLanguage language,
                          int matchCount,
                          @Nonnull ImmutableIntArray matchPositions) {
        this.entity = checkNotNull(entity);
        this.shortForm = checkNotNull(shortForm);
        this.language = checkNotNull(language);
        this.matchCount = matchCount;
        this.matchPositions = checkNotNull(matchPositions);
    }

    @Nonnull
    public OWLEntity getEntity() {
        return entity;
    }

    @Nonnull
    public String getShortForm() {
        return shortForm;
    }

    @Nonnull
    public ImmutableIntArray getMatchPositions() {
        return matchPositions;
    }

    public int getMatchCount() {
        return matchCount;
    }

    @Nonnull
    public DictionaryLanguage getLanguage() {
        return language;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(entity, shortForm, language, matchCount, matchPositions);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ShortFormMatch)) {
            return false;
        }
        ShortFormMatch other = (ShortFormMatch) obj;
        return this.entity.equals(other.entity)
                && this.shortForm.equals(other.shortForm)
                && this.language.equals(other.language)
                && this.matchCount == other.matchCount
                && this.matchPositions.equals(other.matchPositions);
    }


    @Override
    public String toString() {
        return toStringHelper("ShortFormMatch")
                .add("entity", entity)
                .add("shortForm", shortForm)
                .add("language", language)
                .add("matchCount", matchCount)
                .add("matchPositions", matchPositions)
                .toString();
    }

    @Override
    public int compareTo(@Nonnull ShortFormMatch other) {
        return comparator.compare(this, other);
    }
}
