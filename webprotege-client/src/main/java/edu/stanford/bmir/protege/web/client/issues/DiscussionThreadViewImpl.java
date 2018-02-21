package edu.stanford.bmir.protege.web.client.issues;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.issues.Status;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
public class DiscussionThreadViewImpl extends Composite implements DiscussionThreadView {

    interface DiscussionThreadViewImplUiBinder extends UiBinder<HTMLPanel, DiscussionThreadViewImpl> {

    }

    private static DiscussionThreadViewImplUiBinder ourUiBinder = GWT.create(DiscussionThreadViewImplUiBinder.class);

    @UiField
    FlowPanel commentViewContainer;

    @UiField
    Button statusLabel;

    private boolean enabled = false;

    private final List<CommentView> commentViews = new ArrayList<>();

    private StatusChangedHandler statusChangedHandler = () -> {};

    private final WebProtegeClientBundle clientBundle;

    private final Messages messages;

    private Status status = Status.OPEN;

    @Inject
    public DiscussionThreadViewImpl(Messages messages, WebProtegeClientBundle clientBundle) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.clientBundle = clientBundle;
        this.messages = messages;
    }

    @UiHandler("statusLabel")
    protected void handleStatusLabelClicked(ClickEvent event) {
        statusChangedHandler.handleStatusChanged();
    }

    @Override
    public void clear() {
        commentViewContainer.clear();
        commentViews.clear();
    }

    @Override
    public void setStatus(@Nonnull Status status) {
        this.statusLabel.setVisible(true);
        this.status = checkNotNull(status);
        if(status == Status.OPEN) {
            statusLabel.setText(messages.discussionThread_Resolve());
            statusLabel.addStyleName(clientBundle.discussion().discussionThreadStatusOpen());
            statusLabel.removeStyleName(clientBundle.discussion().discussionThreadStatusClosed());
        }
        else {
            statusLabel.setText(messages.discussionThread_Reopen());
            statusLabel.addStyleName(clientBundle.discussion().discussionThreadStatusClosed());
            statusLabel.removeStyleName(clientBundle.discussion().discussionThreadStatusOpen());
        }
    }

    @Nonnull
    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setStatusVisible(boolean visible) {
        statusLabel.setVisible(visible);
    }

    @Override
    public void setStatusChangedHandler(@Nonnull StatusChangedHandler handler) {
        this.statusChangedHandler = checkNotNull(handler);
    }

    @Override
    public void addCommentView(@Nonnull CommentView commentView) {
        commentViewContainer.add(commentView);
    }

    /**
     * Returns true if the widget is enabled, false if not.
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets whether this widget is enabled.
     *
     * @param b <code>true</code> to enable the widget, <code>false</code>
     *                to disable it
     */
    @Override
    public void setEnabled(boolean b) {
        this.enabled = b;
        commentViews.forEach(v -> v.setEnabled(b));
    }
}