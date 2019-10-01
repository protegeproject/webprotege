package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import dagger.Module;
import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.server.change.RemoveAxiomChange;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-06
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectOntologiesIndexImpl_TestCase {

    @Mock
    private OWLOntologyID rootOntologyId;

    private ProjectOntologiesIndexImpl impl;

    @Mock
    private OWLAxiom axiom;

    @Before
    public void setUp() {
        impl = new ProjectOntologiesIndexImpl();
        impl.applyChanges(ImmutableList.of(AddAxiomChange.of(rootOntologyId, axiom)));
    }

    @Test
    public void shouldReturnStreamOfReferencedOntologyId() {
        var ontologyIdStream = impl.getOntologyIds();
        var ontologyIds = ontologyIdStream.collect(Collectors.toSet());
        assertThat(ontologyIds, contains(rootOntologyId));
    }

    @Test
    public void shouldOnlyContainReferencedOntologies() {
        impl.applyChanges(ImmutableList.of(RemoveAxiomChange.of(rootOntologyId, axiom)));
        assertThat(impl.getOntologyIds().count(), is(0L));
    }
}
