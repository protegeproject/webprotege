package edu.stanford.bmir.protege.web.client.individualslist;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.entity.EntityNodeHtmlRenderer;
import edu.stanford.bmir.protege.web.client.list.ListBoxCellRenderer;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29 Jul 2018
 */
public class EntityNodeListCellRenderer implements ListBoxCellRenderer<EntityNode> {

    @Nonnull
    private final EntityNodeHtmlRenderer renderer;

    @Inject
    public EntityNodeListCellRenderer(@Nonnull EntityNodeHtmlRenderer renderer) {
        this.renderer = checkNotNull(renderer);
    }

    @Override
    public IsWidget render(EntityNode element) {
        return new HTMLPanel(renderer.getHtmlRendering(element));
    }

    public void setDisplayLanguage(@Nonnull DisplayNameSettings displayLanguage) {
        renderer.setDisplayLanguage(displayLanguage);
    }
}
