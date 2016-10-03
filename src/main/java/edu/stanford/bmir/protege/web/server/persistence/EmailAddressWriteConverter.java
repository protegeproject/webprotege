package edu.stanford.bmir.protege.web.server.persistence;

import edu.stanford.bmir.protege.web.shared.user.EmailAddress;

import org.springframework.data.convert.WritingConverter;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/20/13
 */
@WritingConverter
public class EmailAddressWriteConverter implements Converter<EmailAddress, String> {

    public String convert(EmailAddress source) {
        return source.getEmailAddress();
    }
}
