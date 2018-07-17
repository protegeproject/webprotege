package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jul 2018
 */
public class EntityDefaultLanguagesViewImpl extends Composite implements EntityDefaultLanguagesView {

    interface EntityDefaultLanguagesViewImplUiBinder extends UiBinder<HTMLPanel, EntityDefaultLanguagesViewImpl> {

    }

    private static EntityDefaultLanguagesViewImplUiBinder ourUiBinder = GWT.create(EntityDefaultLanguagesViewImplUiBinder.class);

    @Inject
    public EntityDefaultLanguagesViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}