package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.client.renderer.PrimitiveDataIconProvider;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Jun 2018
 */
public class MatchResult extends Composite {

    interface MatchResultUiBinder extends UiBinder<HTMLPanel, MatchResult> {

    }

    private static MatchResultUiBinder ourUiBinder = GWT.create(MatchResultUiBinder.class);

    @Nonnull
    private final PrimitiveDataIconProvider iconProvider;

    @UiField
    Label displayNameField;

    @Inject
    public MatchResult(@Nonnull PrimitiveDataIconProvider iconProvider) {
        this.iconProvider = iconProvider;
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void setEntityData(OWLEntityData entityData) {
        addStyleName(iconProvider.getIconInsetStyleName(entityData));
        displayNameField.setText(entityData.getBrowserText());
    }

    public void setClickHandler(ClickHandler clickHandler) {
        displayNameField.addClickHandler(clickHandler);
    }


}