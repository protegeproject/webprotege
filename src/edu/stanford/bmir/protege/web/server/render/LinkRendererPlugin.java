package edu.stanford.bmir.protege.web.server.render;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 21/02/2014
 */
public interface LinkRendererPlugin {

    boolean isRenderableAsLink(String link);

    LinkInfo renderLink(String link);
}
