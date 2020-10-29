package edu.stanford.bmir.protege.web.server.mansyntax;

import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentIdDisplayNameProvider;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/03/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class ShellOntologyChecker_TestCase {

    private static final String ONTOLOGY_SHORT_FORM = "OntologyShortForm";

    private static final String DEFAULT_ONTOLOGY_SHORT_FORM = "DefaultOntologyShortForm";

    @Mock
    protected OntologyDocumentIdDisplayNameProvider displayNameProvider;

    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Mock
    private DefaultOntologyIdManager defaultOntologyManager;

    @Mock
    private OntologyDocumentId ontologyId;

    @Mock
    private OntologyDocumentId defaultOntologyId;

    private ShellOntologyChecker checker;

    @Before
    public void setUp() {
        checker = new ShellOntologyChecker(projectOntologiesIndex,
                                           displayNameProvider,
                                           defaultOntologyManager);
        when(defaultOntologyManager.getDefaultOntologyDocumentId())
                .thenReturn(defaultOntologyId);
        when(projectOntologiesIndex.getOntologyDocumentIds())
                .thenAnswer(invocation -> Stream.of(ontologyId, defaultOntologyId));
        when(displayNameProvider.getDisplayName(ontologyId))
                .thenReturn(ONTOLOGY_SHORT_FORM);
        when(displayNameProvider.getDisplayName(defaultOntologyId))
                .thenReturn(DEFAULT_ONTOLOGY_SHORT_FORM);
    }

    /**
     * Test for legacy behaviour
     */
    @Test
    public void shouldReturnRootOntologyForNullArgument() {
        OWLOntology ont = checker.getOntology(null);
        assertThat(ont, Matchers.is(notNullValue()));
        assertThat(ont.getOntologyID(), is(equalTo(defaultOntologyId)));
    }

    @Test
    public void shouldReturnNullForUnknownShortForm() {
        OWLOntology ont = checker.getOntology("x");
        assertThat(ont, is((OWLOntology) null));
    }

    @Test
    public void shouldReturnOntologyWithGivenShortForm() {
        OWLOntology ont = checker.getOntology(ONTOLOGY_SHORT_FORM);
        assertThat(ont, Matchers.is(notNullValue()));
        assertThat(ont.getOntologyID(), is(equalTo(ontologyId)));
    }

}
