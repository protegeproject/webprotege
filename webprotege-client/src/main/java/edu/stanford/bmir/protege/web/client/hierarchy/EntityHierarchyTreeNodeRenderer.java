package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.DataResource;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.bmir.protege.web.shared.lang.DisplayDictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import edu.stanford.bmir.protege.web.shared.watches.WatchType;
import edu.stanford.protege.gwt.graphtree.client.TreeNodeRenderer;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
public class EntityHierarchyTreeNodeRenderer implements TreeNodeRenderer<EntityHierarchyNode> {

    private final LoggedInUserProvider loggedInUserProvider;

    private DisplayDictionaryLanguage language = DisplayDictionaryLanguage.empty();

    @Inject
    public EntityHierarchyTreeNodeRenderer(@Nonnull LoggedInUserProvider loggedInUserProvider) {
        this.loggedInUserProvider = checkNotNull(loggedInUserProvider);
    }

    public void setDisplayLanguage(@Nonnull DisplayDictionaryLanguage language) {
        this.language = checkNotNull(language);
    }

    @Override
    public String getHtmlRendering(EntityHierarchyNode node) {
        GWT.log("[EntityHierarchyTreeNodeRenderer] Rendering node: " + node);
        StringBuilder sb = new StringBuilder();
        sb.append("<div style='display: flex; flex-direction: row; align-items: center;'>");
        String iconIri;
        DataResource icon = getIcon(node);
        sb.append("<img src='").append(icon.getSafeUri().asString()).append("'/>");
        if (node.isDeprecated()) {
            sb.append("<div style=\"text-decoration: line-through; color: #a0a0a0;\">");
        }
        else {
            sb.append("<div>");
        }
        if(language.getPrimaryLanguage().isPresent()) {
            sb.append(node.getText(language.getPrimaryLanguage().get()));
        }
        else {
            sb.append(node.getText());
        }
        language.getSecondaryLanguage().ifPresent(secondaryLanguage -> {
            String secondaryText = node.getText(secondaryLanguage, "");
            if(!secondaryText.isEmpty()) {
                sb.append(" <span style=\"color: #909090;\">(");
                sb.append(secondaryText);
                sb.append(")</span>");
            }
        });
        sb.append("</div>");

        if (node.getOpenCommentCount() > 0) {
            sb.append("<img style='padding-left: 6px;' src='").append(BUNDLE.svgCommentSmallFilledIcon().getSafeUri().asString()).append("'/>");
            sb.append("<div style='padding-left: 4px; padding-bottom: 4px; font-size: smaller;'> (").append(node.getOpenCommentCount()).append(")</div>");
        }
        node.getWatches().stream()
            .filter(w -> loggedInUserProvider.getCurrentUserId().equals(w.getUserId()))
            .map(Watch::getType)
            .forEach(watchType -> {
                sb.append("<img style='padding-left: 4px;' src='");
                if (watchType == WatchType.ENTITY) {
                    sb.append(BUNDLE.svgEyeIcon().getSafeUri().asString());
                }
                else {
                    sb.append(BUNDLE.svgEyeIconDown().getSafeUri().asString());
                }
                sb.append("'/>");
            });
        Collection<Tag> tags = node.getTags();
        tags.forEach(tag -> {
            sb.append("<div title='")
              .append(tag.getDescription())
              .append("' class='wp-tag wp-tag--inline-tag' style='color:")
              .append(tag.getColor().getHex())
              .append("; background-color:")
              .append(tag.getBackgroundColor().getHex())
              .append(";'>");
            sb.append(tag.getLabel());
            sb.append("</div>");
        });
        sb.append("</div>");
        return sb.toString();
    }

    @Nonnull
    private DataResource getIcon(@Nonnull EntityHierarchyNode node) {
        OWLEntity entity = node.getEntity();
        if (entity.isOWLClass()) {
            if (node.isDeprecated()) {
                return BUNDLE.svgDeprecatedClassIcon();
            }
            else {
                return BUNDLE.svgClassIcon();
            }
        }
        else if (entity.isOWLObjectProperty()) {
            return BUNDLE.svgObjectPropertyIcon();
        }
        else if (entity.isOWLDataProperty()) {
            return BUNDLE.svgDataPropertyIcon();
        }
        else if (entity.isOWLAnnotationProperty()) {
            return BUNDLE.svgAnnotationPropertyIcon();
        }
        else if (entity.isOWLNamedIndividual()) {
            return BUNDLE.svgIndividualIcon();
        }
        else {
            return BUNDLE.svgDatatypeIcon();
        }
    }
}
