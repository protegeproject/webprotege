package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.entity.EntityItemMapper;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorPresenter;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorViewImpl;
import edu.stanford.bmir.protege.web.client.renderer.PrimitiveDataIconProvider;
import edu.stanford.bmir.protege.web.client.ui.ScrollTracker;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.perspective.EntityTypePerspectiveMapper;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.place.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
public class QueryPortletViewImpl extends Composite implements QueryPortletView {

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getDecimalFormat();

    private static QueryPortletViewImplUiBinder ourUiBinder = GWT.create(QueryPortletViewImplUiBinder.class);

    @Nonnull
    private final PrimitiveDataIconProvider primitiveDataIconProvider;

    private final PaginatorPresenter paginatorPresenter;

    @Nonnull
    private final EntityTypePerspectiveMapper typePerspectiveMapper;

    @Nonnull
    private final PlaceController placeController;

    @Nonnull
    private final Provider<MatchResult> matchResultProvider;

    @UiField
    SimplePanel criteriaContainer;

    @UiField
    InlineLabel resultsCount;

    @UiField
    FlowPanel resultsContainer;

    @UiField
    Button executeButton;

    @UiField(provided = true)
    PaginatorViewImpl paginator;

    private ExecuteQueryHandler executeHandler = () -> {};

    private ScrollTracker scrollTracker;

    @Inject
    public QueryPortletViewImpl(@Nonnull PrimitiveDataIconProvider primitiveDataIconProvider,
                                @Nonnull EntityTypePerspectiveMapper typePerspectiveMapper,
                                @Nonnull PlaceController placeController,
                                @Nonnull Provider<MatchResult> matchResultProvider,
                                @Nonnull PaginatorPresenter paginatorPresenter) {
        this.primitiveDataIconProvider = primitiveDataIconProvider;
        this.typePerspectiveMapper = typePerspectiveMapper;
        this.placeController = placeController;
        this.matchResultProvider = matchResultProvider;
        this.paginatorPresenter = paginatorPresenter;
        paginator = (PaginatorViewImpl) paginatorPresenter.getView();
        initWidget(ourUiBinder.createAndBindUi(this));
        scrollTracker = new ScrollTracker(resultsContainer);
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        scrollTracker.start();
    }

    @Override
    public void setExecuteEnabled(boolean enabled) {
        executeButton.setEnabled(enabled);
    }

    @Override
    public void setExecuteHandler(@Nonnull ExecuteQueryHandler handler) {
        this.executeHandler = checkNotNull(handler);
    }

    @UiHandler("executeButton")
    protected void handleExecute(ClickEvent event) {
        executeHandler.handleExecute();
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getCriteriaContainer() {
        return criteriaContainer;
    }

    @Override
    public void clearResults() {
        resultsContainer.clear();
    }

    @Override
    public void setResult(@Nonnull Page<EntityNode> result) {
        resultsCount.setText(NUMBER_FORMAT.format(result.getTotalElements()) + " results");
        resultsContainer.clear();
        result.getPageElements().stream()
              .map(ed -> {
                  MatchResult res = matchResultProvider.get();
                  res.setClickHandler(event -> handleClick(event, ed.getEntityData()));
                  res.setEntityData(ed);
                  return res;
              }).forEach(mr -> resultsContainer.add(mr));
        ;
    }

    private void handleClick(ClickEvent event, OWLEntityData ed) {
                PerspectiveId perspectiveId = typePerspectiveMapper
                .getPerspectiveId(ed.getEntity().getEntityType());

        Place place = placeController.getWhere();
        if(place instanceof ProjectViewPlace) {
            EntityItemMapper.getItem(ed.getEntity()).ifPresent(item -> {
                ProjectViewPlace nextPlace = ((ProjectViewPlace) place).builder()
                                                                       .withPerspectiveId(perspectiveId)
                                                                       .clearSelection()
                                                                       .withSelectedItem(item)
                                                                       .build();
                placeController.goTo(nextPlace);
            });
        }
    }

    @Override
    public void setPageCount(int pageCount) {
        paginatorPresenter.setPageCount(pageCount);
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
    public void setPageNumberChangedHandler(PageNumberChangedHandler handler) {
        paginatorPresenter.setPageNumberChangedHandler(handler);
    }

    interface QueryPortletViewImplUiBinder extends UiBinder<HTMLPanel, QueryPortletViewImpl> {

    }

}