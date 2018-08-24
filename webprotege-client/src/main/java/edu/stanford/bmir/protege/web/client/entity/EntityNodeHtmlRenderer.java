package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.DataResource;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;
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
public class EntityNodeHtmlRenderer implements TreeNodeRenderer<EntityNode> {

    private static final String NO_DISPLAY_NAME = "_____";

    private final LoggedInUserProvider loggedInUserProvider;

    @Nonnull
    private final Messages messages;

    private DisplayNameSettings displayNameSettings = DisplayNameSettings.empty();

    @Inject
    public EntityNodeHtmlRenderer(@Nonnull LoggedInUserProvider loggedInUserProvider,
                                  @Nonnull Messages messages) {
        this.loggedInUserProvider = checkNotNull(loggedInUserProvider);
        this.messages = checkNotNull(messages);
    }

    public void setDisplayLanguage(@Nonnull DisplayNameSettings displayNameSettings) {
        this.displayNameSettings = checkNotNull(displayNameSettings);
    }

    @Override
    public String getHtmlRendering(EntityNode node) {
        GWT.log("[EntityNodeHtmlRenderer] Settings: " + displayNameSettings);
        GWT.log("[EntityNodeHtmlRenderer] Rendering node: " + node);
        StringBuilder sb = new StringBuilder();
        sb.append("<div class='wp-entity-node'>");
        renderIcon(node, sb);
        renderDisplayName(node, sb);
        renderCommentsIcon(node, sb);
        renderWatchesIcon(node, sb);
        renderTags(node, sb);
        sb.append("</div>");
        return sb.toString();
    }

    private void renderIcon(EntityNode node, StringBuilder sb) {
        String iconIri;
        DataResource icon = getIcon(node);
        sb.append("<img src='").append(icon.getSafeUri().asString()).append("'/>");
    }

    private void renderDisplayName(EntityNode node, StringBuilder sb) {
        if (node.isDeprecated()) {
            sb.append("<div class='wp-entity-node__display-name wp-entity-node__display-name--deprecated-entity'>");
        }
        else {
            sb.append("<div class='wp-entity-node__display-name'>");
        }
        renderPrimaryDisplayName(node, sb);
        renderSecondaryDisplayName(node, sb);
        sb.append("</div>");
    }

    private void renderPrimaryDisplayName(EntityNode node, StringBuilder sb) {
        if (node.getEntity().isBuiltIn()) {
            sb.append(node.getBrowserText());
        }
        else if (!displayNameSettings.getPrimaryDisplayNameLanguages().isEmpty()) {
            // Rendering is based on user settings
            String text = node.getText(displayNameSettings.getPrimaryDisplayNameLanguages(), null);
            if (text == null) {
                renderNoDisplayName(node, sb);
            }
            else {
                sb.append("<span class='wp-entity-node__display-name__primary-language'>");
                sb.append(text);
                sb.append("</span>");
            }
        }
        else {
            // Rendering is based on the project settings
            if (node.getBrowserText().isEmpty()) {
                // No rendering available given the settings
                renderNoDisplayName(node, sb);
            }
            else {
                sb.append(node.getBrowserText());
            }
        }
    }

    private void renderSecondaryDisplayName(EntityNode node, StringBuilder sb) {
        String secondaryText = node.getText(displayNameSettings.getSecondaryDisplayNameLanguages(), null);
        if (secondaryText != null) {
            sb.append(" <span class='wp-entity-node__display-name__secondary-language'>");
            sb.append(secondaryText);
            sb.append("</span>");
        }
    }

    private void renderCommentsIcon(EntityNode node, StringBuilder sb) {
        if (node.getOpenCommentCount() > 0) {
            sb.append("<img style='padding-left: 6px;' src='").append(BUNDLE.svgCommentSmallFilledIcon().getSafeUri().asString()).append("'/>");
            sb.append("<div style='padding-left: 4px; padding-bottom: 4px; font-size: smaller;'> (").append(node.getOpenCommentCount()).append(")</div>");
        }
    }

    private void renderWatchesIcon(EntityNode node, StringBuilder sb) {
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
    }

    private void renderTags(EntityNode node, StringBuilder sb) {
        Collection<Tag> tags = node.getTags();
        tags.forEach(tag -> {
            renderTag(tag, sb);
        });
    }

    private void renderTag(Tag tag, StringBuilder sb) {
        sb.append("<div title='")
          .append(tag.getDescription())
          .append("' class='wp-tag wp-tag--inline-tag' style='color:")
          .append(tag.getColor().getHex())
          .append("; background-color:")
          .append(tag.getBackgroundColor().getHex())
          .append(";'>");
        sb.append(tag.getLabel());
        sb.append("</div>");
    }

    private void renderNoDisplayName(EntityNode node, StringBuilder sb) {
        sb.append("<span class='wp-entity-node__display-name__no-display-name' title='")
          .append(messages.language_noDisplayName_help(node.getEntity().getEntityType().getPrintName().toLowerCase(),
                                                       node.getBrowserText()))
          .append("'>");
        sb.append(NO_DISPLAY_NAME);
        sb.append("</span>");
    }

    @Nonnull
    private DataResource getIcon(@Nonnull EntityNode node) {
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
