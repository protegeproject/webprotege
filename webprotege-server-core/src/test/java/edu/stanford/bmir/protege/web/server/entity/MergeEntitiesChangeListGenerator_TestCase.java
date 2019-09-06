package edu.stanford.bmir.protege.web.server.entity;

import com.google.common.collect.ImmutableSet;
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
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import java.util.stream.Stream;

import static edu.stanford.bmir.protege.web.shared.entity.MergedEntityTreatment.DELETE_MERGED_ENTITY;
import static edu.stanford.bmir.protege.web.shared.entity.MergedEntityTreatment.DEPRECATE_MERGED_ENTITY;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
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

    private OWLOntology rootOntology;

    private OWLDataFactory dataFactory;

    private ImmutableSet<OWLEntity> sourceEntities;

    private OWLClass targetEntity;

    private OWLAnnotationProperty rdfsLabel;

    private OWLAnnotationProperty skosPrefLabel;

    private OWLAnnotationProperty skosAltLabel;

    private OWLAnnotationProperty rdfsComment;

    private OWLAnnotationValue hello;

    private OWLAnnotationValue bonjour;

    private OWLAnnotationValue hi;

    private OWLClass clsC;

    private OWLOntologyManager manager;

    private OWLClass sourceEntity;

    private EntityRenamer entityRenamer;

    private DefaultOntologyIdManager defaultOntologyIdManager;

    private ProjectOntologiesIndex projectOntologiesIndex;

    private AnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex;

    @Before
    public void setUp() throws Exception {
        manager = OWLManager.createOWLOntologyManager();
        dataFactory = manager.getOWLDataFactory();
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
        rootOntology = Ontology(manager,
                                SubClassOf(sourceEntity, clsC),
                                SubClassOf(targetEntity, clsC),
                                AnnotationAssertion(rdfsLabel, sourceEntity.getIRI(), hello),
                                AnnotationAssertion(skosPrefLabel, sourceEntity.getIRI(), bonjour),
                                AnnotationAssertion(rdfsComment, sourceEntity.getIRI(), hi));
        defaultOntologyIdManager = new DefaultOntologyIdManagerImpl(rootOntology);
        projectOntologiesIndex = new ProjectOntologiesIndexImpl(rootOntology);
        var ontologyIndex = new OntologyIndexImpl(rootOntology);
        annotationAssertionsIndex = new AnnotationAssertionAxiomsBySubjectIndexImpl();
        var axiomsByEntityReference = new AxiomsByEntityReferenceIndexImpl(dataFactory);
        var axiomsByIriReference = new AnnotationAxiomsByIriReferenceIndexImpl();
        var axiomsByTypeIndex = new AxiomsByTypeIndexImpl();
        axiomsByIriReference.load(Stream.of(rootOntology.getOntologyID()), axiomsByTypeIndex);
        var axiomsByReferenceIndex = new AxiomsByReferenceIndexImpl(axiomsByEntityReference,
                                                                    axiomsByIriReference);

        entityRenamer = new EntityRenamer(dataFactory,
                                          projectOntologiesIndex,
                                          axiomsByReferenceIndex);

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
        manager.applyChanges(changeList.getChanges().stream().map(OntologyChange::toOwlOntologyChangeRecord).map(chg -> chg.createOntologyChange(manager)).collect(toList()));
    }

    @Test
    public void shouldReplaceSourceWithTarget_WithDeleteTreatment() {
        createGeneratorAndApplyChanges(DELETE_MERGED_ENTITY);
        assertThat(rootOntology.containsEntityInSignature(sourceEntity), is(false));
        assertThat(rootOntology.containsAxiom(SubClassOf(sourceEntity, clsC)), is(false));
        assertThat(rootOntology.containsAxiom(SubClassOf(targetEntity, clsC)), is(true));
        assertThat(rootOntology.containsAxiom(AnnotationAssertion(rdfsComment, sourceEntity.getIRI(), hi)), is(false));
        assertThat(rootOntology.containsAxiom(AnnotationAssertion(rdfsComment, targetEntity.getIRI(), hi)), is(true));
    }

    @Test
    public void shouldReplaceSourceRdfsLabelWithTargetAltLabel_WithDeleteTreatment() {
        createGeneratorAndApplyChanges(DELETE_MERGED_ENTITY);
        assertThat(rootOntology.containsAxiom(AnnotationAssertion(rdfsLabel, sourceEntity.getIRI(), hello)),
                   is(false));

        assertThat(rootOntology.containsAxiom(AnnotationAssertion(rdfsLabel, targetEntity.getIRI(), hello)),
                   is(false));

        assertThat(rootOntology.containsAxiom(AnnotationAssertion(skosAltLabel, targetEntity.getIRI(), hello)),
                   is(true));
    }

    @Test
    public void shouldReplaceSourceSkosPrefLabelWithTargetAltLabel_WithDeleteTreatment() {
        createGeneratorAndApplyChanges(DELETE_MERGED_ENTITY);
        assertThat(rootOntology.containsAxiom(AnnotationAssertion(skosPrefLabel, sourceEntity.getIRI(), bonjour)),
                   is(false));

        assertThat(rootOntology.containsAxiom(AnnotationAssertion(skosPrefLabel, targetEntity.getIRI(), bonjour)),
                   is(false));

        assertThat(rootOntology.containsAxiom(AnnotationAssertion(skosAltLabel, targetEntity.getIRI(), bonjour)),
                   is(true));
    }

    @Test
    public void shouldNotAddDeprecationWith_WithDeleteTreatment() {
        createGeneratorAndApplyChanges(DELETE_MERGED_ENTITY);
        OWLAnnotationProperty deprecated = dataFactory.getOWLDeprecated();
        assertThat(rootOntology.containsAxiom(AnnotationAssertion(deprecated, sourceEntity.getIRI(), Literal(true))), is(false));
    }

    @Test
    public void shouldAddDeprecationWith_WithDeprecateTreatment() {
        createGeneratorAndApplyChanges(DEPRECATE_MERGED_ENTITY);
        OWLAnnotationProperty deprecated = dataFactory.getOWLDeprecated();
        assertThat(rootOntology.containsAxiom(AnnotationAssertion(deprecated, sourceEntity.getIRI(), Literal(true))), is(true));
    }
}
