package edu.stanford.bmir.protege.web.client.issues;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.shared.issues.Status;

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
    Label statusLabel;

    private boolean enabled = false;

    private final List<CommentView> commentViews = new ArrayList<>();

    private StatusChangedHandler statusChangedHandler = () -> {};

    @Inject
    public DiscussionThreadViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void clear() {
        commentViewContainer.clear();
        commentViews.clear();
    }

    @Override
    public void setStatus(Status status) {
        statusLabel.setText(status.name());
    }

    @Override
    public void setStatusChangedHandler(StatusChangedHandler handler) {
        this.statusChangedHandler = checkNotNull(handler);
    }

    @Override
    public void addCommentView(CommentView commentView) {
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