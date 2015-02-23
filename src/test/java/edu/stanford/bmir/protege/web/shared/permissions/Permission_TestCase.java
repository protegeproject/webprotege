package edu.stanford.bmir.protege.web.shared.permissions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class Permission_TestCase {


    public static final String THE_PERMISSION_NAME = "The Permission Name";

    private Permission permission;

    private Permission otherPermission;


    @Before
    public void setUp() throws Exception {
        permission = Permission.getPermission(THE_PERMISSION_NAME);
        otherPermission = Permission.getPermission(THE_PERMISSION_NAME);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        Permission.getPermission(null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(permission, is(equalTo(permission)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(permission, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(permission, is(equalTo(otherPermission)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(permission.hashCode(), is(otherPermission.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(permission.toString(), startsWith("Permission"));
    }

    @Test
    public void shouldReturnReadPermission() {
        assertThat(Permission.getReadPermission(), is(Permission.getPermission(PermissionName.READ.getPermissionName())));
    }

    @Test
    public void shouldReturnCommentPermission() {
        assertThat(Permission.getCommentPermission(), is(Permission.getPermission(PermissionName.COMMENT.getPermissionName())));
    }

    @Test
    public void shouldReturnWritePermission() {
        assertThat(Permission.getWritePermission(), is(Permission.getPermission(PermissionName.WRITE.getPermissionName())));
    }

    @Test
    public void shouldReturnTrueForRead() {
        assertThat(Permission.getReadPermission().isReadPermission(), is(true));
    }

    @Test
    public void shouldReturnFalseForRead() {
        assertThat(permission.isReadPermission(), is(false));
    }

    @Test
    public void shouldReturnTrueForComment() {
        assertThat(Permission.getCommentPermission().isCommentPermission(), is(true));
    }

    @Test
    public void shouldReturnFalseForComment() {
        assertThat(permission.isCommentPermission(), is(false));
    }

    @Test
    public void shouldReturnTrueForWrite() {
        assertThat(Permission.getWritePermission().isWritePermission(), is(true));
    }

    @Test
    public void shouldReturnFalseForWrite() {
        assertThat(permission.isWritePermission(), is(false));
    }
}