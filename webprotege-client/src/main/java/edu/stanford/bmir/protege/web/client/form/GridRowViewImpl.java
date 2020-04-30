package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.button.DeleteButton;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */
public class GridRowViewImpl extends Composite implements GridRowView {

    interface GridRowViewImplUiBinder extends UiBinder<HTMLPanel, GridRowViewImpl> {

    }

    private static GridRowViewImplUiBinder ourUiBinder = GWT.create(GridRowViewImplUiBinder.class);

    @UiField
    HTMLPanel rowContainer;

    private List<Widget> cellContainers = new ArrayList<>();

    @Inject
    public GridRowViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget addCell(int ordinal, double weight) {
        SimplePanel cellContainer = new SimplePanel();
        cellContainers.add(cellContainer);
        String generalColumnStyle = WebProtegeClientBundle.BUNDLE.style()
                                                     .formGridColumn();
        cellContainer.addStyleName(generalColumnStyle);
        String columnOrdinalStyle = generalColumnStyle + "--col-" + ordinal;
        cellContainer.addStyleName(columnOrdinalStyle);
        rowContainer.add(cellContainer);
        Style style = cellContainer.getElement().getStyle();
        style.setProperty("flexBasis", weight * 100, Style.Unit.PCT);
        return cellContainer;
    }

    @Override
    public void clear() {
        rowContainer.clear();
        cellContainers.clear();
    }

    @Override
    public void setColumnVisible(int colIndex, boolean visible) {
        cellContainers.get(colIndex).setVisible(visible);
    }
}
