package edu.stanford.bmir.protege.web.client.pagination;

import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Mar 2017
 */
public interface PaginatorView extends IsWidget, HasVisibility {

    interface PageNumberHandler {
        void handlePageEdited(String pageNumber);
    }

    interface NextClickedHandler {
        void handleNextClicked();
    }

    interface PreviousClickedHandler {
        void handlePreviousClicked();
    }

    void setNextClickedHandler(@Nonnull NextClickedHandler handler);

    void setNextEnabled(boolean enabled);

    void setPreviousClickedHandler(@Nonnull PreviousClickedHandler handler);

    void setPreviousEnabled(boolean enabled);

    void setPageCount(int numberOfPages);

    void setPageNumberEditedHandler(@Nonnull PageNumberHandler handler);

    void setPageNumber(@Nonnull String currentPageNumber);

    void setElementCount(long elementCount);

    @Nonnull
    String getPageNumber();
}
