package edu.stanford.bmir.protege.web.server.render;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 21/02/2014
 */
public class ImageLinkRenderer implements LinkRendererPlugin {

    @Override
    public boolean isRenderableAsLink(String link) {
        return isHttpLink(link) && hasImageExtension(link);
    }

    private boolean isHttpLink(String link) {
        return link.startsWith("http://");
    }

    private boolean hasImageExtension(String link) {
        return link.endsWith(".png") || link.endsWith(".svg") || link.endsWith(".jpg") || link.endsWith(".gif");
    }

    @Override
    public LinkInfo renderLink(String link) {
        return new LinkInfo(link,
                    "<span class=\"ms-img\">"
                            + "<img src=\"" + link + "\"/>"
                    + "</span>");
    }
}
