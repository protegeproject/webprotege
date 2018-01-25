package edu.stanford.bmir.protege.web.server.mail;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2017
 */
public class MessageId {

    private final String id;

    /**
     * Constructs a {@link MessageId} for the specified id.
     * @param id The id.
     */
    public MessageId(@Nonnull String id) {
        this.id = checkNotNull(id);
    }

    /**
     * Gets the id of this {@link MessageId}.
     * @return The string value of the id.
     */
    @Nonnull
    public String getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MessageId)) {
            return false;
        }
        MessageId other = (MessageId) obj;
        return this.id.equals(other.id);
    }


    @Override
    public String toString() {
        return toStringHelper("MessageId" )
                .addValue(id)
                .toString();
    }
}
