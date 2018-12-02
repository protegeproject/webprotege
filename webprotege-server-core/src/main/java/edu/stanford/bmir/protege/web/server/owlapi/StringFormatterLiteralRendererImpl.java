package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.server.renderer.LiteralLexicalFormTransformer;
import edu.stanford.bmir.protege.web.server.renderer.LiteralRenderer;
import org.apache.commons.lang.StringUtils;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2018-12-02
 */
public class StringFormatterLiteralRendererImpl implements LiteralRenderer {


    public static final int MAX_LITERAL_LENGTH = 50;

    @Nonnull
    private final ShortFormProvider shortFormProvider;

    @Nonnull
    private final LiteralLexicalFormTransformer literalLexicalFormTransformer;

    @Inject
    public StringFormatterLiteralRendererImpl(@Nonnull ShortFormProvider shortFormProvider,
                                              @Nonnull LiteralLexicalFormTransformer literalLexicalFormTransformer) {
        this.shortFormProvider = checkNotNull(shortFormProvider);
        this.literalLexicalFormTransformer = checkNotNull(literalLexicalFormTransformer);
    }

    @Nonnull
    @Override
    public String getLiteralRendering(@Nonnull OWLLiteral literal) {
        String rendering;
        if(isRenderableAsQuotedString(literal)) {
            rendering = "\"" + getLexicalForm(literal) + "\"";
        }
        else if(isRenderableAsNumber(literal)) {
            rendering = getLexicalForm(literal);
        }
        else if(isRendererableAsDateTime(literal)) {
            rendering = getLexicalForm(literal);
        }
        else {
            rendering = getLexicalForm(literal) + "^^" + shortFormProvider.getShortForm(literal.getDatatype());
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

    @Nonnull
    private String getLexicalForm(OWLLiteral literal) {
        return literalLexicalFormTransformer.transformLexicalForm(literal.getLiteral());
    }

    private static boolean isRenderableAsQuotedString(@Nonnull OWLLiteral literal) {
        return literal.isRDFPlainLiteral() || literal.getDatatype().isString();
    }

    private static boolean isRenderableAsNumber(@Nonnull OWLLiteral literal) {
        var datatypeIRI = literal.getDatatype().getIRI();
        if(OWL2Datatype.isBuiltIn(datatypeIRI)) {
            var datatype = OWL2Datatype.getDatatype(datatypeIRI);
            return datatype.isNumeric();
        }
        return false;
    }

    private static boolean isRendererableAsDateTime(@Nonnull OWLLiteral literal) {
        var datatypeIri = literal.getDatatype().getIRI();
        return datatypeIri.equals(OWL2Datatype.XSD_DATE_TIME.getIRI())
                || datatypeIri.equals(OWL2Datatype.XSD_DATE_TIME_STAMP.getIRI());
    }
}
