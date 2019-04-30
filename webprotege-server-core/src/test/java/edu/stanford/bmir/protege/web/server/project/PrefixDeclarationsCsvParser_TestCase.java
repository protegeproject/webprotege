package edu.stanford.bmir.protege.web.server.project;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-04-30
 */
public class PrefixDeclarationsCsvParser_TestCase {

    private static final String input =
            "p1:,http://example.org/p1\n" +
            "p2:,http://example.org/p2";

    private PrefixDeclarationsCsvParser parser;

    @Before
    public void setUp() {
        parser = new PrefixDeclarationsCsvParser();
    }

    @Test
    public void shouldParsePrefixDeclarationsFile() throws IOException {
        var prefixDeclarations = parser.parse(new ByteArrayInputStream(input.getBytes()));

        assertThat(prefixDeclarations, hasSize(2));

        var p1 = prefixDeclarations.get(0);
        assertThat(p1.getPrefixName(), is("p1:"));
        assertThat(p1.getPrefix(), is("http://example.org/p1"));

        var p2 = prefixDeclarations.get(1);
        assertThat(p2.getPrefixName(), is("p2:"));
        assertThat(p2.getPrefix(), is("http://example.org/p2"));
    }
}
