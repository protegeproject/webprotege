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
public class UserIdWriteConverterTestCase {

    @Test
    public void convertShouldReturnSuppliedUserName() {
        UserIdWriteConverter converter = new UserIdWriteConverter();
        String suppliedUserName = "janedoe";
        UserId userId = UserId.getUserId(suppliedUserName);
        String convertedUserName = converter.convert(userId);
        assertEquals(suppliedUserName, convertedUserName);
    }


}
