package edu.stanford.bmir.protege.web.server.inject;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-12
 */
public class MongoCredentialProvider_TestCase {

    private String userName = "THE_USER_NAME";

    private String authenticationDatabase = "AUTHENTICATION_DATABASE";

    private char[] password = "THE_PASSWORD".toCharArray();

    @Before
    public void setUp() {
        createProvider();
    }

    private MongoCredentialProvider createProvider() {
        return new MongoCredentialProvider(userName, authenticationDatabase, password);
    }

    @Test
    public void shouldSupplyUserName() {
        var provider = createProvider();
        var credential = provider.get();
        assertThat(credential.isPresent(), is(true));
        assertThat(credential.get().getUserName(), is(userName));
    }

    @Test
    public void shouldSupplyAuthenticationDatabaseName() {
        var provider = createProvider();
        var credential = provider.get();
        assertThat(credential.isPresent(), is(true));
        assertThat(credential.get().getSource(), is(authenticationDatabase));
    }

    @Test
    public void shouldSupplyPassword() {
        var provider = createProvider();
        var credential = provider.get();
        assertThat(credential.isPresent(), is(true));
        assertThat(credential.get().getPassword(), is(password));
    }

    @Test
    public void shouldNotProvideCredentialsIfUserNameIsEmpty() {
        userName = "";
        var provider = createProvider();
        var credential = provider.get();
        assertThat(credential.isEmpty(), is(true));
    }

    @Test
    public void shouldNotProvideCredentialsIfAuthenticationDbNameIsEmpty() {
        authenticationDatabase = "";
        var provider = createProvider();
        var credential = provider.get();
        assertThat(credential.isEmpty(), is(true));
    }

    @Test
    public void shouldNotProvideCredentialsIfPasswordIsEmpty() {
        password = new char [0];
        var provider = createProvider();
        var credential = provider.get();
        assertThat(credential.isEmpty(), is(true));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfUserNameIsNull() {
        userName = null;
        createProvider();
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfAuthenticationDbNameIsNull() {
        authenticationDatabase = null;
        createProvider();
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfPasswordIsNull() {
        password = null;
        createProvider();
    }
}

