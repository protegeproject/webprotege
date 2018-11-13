package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryManager;
import edu.stanford.bmir.protege.web.server.shortform.EscapingShortFormProvider;
import edu.stanford.bmir.protege.web.shared.frame.GetManchesterSyntaxFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.GetManchesterSyntaxFrameResult;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxFrameRenderer;
import org.semanticweb.owlapi.model.HasImportsClosure;
import org.semanticweb.owlapi.util.OntologyIRIShortFormProvider;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.StringWriter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class GetManchesterSyntaxFrameActionHandler extends AbstractProjectActionHandler<GetManchesterSyntaxFrameAction, GetManchesterSyntaxFrameResult> {

    @Nonnull
    @RootOntology
    private final HasImportsClosure importsClosure;

    @Nonnull
    private final OntologyIRIShortFormProvider ontologyIRIShortFormProvider;

    @Nonnull
    private final DictionaryManager dictionaryManager;

    @Nonnull
    private final RenderingManager renderingManager;

    @Inject
    public GetManchesterSyntaxFrameActionHandler(@Nonnull AccessManager accessManager,
                                                 @Nonnull HasImportsClosure importsClosure,
                                                 @Nonnull OntologyIRIShortFormProvider ontologyIRIShortFormProvider,
                                                 @Nonnull DictionaryManager dictionaryManager,
                                                 @Nonnull RenderingManager renderingManager) {
        super(accessManager);
        this.importsClosure = checkNotNull(importsClosure);
        this.ontologyIRIShortFormProvider = checkNotNull(ontologyIRIShortFormProvider);
        this.dictionaryManager = checkNotNull(dictionaryManager);
        this.renderingManager = checkNotNull(renderingManager);
    }

    @Nonnull
    @Override
    public GetManchesterSyntaxFrameResult execute(@Nonnull GetManchesterSyntaxFrameAction action,
                                                  @Nonnull ExecutionContext executionContext) {
        var writer = new StringWriter();
        var escapingShortFormProvider = new EscapingShortFormProvider(dictionaryManager);
        var frameRenderer = new ManchesterOWLSyntaxFrameRenderer(importsClosure.getImportsClosure(),
                                                                 writer, escapingShortFormProvider);
        frameRenderer.setOntologyIRIShortFormProvider(ontologyIRIShortFormProvider);
        frameRenderer.setRenderExtensions(true);
        frameRenderer.writeFrame(action.getSubject());
        var frameSubject = renderingManager.getRendering(action.getSubject());
        return GetManchesterSyntaxFrameResult.get(frameSubject, writer.getBuffer().toString());
    }

    @Nonnull
    @Override
    public Class<GetManchesterSyntaxFrameAction> getActionClass() {
        return GetManchesterSyntaxFrameAction.class;
    }
}
