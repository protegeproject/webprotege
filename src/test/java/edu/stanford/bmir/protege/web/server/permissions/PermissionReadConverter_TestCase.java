package edu.stanford.bmir.protege.web.server.permissions;

import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/03/16
 */
public class PermissionReadConverter_TestCase {

    public static final String THE_PERMISSION = "The Permission";

    private PermissionReadConverter converter;

    @Before
    public void setUp() {
        converter = new PermissionReadConverter();
    }

    @Test
    public void shouldReadPermission() {
        Permission permission = converter.convert(THE_PERMISSION);
        assertThat(permission.getPermissionName(), is(THE_PERMISSION));
    }
}
