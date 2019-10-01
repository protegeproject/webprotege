package edu.stanford.bmir.protege.web.server.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.server.index.impl.*;
import edu.stanford.bmir.protege.web.server.issues.EntityDiscussionThreadRepository;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManagerImpl;
import edu.stanford.bmir.protege.web.shared.entity.MergedEntityTreatment;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import static edu.stanford.bmir.protege.web.shared.entity.MergedEntityTreatment.DELETE_MERGED_ENTITY;
import static edu.stanford.bmir.protege.web.shared.entity.MergedEntityTreatment.DEPRECATE_MERGED_ENTITY;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Class;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Mar 2018
 */
@RunWith(MockitoJUnitRunner.class)
public class MergeEntitiesChangeListGenerator_TestCase {

    @Mock
    private ProjectId projectId;

    @Mock
    private EntityDiscussionThreadRepository discussionThreadRepo;

    private OWLDataFactory dataFactory;

    private ImmutableSet<OWLEntity> sourceEntities;

    @Mock
    private OWLOntologyID ontologyId;

    private OWLClass targetEntity;

    private OWLAnnotationProperty rdfsLabel;

    private OWLAnnotationProperty skosPrefLabel;

    private OWLAnnotationProperty skosAltLabel;

    private OWLAnnotationProperty rdfsComment;

    private OWLAnnotationValue hello;

    private OWLAnnotationValue bonjour;

    private OWLAnnotationValue hi;

    private OWLClass clsC;

    private OWLClass sourceEntity;

    private EntityRenamer entityRenamer;

    private DefaultOntologyIdManagerImpl defaultOntologyIdManager;

    private ProjectOntologiesIndexImpl projectOntologiesIndex;

    private AnnotationAssertionAxiomsBySubjectIndexImpl annotationAssertionsIndex;

    private AxiomsByEntityReferenceIndexImpl axiomsByEntityReference;

    private AxiomsByReferenceIndexImpl axiomsByReferenceIndex;

    private OntologyAxiomsIndexImpl ontologyAxiomsIndex;

    private AnnotationAxiomsByIriReferenceIndexImpl axiomsByIriReference;

    private AxiomsByTypeIndexImpl axiomsByTypeIndex;

    @Before
    public void setUp() throws Exception {
        dataFactory = new OWLDataFactoryImpl();
        IRI iriA = IRI.create("http://ontology.org/A");
        IRI iriB = IRI.create("http://ontology.org/B");
        IRI iriC = IRI.create("http://ontology.org/C");
        sourceEntity = Class(iriA);
        sourceEntities = ImmutableSet.of(sourceEntity);
        targetEntity = Class(iriB);
        clsC = Class(iriC);
        rdfsLabel = dataFactory.getRDFSLabel();
        skosPrefLabel = dataFactory.getOWLAnnotationProperty(SKOSVocabulary.PREFLABEL.getIRI());
        skosAltLabel = dataFactory.getOWLAnnotationProperty(SKOSVocabulary.ALTLABEL.getIRI());
        rdfsComment = dataFactory.getRDFSComment();
        hello = Literal("Hello", "en");
        bonjour = Literal("Bonjour", "fr");
        hi = Literal("hi", "en");
        var ontologyChanges = ImmutableList.<OntologyChange>of(
                AddAxiomChange.of(ontologyId, SubClassOf(sourceEntity, clsC)),
                AddAxiomChange.of(ontologyId, SubClassOf(targetEntity, clsC)),
                AddAxiomChange.of(ontologyId, AnnotationAssertion(rdfsLabel, sourceEntity.getIRI(), hello)),
                AddAxiomChange.of(ontologyId, AnnotationAssertion(skosPrefLabel, sourceEntity.getIRI(), bonjour)),
                AddAxiomChange.of(ontologyId, AnnotationAssertion(rdfsComment, sourceEntity.getIRI(), hi)));
        projectOntologiesIndex = new ProjectOntologiesIndexImpl();
        defaultOntologyIdManager = new DefaultOntologyIdManagerImpl(projectOntologiesIndex);
        annotationAssertionsIndex = new AnnotationAssertionAxiomsBySubjectIndexImpl();
        axiomsByEntityReference = new AxiomsByEntityReferenceIndexImpl(dataFactory);
        axiomsByIriReference = new AnnotationAxiomsByIriReferenceIndexImpl();
        axiomsByReferenceIndex = new AxiomsByReferenceIndexImpl(axiomsByEntityReference,
                                                                axiomsByIriReference);
        axiomsByTypeIndex = new AxiomsByTypeIndexImpl();
        ontologyAxiomsIndex = new OntologyAxiomsIndexImpl(axiomsByTypeIndex);

        entityRenamer = new EntityRenamer(dataFactory,
                                          this.projectOntologiesIndex,
                                          axiomsByReferenceIndex);

        applyChanges(ontologyChanges);
    }

    private void applyChanges(ImmutableList<OntologyChange> changes) {
        projectOntologiesIndex.applyChanges(changes);
        axiomsByEntityReference.applyChanges(changes);
        axiomsByIriReference.applyChanges(changes);
        axiomsByTypeIndex.applyChanges(changes);
        annotationAssertionsIndex.applyChanges(changes);
    }

    private void createGeneratorAndApplyChanges(MergedEntityTreatment treatment) {
        MergeEntitiesChangeListGenerator gen = new MergeEntitiesChangeListGenerator(sourceEntities,
                                                                                    targetEntity,
                                                                                    treatment,
                                                                                    "The commit message",
                                                                                    projectId,
                                                                                    dataFactory,
                                                                                    discussionThreadRepo,
                                                                                    entityRenamer,
                                                                                    defaultOntologyIdManager,
                                                                                    projectOntologiesIndex,
                                                                                    annotationAssertionsIndex);
        OntologyChangeList<?> changeList = gen.generateChanges(new ChangeGenerationContext(UserId.getUserId("Bob")));
        applyChanges(ImmutableList.copyOf(changeList.getChanges()));
    }

    @Test
    public void shouldReplaceSourceWithTarget_WithDeleteTreatment() {
        createGeneratorAndApplyChanges(DELETE_MERGED_ENTITY);
        assertThat(axiomsByEntityReference.containsEntityInOntologyAxiomsSignature(sourceEntity, ontologyId), is(false));
        assertThat(ontologyAxiomsIndex.containsAxiom(SubClassOf(sourceEntity, clsC), ontologyId), is(false));
        assertThat(ontologyAxiomsIndex.containsAxiom(SubClassOf(targetEntity, clsC), ontologyId), is(true));
        assertThat(ontologyAxiomsIndex.containsAxiom(AnnotationAssertion(rdfsComment, sourceEntity.getIRI(), hi), ontologyId), is(false));
        assertThat(ontologyAxiomsIndex.containsAxiom(AnnotationAssertion(rdfsComment, targetEntity.getIRI(), hi), ontologyId), is(true));
    }

    @Test
    public void shouldReplaceSourceRdfsLabelWithTargetAltLabel_WithDeleteTreatment() {
        createGeneratorAndApplyChanges(DELETE_MERGED_ENTITY);
        assertThat(ontologyAxiomsIndex.containsAxiom(AnnotationAssertion(rdfsLabel, sourceEntity.getIRI(), hello), ontologyId),
                   is(false));

        assertThat(ontologyAxiomsIndex.containsAxiom(AnnotationAssertion(rdfsLabel, targetEntity.getIRI(), hello), ontologyId),
                   is(false));

        assertThat(ontologyAxiomsIndex.containsAxiom(AnnotationAssertion(skosAltLabel, targetEntity.getIRI(), hello), ontologyId),
                   is(true));
    }

    @Test
    public void shouldReplaceSourceSkosPrefLabelWithTargetAltLabel_WithDeleteTreatment() {
        createGeneratorAndApplyChanges(DELETE_MERGED_ENTITY);
        assertThat(ontologyAxiomsIndex.containsAxiom(AnnotationAssertion(skosPrefLabel, sourceEntity.getIRI(), bonjour), ontologyId),
                   is(false));

        assertThat(ontologyAxiomsIndex.containsAxiom(AnnotationAssertion(skosPrefLabel, targetEntity.getIRI(), bonjour), ontologyId),
                   is(false));

        assertThat(ontologyAxiomsIndex.containsAxiom(AnnotationAssertion(skosAltLabel, targetEntity.getIRI(), bonjour), ontologyId),
                   is(true));
    }

    @Test
    public void shouldNotAddDeprecationWith_WithDeleteTreatment() {
        createGeneratorAndApplyChanges(DELETE_MERGED_ENTITY);
        OWLAnnotationProperty deprecated = dataFactory.getOWLDeprecated();
        assertThat(ontologyAxiomsIndex.containsAxiom(AnnotationAssertion(deprecated, sourceEntity.getIRI(), Literal(true)), ontologyId), is(false));
    }

    @Test
    public void shouldAddDeprecationWith_WithDeprecateTreatment() {
        createGeneratorAndApplyChanges(DEPRECATE_MERGED_ENTITY);
        OWLAnnotationProperty deprecated = dataFactory.getOWLDeprecated();
        assertThat(ontologyAxiomsIndex.containsAxiom(AnnotationAssertion(deprecated, sourceEntity.getIRI(), Literal(true)), ontologyId), is(true));
    }
}
