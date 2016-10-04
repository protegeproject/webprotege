package edu.stanford.bmir.protege.web.server.render;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 21/02/2014
 */
public interface LiteralRenderer {

    void renderLiteral(String literal, StringBuilder stringBuilder);
}
