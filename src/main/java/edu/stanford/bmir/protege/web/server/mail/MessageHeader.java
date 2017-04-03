package edu.stanford.bmir.protege.web.server.mail;

import com.google.common.base.Objects;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31 Mar 2017
 */
public class MessageHeader {

    private final String name;

    private final String value;

    public MessageHeader(@Nonnull String name,
                         @Nonnull String value) {
        this.name = checkNotNull(name);
        this.value = checkNotNull(value);
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MessageHeader)) {
            return false;
        }
        MessageHeader other = (MessageHeader) obj;
        return this.name.equals(other.name)
                && this.value.equals(other.value);
    }


    @Override
    public String toString() {
        return toStringHelper("MessageHeader" )
                .add("field", name)
                .add("value", value)
                .toString();
    }

    public String format() {
        return name + ": " + value;
    }

    public static MessageHeader references(@Nonnull String references) {
        return new MessageHeader("References", references);
    }
}
