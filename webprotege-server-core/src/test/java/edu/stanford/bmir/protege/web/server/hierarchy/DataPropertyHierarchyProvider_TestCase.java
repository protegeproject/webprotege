package edu.stanford.bmir.protege.web.server.hierarchy;

import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-22
 */
@RunWith(MockitoJUnitRunner.class)
public class DataPropertyHierarchyProvider_TestCase {

    private DataPropertyHierarchyProviderImpl provider;

    @Mock
    private ProjectId projectId;

    @Mock
    private OWLDataPropertyProvider dataPropertyProvider;

    @Mock
    private ProjectSignatureByTypeIndex projectSignatureIndex;

    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Mock
    private SubDataPropertyAxiomsBySubPropertyIndex subPropertyAxiomsIndex;

    @Mock
    private EntitiesInProjectSignatureIndex entitiesInSignature;

    @Mock
    private OWLOntologyID ontologyId;

    private OWLDataFactory dataFactory = new OWLDataFactoryImpl();

    private OWLDataProperty propertyA = dataFactory.getOWLDataProperty(mockIri());

    private OWLDataProperty propertyB = dataFactory.getOWLDataProperty(mockIri());

    private OWLDataProperty propertyC = dataFactory.getOWLDataProperty(mockIri());

    private OWLDataProperty propertyD = dataFactory.getOWLDataProperty(mockIri());

    @Mock
    private AxiomsByTypeIndex axiomsByTypeIndex;

    @Mock
    private OntologySignatureByTypeIndex ontologySignatureByTypeIndex;

    private static IRI mockIri() {
        return mock(IRI.class);
    }

    @Before
    public void setUp() {

        when(projectOntologiesIndex.getOntologyIds())
                .thenAnswer(invocation -> Stream.of(ontologyId));

        var propertyASubPropertyOfB = dataFactory.getOWLSubDataPropertyOfAxiom(propertyA, propertyB,
                                                                               noAnnotations());

        var propertyBSubPropertyOfC = dataFactory.getOWLSubDataPropertyOfAxiom(propertyB, propertyC,
                                                                               noAnnotations());


        when(subPropertyAxiomsIndex.getSubPropertyOfAxioms(any(), any()))
                .thenAnswer(invocation -> Stream.empty());

        when(subPropertyAxiomsIndex.getSubPropertyOfAxioms(propertyA, ontologyId))
                .thenAnswer(invocation -> Stream.of(propertyASubPropertyOfB));

        when(subPropertyAxiomsIndex.getSubPropertyOfAxioms(propertyB, ontologyId))
                .thenAnswer(invocation -> Stream.of(propertyBSubPropertyOfC));

        when(entitiesInSignature.containsEntityInSignature(propertyC))
                .thenReturn(true);
        when(entitiesInSignature.containsEntityInSignature(propertyD))
                .thenReturn(true);

        when(ontologySignatureByTypeIndex.getSignature(EntityType.DATA_PROPERTY, ontologyId))
                .thenAnswer(invocation -> Stream.of(propertyA, propertyB, propertyC, propertyD));

        when(axiomsByTypeIndex.getAxiomsByType(AxiomType.SUB_DATA_PROPERTY, ontologyId))
                .thenAnswer(invocation -> Stream.of(propertyASubPropertyOfB, propertyBSubPropertyOfC));

        provider = new DataPropertyHierarchyProviderImpl(projectId,
                                                         dataFactory.getOWLTopDataProperty(),
                                                         projectOntologiesIndex,
                                                         axiomsByTypeIndex,
                                                         ontologySignatureByTypeIndex,
                                                         subPropertyAxiomsIndex,
                                                         entitiesInSignature);
    }

    private static Set<OWLAnnotation> noAnnotations() {
        return Collections.emptySet();
    }

    @Test
    public void shouldGetParents() {
        var parents = provider.getParents(propertyA);
        assertThat(parents, contains(propertyB));
    }

    @Test
    public void shouldGetAncestors() {
        var ancestors = provider.getAncestors(propertyA);
        assertThat(ancestors, containsInAnyOrder(propertyB, propertyC));
    }

    @Test
    public void shouldGetChidlren() {
        var children = provider.getChildren(propertyC);
        assertThat(children, contains(propertyB));
    }

    @Test
    public void shouldGetDescendants() {
        var descendants = provider.getDescendants(propertyC);
        assertThat(descendants, containsInAnyOrder(propertyA, propertyB));
    }

    @Test
    public void shouldGetRoots() {
        var roots = provider.getRoots();
        assertThat(roots, hasItems(dataFactory.getOWLTopDataProperty()));
    }

}
