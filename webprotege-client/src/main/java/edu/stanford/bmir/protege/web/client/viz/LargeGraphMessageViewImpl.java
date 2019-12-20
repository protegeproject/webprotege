package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Oct 2018
 */
public class LargeGraphMessageViewImpl extends Composite implements LargeGraphMessageView {

    interface LargeGraphMessageViewImplUiBinder extends UiBinder<HTMLPanel, LargeGraphMessageViewImpl> {

    }

    private static LargeGraphMessageViewImplUiBinder ourUiBinder = GWT.create(LargeGraphMessageViewImplUiBinder.class);

    @UiField
    Button displayButton;

    @UiField
    Label messageField;

    @UiField
    FlowPanel edgeList;

    @Inject
    public LargeGraphMessageViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    private Runnable displayGraphHandler = () -> {};

    @Nonnull
    private Runnable displaySettingsHandler = () -> {};

    @UiHandler("displayButton")
    public void displayButtonClick(ClickEvent event) {
        displayGraphHandler.run();
    }

    @Override
    public void setDisplayMessage(@Nonnull OWLEntityData entity, int nodeCount, int edgeCount) {
        messageField.setText("The entity graph for " + entity.getBrowserText() + " is large, with " + nodeCount + " nodes and " + edgeCount + " edges.  It may take a while to layout and display.");
        edgeList.clear();
    }

    @Override
    public void setDisplayGraphHandler(@Nonnull Runnable handler) {
        this.displayGraphHandler = checkNotNull(handler);
    }

    @Override
    public void setDisplaySettingsHandler(@Nonnull Runnable handler) {
        this.displaySettingsHandler = checkNotNull(handler);
    }
}
