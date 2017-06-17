package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.entitieslist.EntitiesList;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorPresenter;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorView;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorViewImpl;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.selection.EntitySelectionChangedEvent;
import edu.stanford.bmir.protege.web.shared.selection.EntitySelectionChangedHandler;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Jun 2017
 */
public class DeprecatedEntitiesViewImpl extends Composite implements DeprecatedEntitiesView {

    interface DeprecatedEntitiesViewImplUiBinder extends UiBinder<HTMLPanel, DeprecatedEntitiesViewImpl> {

    }

    private static DeprecatedEntitiesViewImplUiBinder ourUiBinder = GWT.create(DeprecatedEntitiesViewImplUiBinder.class);

    @UiField
    EntitiesList<OWLEntityData> entitiesList;

    @UiField(provided = true)
    PaginatorView paginator;

    private HandlerRegistration handlerRegistration = () -> {};

    private final PaginatorPresenter paginatorPresenter;

    @Inject
    public DeprecatedEntitiesViewImpl(PaginatorPresenter paginatorPresenter) {
        this.paginatorPresenter = paginatorPresenter;
        paginator = paginatorPresenter.getView();
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setSelectedEntity(OWLEntityData entity) {
        entitiesList.setSelectedEntity(entity);
    }

    @Override
    public Optional<OWLEntityData> getSelectedEntity() {
        return entitiesList.getSelectedEntity();
    }

    @Override
    public void setSelectionChangedHandler(SelectionHandler<OWLEntityData> handler) {
        handlerRegistration.removeHandler();
        handlerRegistration = entitiesList.addSelectionHandler(handler);
    }

    @Override
    public void setEntities(List<OWLEntityData> entities) {
        entitiesList.setListData(entities);
    }

    @Override
    public void addEntity(OWLEntityData entityData) {
        entitiesList.addAll(singleton(entityData));
    }

    @Override
    public void removeEntity(OWLEntityData entityData) {
        entitiesList.removeAll(singleton(entityData));
    }

    @Override
    public void setPageNumberChangedHandler(@Nonnull HasPagination.PageNumberChangedHandler pageNumberChangedHandler) {
        paginatorPresenter.setPageNumberChangedHandler(pageNumberChangedHandler);
    }

    @Override
    public int getPageNumber() {
        return paginatorPresenter.getPageNumber();
    }

    @Override
    public void setPageNumber(int pageNumber) {
        paginatorPresenter.setPageNumber(pageNumber);
    }

    @Override
    public void setPageCount(int pageCount) {
        paginatorPresenter.setPageCount(pageCount);
    }
}