package edu.stanford.bmir.protege.web.server.crud;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.vocab.Namespaces;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 16/04/2014
 */
public class PrefixedNameExpanderBuilderTestCase {

    private PrefixedNameExpander.Builder builder;

    @Before
    public void setUp() throws Exception {
        builder = PrefixedNameExpander.builder();
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForNullPrefixName() {
        builder.withPrefixNamePrefix(null, "x");
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForNullPrefix() {
        builder.withPrefixNamePrefix("x:", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionForNonColonizedPrefixName() {
        builder.withPrefixNamePrefix("x", "y");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionForDoubleColonizedPrefixName() {
        builder.withPrefixNamePrefix("x::", "y");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionForStartEndDoubleColonizedPrefixName() {
        builder.withPrefixNamePrefix(":x:", "y");
    }

    @Test
    public void shouldAddNamespaces() {
        builder.withNamespaces(Namespaces.values());
    }
}
