package edu.stanford.bmir.protege.web.client.pagination;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Mar 2017
 */
public class PaginatorViewImpl extends Composite implements PaginatorView {

    interface PaginatorViewImplUiBinder extends UiBinder<HTMLPanel, PaginatorViewImpl> {

    }

    @UiField
    protected Button previousButton;

    @UiField
    protected Button nextButton;

    @UiField
    protected TextBox pageNumberField;

    @UiField
    protected HasText pageCountField;

    private PageNumberHandler pageNumberHandler = (value) -> {};

    private NextClickedHandler nextClickedHandler = () -> {};

    private PreviousClickedHandler previousClickedHandler = () -> {};


    private static PaginatorViewImplUiBinder ourUiBinder = GWT.create(PaginatorViewImplUiBinder.class);

    @Inject
    public PaginatorViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("pageNumberField")
    protected void handlePageNumberEdited(ValueChangeEvent<String> event) {
        pageNumberHandler.handlePageEdited(event.getValue());
    }

    @UiHandler("previousButton")
    protected void handlePreviousButtonClicked(ClickEvent event) {
        previousClickedHandler.handlePreviousClicked();
    }

    @UiHandler("nextButton")
    protected void handleNextButtonClicked(ClickEvent event) {
        nextClickedHandler.handleNextClicked();
    }

    @Override
    public void setNextClickedHandler(@Nonnull NextClickedHandler handler) {
        this.nextClickedHandler = checkNotNull(handler);
    }

    @Override
    public void setNextEnabled(boolean enabled) {
        nextButton.setEnabled(enabled);
    }

    @Override
    public void setPreviousClickedHandler(@Nonnull PreviousClickedHandler handler) {
        this.previousClickedHandler = checkNotNull(handler);
    }

    @Override
    public void setPreviousEnabled(boolean enabled) {
        previousButton.setEnabled(enabled);
    }

    @Override
    public void setPageNumberEditedHandler(@Nonnull PageNumberHandler handler) {
        this.pageNumberHandler = checkNotNull(handler);
    }

    @Override
    public void setPageCount(int numberOfPages) {
        pageCountField.setText(Integer.toString(numberOfPages));
    }

    @Override
    public void setPageNumber(@Nonnull String pageNumber) {
        pageNumberField.setText(pageNumber.trim());
    }

    @Nonnull
    @Override
    public String getPageNumber() {
        return pageNumberField.getValue().trim();
    }
}