package edu.stanford.bmir.protege.web.server.renderer;

import edu.stanford.bmir.protege.web.server.render.NullDeprecatedEntityChecker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/01/15
 */
@RunWith(MockitoJUnitRunner.class)
public class NullDeprecatedEntityChecker_TestCase {


    private NullDeprecatedEntityChecker checker;


    @Before
    public void setUp() throws Exception {
        checker = NullDeprecatedEntityChecker.get();
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

    @Test
    public void shouldGenerateToString() {
        assertThat(checker.toString(), startsWith("NullDeprecatedEntityChecker"));
    }

    @Test
    public void shouldReturnFalse() {
        assertThat(checker.isDeprecated(mock(OWLEntity.class)), is(false));
    }
}