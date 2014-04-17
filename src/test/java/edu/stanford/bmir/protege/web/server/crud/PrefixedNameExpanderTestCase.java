package edu.stanford.bmir.protege.web.server.crud;

import com.google.common.base.Optional;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;

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
    public void shouldReturnAbsentForEmptySuppliedName() {
        Optional<IRI> expanded = expander.getExpandedPrefixName("");
        assertThat(expanded, is(equalTo(Optional.<IRI>absent())));
    }

    @Test
    public void shouldReturnAbsentForSuppliedNameEqualToSingleColon() {
        Optional<IRI> expanded = expander.getExpandedPrefixName(":");
        assertThat(expanded, is(equalTo(Optional.<IRI>absent())));
    }

    @Test
    public void shouldReturnAbsentForEmptyPrefixName() {
        Optional<IRI> expanded = expander.getExpandedPrefixName(":" + SUPPLIED_NAME);
        assertThat(expanded, is(equalTo(Optional.<IRI>absent())));
    }

    @Test
    public void shouldReturnAbsentForSuppliedNameWithPrefixNameButWithEmptyLocalName() {
        Optional<IRI> expanded = expander.getExpandedPrefixName(PREFIX_NAME);
        assertThat(expanded, is(equalTo(Optional.<IRI>absent())));
    }

    @Test
    public void shouldReturnAbsentForSuppliedNameWithoutColon() {
        Optional<IRI> expanded = expander.getExpandedPrefixName(SUPPLIED_NAME);
        assertThat(expanded, is(equalTo(Optional.<IRI>absent())));
    }

    @Test
    public void shouldExpandSuppliedNameWithColonThatMatchesPrefixName() {
        Optional<IRI> expanded = expander.getExpandedPrefixName(PREFIX_NAME + SUPPLIED_NAME);
        assertThat(expanded, is(equalTo(Optional.of(IRI.create(PREFIX + SUPPLIED_NAME)))));
    }

    @Test
    public void shouldReturnAbsentForSuppliedNameWithColonThatDoesNotMatchPrefixName() {
        Optional<IRI> expanded = expander.getExpandedPrefixName("abc:" + SUPPLIED_NAME);
        assertThat(expanded, is(equalTo(Optional.<IRI>absent())));
    }
}
