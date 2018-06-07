package edu.stanford.bmir.protege.web.shared.lang;

import com.google.common.base.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/04/16
 */
public class LanguageCode implements Comparable<LanguageCode> {

    private final String lang;

    private final String name;

    public LanguageCode(String lang, String name) {
        this.lang = checkNotNull(lang);
        this.name = checkNotNull(name);
    }

    public String getLang() {
        return lang;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(LanguageCode o) {
        return this.getLang().compareToIgnoreCase(o.lang);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(lang, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof LanguageCode)) {
            return false;
        }
        LanguageCode other = (LanguageCode) obj;
        return this.lang.equals(other.lang) && this.name.equals(other.name);
    }


    @Override
    public String toString() {
        return toStringHelper("LanguageCode")
                .addValue(lang)
                .addValue(name)
                .toString();
    }
}
