package edu.stanford.bmir.protege.web.client.primitive;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.anchor.AnchorClickedHandler;
import edu.stanford.bmir.protege.web.client.library.suggest.EntitySuggestion;
import edu.stanford.bmir.protege.web.client.library.text.ExpandingTextBox;
import edu.stanford.bmir.protege.web.client.library.text.ExpandingTextBoxMode;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;
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

    @Nonnull
    private final ExpandingTextBox textBox;

    @Nonnull
    private final FlowPanel holder;

    @Nonnull
    private final Provider<PrimitiveDataEditorFreshEntityView> errorViewProvider;

    @Nonnull
    private final Provider<PrimitiveDataEditorImageView> imageViewProvider;

    @Nonnull
    private Optional<String> lastStyleName = Optional.empty();

    @Nullable
    private PrimitiveDataEditorFreshEntityView errorView;

    @Nullable
    private PrimitiveDataEditorImageView imageView;

    @Nullable
    private FocusPanel imageViewContainer;

    @Inject
    public PrimitiveDataEditorViewImpl(@Nonnull ExpandingTextBox textBox,
                                       @Nonnull PrimitiveDataEditorImageView imageView,
                                       @Nonnull Provider<PrimitiveDataEditorFreshEntityView> errorViewProvider,
                                       @Nonnull Provider<PrimitiveDataEditorImageView> imageViewProvider) {
        this.textBox = checkNotNull(textBox);
        this.imageView = checkNotNull(imageView);
        this.errorViewProvider = checkNotNull(errorViewProvider);
        this.imageViewProvider = checkNotNull(imageViewProvider);

        this.textBox.addValueChangeHandler(event -> updateImageDisplay());
        this.textBox.addBlurHandler(event -> updateImageDisplay());

        holder = new FlowPanel();
        holder.add(textBox);

        initWidget(holder);
    }

    @Override
    public void setMode(Mode mode) {
        if(mode == Mode.SINGLE_LINE) {
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
        lastStyleName.ifPresent(s -> textBox.getSuggestBox().removeStyleName(s));
        lastStyleName = styleName;
        styleName.ifPresent(s -> textBox.getSuggestBox().addStyleName(s));
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
            if(suggestion instanceof EntitySuggestion) {
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
        if(isPossibleImageLink()) {
            displayImageView();
        }
    }

    private boolean isPossibleImageLink() {
        String text = getText().trim();
        return (text.startsWith("http://") || text.startsWith("https://"))
                && (text.endsWith(".jpg") || text.endsWith(".png") || text.endsWith(".svg") || text.endsWith(".jpeg"));
    }

    private boolean imageViewHasFocus = false;

    private FocusPanel getImageViewContainer() {
        if (imageViewContainer == null) {
            imageViewContainer = new FocusPanel();
            imageViewContainer.getElement().getStyle().setVerticalAlign(Style.VerticalAlign.TOP);
            imageViewContainer.addBlurHandler(event -> {
                imageViewHasFocus = false;
                updateImageDisplay();
            });
            imageViewContainer.addFocusHandler(event -> {
                imageViewHasFocus = true;
                hideImageView();
            });
        }
        return imageViewContainer;
    }

    @Nonnull
    private PrimitiveDataEditorImageView getImageView() {
        if(imageView == null) {
            imageView = imageViewProvider.get();
        }
        return imageView;
    }

    private void displayImageView() {
        String url = getText().trim();
        PrimitiveDataEditorImageView imageView = getImageView();
        imageView.setImageUrl(url);
        FocusPanel imageViewContainer = getImageViewContainer();
        imageViewContainer.setWidget(imageView);
        holder.add(imageViewContainer);
        textBox.setVisible(false);
    }

    private void hideImageView() {
        textBox.setVisible(true);
        if (imageViewContainer != null) {
            int height = imageViewContainer.getOffsetHeight();
            holder.remove(imageViewContainer);
            if(imageViewHasFocus) {
                // Transfer the focus to the text box
                textBox.setFocus(true);
            }
            // Transfer the height of the image view to the height of the
            // expanding text box in order to avoid unnecessary jumping about in the UI
            textBox.setMinHeight(height + "px");
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return textBox.addValueChangeHandler(handler);
    }

    @Override
    public void showErrorMessage(SafeHtml errorMessage, Set<EntityType<?>> expectedTypes) {
        if(errorView == null) {
            errorView = errorViewProvider.get();
        }
        errorView.setExpectedTypes(errorMessage, expectedTypes);
        // Remove any existing view and ensure that the error view appears last
        clearErrorMessage();
        holder.add(errorView);
    }

    @Override
    public void clearErrorMessage() {
        if(errorView != null) {
            holder.remove(errorView);
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
