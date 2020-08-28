package edu.stanford.bmir.protege.web.client.primitive;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.anchor.AnchorClickedHandler;
import edu.stanford.bmir.protege.web.client.library.suggest.EntitySuggestion;
import edu.stanford.bmir.protege.web.client.library.text.ExpandingTextBox;
import edu.stanford.bmir.protege.web.client.library.text.ExpandingTextBoxMode;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/01/2014
 */
public class PrimitiveDataEditorViewImpl extends Composite implements PrimitiveDataEditorView {

    private boolean firstDisplayOfImage = true;

    interface PrimitiveDataEditorViewImplUiBinder extends UiBinder<HTMLPanel, PrimitiveDataEditorViewImpl> {

    }


    @Nonnull
    @UiField(provided = true)
    protected ExpandingTextBox textBox;

    @Nonnull
    private Optional<String> lastStyleName = Optional.empty();

    @UiField
    protected PrimitiveDataEditorFreshEntityViewImpl errorView;


    @UiField
    protected LazyPanel errorViewContainer;

    @UiField
    protected PrimitiveDataEditorImageViewImpl imageView;

    @UiField
    protected LazyPanel imageViewContainer;


    @UiField
    protected FocusPanel imageViewFocusPanel;
    @UiField
    HTMLPanel iconContainer;


    private static PrimitiveDataEditorViewImplUiBinder uiBinder = GWT.create(PrimitiveDataEditorViewImplUiBinder.class);

    @Inject
    public PrimitiveDataEditorViewImpl(@Nonnull ExpandingTextBox textBox) {
        this.textBox = checkNotNull(textBox);

        this.textBox.addValueChangeHandler(event -> updateImageDisplay());
        this.textBox.addBlurHandler(event -> updateImageDisplay());

        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setDeprecated(boolean deprecated) {
        if (deprecated) {
            this.addStyleName(WebProtegeClientBundle.BUNDLE.primitiveData().primitiveData_____deprecated());
        }
        else {
            this.removeStyleName(WebProtegeClientBundle.BUNDLE.primitiveData().primitiveData_____deprecated());
        }
    }

    @Override
    public void setMode(Mode mode) {
        if (mode == Mode.SINGLE_LINE) {
            textBox.setMode(ExpandingTextBoxMode.SINGLE_LINE);
        }
        else {
            textBox.setMode(ExpandingTextBoxMode.MULTI_LINE);
        }
    }

    @Override
    public void setSuggestOracle(SuggestOracle oracle) {
        textBox.setOracle(oracle);
    }

    @Override
    public void setAutoSelectSuggestions(boolean autoSelectSuggestions) {
        textBox.setAutoSelectSuggestions(autoSelectSuggestions);
    }

    @Override
    public void setPrimitiveDataStyleName(Optional<String> styleName) {
        checkNotNull(styleName);
        lastStyleName.ifPresent(s -> iconContainer.removeStyleName(s));
        lastStyleName = styleName;
        styleName.ifPresent(s -> iconContainer.addStyleName(s));
    }

    @Override
    public void setAnchorVisible(boolean b) {
        textBox.setAnchorVisible(b);
    }

    @Override
    public void setAnchorTitle(String title) {
        textBox.setAnchorTitle(title);
    }

    @Override
    public HandlerRegistration addAnchorClickedHandler(AnchorClickedHandler handler) {
        return textBox.addAnchorClickedHandler(handler);
    }

    @Override
    public boolean isEnabled() {
        return textBox.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        textBox.setEnabled(enabled);
    }

    @Override
    public HandlerRegistration addFocusHandler(FocusHandler handler) {
        return textBox.addFocusHandler(handler);
    }

    @Override
    public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
        return textBox.addKeyUpHandler(handler);
    }

    @Override
    public String getPlaceholder() {
        return textBox.getPlaceholder();
    }

    @Override
    public void setPlaceholder(String placeholder) {
        textBox.setPlaceholder(placeholder);
    }

    @Override
    public HandlerRegistration addSelectionHandler(final SelectionHandler<EntitySuggestion> handler) {
        final HandlerRegistration handlerReg = addHandler(handler, SelectionEvent.getType());
        final HandlerRegistration delegateReg = textBox.addSelectionHandler(event -> {
            SuggestOracle.Suggestion suggestion = event.getSelectedItem();
            if (suggestion instanceof EntitySuggestion) {
                SelectionEvent.fire(PrimitiveDataEditorViewImpl.this, (EntitySuggestion) suggestion);
            }
        });
        return () -> {
            handlerReg.removeHandler();
            delegateReg.removeHandler();
        };
    }

    @Override
    public String getText() {
        return textBox.getText();
    }

    @Override
    public void setText(String text) {
        textBox.setText(text);
        updateImageDisplay();
    }

    private void updateImageDisplay() {
        if (isPossibleImageLink()) {
            displayImageView();
        }
    }

    private boolean isPossibleImageLink() {
        String text = getText().trim();
        return (text.startsWith("http://") || text.startsWith("https://")) && (text.endsWith(".jpg") || text.endsWith(
                ".png") || text.endsWith(".svg") || text.endsWith(".jpeg"));
    }

    private boolean imageViewHasFocus = false;

    private void displayImageView() {
        imageViewContainer.setVisible(true);
        imageViewFocusPanel.setVisible(true);
        String url = getText().trim();
        imageView.setImageUrl(url);

        textBox.setVisible(false);

        if (firstDisplayOfImage) {
            firstDisplayOfImage = false;
            textBox.addBlurHandler(event -> {
                imageViewHasFocus = false;
                updateImageDisplay();
            });
            imageViewFocusPanel.addFocusHandler(event -> {
                imageViewHasFocus = true;
                hideImageView();
            });
        }
    }

    private void hideImageView() {
        int height = imageViewFocusPanel.getOffsetHeight();
        if (imageViewHasFocus) {
            // Transfer the focus to the text box
            Scheduler.get().scheduleDeferred(() -> textBox.setFocus(true));
        }
        // Transfer the height of the image view to the height of the
        // expanding text box in order to avoid unnecessary jumping about in the UI
        textBox.setMinHeight(height + "px");
        textBox.setVisible(true);
        imageViewFocusPanel.setVisible(false);

    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return textBox.addValueChangeHandler(handler);
    }

    @Override
    public void showErrorMessage(SafeHtml errorMessage, Set<EntityType<?>> expectedTypes) {
        errorViewContainer.setVisible(true);
        errorView.setExpectedTypes(errorMessage, expectedTypes);
    }

    @Override
    public void clearErrorMessage() {
        if (errorViewContainer.isVisible()) {
            errorViewContainer.setVisible(false);
        }
    }

    @Override
    public void setWrap(boolean wrap) {
        textBox.setWrap(wrap);
    }

    @Override
    public void requestFocus() {
        textBox.setFocus(true);
    }
}
