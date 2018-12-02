package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.server.renderer.LiteralRenderer;
import edu.stanford.bmir.protege.web.server.util.Counter;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxOWLObjectRendererImpl;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.util.IRIShortFormProvider;
import org.semanticweb.owlapi.util.ShortFormProvider;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.joining;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/03/16
 */
public class OWLObjectStringFormatter {

    @Nonnull
    private final ShortFormProvider shortFormProvider;

    @Nonnull
    private final IRIShortFormProvider iriShortFormProvider;

    @Nonnull
    private final LiteralRenderer literalRenderer;

    @Nonnull
    private final OWLObjectRenderer render;

    @Inject
    public OWLObjectStringFormatter(@Nonnull ShortFormProvider shortFormProvider,
                                    @Nonnull IRIShortFormProvider iriShortFormProvider,
                                    @Nonnull LiteralRenderer literalRenderer) {
        this.iriShortFormProvider = checkNotNull(iriShortFormProvider);
        this.shortFormProvider = checkNotNull(shortFormProvider);
        this.literalRenderer = checkNotNull(literalRenderer);
        render = new ManchesterOWLSyntaxOWLObjectRendererImpl();
        render.setShortFormProvider(shortFormProvider);
    }

    public Optional<String> format(String format, Object... objects) {
        return Optional.of(formatString(format, objects));
    }

    public String formatString(String format, Object... objects) {
        String[] formattedObjects = new String[objects.length];
        for (int i = 0; i < objects.length; i++) {
            formattedObjects[i] = renderObject(objects[i]);
        }
        return String.format(format, formattedObjects);
    }

    private String escape(String rendering) {
        if (rendering.startsWith("'" ) || rendering.startsWith("\"" )) {
            return rendering;
        }
        if (rendering.indexOf(' ') != -1) {
            return "'" + rendering + "'";
        }
        return rendering;
    }

    private String renderObject(Object object) {
        if(object instanceof Collection) {
            Counter counter = new Counter();
            Collection<?> objects = (Collection) object;
            String list = objects.stream()
                    .limit(50)
                    .peek(o -> counter.increment())
                    .map(this::renderObject)
                    .collect(joining(", "));
            if(counter.getCounter() < objects.size()) {
                return list + ", ...";
            }
            return list;
        }
        if (!(object instanceof OWLObject)) {
            return object.toString();
        }
        if (object instanceof OWLEntity) {
            return shortFormProvider.getShortForm((OWLEntity) object);
        }
        else if (object instanceof IRI) {
            return iriShortFormProvider.getShortForm((IRI) object);
        }
        else if (object instanceof OWLLiteral) {
            return literalRenderer.getLiteralRendering((OWLLiteral) object);
        }
        else {
            String rendering = this.render.render((OWLObject) object);
            return escape(rendering);
        }
    }

}
