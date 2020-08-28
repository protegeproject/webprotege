package edu.stanford.bmir.protege.web.client.entity;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.DataResource;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.tag.ProjectTagsStyleManager;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageData;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import edu.stanford.bmir.protege.web.shared.watches.WatchType;
import edu.stanford.protege.gwt.graphtree.client.TreeNodeRenderer;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
public class EntityNodeHtmlRenderer implements TreeNodeRenderer<EntityNode> {

    private static final String NO_DISPLAY_NAME = "_____";

    private final LoggedInUserProvider loggedInUserProvider;

    @Nonnull
    private final Messages messages;

    private ImmutableList<DictionaryLanguage> primaryLanguages = ImmutableList.of();

    private ImmutableList<DictionaryLanguage> secondaryLanguages = ImmutableList.of();

    private boolean renderTags = true;

    private int highlightStart = -1;

    private int highlightEnd = -1;

    @Inject
    public EntityNodeHtmlRenderer(@Nonnull LoggedInUserProvider loggedInUserProvider,
                                  @Nonnull Messages messages) {
        this.loggedInUserProvider = checkNotNull(loggedInUserProvider);
        this.messages = checkNotNull(messages);
    }

    public void setRenderTags(boolean renderTags) {
        this.renderTags = renderTags;
    }

    public void setDisplayLanguage(@Nonnull DisplayNameSettings displayNameSettings) {
        primaryLanguages = displayNameSettings.getPrimaryDisplayNameLanguages().stream()
                                              .collect(toImmutableList());
        secondaryLanguages = displayNameSettings.getSecondaryDisplayNameLanguages().stream()
                                              .collect(toImmutableList());

    }

    public void clearHighlight() {
        highlightStart = -1;
        highlightEnd = -1;
    }

    public void setHighlight(int start, int end) {
        this.highlightStart = start;
        this.highlightEnd = end;
    }

    @Override
    public String getHtmlRendering(EntityNode node) {
        StringBuilder sb = new StringBuilder();
        if (node.isDeprecated()) {
            sb.append("<div class='wp-entity-node wp-pd wp-pd--deprecated'>");
        }
        else {
            sb.append("<div class='wp-entity-node wp-pd'>");
        }
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
        sb.append("<img src='").append(icon.getSafeUri().asString()).append("' class='wp-pd__icon'/>");
    }

    private void renderDisplayName(EntityNode node, StringBuilder sb) {
        if (node.isDeprecated()) {
            sb.append("<div class='wp-entity-node__display-name wp-entity-node__display-name--deprecated-entity wp-pd__text'>");
        }
        else {
            sb.append("<div class='wp-entity-node__display-name wp-pd__text'>");
        }
        renderPrimaryDisplayName(node, sb);
        renderSecondaryDisplayName(node, sb);
        sb.append("</div>");
    }

    @Nonnull
    public String getPrimaryDisplayName(@Nonnull EntityNode node) {
        if(!primaryLanguages.isEmpty()) {
            return node.getText(primaryLanguages, NO_DISPLAY_NAME);
        }
        else {
            return node.getBrowserText();
        }
    }

    private void renderPrimaryDisplayName(EntityNode node, StringBuilder sb) {
        if (node.getEntity().isBuiltIn()) {
            sb.append(highlightText(node.getBrowserText()));
        }
        else if (!primaryLanguages.isEmpty()) {
            // Rendering is based on user settings
            String text = node.getText(primaryLanguages, null);
            if (text == null) {
                renderNoDisplayName(node, sb);
            }
            else {
                sb.append("<span class='wp-entity-node__display-name__primary-language'>");
                sb.append(highlightText(text));
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
                sb.append(highlightText(node.getBrowserText()));
            }
        }
    }

    private String highlightText(@Nonnull String text) {
        if(highlightStart < 0) {
            return text;
        }
        int start = Math.min(highlightStart, text.length() - 1);
        int end = Math.min(highlightEnd, text.length() - 1);
        if(start < end) {
            StringBuilder sb = new StringBuilder();
            sb.append("<div>");
            sb.append(text.substring(0, start));
            sb.append("<span class=\"web-protege-entity-match-substring\">");
            sb.append(text.substring(start, end));
            sb.append("</span>");
            sb.append(text.substring(end));
            sb.append("</div>");
            return sb.toString();
        }
        else {
            return text;
        }
    }

    private void renderSecondaryDisplayName(EntityNode node, StringBuilder sb) {
        String secondaryText = node.getText(secondaryLanguages, null);
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
        if(!renderTags) {
            return;
        }
        Collection<Tag> tags = node.getTags();
        tags.forEach(tag -> renderTag(tag, sb));
    }

    private void renderTag(Tag tag, StringBuilder sb) {
        sb.append("<div title='")
          .append(tag.getDescription())
          .append("' class='wp-tag wp-tag--inline-tag ")
          .append(ProjectTagsStyleManager.getTagClassName(tag.getTagId()))
          .append("'")
          .append(">");
        sb.append(tag.getLabel());
        sb.append("</div>");
    }

    private void renderNoDisplayName(EntityNode node, StringBuilder sb) {
        sb.append("<span class='wp-entity-node__display-name__no-display-name' title='")
          .append(messages.displayName_noDisplayName_helpText(node.getEntity().getEntityType().getPrintName().toLowerCase(),
                                                              node.getBrowserText()))
          .append("'>");
        sb.append(NO_DISPLAY_NAME);
        sb.append("</span>");
    }

    @Nonnull
    private DataResource getIcon(@Nonnull EntityNode node) {
        OWLEntity entity = node.getEntity();
        if (entity.isOWLClass()) {
            return BUNDLE.svgClassIcon();
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

    public void setPrimaryDisplayLanguage(@Nonnull DictionaryLanguage language) {
        this.primaryLanguages = ImmutableList.of(checkNotNull(language));
    }
}
