package edu.stanford.bmir.protege.web.server.crud.uuid;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.crud.ChangeSetEntityCrudSession;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudContext;
import edu.stanford.bmir.protege.web.server.crud.PrefixedNameExpander;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityShortForm;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UUIDSuffixSettings;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.Namespaces;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.List;

import static edu.stanford.bmir.protege.web.server.OWLDeclarationAxiomMatcher.declarationFor;
import static edu.stanford.bmir.protege.web.server.OWLEntityMatcher.hasIRI;
import static edu.stanford.bmir.protege.web.server.OWLEntityMatcher.owlThing;
import static edu.stanford.bmir.protege.web.server.RdfsLabelWithLexicalValueMatcher.rdfsLabelWithLexicalValue;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 16/04/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class UUIDEntityCrudKitHandlerTestCase {

    public static final String PREFIX = "http://stuff/";
    @Mock
    protected EntityCrudKitPrefixSettings prefixSettings;

    @Mock
    protected UUIDSuffixSettings suffixSettings;

    @Mock
    protected EntityCrudContext crudContext;

    @Mock
    protected EntityShortForm entityShortForm;

    @Mock
    protected OWLOntology ontology;

    @Mock
    protected OntologyChangeList.Builder<OWLClass> builder;

    @Mock
    protected ChangeSetEntityCrudSession session;

    private UUIDEntityCrudKitHandler handler;

    @Before
    public void setUp() throws Exception {
        OWLDataFactoryImpl dataFactory = new OWLDataFactoryImpl();
        when(prefixSettings.getIRIPrefix()).thenReturn(PREFIX);
        when(crudContext.getDataFactory()).thenReturn(dataFactory);
        when(crudContext.getTargetOntology()).thenReturn(ontology);
        when(crudContext.getPrefixedNameExpander()).thenReturn(PrefixedNameExpander.builder().withNamespaces(Namespaces.values()).build());
        when(crudContext.getTargetLanguage()).thenReturn(Optional.absent());
        when(ontology.containsEntityInSignature(any(OWLEntity.class))).thenReturn(true);
        handler = new UUIDEntityCrudKitHandler(prefixSettings, suffixSettings);
    }

    @Test
    public void shouldAddDeclaration() {
        when(entityShortForm.getShortForm()).thenReturn("A");
        OWLClass cls = handler.create(session, EntityType.CLASS, entityShortForm, crudContext, builder);
        ArgumentCaptor<OWLDeclarationAxiom> addAxiomCaptor = ArgumentCaptor.forClass(OWLDeclarationAxiom.class);
        verify(builder, atLeast(1)).addAxiom(any(OWLOntology.class), addAxiomCaptor.capture());
        List<OWLDeclarationAxiom> addedAxioms = addAxiomCaptor.getAllValues();
        assertThat(addedAxioms, (Matcher) hasItem(is(declarationFor(cls))));
    }

    @Test
    public void shouldAddLabelEqualToSuppliedName() {
        String suppliedName = "MyLabel";
        when(entityShortForm.getShortForm()).thenReturn(suppliedName);
        handler.create(session, EntityType.CLASS, entityShortForm, crudContext, builder);
        verifyHasLabelEqualTo(suppliedName);
    }

    @Test
    public void shouldCreatedExpandedPrefixName() {
        when(entityShortForm.getShortForm()).thenReturn("owl:Thing");
        OWLClass cls = handler.create(session, EntityType.CLASS, entityShortForm, crudContext, builder);
        assertThat(cls, is(owlThing()));
    }

    @Test
    public void shouldAddLabelEqualToPrefixedName() {
        String suppliedName = "owl:Thing";
        when(entityShortForm.getShortForm()).thenReturn(suppliedName);
        handler.create(session, EntityType.CLASS, entityShortForm, crudContext, builder);
        verifyHasLabelEqualTo(suppliedName);
    }

    @Test
    public void shouldCreateEntityWithAbsoluteIriIfSpecified() {
        String expectedIRI = "http://stuff.com/A";
        String shortForm = "<" + expectedIRI + ">";
        when(entityShortForm.getShortForm()).thenReturn(shortForm);
        OWLClass cls = handler.create(session, EntityType.CLASS, entityShortForm, crudContext, builder);
        assertThat(cls, hasIRI(expectedIRI));
        verifyHasLabelEqualTo(expectedIRI);
    }


    private void verifyHasLabelEqualTo(String label) {
        ArgumentCaptor<OWLAnnotationAssertionAxiom> addAxiomCaptor = ArgumentCaptor.forClass(OWLAnnotationAssertionAxiom.class);
        verify(builder, atLeast(1)).addAxiom(any(OWLOntology.class), addAxiomCaptor.capture());
        List<OWLAnnotationAssertionAxiom> addedAxioms = addAxiomCaptor.getAllValues();
        assertThat(addedAxioms, (Matcher) hasItem(rdfsLabelWithLexicalValue(label)));
    }


}
