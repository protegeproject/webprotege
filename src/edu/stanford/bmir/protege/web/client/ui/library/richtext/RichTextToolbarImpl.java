package edu.stanford.bmir.protege.web.client.ui.library.richtext;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/04/2013
 */
public class RichTextToolbarImpl extends Composite implements RichTextToolbar {

    interface RichTextToolbarImplUiBinder extends UiBinder<HTMLPanel, RichTextToolbarImpl> {

    }

    private static RichTextToolbarImplUiBinder ourUiBinder = GWT.create(RichTextToolbarImplUiBinder.class);

    @UiField
    protected ToggleButton boldButton;

    @UiField
    protected ToggleButton italicButton;

    @UiField
    protected ToggleButton underlineButton;

    @UiField
    protected Button bulletedListButton;

    @UiField
    protected Button numberedListButton;

    @UiField
    protected ButtonBase createLinkButton;


    private RichTextToolbarHandler handler = new RichTextToolbarHandler() {
        @Override
        public void handleToggleFontWeight() {
        }

        @Override
        public void handleToggleFontStyle() {
        }

        @Override
        public void handleToggleTextDecoration() {
        }

        @Override
        public void handleInsertUnorderedList() {
        }

        @Override
        public void handleInsertOrderedList() {
        }

        @Override
        public void handleCreateLink() {
        }
    };

    public RichTextToolbarImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);

    }

    @Override
    public void setRichTextToolbarHandler(RichTextToolbarHandler handler) {
        this.handler = checkNotNull(handler);
    }



    @Override
    public void setTextDecoration(TextDecoration textDecoration) {
        underlineButton.setDown(textDecoration == TextDecoration.UNDERLINE);
    }

    @Override
    public void setFontWeight(FontWeight fontWeight) {
        boldButton.setDown(fontWeight == FontWeight.BOLD);
    }

    @Override
    public void setFontStyle(FontStyle fontStyle) {
        italicButton.setDown(fontStyle == FontStyle.ITALIC);
    }



    @UiHandler("boldButton")
    void handleBoldClicked(ClickEvent clickEvent) {
        handler.handleToggleFontWeight();
    }

    @UiHandler("italicButton")
    void handleItalicClicked(ClickEvent clickEvent) {
        handler.handleToggleFontStyle();
    }

    @UiHandler("underlineButton")
    void handleUnderlineClicked(ClickEvent clickEvent) {
        handler.handleToggleTextDecoration();
    }

    @UiHandler("bulletedListButton")
    void handleBulletedListClicked(ClickEvent clickEvent) {
        handler.handleInsertUnorderedList();
    }

    @UiHandler("numberedListButton")
    void handleNumberedListClicked(ClickEvent clickEvent) {
        handler.handleInsertOrderedList();
    }

    @UiHandler("createLinkButton")
    void handleCreateLinkButtonClicked(ClickEvent clickEvent) {
        handler.handleCreateLink();
    }

}