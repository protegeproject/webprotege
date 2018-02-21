package edu.stanford.bmir.protege.web.server.persistence;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/20/13
 */
public class UserIdReadConverterTestCase {

    @Test
    public void convertShouldReturnUserIdWithSuppliedUserName() {
        UserIdReadConverter converter = new UserIdReadConverter();
        String suppliedName = "janedoe";
        UserId UserId = converter.convert(suppliedName);
        assertEquals(suppliedName, UserId.getUserName());
    }
}
