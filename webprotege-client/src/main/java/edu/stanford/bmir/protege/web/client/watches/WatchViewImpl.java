package edu.stanford.bmir.protege.web.client.watches;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28/02/16
 */
public class WatchViewImpl extends Composite implements WatchView {

    interface WatchTypeSelectorViewImplUiBinder extends UiBinder<HTMLPanel, WatchViewImpl> {

    }

    private static WatchTypeSelectorViewImplUiBinder ourUiBinder = GWT.create(WatchTypeSelectorViewImplUiBinder.class);

    @Inject
    public WatchViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiField
    protected RadioButton none;

    @UiField
    protected RadioButton entity;

    @UiField
    protected RadioButton branch;

    @Override
    public WatchTypeSelection getSelectedType() {
        if(none.getValue()) {
            return WatchTypeSelection.NONE_SELECTED;
        }
        if(entity.getValue()) {
            return WatchTypeSelection.ENTITY_SELECTED;
        }
        if(branch.getValue()) {
            return WatchTypeSelection.BRANCH_SELECTED;
        }
        return WatchTypeSelection.NONE_SELECTED;
    }

    @Override
    public void setSelectedType(WatchTypeSelection watchTypeSelection) {
        switch (watchTypeSelection) {
            case NONE_SELECTED:
                none.setValue(true);
                entity.setValue(false);
                branch.setValue(false);
                break;
            case ENTITY_SELECTED:
                none.setValue(false);
                entity.setValue(true);
                branch.setValue(false);
                break;
            case BRANCH_SELECTED:
                none.setValue(false);
                entity.setValue(false);
                branch.setValue(true);
                break;
        }

    }

    @Override
    protected void onAttach() {
        super.onAttach();
        none.setFocus(true);
    }
}