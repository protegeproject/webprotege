package edu.stanford.bmir.protege.web.server.session;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/15
 */
public class WebProtegeSessionAttribute<T> {

    public static final WebProtegeSessionAttribute<UserId> LOGGED_IN_USER = new WebProtegeSessionAttribute<>("loggedInUser");

    private final String attributeName;

    public WebProtegeSessionAttribute(String attributeName) {
        this.attributeName = checkNotNull(attributeName);
    }

    public String getAttributeName() {
        return attributeName;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(attributeName);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof WebProtegeSessionAttribute)) {
            return false;
        }
        WebProtegeSessionAttribute other = (WebProtegeSessionAttribute) obj;
        return this.attributeName.equals(other.attributeName);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("WebProtegeSessionAttribute")
                .addValue(attributeName)
                .toString();
    }
}
