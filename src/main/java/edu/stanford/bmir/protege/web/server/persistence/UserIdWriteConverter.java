package edu.stanford.bmir.protege.web.server.persistence;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/20/13
 * <p>
 *     An implementation of a {@link Converter} that writes a {@link UserId} to a {@link String}.
 * </p>
 */
@WritingConverter
public class UserIdWriteConverter implements Converter<UserId, String> {

    public String convert(UserId userId) {
        return userId.getUserName();
    }
}
