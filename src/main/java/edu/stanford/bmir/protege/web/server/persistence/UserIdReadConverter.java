package edu.stanford.bmir.protege.web.server.persistence;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/20/13
 * <p>
 *     An implementation of {@link Converter} that reads a {@link UserId} from a {@link String}.
 * </p>
 */
@ReadingConverter
public class UserIdReadConverter implements Converter<String, UserId> {

    public UserId convert(String userId) {
        return UserId.getUserId(userId);
    }
}
