package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.mansyntax.ShellOwlOntology;
import edu.stanford.bmir.protege.web.server.mansyntax.render.OwlOntologyFacadeFactory;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryManager;
import edu.stanford.bmir.protege.web.server.shortform.EscapingShortFormProvider;
import edu.stanford.bmir.protege.web.shared.frame.GetManchesterSyntaxFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.GetManchesterSyntaxFrameResult;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxFrameRenderer;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.util.OntologyIRIShortFormProvider;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.StringWriter;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class GetManchesterSyntaxFrameActionHandler extends AbstractProjectActionHandler<GetManchesterSyntaxFrameAction, GetManchesterSyntaxFrameResult> {

    @Nonnull
    private final OntologyIRIShortFormProvider ontologyIRIShortFormProvider;

    @Nonnull
    private final DictionaryManager dictionaryManager;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final OwlOntologyFacadeFactory ontologyFacadeFactory;

    @Inject
    public GetManchesterSyntaxFrameActionHandler(@Nonnull AccessManager accessManager,
                                                 @Nonnull OntologyIRIShortFormProvider ontologyIRIShortFormProvider,
                                                 @Nonnull DictionaryManager dictionaryManager,
                                                 @Nonnull RenderingManager renderingManager,
                                                 @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                                 @Nonnull OwlOntologyFacadeFactory ontologyFacadeFactory) {
        super(accessManager);
        this.ontologyIRIShortFormProvider = checkNotNull(ontologyIRIShortFormProvider);
        this.dictionaryManager = checkNotNull(dictionaryManager);
        this.renderingManager = checkNotNull(renderingManager);
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.ontologyFacadeFactory = ontologyFacadeFactory;
    }

    @Nonnull
    @Override
    public GetManchesterSyntaxFrameResult execute(@Nonnull GetManchesterSyntaxFrameAction action,
                                                  @Nonnull ExecutionContext executionContext) {
        var writer = new StringWriter();
        var escapingShortFormProvider = new EscapingShortFormProvider(dictionaryManager);
        var frameRenderer = new ManchesterOWLSyntaxFrameRenderer(
                getShellImportsClosure(),
                writer, escapingShortFormProvider);
        frameRenderer.setOntologyIRIShortFormProvider(ontologyIRIShortFormProvider);
        frameRenderer.setRenderExtensions(true);
        frameRenderer.writeFrame(action.getSubject());
        var frameSubject = renderingManager.getRendering(action.getSubject());
        return GetManchesterSyntaxFrameResult.get(frameSubject, writer.getBuffer().toString());
    }

    private Set<OWLOntology> getShellImportsClosure() {
        return projectOntologiesIndex.getOntologyIds()
                              .map(ontologyFacadeFactory::create)
                              .collect(Collectors.toSet());
    }

    @Nonnull
    @Override
    public Class<GetManchesterSyntaxFrameAction> getActionClass() {
        return GetManchesterSyntaxFrameAction.class;
    }
}
