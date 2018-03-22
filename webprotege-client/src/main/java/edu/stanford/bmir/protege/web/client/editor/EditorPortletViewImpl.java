package edu.stanford.bmir.protege.web.client.editor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Mar 2018
 */
public class EditorPortletViewImpl extends Composite implements EditorPortletView {

    interface EditorPortletViewImplUiBinder extends UiBinder<HTMLPanel, EditorPortletViewImpl> {
    }

    private static EditorPortletViewImplUiBinder ourUiBinder = GWT.create(EditorPortletViewImplUiBinder.class);

    @UiField
    SimplePanel tagListViewContainer;

    @UiField
    SimplePanel editorViewContainer;

    @Inject
    public EditorPortletViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getTagListViewContainer() {
        return tagListViewContainer;
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getEditorViewContainer() {
        return editorViewContainer;
    }
}