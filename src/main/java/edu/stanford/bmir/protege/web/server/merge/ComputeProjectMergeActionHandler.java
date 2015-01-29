package edu.stanford.bmir.protege.web.server.merge;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
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
import edu.stanford.bmir.protege.web.server.util.DefaultTempFileFactory;
import edu.stanford.bmir.protege.web.server.util.ZipInputStreamChecker;
import edu.stanford.bmir.protege.web.shared.axiom.OWLAxiomData;
import edu.stanford.bmir.protege.web.shared.merge.ComputeProjectMergeAction;
import edu.stanford.bmir.protege.web.shared.merge.ComputeProjectMergeResult;
import edu.stanford.bmir.protege.web.shared.merge.OntologyDiff;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import java.io.File;
import java.io.IOException;
import java.util.Set;

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

            OntologyDiffCalculator diffCalculator = new OntologyDiffCalculator(new AnnotationDiffCalculator(), new AxiomDiffCalculator());
            ModifiedProjectOntologiesCalculator ontsCalculator = new ModifiedProjectOntologiesCalculator(
                    ImmutableSet.copyOf(project.getRootOntology().getImportsClosure()),
                    ImmutableSet.copyOf(uploadedRootOntology.getImportsClosure()),
                    diffCalculator
            );

            Set<OntologyDiff> diffs = ontsCalculator.getModifiedOntologyDiffs();

            StringBuilder sb = new StringBuilder();

            final ShortFormProvider uploadedOntologyShortFormProvider = new WebProtegeShortFormProvider(new HasAnnotationAssertionAxiomsImpl(uploadedRootOntology), new HasLang() {
                @Override
                public String getLang() {
                    return project.getLang();
                }
            });

            ManchesterSyntaxObjectRenderer renderer = new ManchesterSyntaxObjectRenderer(
                    new ShortFormProvider() {
                        @Override
                        public String getShortForm(OWLEntity owlEntity) {
                            if(owlEntity.isBuiltIn() || project.getRootOntology().containsEntityInSignature(owlEntity, true)) {
                                return project.getRenderingManager().getShortForm(owlEntity);
                            }
                            else if(uploadedRootOntology.containsEntityInSignature(owlEntity, true)) {
                                return uploadedOntologyShortFormProvider.getShortForm(owlEntity);
                            }
                            else {
                                return project.getRenderingManager().getShortForm(owlEntity);
                            }
                        }

                        @Override
                        public void dispose() {

                        }
                    },
                    new DefaultEntityIRIChecker(project.getRootOntology()),
                    LiteralStyle.REGULAR,
                    new NullHttpLinkRenderer(),
                    new MarkdownLiteralRenderer()
            );
            HighlightedEntityChecker highlightChecker = NullHighlightedEntityChecker.get();
            DeprecatedEntityChecker deprecatedChecker = NullDeprecatedEntityChecker.get();
            for(OntologyDiff diff : diffs) {
                for(OWLAxiom ax : diff.getAxiomDiff().getAdded()) {
                    sb.append("<div class=\"diff-add-axiom\">");
                    sb.append(renderer.render(ax, highlightChecker, deprecatedChecker));
                    sb.append("</div>\n");
                }
                for(OWLAxiom ax : diff.getAxiomDiff().getRemoved()) {
                    sb.append("<div class=\"diff-remove-axiom\">");
                    sb.append(renderer.render(ax, highlightChecker, deprecatedChecker));
                    sb.append("</div>");
                }
            }
            return new ComputeProjectMergeResult(new SafeHtmlBuilder().appendHtmlConstant(sb.toString()).toSafeHtml());



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
