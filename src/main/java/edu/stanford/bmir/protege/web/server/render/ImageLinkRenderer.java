package edu.stanford.bmir.protege.web.server.render;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 21/02/2014
 */
public class ImageLinkRenderer implements LinkRendererPlugin {

    private static Set<String> extensions = Sets.newHashSet();

    static {
        extensions.add(".png");
        extensions.add(".jpg");
        extensions.add(".jpeg");
        extensions.add(".gif");
        extensions.add(".svg");
        extensions.add(".bmp");
        extensions.add(".png");
    }

    @Override
    public boolean isRenderableAsLink(String link) {
        String lowerCaseLink = link.toLowerCase();
        return isHttpLink(lowerCaseLink) && hasImageExtension(lowerCaseLink);
    }

    private boolean isHttpLink(String link) {
        return link.startsWith("http://") || link.startsWith("https://");
    }

    private boolean hasImageExtension(String link) {
        int lastDotIndex = link.lastIndexOf('.');
        if(lastDotIndex == -1) {
            return false;
        }
        String extension = link.substring(lastDotIndex);
        return extensions.contains(extension);
    }

    @Override
    public LinkInfo renderLink(String link) {
        return new LinkInfo(link,
                    "<span class=\"ms-img\">"
                            + "<img src=\"" + link + "\"/>"
                    + "</span>");
    }
}
