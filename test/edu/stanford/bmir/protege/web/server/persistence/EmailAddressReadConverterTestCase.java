package edu.stanford.bmir.protege.web.server.persistence;

import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/20/13
 */
public class EmailAddressReadConverterTestCase {

    @Test
    public void convertShouldReturnEmailAddressWithAddressEqualToSuppliedAddress() {
        EmailAddressReadConverter converter = new EmailAddressReadConverter();
        String suppliedAddress = "jane.doe@stanford.edu";
        EmailAddress convertedAddress = converter.convert(suppliedAddress);
        assertEquals(suppliedAddress, convertedAddress.getEmailAddress());
    }
}
