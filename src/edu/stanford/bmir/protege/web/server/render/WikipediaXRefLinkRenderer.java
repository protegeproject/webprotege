package edu.stanford.bmir.protege.web.server.render;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 21/02/2014
 */
public class WikipediaXRefLinkRenderer implements LinkRendererPlugin {

    @Override
    public boolean isRenderableAsLink(String link) {
        return link.startsWith("wikipedia:");
    }

    @Override
    public LinkInfo renderLink(String link) {
        return new LinkInfo("http://wikipedia.org/wiki/" + link, link);
    }
}
