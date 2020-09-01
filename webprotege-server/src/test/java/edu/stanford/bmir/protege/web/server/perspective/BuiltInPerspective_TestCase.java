package edu.stanford.bmir.protege.web.server.perspective;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-01
 */
public class BuiltInPerspective_TestCase {

    private BuiltInPerspectiveLoader loader;

    @Before
    public void setUp() throws Exception {
        loader = new BuiltInPerspectiveLoader(new ObjectMapperProvider().get());
    }

    @Test
    public void shouldLoadBuiltInClassesPerspective() throws IOException {
        var perspective = loader.load("builtin-perspective-data/Classes.json");
        assertThat(perspective.getPerspectiveId().getId(), is("69df8fa8-4f84-499e-9341-28eb5085c40b"));
        assertThat(perspective.getLabel().get("en"), is("Classes"));
        assertThat(perspective.getLayout(), is(not(nullValue())));
    }


}
