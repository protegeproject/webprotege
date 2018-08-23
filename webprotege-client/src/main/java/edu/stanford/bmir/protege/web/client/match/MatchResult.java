package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.client.entity.EntityNodeHtmlRenderer;
import edu.stanford.bmir.protege.web.client.renderer.PrimitiveDataIconProvider;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Jun 2018
 */
public class MatchResult extends Composite {

    private EntityNodeHtmlRenderer renderer;

    interface MatchResultUiBinder extends UiBinder<HTMLPanel, MatchResult> {

    }

    private static MatchResultUiBinder ourUiBinder = GWT.create(MatchResultUiBinder.class);

    @Nonnull
    private final PrimitiveDataIconProvider iconProvider;

    @UiField
    Label displayNameField;

    @UiField
    HTMLPanel outer;

    @Inject
    public MatchResult(@Nonnull PrimitiveDataIconProvider iconProvider,
                       @Nullable EntityNodeHtmlRenderer renderer) {
        this.iconProvider = iconProvider;
        this.renderer = renderer;
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void setEntityData(EntityNode node) {
        String html = renderer.getHtmlRendering(node);
        displayNameField.getElement().setInnerHTML(html);
    }

    public void setClickHandler(ClickHandler clickHandler) {
        displayNameField.addClickHandler(clickHandler);
    }


}