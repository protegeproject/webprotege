package edu.stanford.bmir.protege.web.client.watches;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28/02/16
 */
public class WatchTypeSelectorViewImpl extends Composite implements WatchTypeSelectorView {

    interface WatchTypeSelectorViewImplUiBinder extends UiBinder<HTMLPanel, WatchTypeSelectorViewImpl> {

    }

    private static WatchTypeSelectorViewImplUiBinder ourUiBinder = GWT.create(WatchTypeSelectorViewImplUiBinder.class);

    public WatchTypeSelectorViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiField
    protected RadioButton none;

    @UiField
    protected RadioButton entity;

    @UiField
    protected RadioButton branch;

    @Override
    public WatchType getSelectedType() {
        if(none.getValue()) {
            return WatchType.NONE;
        }
        if(entity.getValue()) {
            return WatchType.ENTITY;
        }
        if(branch.getValue()) {
            return WatchType.BRANCH;
        }
        return WatchType.NONE;
    }

    @Override
    public void setSelectedType(WatchType watchType) {
        switch (watchType) {
            case NONE:
                none.setValue(true);
                entity.setValue(false);
                branch.setValue(false);
                break;
            case ENTITY:
                none.setValue(false);
                entity.setValue(true);
                branch.setValue(false);
                break;
            case BRANCH:
                none.setValue(false);
                entity.setValue(false);
                branch.setValue(true);
                break;
        }

    }
}