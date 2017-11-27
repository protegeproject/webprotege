package edu.stanford.bmir.protege.web.client.primitive;

import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SuggestOracle;
import edu.stanford.bmir.protege.web.client.anchor.AnchorClickedHandler;
import edu.stanford.bmir.protege.web.client.library.suggest.EntitySuggestion;
import edu.stanford.bmir.protege.web.client.library.text.ExpandingTextBox;
import edu.stanford.bmir.protege.web.client.library.text.ExpandingTextBoxMode;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
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

    private ExpandingTextBox textBox;

    private Provider<PrimitiveDataEditorFreshEntityView> errorViewProvider;

    private Optional<String> lastStyleName = Optional.empty();

    private final FlowPanel holder;

    @Inject
    public PrimitiveDataEditorViewImpl(@Nonnull ExpandingTextBox textBox,
                                       @Nonnull Provider<PrimitiveDataEditorFreshEntityView> errorViewProvider) {
        this.textBox = checkNotNull(textBox);
        this.errorViewProvider = checkNotNull(errorViewProvider);

        holder = new FlowPanel();
        holder.getElement().setAttribute("hello", "x");
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
        if(lastStyleName.isPresent()) {
            textBox.getSuggestBox().removeStyleName(lastStyleName.get());
        }
        lastStyleName = styleName;
        if (styleName.isPresent()) {
            textBox.getSuggestBox().addStyleName(styleName.get());
        }
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
        final HandlerRegistration delegateReg = textBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            @Override
            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event) {
                SuggestOracle.Suggestion suggestion = event.getSelectedItem();
                if(suggestion instanceof EntitySuggestion) {
                    SelectionEvent.fire(PrimitiveDataEditorViewImpl.this, (EntitySuggestion) suggestion);
                }
            }
        });
        return new HandlerRegistration() {
            @Override
            public void removeHandler() {
                handlerReg.removeHandler();
                delegateReg.removeHandler();
            }
        };
    }

    @Override
    public String getText() {
        return textBox.getText();
    }

    @Override
    public void setText(String text) {
        textBox.setText(text);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return textBox.addValueChangeHandler(handler);
    }

    @Override
    public void showErrorMessage(SafeHtml errorMessage, Set<EntityType<?>> expectedTypes) {
        if(holder.getWidgetCount() > 1) {
            clearErrorMessage();
        }
        PrimitiveDataEditorFreshEntityView error = errorViewProvider.get();
        error.setExpectedTypes(errorMessage, expectedTypes);
        holder.add(error);
    }

    @Override
    public void clearErrorMessage() {
        if(holder.getWidgetCount() == 1) {
            return;
        }
        holder.remove(1);
    }
}
