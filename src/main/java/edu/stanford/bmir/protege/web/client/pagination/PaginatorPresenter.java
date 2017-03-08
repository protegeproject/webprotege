package edu.stanford.bmir.protege.web.client.pagination;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Mar 2017
 */
public class PaginatorPresenter {

    private final PaginatorView view;

    private int pageNumber = 1;

    private int pageCount = 1;

    private HasPagination.PageNumberChangedHandler pageNumberChangedHandler = (pageNumber) -> {};

    @Inject
    public PaginatorPresenter(@Nonnull PaginatorView view) {
        this.view = view;
        view.setNextClickedHandler(this::handleNextClicked);
        view.setPreviousClickedHandler(this::handlePreviousClicked);
        view.setPageNumberEditedHandler(this::handlePageNumberEdited);
        setPageCount(1);
        setPageNumber(1);
    }

    public void setPageNumberChangedHandler(@Nonnull HasPagination.PageNumberChangedHandler pageNumberChangedHandler) {
        this.pageNumberChangedHandler = checkNotNull(pageNumberChangedHandler);
    }

    public PaginatorView getView() {
        return view;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        if(pageNumber < 1) {
            return;
        }
        if(pageNumber > pageCount) {
            return;
        }
        this.pageNumber = pageNumber;
        view.setPageNumber(Integer.toString(pageNumber));
        updateState();
    }

    public void setPageCount(int pageCount) {
        if(pageCount < 1) {
            return;
        }
        this.pageCount = pageCount;
        view.setPageCount(pageCount);
        updateState();
    }


    private void handlePageNumberEdited(String value) {
        try {
            int pageNumber = Integer.parseInt(value);
            setPageNumber(pageNumber);
            if(this.pageNumber != pageNumber) {
                view.setPageNumber(Integer.toString(this.pageNumber));
            }
            firePageNumberChanged();
        } catch (NumberFormatException e) {
            view.setPageNumber(Integer.toString(this.pageNumber));
        }
    }

    private void handlePreviousClicked() {
        if(pageNumber == 1) {
            return;
        }
        pageNumber--;
        setPageNumber(pageNumber);
        firePageNumberChanged();
    }

    private void handleNextClicked() {
        if(pageNumber == pageCount) {
            return;
        }
        pageNumber++;
        setPageNumber(pageNumber);
        firePageNumberChanged();
    }


    private void firePageNumberChanged() {
        pageNumberChangedHandler.handlePageNumberChanged(this.pageNumber);
    }

    private void updateState() {
        view.setPreviousEnabled(pageNumber != 1);
        view.setNextEnabled(pageNumber != pageCount);
    }
}
