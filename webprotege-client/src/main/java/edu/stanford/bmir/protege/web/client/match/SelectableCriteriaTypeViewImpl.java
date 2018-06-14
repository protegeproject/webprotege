package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class SelectableCriteriaTypeViewImpl extends Composite implements SelectableCriteriaTypeView {

    interface SelectableCriteriaTypeViewImplUiBinder extends UiBinder<HTMLPanel, SelectableCriteriaTypeViewImpl> {

    }

    private static SelectableCriteriaTypeViewImplUiBinder ourUiBinder = GWT.create(SelectableCriteriaTypeViewImplUiBinder.class);

    @UiField
    ListBox listBox;

    @UiField
    SimplePanel viewContainer;

    @Nonnull
    private SelectedNameChangedHander selectedNameChangedHander = () -> {};

    @Inject
    public SelectableCriteriaTypeViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        listBox.addChangeHandler(event -> selectedNameChangedHander.handleSelectedNameChanged());
    }

    @Override
    public int getSelectedIndex() {
        return listBox.getSelectedIndex();
    }

    @Override
    public void setSelectableNames(@Nonnull List<String> names) {
        listBox.clear();
        names.forEach(name -> listBox.addItem(name));
    }

    @Override
    public void setSelectedName(int index) {
        listBox.setSelectedIndex(index);
    }

    @Nonnull
    @Override
    public Optional<String> getSelectedName() {
        return Optional.ofNullable(listBox.getSelectedValue());
    }

    @Override
    public void setSelectedNameChangedHandler(@Nonnull SelectedNameChangedHander handler) {
        this.selectedNameChangedHander = checkNotNull(handler);
    }

    @Override
    public void setWidget(IsWidget w) {
        viewContainer.setWidget(w);
    }
}