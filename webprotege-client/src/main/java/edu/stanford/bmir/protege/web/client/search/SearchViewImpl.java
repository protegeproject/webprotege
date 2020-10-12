package edu.stanford.bmir.protege.web.client.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.dlg.AcceptKeyHandler;
import edu.stanford.bmir.protege.web.client.library.dlg.HasAcceptKeyHandler;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.library.text.PlaceholderTextBox;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorPresenter;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorView;
import edu.stanford.bmir.protege.web.client.progress.BusyViewImpl;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Apr 2017
 */
public class SearchViewImpl extends Composite implements SearchView {

    private static SearchViewImplUiBinder ourUiBinder = GWT.create(SearchViewImplUiBinder.class);

    @UiField
    public SimplePanel searchResultsContainer;

    @UiField
    protected SimplePanel searchFilterContainer;

    @UiField
    protected PlaceholderTextBox searchStringField;

    @UiField
    BusyViewImpl busyView;

    @UiField
    SimplePanel langTagsFilterContainer;

    @UiField
    HTMLPanel searchFilterPanel;

    @UiField
    HTMLPanel langTagsFilterPanel;

    @Nonnull
    private IncrementSelectionHandler incrementSelectionHandler = () -> {};

    @Nonnull
    private DecrementSelectionHandler decrementSelectionHandler = () -> {};

    @Nonnull
    private SearchStringChangedHandler searchStringChangedHandler = () -> {};

    @Nonnull
    private AcceptKeyHandler acceptKeyHandler = () -> {};

    private String previousSearchString = "";

    @Inject
    public SearchViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        Element element = searchStringField.getElement();
        element.setPropertyString("autocomplete", "off");
        element.setPropertyString("autocorrect", "off");
        element.setPropertyString("autocapitalize", "off");
        element.setPropertyString("spellcheck", "off");
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getLangTagFilterContainer() {
        return langTagsFilterContainer;
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getSearchResultsContainer() {
        return searchResultsContainer;
    }

    @Override
    public void setLangTagFilterVisible(boolean visible) {
        langTagsFilterPanel.setVisible(visible);
    }

    @UiHandler("searchStringField")
    protected void handleSearchStringFileKeyUp(KeyUpEvent event) {
        int keyCode = event.getNativeEvent().getKeyCode();
        if (keyCode != KeyCodes.KEY_UP && keyCode != KeyCodes.KEY_DOWN && keyCode != KeyCodes.KEY_ENTER) {
            performSearchIfChanged();
        }
    }

    private void performSearchIfChanged() {
        String searchString = searchStringField.getText();
        if (!previousSearchString.equals(searchString)) {
            previousSearchString = searchString;
            searchStringChangedHandler.handleSearchStringChanged();
        }
    }

    @Override
    public void setIncrementSelectionHandler(@Nonnull IncrementSelectionHandler handler) {
        incrementSelectionHandler = checkNotNull(handler);
    }

    @Override
    public void setDecrementSelectionHandler(@Nonnull DecrementSelectionHandler handler) {
        decrementSelectionHandler = checkNotNull(handler);
    }

    @Override
    public void setAcceptKeyHandler(@Nonnull AcceptKeyHandler acceptKeyHandler) {
        this.acceptKeyHandler = checkNotNull(acceptKeyHandler);
    }

    @UiHandler("searchStringField")
    protected void handleSearchStringFieldKeyDown(KeyDownEvent event) {
        if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_DOWN) {
            event.preventDefault();
            incrementSelectionHandler.handleIncrementSelection();
        }
        else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_UP) {
            event.preventDefault();
            decrementSelectionHandler.handleDecrementSelection();
        }
        else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
            event.preventDefault();
            acceptKeyHandler.handleAcceptKey();
        }
        else {
            performSearchIfChanged();
        }
    }

    @Override
    public Optional<HasRequestFocus> getInitialFocusable() {
        return Optional.of(() -> searchStringField.setFocus(true));
    }

    @Override
    public void setBusy(boolean busy) {
        busyView.setVisible(busy);
    }

    @Override
    public String getSearchString() {
        return searchStringField.getText();
    }

    @Override
    public void setSearchStringChangedHandler(SearchStringChangedHandler handler) {
        this.searchStringChangedHandler = handler;
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        searchStringField.setFocus(true);
    }

    @Override
    public void setSearchFilterVisible(boolean visible) {
        searchFilterPanel.setVisible(visible);
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getSearchFilterContainer() {
        return searchFilterContainer;
    }

    interface SearchViewImplUiBinder extends UiBinder<HTMLPanel, SearchViewImpl> {

    }
}