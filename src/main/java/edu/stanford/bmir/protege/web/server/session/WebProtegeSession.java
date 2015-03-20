package edu.stanford.bmir.protege.web.server.session;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/15
 */
public interface WebProtegeSession {

    /**
     * Gets an attribute.
     * @param attribute The attribute to get.  Not {@code null}.
     * @param <T> The type of the attribute value.
     * @return The optional attribute value.  Not {@code null}.
     * @throws java.lang.NullPointerException if {@code attribute} is {@code null}.
     */
    <T> Optional<T> getAttribute(WebProtegeSessionAttribute<T> attribute);

    /**
     * Sets an attribute.
     * @param attribute The attribute to set.  Not {@code null}.
     * @param value The value to set.  Not {@code null}.
     * @param <T> The type of the value
     * @throws java.lang.NullPointerException if any parameters are {@code null}.
     */
    <T> void setAttribute(WebProtegeSessionAttribute<T> attribute, T value);

    /**
     * Removes an attribute.  This has no effect if the attribute has not been set.
     * @param attribute The attribute to be removed. Not {@code null}.
     */
    void removeAttribute(WebProtegeSessionAttribute<?> attribute);

    /**
     * Gets the user in this session.
     * @return The {@link edu.stanford.bmir.protege.web.shared.user.UserId} of the user in this session.
     * Not {@code null}.  If there is not a specific user in this session then the {@link edu.stanford.bmir.protege.web.shared.user.UserId}
     * of the guest user will be returned.
     */
    UserId getUserInSession();

    /**
     * Sets the user in the session.
     * @param userId The userId.  Not {@code null}.
     * @throws java.lang.NullPointerException if any parameters are {@code null}.
     */
    void setUserInSession(UserId userId);

    /**
     *  Sets the guest user as the user in this session.
     */
    void clearUserInSession();

}
