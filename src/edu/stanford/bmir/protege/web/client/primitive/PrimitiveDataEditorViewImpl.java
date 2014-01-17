package edu.stanford.bmir.protege.web.client.primitive;

import com.google.common.base.Optional;
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
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.client.ui.anchor.AnchorClickedHandler;
import edu.stanford.bmir.protege.web.client.ui.library.suggest.EntitySuggestion;
import edu.stanford.bmir.protege.web.client.ui.library.text.ExpandingTextBox;
import edu.stanford.bmir.protege.web.client.ui.library.text.ExpandingTextBoxMode;
import org.semanticweb.owlapi.model.EntityType;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/01/2014
 */
public class PrimitiveDataEditorViewImpl extends Composite implements PrimitiveDataEditorView {

    private ExpandingTextBox delegate;

    private PrimitiveDataEditorFreshEntityView errorView;

    private Optional<String> lastStyleName = Optional.absent();

    @Inject
    public PrimitiveDataEditorViewImpl(ExpandingTextBox delegate, PrimitiveDataEditorFreshEntityView errorView) {
        FlowPanel holder = new FlowPanel();
        this.delegate = delegate;
        holder.add(delegate);
        this.errorView = errorView;
        this.errorView.asWidget().setVisible(false);
        holder.add(errorView);
        initWidget(holder);
    }

    @Override
    public void setMode(Mode mode) {
        if(mode == Mode.SINGLE_LINE) {
            delegate.setMode(ExpandingTextBoxMode.SINGLE_LINE);
        }
        else {
            delegate.setMode(ExpandingTextBoxMode.MULTI_LINE);
        }
    }

    @Override
    public void setSuggestOracle(SuggestOracle oracle) {
        delegate.setOracle(oracle);
    }

    @Override
    public void setPrimitiveDataStyleName(Optional<String> styleName) {
        checkNotNull(styleName);
        if(lastStyleName.isPresent()) {
            delegate.getSuggestBox().removeStyleName(lastStyleName.get());
        }
        lastStyleName = styleName;
        if (styleName.isPresent()) {
            delegate.getSuggestBox().addStyleName(styleName.get());
        }
    }

    @Override
    public void setAnchorVisible(boolean b) {
        delegate.setAnchorVisible(b);
    }

    @Override
    public void setAnchorTitle(String title) {
        delegate.setAnchorTitle(title);
    }

    @Override
    public HandlerRegistration addAnchorClickedHandler(AnchorClickedHandler handler) {
        return delegate.addAnchorClickedHandler(handler);
    }

    @Override
    public boolean isEnabled() {
        return delegate.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        delegate.setEnabled(enabled);
    }

    @Override
    public HandlerRegistration addFocusHandler(FocusHandler handler) {
        return delegate.addFocusHandler(handler);
    }

    @Override
    public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
        return delegate.addKeyUpHandler(handler);
    }

    @Override
    public String getPlaceholder() {
        return delegate.getPlaceholder();
    }

    @Override
    public void setPlaceholder(String placeholder) {
        delegate.setPlaceholder(placeholder);
    }

    @Override
    public HandlerRegistration addSelectionHandler(final SelectionHandler<EntitySuggestion> handler) {
        final HandlerRegistration handlerReg = addHandler(handler, SelectionEvent.getType());
        final HandlerRegistration delegateReg = delegate.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
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
        return delegate.getText();
    }

    @Override
    public void setText(String text) {
        delegate.setText(text);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return delegate.addValueChangeHandler(handler);
    }

    @Override
    public void showErrorMessage(SafeHtml errorMessage, Set<EntityType<?>> expectedTypes) {
        errorView.setExpectedTypes(errorMessage, expectedTypes);
        errorView.asWidget().setVisible(true);
    }

    @Override
    public void clearErrorMessage() {
        errorView.asWidget().setVisible(false);
    }
}
