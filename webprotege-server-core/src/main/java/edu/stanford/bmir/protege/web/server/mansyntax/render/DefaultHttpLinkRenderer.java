package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 21/02/2014
 */
public class DefaultHttpLinkRenderer implements HttpLinkRenderer {

    private List<LinkRendererPlugin> pluginList = new ArrayList<>();

    @Inject
    public DefaultHttpLinkRenderer() {
        pluginList.add(new WikipediaXRefLinkRenderer());
        pluginList.add(new ImageLinkRenderer());
        pluginList.add(new PlainLinkRenderer());
    }

    @Override
    public boolean isLink(OWLLiteral literal) {
        String normalisedLiteral = literal.getLiteral().toLowerCase();
        for(LinkRendererPlugin plugin : pluginList) {
            if(plugin.isRenderableAsLink(normalisedLiteral)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void renderLink(String link, StringBuilder builder) {
        String normalisedLiteral = link.toLowerCase();
        for(LinkRendererPlugin plugin : pluginList) {
            if(plugin.isRenderableAsLink(normalisedLiteral)) {
                LinkInfo linkInfo = plugin.renderLink(link);
                renderPlainLink(linkInfo.getLinkAddress(), linkInfo.getLinkContent(), builder);
                return;
            }
        }
    }

    private boolean isWikipediaXRef(String link) {
        return link.toLowerCase().startsWith("wikipedia:");
    }


    private boolean isImage(String link) {
        String normalisedLink = link.toLowerCase();
        return normalisedLink.endsWith(".png") || normalisedLink.endsWith("jpg") || normalisedLink.endsWith(".svg");
    }

    private void renderWikipediaXref(String link, StringBuilder builder) {
        renderPlainLink("http://en.wikipedia.org/wiki/" + link.substring("wikipedia:".length()), link, builder);
    }

    private void renderImage(String link, StringBuilder builder) {
        builder.append("<span class=\"ms-img\">");
        builder.append("<a target=\"_blank\" href=\"").append(link).append("\">");
        builder.append("<img src=\"");
        builder.append(link);
        builder.append("\"/>");
        builder.append("</a>");
        builder.append("</span>");
    }

    private void renderPlainLink(String link, String display, StringBuilder builder) {
        builder.append("<a target=\"_blank\" href=\"");
        builder.append(link);
        builder.append("\">");
        builder.append("<span class=\"iri\">");
        builder.append(display);
        builder.append("</span>");
        builder.append("</a>");
    }

    private void renderEscaped(String iri, StringBuilder sb) {
        SafeHtmlBuilder htmlBuilder = new SafeHtmlBuilder();
        htmlBuilder.appendEscaped(iri);
        sb.append(htmlBuilder.toSafeHtml().asString());
    }

}
