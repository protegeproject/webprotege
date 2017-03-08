package edu.stanford.bmir.protege.web.client.issues;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Mar 2017
 */
public interface CommentedEntitiesView extends IsWidget, HasPagination {

    void setEntities(@Nonnull List<OWLEntityData> entities);

    void clear();

    void addEntity(@Nonnull OWLEntityData entity);

    void removeEntity(@Nonnull OWLEntityData entity);

    void setSelectionHandler(@Nonnull SelectionHandler<OWLEntityData> selectionHandler);

    void setPageCount(int pageCount);

    void setPageNumber(int pageNumber);

    int getPageNumber();

    void setPageNumberChangedHandler(HasPagination.PageNumberChangedHandler handler);
}
