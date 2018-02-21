package edu.stanford.bmir.protege.web.client.perspective;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/16
 */
public class EmptyPerspectiveViewImpl extends Composite implements EmptyPerspectiveView {

    interface EmptyPerspectiveViewImplUiBinder extends UiBinder<HTMLPanel, EmptyPerspectiveViewImpl> {

    }

    @UiField
    protected HTMLPanel hasText;

    private static EmptyPerspectiveViewImplUiBinder ourUiBinder = GWT.create(EmptyPerspectiveViewImplUiBinder.class);

    private AddViewHandler addViewHandler = () -> {};

    @Inject
    public EmptyPerspectiveViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        addDomHandler(event -> {
            addViewHandler.handleAddView();
        }, ClickEvent.getType());

    }

    @Override
    public void setAddViewHandler(@Nonnull AddViewHandler addViewHandler) {
        this.addViewHandler = checkNotNull(addViewHandler);
    }
}