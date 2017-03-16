package edu.stanford.bmir.protege.web.server.revision;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.ChangeRecordComparator;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.mansyntax.render.DeprecatedEntityCheckerImpl;
import edu.stanford.bmir.protege.web.server.mansyntax.render.EntityIRICheckerImpl;
import edu.stanford.bmir.protege.web.server.mansyntax.render.NullHighlightedEntityChecker;
import edu.stanford.bmir.protege.web.server.owlapi.HasAnnotationAssertionAxiomsImpl;
import edu.stanford.bmir.protege.web.server.renderer.OWLObjectRendererImpl;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.server.shortform.*;
import edu.stanford.bmir.protege.web.shared.axiom.*;
import edu.stanford.bmir.protege.web.shared.change.ProjectChange;
import edu.stanford.bmir.protege.web.shared.object.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.OWLEntityComparator;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Mar 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectChangesManager_IT {

    public static final int CHANGE_COUNT = 100;

    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    private File changeHistoryFile;

    private ProjectChangesManager changesManager;

    private ProjectId projectId = ProjectId.get(UUID.randomUUID().toString());

    @Mock
    private WebProtegeLogger logger;

    @Before
    public void setUp() throws Exception {
        changeHistoryFile = temporaryFolder.newFile();
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology rootOntology = manager.createOntology(IRI.create("http://stuff.com/ont"));
        OWLDataFactory dataFactory = manager.getOWLDataFactory();
        RevisionManager revisionManager = new RevisionManagerImpl(new RevisionStoreImpl(
                projectId,
                changeHistoryFile,
                dataFactory,
                logger
        ));
        WebProtegeIRIShortFormProvider iriShortFormProvider = new WebProtegeIRIShortFormProvider(
                DefaultShortFormAnnotationPropertyIRIs.asImmutableList(),
                new HasAnnotationAssertionAxiomsImpl(rootOntology),
                () -> ""
        );
        WebProtegeShortFormProvider webProtegeShortFormProvider = new WebProtegeShortFormProvider(iriShortFormProvider);
        WebProtegeOntologyIRIShortFormProvider ontologyIRIShortFormProvider = new WebProtegeOntologyIRIShortFormProvider(
                rootOntology);
        OWLEntityComparator entityComparator = new OWLEntityComparator(
                webProtegeShortFormProvider
        );
        OWLClassExpressionSelector classExpressionSelector = new OWLClassExpressionSelector(entityComparator);
        OWLObjectPropertyExpressionSelector objectPropertyExpressionSelector = new OWLObjectPropertyExpressionSelector(
                entityComparator);
        OWLDataPropertyExpressionSelector dataPropertyExpressionSelector = new OWLDataPropertyExpressionSelector(
                entityComparator);
        OWLIndividualSelector individualSelector = new OWLIndividualSelector(entityComparator);
        SWRLAtomSelector atomSelector = new SWRLAtomSelector((o1, o2) -> 0);
        EntitiesByRevisionCache entitiesByRevisionCache = new EntitiesByRevisionCache(
                new AxiomSubjectProvider(
                        classExpressionSelector,
                        objectPropertyExpressionSelector,
                        dataPropertyExpressionSelector,
                        individualSelector,
                        atomSelector
                ),
                rootOntology,
                dataFactory
        );
        RenderingManager renderingManager = new RenderingManager(
                rootOntology,
                dataFactory,
                new EntityIRICheckerImpl(rootOntology),
                new DeprecatedEntityCheckerImpl(rootOntology),
                new WebProtegeBidirectionalShortFormProvider(rootOntology, webProtegeShortFormProvider),
                ontologyIRIShortFormProvider,
                new NullHighlightedEntityChecker(),
                logger
        );

        AxiomComparatorImpl axiomComparator = new AxiomComparatorImpl(
                new AxiomBySubjectComparator(
                        new AxiomSubjectProvider(
                                classExpressionSelector,
                                objectPropertyExpressionSelector,
                                dataPropertyExpressionSelector,
                                individualSelector,
                                atomSelector
                        ),
                        new OWLObjectComparatorImpl(renderingManager)
                ),
                new AxiomByTypeComparator(DefaultAxiomTypeOrdering.get()),
                new AxiomByRenderingComparator(new OWLObjectRendererImpl(renderingManager))
        );
        changesManager = new ProjectChangesManager(
                revisionManager,
                entitiesByRevisionCache,
                renderingManager,
                new ChangeRecordComparator(
                        axiomComparator,
                        (o1, o2) -> 0
                ),
                ontologyIRIShortFormProvider
        );


        createChanges(manager, rootOntology, dataFactory, revisionManager);
    }

    private static void createChanges(OWLOntologyManager manager,
                       OWLOntology rootOntology,
                       OWLDataFactory dataFactory,
                       RevisionManager revisionManager) {
        PrefixManager pm = new DefaultPrefixManager();
        pm.setDefaultPrefix("http://stuff.com/" );
        List<OWLOntologyChange> changeList = new ArrayList<>();
        for (int i = 0; i < CHANGE_COUNT; i++) {
            OWLClass clsA = dataFactory.getOWLClass(":A" + i, pm);
            OWLClass clsB = dataFactory.getOWLClass(":B" + i, pm);

            OWLSubClassOfAxiom ax = dataFactory.getOWLSubClassOfAxiom(clsA, clsB);
            changeList.add(new AddAxiom(rootOntology, ax));

            OWLAnnotationProperty rdfsLabel = dataFactory.getRDFSLabel();
            IRI annotationSubject = clsA.getIRI();
            OWLAnnotationAssertionAxiom labAxA = dataFactory.getOWLAnnotationAssertionAxiom(
                    rdfsLabel,
                    annotationSubject,
                    dataFactory.getOWLLiteral("Class A " + i)
            );
            changeList.add(new AddAxiom(rootOntology, labAxA));

            OWLAnnotationAssertionAxiom labAxB = dataFactory.getOWLAnnotationAssertionAxiom(
                    rdfsLabel,
                    annotationSubject,
                    dataFactory.getOWLLiteral("Class B " + i)
            );
            changeList.add(new AddAxiom(rootOntology, labAxB));
        }
        manager.applyChanges(changeList);
        revisionManager.addRevision(UserId.getUserId("MH" ),
                                    changeList.stream().map(OWLOntologyChange::getChangeRecord).collect(toList()),
                                    "Adding axioms");
    }

    @Test
    public void shouldSaveChangesToFile() {
        assertThat(changeHistoryFile.length(), is(greaterThan(0L)));
    }

    @Test
    public void shouldGetChanges() {
        ImmutableList<ProjectChange> projectChanges = changesManager.getProjectChanges(Optional.empty());
        assertThat(projectChanges.size(), is(1));
    }

    @Test
    public void shouldContainOntologyChanges() {
        ImmutableList<ProjectChange> projectChanges = changesManager.getProjectChanges(Optional.empty());
        assertThat(projectChanges.get(0).getChangeCount(), is(3 * CHANGE_COUNT));
    }
}
