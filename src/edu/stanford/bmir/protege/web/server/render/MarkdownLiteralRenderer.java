package edu.stanford.bmir.protege.web.server.render;

import org.markdown4j.Markdown4jProcessor;
import org.semanticweb.owlapi.model.OWLLiteral;

import java.io.IOException;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/02/2014
 */
public class MarkdownLiteralRenderer implements LiteralRenderer {

    @Override
    public void renderLiteral(String literal, StringBuilder stringBuilder) {
        try {
            Markdown4jProcessor markdown4jProcessor = new Markdown4jProcessor();
            String rendering = markdown4jProcessor.process(literal);
            stringBuilder.append(rendering.trim());
        } catch (IOException e) {
            stringBuilder.append(literal);
        }
    }
}
