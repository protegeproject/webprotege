package edu.stanford.bmir.protege.web.server.persistence;

import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/20/13
 */
@ReadingConverter
public class EmailAddressReadConverter implements Converter<String, EmailAddress> {

    public EmailAddress convert(String source) {
        return new EmailAddress(source);
    }
}