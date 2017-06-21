package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.shortform.EscapingShortFormProvider;
import edu.stanford.bmir.protege.web.server.shortform.WebProtegeBidirectionalShortFormProvider;
import edu.stanford.bmir.protege.web.shared.frame.GetManchesterSyntaxFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.GetManchesterSyntaxFrameResult;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxFrameRenderer;
import org.semanticweb.owlapi.model.HasImportsClosure;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.util.OntologyIRIShortFormProvider;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.StringWriter;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class GetManchesterSyntaxFrameActionHandler extends AbstractHasProjectActionHandler<GetManchesterSyntaxFrameAction, GetManchesterSyntaxFrameResult> {

    @Nonnull
    @RootOntology
    private final HasImportsClosure importsClosure;

    @Nonnull
    private final OntologyIRIShortFormProvider ontologyIRIShortFormProvider;

    @Nonnull
    private final WebProtegeBidirectionalShortFormProvider shortFormProvider;

    @Inject
    public GetManchesterSyntaxFrameActionHandler(@Nonnull AccessManager accessManager,
                                                 @Nonnull HasImportsClosure importsClosure,
                                                 @Nonnull OntologyIRIShortFormProvider ontologyIRIShortFormProvider,
                                                 @Nonnull WebProtegeBidirectionalShortFormProvider shortFormProvider) {
        super(accessManager);
        this.importsClosure = importsClosure;
        this.ontologyIRIShortFormProvider = ontologyIRIShortFormProvider;
        this.shortFormProvider = shortFormProvider;
    }

    @Override
    public GetManchesterSyntaxFrameResult execute(GetManchesterSyntaxFrameAction action,
                                                     ExecutionContext executionContext) {
        StringWriter writer = new StringWriter();
        EscapingShortFormProvider entityShortFormProvider = new EscapingShortFormProvider(shortFormProvider);
        final ManchesterOWLSyntaxFrameRenderer frameRenderer = new ManchesterOWLSyntaxFrameRenderer(importsClosure.getImportsClosure(), writer, entityShortFormProvider);
        frameRenderer.setOntologyIRIShortFormProvider(ontologyIRIShortFormProvider);
        frameRenderer.setRenderExtensions(true);
//        frameRenderer.setRenderOntologyLists(true);
//        frameRenderer.setUseTabbing(true);
//        frameRenderer.setUseWrapping(true);
        frameRenderer.writeFrame(action.getSubject());
//        frameRenderer.writeEntityNaryAxioms(action.getSubject());
//        frameRenderer.writeRulesContainingPredicate(action.getSubject());
        return new GetManchesterSyntaxFrameResult(writer.getBuffer().toString());
    }

    @Override
    public Class<GetManchesterSyntaxFrameAction> getActionClass() {
        return GetManchesterSyntaxFrameAction.class;
    }
}
