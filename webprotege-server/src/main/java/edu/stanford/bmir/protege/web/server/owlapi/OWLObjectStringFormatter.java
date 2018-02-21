package edu.stanford.bmir.protege.web.server.owlapi;

import org.apache.commons.lang.StringUtils;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxOWLObjectRendererImpl;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.util.IRIShortFormProvider;
import org.semanticweb.owlapi.util.ShortFormProvider;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/03/16
 */
public class OWLObjectStringFormatter {

    public static final int MAX_LITERAL_LENGTH = 50;

    private final ShortFormProvider shortFormProvider;

    private final IRIShortFormProvider iriShortFormProvider;

    private final OWLObjectRenderer render;

    @Inject
    public OWLObjectStringFormatter(ShortFormProvider shortFormProvider, IRIShortFormProvider iriShortFormProvider) {
        this.shortFormProvider = shortFormProvider;
        this.iriShortFormProvider = iriShortFormProvider;
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
            OWLLiteral literal = (OWLLiteral) object;
            String rendering;
            if(literal.isRDFPlainLiteral() || literal.getDatatype().isString()) {
                rendering = "\"" + literal.getLiteral() + "\"";
            }
            else {
                rendering = this.render.render((OWLLiteral) object);
            }

            int startIndex = rendering.indexOf("\"" );
            int endIndex = rendering.lastIndexOf("\"" );
            if (startIndex == -1 || endIndex == -1) {
                return rendering;
            }
            if(endIndex - startIndex < 10) {
                return rendering;
            }
            if (rendering.length() < MAX_LITERAL_LENGTH) {
                return rendering;
            }
            String withoutQuotes = rendering.substring(startIndex + 1, endIndex);
            String abbreviatedLexicalValue =  StringUtils.abbreviate(withoutQuotes,
                                                MAX_LITERAL_LENGTH);
            String prefix = rendering.substring(0, startIndex + 1);
            String suffix = rendering.substring(endIndex);
            return prefix + abbreviatedLexicalValue + suffix;

        }
        else {
            String rendering = this.render.render((OWLObject) object);
            return escape(rendering);
        }
    }

}
