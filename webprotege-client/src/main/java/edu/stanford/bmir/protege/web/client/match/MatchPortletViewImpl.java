package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorPresenter;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorView;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorViewImpl;
import edu.stanford.bmir.protege.web.client.renderer.PrimitiveDataIconProvider;
import edu.stanford.bmir.protege.web.client.search.EntitySearchResultRendering;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchResult;
import edu.stanford.bmir.protege.web.shared.search.SearchField;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
public class MatchPortletViewImpl extends Composite implements MatchPortletView {

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getDecimalFormat();

    private static MatchPortletViewImplUiBinder ourUiBinder = GWT.create(MatchPortletViewImplUiBinder.class);

    @Nonnull
    private final PrimitiveDataIconProvider primitiveDataIconProvider;

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

    private final PaginatorPresenter paginatorPresenter;

    @Inject
    public MatchPortletViewImpl(@Nonnull PrimitiveDataIconProvider primitiveDataIconProvider) {
        this.primitiveDataIconProvider = primitiveDataIconProvider;
        paginatorPresenter = new PaginatorPresenter(new PaginatorViewImpl());
        paginator = (PaginatorViewImpl) paginatorPresenter.getView();
        initWidget(ourUiBinder.createAndBindUi(this));
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
    public void setResult(@Nonnull Page<OWLEntityData> result) {
        resultsCount.setText(NUMBER_FORMAT.format(result.getTotalElements()) + " results");
        String rendering = result.getPageElements().stream()
                                  .sorted()
                                  .map(this::render)
                                  .collect(Collectors.joining("\n"));
        resultsContainer.getElement().setInnerHTML(rendering);
    }

    private String render(OWLEntityData ed) {
        return "<div style='height: 22px; line-height: 21px;' class='"
                + primitiveDataIconProvider.getIconInsetStyleName(ed)
                + "'>"
                + ed.getBrowserText()
                + "</div>";
    }

    @Override
    public void setPageCount(int pageCount) {
        paginatorPresenter.setPageCount(pageCount);
    }

    @Override
    public void setPageNumber(int pageNumber) {
        paginatorPresenter.setPageNumber(pageNumber);
    }

    @Override
    public int getPageNumber() {
        return paginatorPresenter.getPageNumber();
    }

    @Override
    public void setPageNumberChangedHandler(PageNumberChangedHandler handler) {
        paginatorPresenter.setPageNumberChangedHandler(handler);
    }

    interface MatchPortletViewImplUiBinder extends UiBinder<HTMLPanel, MatchPortletViewImpl> {

    }
}