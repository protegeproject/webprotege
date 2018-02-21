package edu.stanford.bmir.protege.web.server.renderer;

import edu.stanford.bmir.protege.web.server.mansyntax.render.NullHighlightedEntityChecker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/01/15
 */
@RunWith(MockitoJUnitRunner.class)
public class NullHighlightedEntityChecker_TestCase {


    private NullHighlightedEntityChecker checker;

    @Before
    public void setUp() throws Exception {
        checker = NullHighlightedEntityChecker.get();
    }

    @Test
    public void shouldReturnFalse() {
        assertThat(checker.isHighlighted(mock(OWLEntity.class)), is(false));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(checker.toString(), startsWith("NullHighlightedEntityChecker"));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(checker, is(equalTo(checker)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(checker, is(not(equalTo(null))));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(checker.hashCode(), is(checker.hashCode()));
    }

}