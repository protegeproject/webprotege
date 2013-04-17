package edu.stanford.bmir.protege.web.client.ui.library.text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.ui.library.common.HasPlaceholder;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.AcceptKeyHandler;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.HasAcceptKeyHandler;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/12/2012
 */
public class ExpandingTextBoxImpl extends SimplePanel implements Focusable, HasAcceptKeyHandler, HasText, HasEnabled, HasPlaceholder, HasValueChangeHandlers<String>, HasSelectionHandlers<SuggestOracle.Suggestion>, HasKeyUpHandlers, HasFocusHandlers {

    /**
     * For internal use.  The name of the element which has inner text set to size the suggest box to fit its content.
     */
    private static final String SIZING_TEXT_ELEMENT_NAME = "span";


    /**
     * An empty suggest oracle that makes no suggestions.
     */
    public static final SuggestOracle EMPTY_SUGGEST_ORACLE = new SuggestOracle() {
        @Override
        public void requestSuggestions(Request request, Callback callback) {

        }
    };


    private SuggestOracle delegateSuggestOracle = EMPTY_SUGGEST_ORACLE;


    interface ExpandingTextBoxImplUiBinder extends UiBinder<HTMLPanel, ExpandingTextBoxImpl> {

    }

    private static ExpandingTextBoxImplUiBinder ourUiBinder = GWT.create(ExpandingTextBoxImplUiBinder.class);

    @UiField(provided = true)
    protected final SuggestBox suggestBox;

    @UiField
    protected Anchor anchor;

    private ExpandingTextBoxMode mode = ExpandingTextBoxMode.SINGLE_LINE;

    private AcceptKeyHandler acceptKeyHandler = new AcceptKeyHandler() {
        @Override
        public void handleAcceptKey() {
        }
    };

    public ExpandingTextBoxImpl() {
        TextArea textArea = new TextArea();
        SuggestOracle proxyOracle = createProxySuggestOracle();
        this.suggestBox = new SuggestBox(proxyOracle, textArea);
//        this.suggestBox.setAutoSelectEnabled(false);

        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        setWidget(rootElement);
        textArea.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                doPreElements(false);
            }
        });
        textArea.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && mode == ExpandingTextBoxMode.SINGLE_LINE) {
                    event.preventDefault();
                    acceptKeyHandler.handleAcceptKey();
                }
                else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && event.isControlKeyDown() && mode == ExpandingTextBoxMode.MULTI_LINE) {
                    event.preventDefault();
                    acceptKeyHandler.handleAcceptKey();
                }
                doPreElements(mode == ExpandingTextBoxMode.MULTI_LINE && event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.isControlKeyDown());


            }
        });
    }

    @Override
    public void setAcceptKeyHandler(AcceptKeyHandler acceptKeyHandler) {
        this.acceptKeyHandler = checkNotNull(acceptKeyHandler);
    }

    public void setAnchorVisible(boolean visible) {
        anchor.setVisible(visible);
    }

    public void setAnchorTitle(String title) {
        checkNotNull(title);
        anchor.setTitle(title);
    }

    public HandlerRegistration addAnchorClickHandler(ClickHandler clickHandler) {
        checkNotNull(clickHandler);
        return anchor.addClickHandler(clickHandler);
    }


    /**
     * Returns true if the widget is enabled, false if not.
     */
    @Override
    public boolean isEnabled() {
        return suggestBox.getTextBox().isEnabled();
    }

    /**
     * Sets whether this widget is enabled.
     * @param enabled <code>true</code> to enable the widget, <code>false</code>
     * to disable it
     */
    @Override
    public void setEnabled(boolean enabled) {
        suggestBox.getTextBox().setEnabled(enabled);
    }

    private SuggestOracle getDelegateSuggestOracle() {
        return delegateSuggestOracle;
    }

    public void setSuggestOracle(SuggestOracle suggestOracle) {
        if (suggestOracle == null) {
            delegateSuggestOracle = EMPTY_SUGGEST_ORACLE;
        }
        else {
            delegateSuggestOracle = suggestOracle;
        }
    }

    public void setMode(ExpandingTextBoxMode mode) {
        this.mode = mode;
    }

    public ExpandingTextBoxMode getMode() {
        return mode;
    }

    private void doPreElements(boolean addNewLine) {
        NodeList<Element> pres = getElement().getElementsByTagName(SIZING_TEXT_ELEMENT_NAME);
        for (int i = 0; i < pres.getLength(); i++) {
            Element preElement = pres.getItem(i);
            preElement.setInnerText(suggestBox.getText() + (addNewLine ? "\n" : ""));
        }
    }

    /**
     * Gets this object's text.
     * @return the object's text
     */
    @Override
    public String getText() {
        return suggestBox.getText();
    }

    /**
     * Sets this object's text.
     * @param text the object's new text
     */
    @Override
    public void setText(String text) {
        suggestBox.setValue(text);
        doPreElements(false);
    }

    @Override
    public String getPlaceholder() {
        return suggestBox.getTextBox().getElement().getAttribute("placeholder");
    }

    @Override
    public void setPlaceholder(String placeholder) {
        suggestBox.getTextBox().getElement().setAttribute("placeholder", placeholder);
    }

    /**
     * Adds a {@link com.google.gwt.event.logical.shared.ValueChangeEvent} handler.
     * @param handler the handler
     * @return the registration for the event
     */
    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return suggestBox.addValueChangeHandler(handler);
    }


    /**
     * Creates a {@link SuggestOracle} that delegates to the proxy.
     * @return A {@link SuggestOracle} that delegates to the {@link SuggestOracle} returned by the
     *         {@link #getDelegateSuggestOracle()}.
     */
    private SuggestOracle createProxySuggestOracle() {
        return new SuggestOracle() {

            @Override
            public void requestSuggestions(final Request request, Callback callback) {
                getDelegateSuggestOracle().requestSuggestions(request, callback);
            }

            @Override
            public boolean isDisplayStringHTML() {
                return getDelegateSuggestOracle().isDisplayStringHTML();
            }

            @Override
            public void requestDefaultSuggestions(Request request, Callback callback) {
                getDelegateSuggestOracle().requestDefaultSuggestions(request, callback);
            }
        };
    }

    /**
     * Adds a {@link com.google.gwt.event.logical.shared.SelectionEvent} handler.
     * @param handler the handler
     * @return the registration for the event
     */
    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<SuggestOracle.Suggestion> handler) {
        return suggestBox.addSelectionHandler(handler);
    }

    /**
     * Adds a {@link com.google.gwt.event.dom.client.FocusEvent} handler.
     * @param handler the focus handler
     * @return {@link com.google.gwt.event.shared.HandlerRegistration} used to remove this handler
     */
    @Override
    public HandlerRegistration addFocusHandler(FocusHandler handler) {
        return suggestBox.getTextBox().addFocusHandler(handler);
    }

    /**
     * Adds a {@link com.google.gwt.event.dom.client.KeyUpEvent} handler.
     * @param handler the key up handler
     * @return {@link com.google.gwt.event.shared.HandlerRegistration} used to remove this handler
     */
    @Override
    public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
        return suggestBox.addKeyUpHandler(handler);
    }

    public SuggestBox getSuggestBox() {
        return suggestBox;
    }

    /**
     * Gets the widget's position in the tab index.
     * @return the widget's tab index
     */
    @Override
    public int getTabIndex() {
        return suggestBox.getTabIndex();
    }

    /**
     * Sets the widget's 'access key'. This key is used (in conjunction with a
     * browser-specific modifier key) to automatically focus the widget.
     * @param key the widget's access key
     */
    @Override
    public void setAccessKey(char key) {
        suggestBox.setAccessKey(key);
    }

    /**
     * Explicitly focus/unfocus this widget. Only one widget can have focus at a
     * time, and the widget that does will receive all keyboard events.
     * @param focused whether this widget should take focus or release it
     */
    @Override
    public void setFocus(boolean focused) {
        suggestBox.setFocus(focused);
    }

    /**
     * Sets the widget's position in the tab index. If more than one widget has
     * the same tab index, each such widget will receive focus in an arbitrary
     * order. Setting the tab index to <code>-1</code> will cause this widget to
     * be removed from the tab order.
     * @param index the widget's tab index
     */
    @Override
    public void setTabIndex(int index) {
        suggestBox.setTabIndex(index);
    }
}