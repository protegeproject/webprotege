package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/03/2014
 */
public class ApplyChangesActionViewImpl extends Composite implements ApplyChangesActionView  {

    interface ApplyChangesActionViewImplUiBinder extends UiBinder<HTMLPanel, ApplyChangesActionViewImpl> {
    }

    private static ApplyChangesActionViewImplUiBinder ourUiBinder = GWT.create(ApplyChangesActionViewImplUiBinder.class);

    private ApplyChangesActionHandler handler = new ApplyChangesActionHandler() {
        @Override
        public void handleApplyChanges() {

        }

        @Override
        public void handleApplyChangesWithCommitMessage() {

        }
    };

    @UiField
    protected HasClickHandlers applyChanges;

    @UiField
    protected HasClickHandlers applyChangesWithCommitMessage;

    public ApplyChangesActionViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @UiHandler("applyChanges")
    protected void handleApplyChanges(ClickEvent event) {
        handler.handleApplyChanges();
    }

    @UiHandler("applyChangesWithCommitMessage")
    protected void handleApplyChangesWithCommitMessage(ClickEvent event) {
        handler.handleApplyChangesWithCommitMessage();
    }

    @Override
    public void setHandler(ApplyChangesActionHandler handler) {
        this.handler = checkNotNull(handler);
    }
}
