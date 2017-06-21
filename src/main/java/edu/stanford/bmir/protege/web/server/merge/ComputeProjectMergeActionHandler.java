package edu.stanford.bmir.protege.web.server.merge;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import edu.stanford.bmir.protege.web.client.csv.DocumentId;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.inject.UploadsDirectory;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.mansyntax.render.*;
import edu.stanford.bmir.protege.web.server.owlapi.HasAnnotationAssertionAxiomsImpl;
import edu.stanford.bmir.protege.web.server.owlapi.WebProtegeOWLManager;
import edu.stanford.bmir.protege.web.server.project.*;
import edu.stanford.bmir.protege.web.server.shortform.DefaultShortFormAnnotationPropertyIRIs;
import edu.stanford.bmir.protege.web.server.shortform.WebProtegeIRIShortFormProvider;
import edu.stanford.bmir.protege.web.server.shortform.WebProtegeShortFormProvider;
import edu.stanford.bmir.protege.web.server.util.TempFileFactoryImpl;
import edu.stanford.bmir.protege.web.server.util.ZipInputStreamChecker;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.diff.DiffOperation;
import edu.stanford.bmir.protege.web.shared.merge.ComputeProjectMergeAction;
import edu.stanford.bmir.protege.web.shared.merge.ComputeProjectMergeResult;
import edu.stanford.bmir.protege.web.shared.merge.OntologyDiff;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_ONTOLOGY;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.UPLOAD_AND_MERGE;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class ComputeProjectMergeActionHandler extends AbstractHasProjectActionHandler<ComputeProjectMergeAction, ComputeProjectMergeResult> {

    private static final Logger logger = LoggerFactory.getLogger(ComputeProjectMergeActionHandler.class);

    @Nonnull
    @UploadsDirectory
    private final File uploadsDirectory;

    @Nonnull
    @RootOntology
    private final OWLOntology projectRootOntology;

    @Nonnull
    private final Comparator<OWLAxiom> axiomComparator;

    @Nonnull
    private final ShortFormProvider shortFormProvider;

    @Nonnull
    private final HasLang hasLang;

    @Inject
    public ComputeProjectMergeActionHandler(@Nonnull AccessManager accessManager,
                                            @Nonnull @UploadsDirectory File uploadsDirectory,
                                            @Nonnull @RootOntology OWLOntology projectRootOntology,
                                            @Nonnull Comparator<OWLAxiom> axiomComparator,
                                            @Nonnull ShortFormProvider shortFormProvider,
                                            @Nonnull HasLang lang) {
        super(accessManager);
        this.uploadsDirectory = uploadsDirectory;
        this.projectRootOntology = projectRootOntology;
        this.axiomComparator = axiomComparator;
        this.shortFormProvider = shortFormProvider;
        this.hasLang = lang;
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions() {
        return Arrays.asList(EDIT_ONTOLOGY, UPLOAD_AND_MERGE);
    }

    @Override
    public ComputeProjectMergeResult execute(ComputeProjectMergeAction action, ExecutionContext executionContext) {
        try {
            DocumentId documentId = action.getProjectDocumentId();

            OWLOntology uploadedRootOntology = loadUploadedOntology(documentId);

            Set<OntologyDiff> diffs = computeDiff(uploadedRootOntology, projectRootOntology);

            List<DiffElement<String, SafeHtml>> transformedDiff = renderDiff(uploadedRootOntology, diffs);

            return new ComputeProjectMergeResult(transformedDiff);

        } catch (Exception e) {
            logger.info("An error occurred whilst merging ontologies", e);
            throw new RuntimeException(e);
        }
    }

    private OWLOntology loadUploadedOntology(DocumentId documentId) throws IOException, OWLOntologyCreationException {
        // Extract sources
        UploadedProjectSourcesExtractor extractor = new UploadedProjectSourcesExtractor(
                new ZipInputStreamChecker(),
                new ZipArchiveProjectSourcesExtractor(
                        new TempFileFactoryImpl(),
                        new RootOntologyDocumentMatcherImpl()),
                new SingleDocumentProjectSourcesExtractor());
        // Load sources
        OWLOntologyManager rootOntologyManager = WebProtegeOWLManager.createOWLOntologyManager();
        final File file = new File(uploadsDirectory, documentId.getDocumentId());
        RawProjectSources rawProjectSources = extractor.extractProjectSources(file);
        OWLOntologyLoaderConfiguration loaderConfig = new OWLOntologyLoaderConfiguration();
        RawProjectSourcesImporter importer = new RawProjectSourcesImporter(rootOntologyManager, loaderConfig);
        return importer.importRawProjectSources(rawProjectSources);
    }

    private List<DiffElement<String, SafeHtml>> renderDiff(OWLOntology uploadedRootOntology, Set<OntologyDiff> diffs) {

        final ShortFormProvider dualShortFormProvider = getShortFormProvider(uploadedRootOntology);

        final OWLObjectRenderer renderer = getManchesterSyntaxObjectRenderer(projectRootOntology,
                                                                             uploadedRootOntology,
                                                                             dualShortFormProvider);

        List<DiffElement<String, OWLAxiom>> diffElements = getDiffElements(diffs);
        sortDiff(diffElements);

        // Transform from OWLAxiom to SafeHtml
        List<DiffElement<String, SafeHtml>> transformedDiff = new ArrayList<>();
        for (DiffElement<String, OWLAxiom> element : diffElements) {
            String html = renderer.render(element.getLineElement());
            SafeHtml rendering = new SafeHtmlBuilder().appendHtmlConstant(html).toSafeHtml();
            transformedDiff.add(new DiffElement<>(element.getDiffOperation(), element.getSourceDocument(), rendering));
        }
        return transformedDiff;
    }

    private Set<OntologyDiff> computeDiff(OWLOntology uploadedRootOntology, OWLOntology projectRootOntology) {
        OntologyDiffCalculator diffCalculator = new OntologyDiffCalculator(
                new AnnotationDiffCalculator(),
                new AxiomDiffCalculator()
        );
        ModifiedProjectOntologiesCalculator ontsCalculator = new ModifiedProjectOntologiesCalculator(
                ImmutableSet.copyOf(projectRootOntology.getImportsClosure()),
                ImmutableSet.copyOf(uploadedRootOntology.getImportsClosure()),
                diffCalculator
        );
        return ontsCalculator.getModifiedOntologyDiffs();
    }


    private void sortDiff(List<DiffElement<String, OWLAxiom>> diffElements) {

        Collections.sort(diffElements, new Comparator<DiffElement<String, OWLAxiom>>() {
            @Override
            public int compare(DiffElement<String, OWLAxiom> o1, DiffElement<String, OWLAxiom> o2) {
                int diff = axiomComparator.compare(o1.getLineElement(), o2.getLineElement());
                if (diff != 0) {
                    return diff;
                }
                int opDiff = o1.getDiffOperation().compareTo(o2.getDiffOperation());
                if (opDiff != 0) {
                    return opDiff;
                }
                return o1.getSourceDocument().compareTo(o2.getSourceDocument());
            }
        });
    }

    private List<DiffElement<String, OWLAxiom>> getDiffElements(Set<OntologyDiff> diffs) {
        List<DiffElement<String, OWLAxiom>> diffElements = new ArrayList<>();
        for (OntologyDiff diff : diffs) {
            for (OWLAxiom ax : diff.getAxiomDiff().getAdded()) {
                diffElements.add(new DiffElement<>(DiffOperation.ADD, "ontology", ax));
            }
            for (OWLAxiom ax : diff.getAxiomDiff().getRemoved()) {
                diffElements.add(new DiffElement<>(DiffOperation.REMOVE, "ontology", ax));
            }
        }
        return diffElements;
    }

    private OWLObjectRenderer getManchesterSyntaxObjectRenderer(final OWLOntology projectRootOntology,
                                                                final OWLOntology uploadedRootOntology,
                                                                ShortFormProvider dualShortFormProvider) {
        return new ManchesterSyntaxObjectRenderer(
                dualShortFormProvider,
                new EntityIRIChecker() {

                    private EntityIRIChecker firstDelegate = new EntityIRICheckerImpl(projectRootOntology);

                    private EntityIRIChecker secondDelegate = new EntityIRICheckerImpl(uploadedRootOntology);

                    @Override
                    public boolean isEntityIRI(IRI iri) {
                        return firstDelegate.isEntityIRI(iri) || secondDelegate.isEntityIRI(iri);
                    }

                    @Override
                    public Collection<OWLEntity> getEntitiesWithIRI(IRI iri) {
                        Collection<OWLEntity> first = firstDelegate.getEntitiesWithIRI(iri);
                        if (!first.isEmpty()) {
                            return first;
                        }
                        return secondDelegate.getEntitiesWithIRI(iri);
                    }
                },
                LiteralStyle.REGULAR,
                new NullHttpLinkRenderer(),
                new MarkdownLiteralRenderer()
        );
    }

    private ShortFormProvider getShortFormProvider(
            final OWLOntology uploadedRootOntology) {

        final ShortFormProvider uploadedOntologyShortFormProvider = new WebProtegeShortFormProvider(
                new WebProtegeIRIShortFormProvider(
                        DefaultShortFormAnnotationPropertyIRIs.asImmutableList(),
                        new HasAnnotationAssertionAxiomsImpl(uploadedRootOntology), new HasLang() {
                    @Override
                    public String getLang() {
                        return hasLang.getLang();
                    }
                }
                )
        );

        return new ShortFormProvider() {
            @Override
            public String getShortForm(OWLEntity owlEntity) {
                if (owlEntity.isBuiltIn()) {
                    return shortFormProvider.getShortForm(owlEntity);
                }
                else if (projectRootOntology.containsEntityInSignature(owlEntity, true)) {
                    return shortFormProvider.getShortForm(owlEntity);
                }
                else if (uploadedRootOntology.containsEntityInSignature(owlEntity, true)) {
                    return uploadedOntologyShortFormProvider.getShortForm(owlEntity);
                }
                else {
                    return shortFormProvider.getShortForm(owlEntity);
                }
            }

            @Override
            public void dispose() {

            }
        };
    }

    @Override
    public Class<ComputeProjectMergeAction> getActionClass() {
        return ComputeProjectMergeAction.class;
    }
}
