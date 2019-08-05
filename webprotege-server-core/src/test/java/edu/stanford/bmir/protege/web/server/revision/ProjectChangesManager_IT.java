package edu.stanford.bmir.protege.web.server.revision;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.axiom.*;
import edu.stanford.bmir.protege.web.server.change.ChangeRecordComparator;
import edu.stanford.bmir.protege.web.server.diff.Revision2DiffElementsTranslator;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsIndex;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsIndexCachingImpl;
import edu.stanford.bmir.protege.web.server.lang.ActiveLanguagesManagerImpl;
import edu.stanford.bmir.protege.web.server.lang.LanguageManager;
import edu.stanford.bmir.protege.web.server.mansyntax.render.*;
import edu.stanford.bmir.protege.web.server.object.OWLObjectComparatorImpl;
import edu.stanford.bmir.protege.web.server.owlapi.HasAnnotationAssertionAxiomsImpl;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsRepository;
import edu.stanford.bmir.protege.web.server.renderer.OWLObjectRendererImpl;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.server.shortform.*;
import edu.stanford.bmir.protege.web.shared.change.ProjectChange;
import edu.stanford.bmir.protege.web.shared.object.*;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

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
    private ProjectDetailsRepository repo;

    @Before
    public void setUp() throws Exception {
        changeHistoryFile = temporaryFolder.newFile();
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology rootOntology = manager.createOntology(IRI.create("http://stuff.com/ont"));
        OWLDataFactory dataFactory = manager.getOWLDataFactory();
        RevisionManager revisionManager = new RevisionManagerImpl(new RevisionStoreImpl(
                projectId,
                changeHistoryFile,
                dataFactory
        ));
        when(repo.findOne(projectId)).thenReturn(Optional.empty());
        when(repo.getDisplayNameLanguages(projectId)).thenReturn(ImmutableList.of());
        WebProtegeIRIShortFormProvider iriShortFormProvider = new WebProtegeIRIShortFormProvider(
                DefaultShortFormAnnotationPropertyIRIs.asImmutableList(),
                new HasAnnotationAssertionAxiomsImpl(rootOntology),
                () -> "",
                new LocalNameExtractor()
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
        AnnotationAssertionAxiomsIndex assertionAxiomsIndex = new AnnotationAssertionAxiomsIndexCachingImpl(rootOntology);
        LanguageManager languageManager = new LanguageManager(projectId, new ActiveLanguagesManagerImpl(projectId,
                                                                                                        assertionAxiomsIndex), repo);
        RenderingManager renderingManager = new RenderingManager(
                new DictionaryManager(languageManager, new MultiLingualDictionaryImpl(projectId, new DictionaryBuilder(projectId, rootOntology), new DictionaryUpdater(rootOntology)),
                                      new BuiltInShortFormDictionary(new ShortFormCache(), dataFactory)),
                new DeprecatedEntityCheckerImpl(rootOntology),
                new ManchesterSyntaxObjectRenderer(
                        webProtegeShortFormProvider,
                        new EntityIRICheckerImpl(rootOntology),
                        LiteralStyle.BRACKETED,
                        new DefaultHttpLinkRenderer(),
                        new MarkdownLiteralRenderer()
                ));

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
        changesManager = new ProjectChangesManager(projectId, revisionManager,
                                                   renderingManager,
                                                   new ChangeRecordComparator(
                        axiomComparator,
                        (o1, o2) -> 0
                ),
                                                   () -> new Revision2DiffElementsTranslator(ontologyIRIShortFormProvider, rootOntology));


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
        Page<ProjectChange> projectChanges = changesManager.getProjectChanges(Optional.empty(),
                                                                              PageRequest.requestFirstPage());
        assertThat(projectChanges.getPageElements().size(), is(1));
    }

    @Test
    public void shouldContainOntologyChanges() {
        Page<ProjectChange> projectChanges = changesManager.getProjectChanges(Optional.empty(),
                                                                                       PageRequest.requestFirstPage());
        assertThat(projectChanges.getPageElements().get(0).getChangeCount(), is(3 * CHANGE_COUNT));
    }
}
