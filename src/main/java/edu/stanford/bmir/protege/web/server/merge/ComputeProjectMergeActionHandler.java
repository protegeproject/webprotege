package edu.stanford.bmir.protege.web.server.merge;

import edu.stanford.bmir.protege.web.shared.merge.ComputeProjectMergeResult;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import edu.stanford.bmir.protege.web.client.rpc.data.DocumentId;
import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.FixedMessageChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.comparator.AxiomComparator;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.filesubmission.FileUploadConstants;
import edu.stanford.bmir.protege.web.server.owlapi.*;
import edu.stanford.bmir.protege.web.server.owlapi.manager.WebProtegeOWLManager;
import edu.stanford.bmir.protege.web.server.render.*;
import edu.stanford.bmir.protege.web.server.shortform.DefaultShortFormAnnotationPropertyIRIs;
import edu.stanford.bmir.protege.web.server.shortform.WebProtegeIRIShortFormProvider;
import edu.stanford.bmir.protege.web.server.shortform.WebProtegeShortFormProvider;
import edu.stanford.bmir.protege.web.server.util.DefaultTempFileFactory;
import edu.stanford.bmir.protege.web.server.util.ZipInputStreamChecker;
import edu.stanford.bmir.protege.web.shared.axiom.*;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.diff.DiffOperation;
import edu.stanford.bmir.protege.web.shared.merge.ComputeProjectMergeAction;
import edu.stanford.bmir.protege.web.shared.merge.ComputeProjectMergeResult;
import edu.stanford.bmir.protege.web.shared.merge.OntologyDiff;
import edu.stanford.bmir.protege.web.shared.object.*;
import org.apache.commons.lang.StringUtils;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.AxiomSubjectProvider;
import org.semanticweb.owlapi.util.OWLEntityComparator;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class ComputeProjectMergeActionHandler extends AbstractHasProjectActionHandler<ComputeProjectMergeAction, ComputeProjectMergeResult> {


    @Override
    protected RequestValidator<ComputeProjectMergeAction> getAdditionalRequestValidator(ComputeProjectMergeAction action, RequestContext requestContext) {
        return UserHasProjectReadPermissionValidator.get();
    }

    @Override
    protected ComputeProjectMergeResult execute(ComputeProjectMergeAction action, final OWLAPIProject project, ExecutionContext executionContext) {
        try {


            DocumentId documentId = action.getProjectDocumentId();

            // Extract sources
            UploadedProjectSourcesExtractor extractor = new UploadedProjectSourcesExtractor(
                    new ZipInputStreamChecker(),
                    new ZipArchiveProjectSourcesExtractor(
                            new DefaultTempFileFactory(),
                            new DefaultRootOntologyDocumentMatcher()),
                    new SingleDocumentProjectSourcesExtractor());
            // Load sources
            OWLOntologyManager rootOntologyManager = WebProtegeOWLManager.createOWLOntologyManager();
            final File file = new File(FileUploadConstants.UPLOADS_DIRECTORY, documentId.getDocumentId());

            RawProjectSources rawProjectSources = extractor.extractProjectSources(file);
            OWLOntologyLoaderConfiguration loaderConfig = new OWLOntologyLoaderConfiguration();
            RawProjectSourcesImporter importer = new RawProjectSourcesImporter(rootOntologyManager, loaderConfig);
            final OWLOntology uploadedRootOntology = importer.importRawProjectSources(rawProjectSources);


            // Compute diff
            OntologyDiffCalculator diffCalculator = new OntologyDiffCalculator(
                    new AnnotationDiffCalculator(),
                    new AxiomDiffCalculator()
            );

            ModifiedProjectOntologiesCalculator ontsCalculator = new ModifiedProjectOntologiesCalculator(
                    ImmutableSet.copyOf(project.getRootOntology().getImportsClosure()),
                    ImmutableSet.copyOf(uploadedRootOntology.getImportsClosure()),
                    diffCalculator
            );

            Set<OntologyDiff> diffs = ontsCalculator.getModifiedOntologyDiffs();


            final ShortFormProvider dualShortFormProvider = getShortFormProvider(project, uploadedRootOntology);
            final ManchesterSyntaxObjectRenderer renderer = getManchesterSyntaxObjectRenderer(project, uploadedRootOntology, dualShortFormProvider);

            List<DiffElement<String, OWLAxiom>> diffElements = getDiffElements(diffs);
            sortDiff(dualShortFormProvider, renderer, diffElements);

            List<DiffElement<String, SafeHtml>> transformedDiff = new ArrayList<>();
            for (DiffElement<String, OWLAxiom> element : diffElements) {
                HighlightedEntityChecker highlightChecker = NullHighlightedEntityChecker.get();
                DeprecatedEntityChecker deprecatedChecker = NullDeprecatedEntityChecker.get();
                String html = renderer.render(element.getLineElement(), highlightChecker, deprecatedChecker);
                SafeHtml rendering = new SafeHtmlBuilder().appendHtmlConstant(html).toSafeHtml();
                transformedDiff.add(new DiffElement<>(element.getDiffOperation(), element.getSourceDocument(), rendering));
            }
            return new ComputeProjectMergeResult(transformedDiff);


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void sortDiff(ShortFormProvider dualShortFormProvider, ManchesterSyntaxObjectRenderer renderer, List<DiffElement<String, OWLAxiom>> diffElements) {
        final DefaultAxiomComparator axiomComparator = getDefaultAxiomComparator(dualShortFormProvider, renderer);


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

    private DefaultAxiomComparator getDefaultAxiomComparator(ShortFormProvider dualShortFormProvider, ManchesterSyntaxObjectRenderer renderer) {
        OWLEntityComparator comparator = new OWLEntityComparator(dualShortFormProvider);
        Comparator<OWLObject> owlObjectComparator = Comparator.<OWLObject>naturalOrder();

        return new DefaultAxiomComparator(
                new AxiomBySubjectComparator(
                        new edu.stanford.bmir.protege.web.shared.axiom.AxiomSubjectProvider(
                        new OWLClassExpressionSelector(comparator),
                        new OWLObjectPropertyExpressionSelector(comparator),
                        new OWLDataPropertyExpressionSelector(comparator),
                        new OWLIndividualSelector(comparator),
                        new SWRLAtomSelector(owlObjectComparator)),
                        owlObjectComparator
                ),
                new AxiomByTypeComparator(DefaultAxiomTypeOrdering.get()),
                new AxiomByRenderingComparator(renderer)
        );
    }

    private ManchesterSyntaxObjectRenderer getManchesterSyntaxObjectRenderer(final OWLAPIProject project, final OWLOntology uploadedRootOntology, ShortFormProvider dualShortFormProvider) {
        return new ManchesterSyntaxObjectRenderer(
                        dualShortFormProvider,
                        new EntityIRIChecker() {

                            private EntityIRIChecker firstDelegate = new DefaultEntityIRIChecker(project.getRootOntology());

                            private EntityIRIChecker secondDelegate = new DefaultEntityIRIChecker(uploadedRootOntology);

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

    private ShortFormProvider getShortFormProvider(final OWLAPIProject project, final OWLOntology uploadedRootOntology) {
        final ShortFormProvider uploadedOntologyShortFormProvider = new WebProtegeShortFormProvider(
                new WebProtegeIRIShortFormProvider(
                        DefaultShortFormAnnotationPropertyIRIs.asImmutableList(),
                        new HasAnnotationAssertionAxiomsImpl(uploadedRootOntology), new HasLang() {
                    @Override
                    public String getLang() {
                        return project.getLang();
                    }
                }
                )
        );

        final ShortFormProvider projectShortFormProvider = project.getRenderingManager().getShortFormProvider();

        return new ShortFormProvider() {
            @Override
            public String getShortForm(OWLEntity owlEntity) {
                if (owlEntity.isBuiltIn()) {
                    return projectShortFormProvider.getShortForm(owlEntity);
                } else if (project.getRootOntology().containsEntityInSignature(owlEntity, true)) {
                    return projectShortFormProvider.getShortForm(owlEntity);
                } else if (uploadedRootOntology.containsEntityInSignature(owlEntity, true)) {
                    return uploadedOntologyShortFormProvider.getShortForm(owlEntity);
                } else {
                    return projectShortFormProvider.getShortForm(owlEntity);
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
