package edu.stanford.bmir.protege.web.shared.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

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
@JsonPropertyOrder({"prefixName", "prefix"})
public class PrefixDeclaration implements IsSerializable {

    private String prefixName;

    private String prefix;


    private PrefixDeclaration(@Nonnull String prefixName,
                              @Nonnull String prefix) {
        this.prefixName = checkNotNull(prefixName);
        this.prefix = checkNotNull(prefix);
    }

    @GwtSerializationConstructor
    private PrefixDeclaration() {
    }

    /**
     * Gets the prefix name that maps to the declared prefix.
     */
    @JsonProperty("prefixName")
    @Nonnull
    public String getPrefixName() {
        return prefixName;
    }

    /**
     * Gets the declared prefix.
     */
    @JsonProperty("prefix")
    @Nonnull
    public String getPrefix() {
        return prefix;
    }

    /**
     *  Determines if the prefix in this declaration has a common terminating character.  The vast majority
     * of well-known prefixes end with either a slash or a hash (according to prefix.cc).  Here, we also allow
     * an underscore as a common terminating character in order to support the kinds of prefixes used
     * for OBO identifiers.
     * @return true if the prefix associated with this prefix declaration ends with a slash (/), hash (#) or
     * underscore (_), otherwise false.
     */
    @JsonIgnore
    public boolean isPrefixWithCommonTerminatingCharacter() {
        return prefix.endsWith("/") || prefix.endsWith("#") || prefix.endsWith("_");
    }

    /**
     * Constructs a prefix declaration from the specified prefix name and the specified prefix.
     * @param prefixName The prefix name. Any string terminated by a colon is accepted here.
     *                   If the prefix name does not end with a colon then an {@link IllegalArgumentException} will be thrown.
     * @param prefix     The prefix.  Any string is accepted here.  Not checks or guarantees are made
     *                   about the prefix.
     * @return           A prefix declaration that maps the specified prefix name to the specified prefix.
     */
    @JsonCreator
    @Nonnull
    public static PrefixDeclaration get(@Nonnull @JsonProperty("prefixName") String prefixName,
                                        @Nonnull @JsonProperty("prefix") String prefix) {
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
