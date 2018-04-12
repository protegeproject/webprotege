package edu.stanford.bmir.protege.web.client.primitive;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Node;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Apr 2018
 */
public class PrimitiveDataEditorImageViewImpl extends Composite implements PrimitiveDataEditorImageView {

    interface PrimitiveDataEditorImageViewImplUiBinder extends UiBinder<HTMLPanel, PrimitiveDataEditorImageViewImpl> {

    }

    private static PrimitiveDataEditorImageViewImplUiBinder ourUiBinder = GWT.create(PrimitiveDataEditorImageViewImplUiBinder.class);

    @Inject
    public PrimitiveDataEditorImageViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
    @Override
    public void setImageUrl(@Nonnull String url) {
        Element element = getElement();
        Node childNode = element.getFirstChild();
        Element imgElement = childNode.cast();
        imgElement.setAttribute("src", url);
    }
}