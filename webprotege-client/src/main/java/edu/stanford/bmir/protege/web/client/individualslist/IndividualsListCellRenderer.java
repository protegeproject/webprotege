package edu.stanford.bmir.protege.web.client.individualslist;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.client.entity.EntityNodeHtmlRenderer;
import edu.stanford.bmir.protege.web.client.list.ListBoxCellRenderer;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.lang.DisplayDictionaryLanguage;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29 Jul 2018
 */
public class IndividualsListCellRenderer implements ListBoxCellRenderer<EntityNode> {

    @Nonnull
    private final EntityNodeHtmlRenderer renderer;

    @Inject
    public IndividualsListCellRenderer(@Nonnull EntityNodeHtmlRenderer renderer) {
        this.renderer = checkNotNull(renderer);
    }

    @Override
    public IsWidget render(EntityNode element) {
        return new HTMLPanel(renderer.getHtmlRendering(element));
    }

    public void setDisplayLanguage(@Nonnull DisplayDictionaryLanguage displayLanguage) {
        renderer.setDisplayLanguage(displayLanguage);
    }
}
