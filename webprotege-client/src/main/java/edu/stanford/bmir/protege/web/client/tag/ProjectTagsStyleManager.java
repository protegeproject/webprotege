package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import edu.stanford.bmir.protege.web.client.project.ProjectView;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import edu.stanford.bmir.protege.web.shared.tag.TagId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31 Aug 2018
 */
public class ProjectTagsStyleManager {

    private static final String WP_PROJECT_TAG_STYLES_ELEMENT_ID = "wp-project-tag-styles";

    @Inject
    public ProjectTagsStyleManager() {
    }

    @Nonnull
    public static String getProjectTagStylesElementId() {
        return WP_PROJECT_TAG_STYLES_ELEMENT_ID;
    }

    public void setProjectTags(Collection<Tag> tags,
                               ProjectView view) {
        StringBuilder sb = new StringBuilder();
        tags.forEach(tag -> {
            renderTagStyles(sb, tag);
            sb.append("\n\n");
        });
        Element element = view.asWidget().getElement();
        NodeList<Element> styleElements = element.getElementsByTagName("style");
        for(int i = 0; i < styleElements.getLength(); i++) {
            Element styleElement = styleElements.getItem(i);
            if(styleElement.getId().equalsIgnoreCase(WP_PROJECT_TAG_STYLES_ELEMENT_ID)) {
                styleElement.setInnerText(sb.toString());
            }
        }
    }

    private void renderTagStyles(StringBuilder sb, Tag tag) {
        String id = tag.getTagId().getId();
        sb.append("/* Tag ").append(tag.getTagId()).append("(").append(tag.getLabel()).append(") */\n");
        sb.append(".").append(getTagClassName(tag.getTagId())).append("{");
        sb.append("color: ").append(tag.getColor().getHex()).append(";");
        sb.append("background-color: ").append(tag.getBackgroundColor().getHex()).append(";");
        sb.append("}");
        sb.append(".").append(getTagHiddenClassName(tag.getTagId())).append("{");
        sb.append("}");
        sb.append(".").append(getTagHiddenClassName(tag.getTagId())).append(" .").append(getTagClassName(tag.getTagId())).append("{");
        sb.append("display: none;");
        sb.append("}");
    }

    public static String getTagClassName(@Nonnull TagId tagId) {
        return "wp-tag-" + tagId.getId();
    }

    public static String getTagHiddenClassName(@Nonnull TagId tagId) {
        return getTagClassName(tagId) + "--hidden";
    }

}
