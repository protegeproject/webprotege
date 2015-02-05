package edu.stanford.bmir.protege.web.server.metaproject;

import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.User;
import edu.stanford.smi.protege.server.metaproject.impl.MetaProjectImpl;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 05/02/15
 */
public class NullUserNameInMetaProject_TestCase {


    private MetaProject metaProject;

    @Before
    public void setUp() throws Exception {
        URI metaProjectURI = getClass().getResource("NullUserNameMetaproject.pprj").toURI();
        metaProject = new MetaProjectImpl(metaProjectURI);
    }

    @Test
    public void shouldFindUser() {
        Set<User> users = metaProject.getUsers();
        assertThat(users, hasItem(userWithName("John Smith")));
    }

    @Test
    public void shouldFindUserWithoutUserName() {
        Set<User> users = metaProject.getUsers();
        assertThat(users, hasItem(userWithName(null)));
    }

    private TypeSafeMatcher<User> userWithName(final String name) {
        return new TypeSafeMatcher<User>() {
            @Override
            protected boolean matchesSafely(User item) {
                    return name == null ? item.getName() == null : item.getName().equals(name);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Does not match user with name: " + name);
            }
        };
    }
}
