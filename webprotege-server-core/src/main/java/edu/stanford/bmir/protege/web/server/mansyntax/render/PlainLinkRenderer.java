package edu.stanford.bmir.protege.web.server.mansyntax.render;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 21/02/2014
 */
public class PlainLinkRenderer implements LinkRendererPlugin {

    @Override
    public boolean isRenderableAsLink(String link) {
        return link.startsWith("http");
    }

    @Override
    public LinkInfo renderLink(String link) {
        return new LinkInfo(link, link);
    }
}
