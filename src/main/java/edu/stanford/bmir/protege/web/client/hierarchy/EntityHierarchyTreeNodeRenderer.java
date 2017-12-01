package edu.stanford.bmir.protege.web.client.hierarchy;

import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import edu.stanford.bmir.protege.web.shared.watches.WatchType;
import edu.stanford.protege.gwt.graphtree.client.TreeNodeRenderer;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
public class EntityHierarchyTreeNodeRenderer implements TreeNodeRenderer<EntityHierarchyNode> {

    public EntityHierarchyTreeNodeRenderer() {
    }

    @Override
    public String getHtmlRendering(EntityHierarchyNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style='display: flex; flex-direction: row; align-items: center;'>");
        sb.append("<img src='").append(WebProtegeClientBundle.BUNDLE.svgClassIcon().getSafeUri().asString()).append("'/>");
        sb.append("<div>");
        sb.append(node.getBrowserText());
        sb.append("</div>");

        if(node.getOpenCommentCount() > 0) {
            sb.append("<img style='padding-left: 6px;' src='").append(WebProtegeClientBundle.BUNDLE.svgCommentSmallFilledIcon().getSafeUri().asString()).append("'/>");
            sb.append("<div style='padding-left: 4px; padding-bottom: 4px; font-size: smaller;'> (").append(node.getOpenCommentCount()).append(")</div>");
        }
        node.getWatches().stream()
                 .map(Watch::getType)
                 .forEach(watchType -> {
                     sb.append("<img style='padding-left: 4px;' src='");
                     if(watchType == WatchType.ENTITY) {
                         sb.append(WebProtegeClientBundle.BUNDLE.svgEyeIcon().getSafeUri().asString());
                     }
                     else {
                         sb.append(WebProtegeClientBundle.BUNDLE.svgEyeIconDown().getSafeUri().asString());
                     }
                     sb.append("'/>");
                 });
        sb.append("</div>");
        return sb.toString();
    }
}
