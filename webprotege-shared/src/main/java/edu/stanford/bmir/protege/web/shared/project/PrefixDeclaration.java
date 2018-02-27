package edu.stanford.bmir.protege.web.shared.project;

import com.google.common.base.Objects;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Feb 2018
 *
 * A small class that supports the mapping of a prefix name to a prefix.  This class does not
 * perform any proper checking of prefix names or prefixes.  The only requirement is that
 * prefix names must be terminated with a colon.
 */
public class PrefixDeclaration {

    private final String prefixName;

    private final String prefix;


    private PrefixDeclaration(@Nonnull String prefixName,
                              @Nonnull String prefix) {
        this.prefixName = checkNotNull(prefixName);
        this.prefix = checkNotNull(prefix);
    }

    /**
     * Gets the prefix name that maps to the declared prefix.
     */
    @Nonnull
    public String getPrefixName() {
        return prefixName;
    }

    /**
     * Gets the declared prefix.
     */
    @Nonnull
    public String getPrefix() {
        return prefix;
    }

    /**
     * Constructs a prefix declaration from the specified prefix name and the specified prefix.
     * @param prefixName The prefix name. Any string terminated by a colon is accepted here.
     *                   If the prefix name does not end with a colon then an {@link IllegalArgumentException} will be thrown.
     * @param prefix     The prefix.  Any string is accepted here.  Not checks or guarantees are made
     *                   about the prefix.
     * @return           A prefix declaration that maps the specified prefix name to the specified prefix.
     */
    @Nonnull
    public static PrefixDeclaration get(@Nonnull String prefixName,
                                        @Nonnull String prefix) {
        checkArgument(prefixName.endsWith(":"), "A prefix name must end with a colon.");
        return new PrefixDeclaration(prefixName, prefix);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(prefixName, prefix);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PrefixDeclaration)) {
            return false;
        }
        PrefixDeclaration other = (PrefixDeclaration) obj;
        return this.prefixName.equals(other.prefixName)
                && this.prefix.equals(other.prefix);
    }


    @Override
    public String toString() {
        return toStringHelper("PrefixDeclaration")
                .add("prefixName", prefixName)
                .add("prefix", prefix)
                .toString();
    }
}
