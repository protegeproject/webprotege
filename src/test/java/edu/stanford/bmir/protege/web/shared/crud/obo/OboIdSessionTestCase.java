package edu.stanford.bmir.protege.web.shared.crud.obo;

import edu.stanford.bmir.protege.web.server.crud.obo.OBOIdSession;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 15/09/2014
 */
public class OboIdSessionTestCase {

    private OBOIdSession session;

    @Before
    public void setUp() throws Exception {
        session = new OBOIdSession();
    }

    @Test
    public void shouldAddSession() {
        int id = 3000;
        session.addSessionId(id);
        assertThat(session.isSessionId(id), is(true));
    }

    @Test
    public void shouldNotContainSession() {
        assertThat(session.isSessionId(3000), is(false));
    }
}
