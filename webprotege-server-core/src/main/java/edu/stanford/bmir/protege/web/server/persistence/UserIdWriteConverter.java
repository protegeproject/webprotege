package edu.stanford.bmir.protege.web.server.persistence;

import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/20/13
 * <p>
 *     An implementation of a {@link Converter} that writes a {@link UserId} to a {@link String}.
 * </p>
 */
public class UserIdWriteConverter implements Converter<UserId, String> {

    public String convert(UserId userId) {
        return userId.getUserName();
    }
}
