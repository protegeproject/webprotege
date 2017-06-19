package edu.stanford.bmir.protege.web.server.crud;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;


/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 16/04/2014
 */
public class PrefixedNameExpanderTestCase {

    public static final String PREFIX_NAME = "myPrefixName:";

    public static final String PREFIX = "thePrefix";

    public static final String SUPPLIED_NAME = "Name";


    private PrefixedNameExpander expander;


    @Before
    public void setUp() {
        expander = PrefixedNameExpander.builder().withPrefixNamePrefix(PREFIX_NAME, PREFIX).build();
    }

    @Test
    public void shouldReturnemptyForEmptySuppliedName() {
        java.util.Optional<IRI> expanded = expander.getExpandedPrefixName("");
        assertThat(expanded, is(equalTo(Optional.<IRI>empty())));
    }

    @Test
    public void shouldReturnemptyForSuppliedNameEqualToSingleColon() {
        java.util.Optional<IRI> expanded = expander.getExpandedPrefixName(":");
        assertThat(expanded, is(equalTo(Optional.<IRI>empty())));
    }

    @Test
    public void shouldReturnemptyForEmptyPrefixName() {
        java.util.Optional<IRI> expanded = expander.getExpandedPrefixName(":" + SUPPLIED_NAME);
        assertThat(expanded, is(equalTo(Optional.<IRI>empty())));
    }

    @Test
    public void shouldReturnemptyForSuppliedNameWithPrefixNameButWithEmptyLocalName() {
        java.util.Optional<IRI> expanded = expander.getExpandedPrefixName(PREFIX_NAME);
        assertThat(expanded, is(equalTo(Optional.<IRI>empty())));
    }

    @Test
    public void shouldReturnemptyForSuppliedNameWithoutColon() {
        java.util.Optional<IRI> expanded = expander.getExpandedPrefixName(SUPPLIED_NAME);
        assertThat(expanded, is(equalTo(Optional.<IRI>empty())));
    }

    @Test
    public void shouldExpandSuppliedNameWithColonThatMatchesPrefixName() {
        java.util.Optional<IRI> expanded = expander.getExpandedPrefixName(PREFIX_NAME + SUPPLIED_NAME);
        assertThat(expanded, is(equalTo(Optional.of(IRI.create(PREFIX + SUPPLIED_NAME)))));
    }

    @Test
    public void shouldReturnemptyForSuppliedNameWithColonThatDoesNotMatchPrefixName() {
        java.util.Optional<IRI> expanded = expander.getExpandedPrefixName("abc:" + SUPPLIED_NAME);
        assertThat(expanded, is(equalTo(Optional.<IRI>empty())));
    }
}
