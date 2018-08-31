package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasContextMenuHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.color.Color;
import edu.stanford.bmir.protege.web.shared.tag.TagId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Mar 2018
 */
public interface TagView extends IsWidget, HasAllMouseHandlers, HasContextMenuHandlers {

    void setTagId(@Nonnull TagId tagId);

    void setLabel(@Nonnull String label);

    void setDescription(@Nonnull String description);
}
