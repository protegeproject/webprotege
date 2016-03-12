package edu.stanford.bmir.protege.web.server.permissions;

import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/03/16
 */
@RunWith(MockitoJUnitRunner.class)
public class PermissionWriteConverter_TestCase {

    public static final String PERMISSION = "The Permission";

    private PermissionWriteConverter converter;

    @Mock
    private Permission permission;

    @Before
    public void setUp() {
        converter = new PermissionWriteConverter();
        when(permission.getPermissionName()).thenReturn(PERMISSION);
    }

    @Test
    public void shouldWritePermission() {
        String converted = converter.convert(permission);
        assertThat(converted, is(PERMISSION));
    }
}
