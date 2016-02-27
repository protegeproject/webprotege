package edu.stanford.bmir.protege.web.client.ui.library.richtext;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.*;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.logical.shared.InitializeEvent;
import com.google.gwt.event.logical.shared.InitializeHandler;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/04/2013
 */
public class RichTextEditorViewImpl extends Composite implements RichTextEditorView, Focusable {

    private static final RegExp LAST_ELEMENT_BR_REG_EXP = RegExp.compile("(<br>)(</.*>)", "g");

    private static final String LAST_ELEMENT_BR_REPL = "$2";

    private static final RegExp EMPTY_PARAGRAPH_ELEMENT_REG_EXP = RegExp.compile("<p>(<br>)?</p>", "g");

    private static final String EMPTY_PARAGRAPH_ELEMENT_REPL = "";

    private static final RegExp TRAILING_LINE_BREAKS_REG_EXP = RegExp.compile("(<br>)+", "g");

    private static final String TRAILING_LINE_BREAKS_REPL = "<br>";



    interface RichTextEditorViewImplUiBinder extends UiBinder<HTMLPanel, RichTextEditorViewImpl> {

    }

    @UiField
    protected RichTextArea richTextArea;

    private static RichTextEditorViewImplUiBinder ourUiBinder = GWT.create(RichTextEditorViewImplUiBinder.class);



    private FormattingChangedHandler formattingChangedHandler = new FormattingChangedHandler() {
        @Override
        public void handleFormattingChanged() {

        }
    };

    public RichTextEditorViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        richTextArea.addInitializeHandler(new InitializeHandler() {
            @Override
            public void onInitialize(InitializeEvent event) {
                try {
                    IFrameElement iFrameElement = IFrameElement.as(richTextArea.getElement());
                    iFrameElement.setMarginWidth(0);

                    Document document = iFrameElement.getContentDocument();
                    BodyElement body = document.getBody();
                    HeadElement head = HeadElement.as(Element.as(body.getPreviousSibling()));
                    StyleElement style = document.createStyleElement();
                    style.setInnerText("body{ font-size: 12px; font-family: Helvetica Neue, Helvetica, Arial, sans-serif;} ul {margin-left: 20px; margin-top: 0; margin-bottom: 0; list-style: disc, inside;} ol {margin-left: 20px; margin-top: 0; margin-bottom: 0; list-style: decimal, inside;}");
                    head.appendChild(style);
                }
                catch (Exception e) {
                    GWT.log("Problem setting up rich text area", e);
                }
            }
        });
    }

    private void pruneTrailingEmptyElements(Node element) {
        for(int i = 0; i < element.getChildCount(); i++) {
            pruneTrailingEmptyElements(element.getChild(i));
        }
        while(true) {
            Node lastChild = element.getLastChild();
            if(lastChild instanceof Element) {
                if(((Element) lastChild).getInnerHTML().trim().isEmpty()) {
                    lastChild.removeFromParent();
                }
                else {
                    break;
                }
            }
            else {
                break;
            }
        }

    }

    @Override
    public String getText() {
        String htmlString = removeEmptyElements();
        return htmlString;
    }


    @Override
    public void setText(String text) {
        richTextArea.setHTML(text);
    }

    @Override
    public void setFormattingChangedHandler(FormattingChangedHandler handler) {
        this.formattingChangedHandler = handler;
    }

    @UiHandler("richTextArea")
    void handleKeyDownEvent(KeyDownEvent event) {
        formattingChangedHandler.handleFormattingChanged();
    }

    @UiHandler("richTextArea")
    void handleKeyPressedEvent(KeyPressEvent event) {
        formattingChangedHandler.handleFormattingChanged();
    }

    @UiHandler("richTextArea")
    void handleMouseClickedEvent(ClickEvent event) {
        formattingChangedHandler.handleFormattingChanged();
    }


    @Override
    public FontStyle getFontStyle() {
        RichTextArea.Formatter formatter = richTextArea.getFormatter();
        return formatter.isItalic() ? FontStyle.ITALIC : FontStyle.NORMAL;
    }

    @Override
    public void toggleFontStyle() {
        RichTextArea.Formatter formatter = richTextArea.getFormatter();
        formatter.toggleItalic();
        formattingChangedHandler.handleFormattingChanged();
    }

    @Override
    public FontWeight getFontWeight() {
        RichTextArea.Formatter formatter = richTextArea.getFormatter();
        return formatter.isBold() ? FontWeight.BOLD : FontWeight.NORMAL;
    }

    @Override
    public void toggleFontWeight() {
        RichTextArea.Formatter formatter = richTextArea.getFormatter();
        formatter.toggleBold();
        formattingChangedHandler.handleFormattingChanged();
    }

    @Override
    public TextDecoration getTextDecoration() {
        RichTextArea.Formatter formatter = richTextArea.getFormatter();
        return formatter.isUnderlined() ? TextDecoration.UNDERLINE : TextDecoration.NONE;
    }

    @Override
    public void toggleTextDecoration() {
        RichTextArea.Formatter formatter = richTextArea.getFormatter();
        formatter.toggleUnderline();
        formattingChangedHandler.handleFormattingChanged();
    }

    @Override
    public void insertUnorderedList() {
        RichTextArea.Formatter formatter = richTextArea.getFormatter();
        formatter.insertUnorderedList();
        formattingChangedHandler.handleFormattingChanged();
    }

    @Override
    public void insertOrderedList() {
        RichTextArea.Formatter formatter = richTextArea.getFormatter();
        formatter.insertOrderedList();
        formattingChangedHandler.handleFormattingChanged();
    }


    @Override
    public void createLink(String linkURL) {
        RichTextArea.Formatter formatter = richTextArea.getFormatter();
        formatter.createLink(linkURL);
        formattingChangedHandler.handleFormattingChanged();
    }

    @Override
    public Widget getWidget() {
        return this;
    }



    private String removeEmptyElements() {
        String htmlString = richTextArea.getHTML().trim();
        htmlString = LAST_ELEMENT_BR_REG_EXP.replace(htmlString, LAST_ELEMENT_BR_REPL);
        htmlString = EMPTY_PARAGRAPH_ELEMENT_REG_EXP.replace(htmlString, EMPTY_PARAGRAPH_ELEMENT_REPL);
        htmlString = TRAILING_LINE_BREAKS_REG_EXP.replace(htmlString, TRAILING_LINE_BREAKS_REPL);
        return htmlString;
    }


    @Override
    public int getTabIndex() {
        return richTextArea.getTabIndex();
    }

    @Override
    public void setAccessKey(char key) {
        richTextArea.setAccessKey(key);
    }

    @Override
    public void setFocus(boolean focused) {
        richTextArea.setFocus(focused);
    }

    @Override
    public void setTabIndex(int index) {
        richTextArea.setTabIndex(index);
    }
}