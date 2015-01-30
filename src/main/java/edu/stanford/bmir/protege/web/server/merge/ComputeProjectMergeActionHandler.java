package edu.stanford.bmir.protege.web.server.merge;

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
import edu.stanford.bmir.protege.web.shared.axiom.OWLAxiomData;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.diff.DiffOperation;
import edu.stanford.bmir.protege.web.shared.merge.ComputeProjectMergeAction;
import edu.stanford.bmir.protege.web.shared.merge.ComputeProjectMergeResult;
import edu.stanford.bmir.protege.web.shared.merge.OntologyDiff;
import org.apache.commons.lang.StringUtils;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.AxiomSubjectProvider;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

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
            final File file = new File(FileUploadConstants.UPLOADS_DIRECTORY, documentId.getDocumentId());

            UploadedProjectSourcesExtractor extractor = new UploadedProjectSourcesExtractor(
                    new ZipInputStreamChecker(),
                    new ZipArchiveProjectSourcesExtractor(
                            new DefaultTempFileFactory(),
                            new DefaultRootOntologyDocumentMatcher()),
                    new SingleDocumentProjectSourcesExtractor());


            OWLOntologyManager rootOntologyManager = WebProtegeOWLManager.createOWLOntologyManager();
            RawProjectSources rawProjectSources = extractor.extractProjectSources(file);
            OWLOntologyLoaderConfiguration loaderConfig = new OWLOntologyLoaderConfiguration();
            RawProjectSourcesImporter importer = new RawProjectSourcesImporter(rootOntologyManager, loaderConfig);
            final OWLOntology uploadedRootOntology = importer.importRawProjectSources(rawProjectSources);
            System.out.println("Loaded uploadedRootOntology");

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

            StringBuilder sb = new StringBuilder();

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

            final ShortFormProvider dualShortFormProvider = new ShortFormProvider() {
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
            ManchesterSyntaxObjectRenderer renderer = new ManchesterSyntaxObjectRenderer(
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
            HighlightedEntityChecker highlightChecker = NullHighlightedEntityChecker.get();
            DeprecatedEntityChecker deprecatedChecker = NullDeprecatedEntityChecker.get();
            List<DiffElement<String, OWLAxiom>> diffElements = new ArrayList<>();
            for (OntologyDiff diff : diffs) {
                for (OWLAxiom ax : diff.getAxiomDiff().getAdded()) {
                    diffElements.add(new DiffElement<>(DiffOperation.ADD, "ontology", ax));
                }
                for (OWLAxiom ax : diff.getAxiomDiff().getRemoved()) {
                    diffElements.add(new DiffElement<>(DiffOperation.REMOVE, "ontology", ax));
                }
            }
            final Comparator<OWLAxiom> comp = new Comparator<OWLAxiom>() {
                @Override
                public int compare(OWLAxiom o1, OWLAxiom o2) {
                    AxiomSubjectProvider subjectProvider = new AxiomSubjectProvider();
                    OWLObject subj1 = subjectProvider.getSubject(o1);
                    OWLObject subj2 = subjectProvider.getSubject(o2);
                    OWLEntity entity1 = null;
                    if (subj1 instanceof OWLEntity) {
                        entity1 = (OWLEntity) subj1;
                    }
                    OWLEntity entity2 = null;
                    if (subj2 instanceof OWLEntity) {
                        entity2 = (OWLEntity) subj2;
                    }
                    int diff = 0;
                    if (entity1 != null && entity2 != null) {
                        diff = dualShortFormProvider.getShortForm(entity1).compareTo(dualShortFormProvider.getShortForm(entity2));
                    }
                    if (diff != 0) {
                        return diff;
                    }
                    int distance = StringUtils.getLevenshteinDistance(o1.toString(), o2.toString());
                    return distance;
//                    return o1.compareTo(o2);
                }
            };
            Collections.sort(diffElements, new Comparator<DiffElement<String, OWLAxiom>>() {
                @Override
                public int compare(DiffElement<String, OWLAxiom> o1, DiffElement<String, OWLAxiom> o2) {
                    int diff = comp.compare(o1.getLineElement(), o2.getLineElement());
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

            List<DiffElement<String, SafeHtml>> transformedDiff = new ArrayList<>();
            for (DiffElement<String, OWLAxiom> element : diffElements) {
                String html = renderer.render(element.getLineElement(), highlightChecker, deprecatedChecker);
                SafeHtml rendering = new SafeHtmlBuilder().appendHtmlConstant(html).toSafeHtml();
                transformedDiff.add(new DiffElement<>(element.getDiffOperation(), element.getSourceDocument(), rendering));
            }
            return new ComputeProjectMergeResult(transformedDiff);


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (OWLOntologyCreationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<ComputeProjectMergeAction> getActionClass() {
        return ComputeProjectMergeAction.class;
    }
}
