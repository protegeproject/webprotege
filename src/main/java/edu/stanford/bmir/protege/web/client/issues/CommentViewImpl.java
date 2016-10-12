package edu.stanford.bmir.protege.web.client.issues;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.ui.library.timelabel.ElapsedTimeLabel;
import edu.stanford.bmir.protege.web.client.user.UserIcon;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
public class CommentViewImpl extends Composite implements CommentView {

    interface CommentViewImplUiBinder extends UiBinder<HTMLPanel, CommentViewImpl> {

    }

    private static CommentViewImplUiBinder ourUiBinder = GWT.create(CommentViewImplUiBinder.class);

    @UiField
    ElapsedTimeLabel createdAtField;

    @UiField
    Label userNameField;

    @UiField
    AcceptsOneWidget userIconField;

    @UiField
    HTML bodyField;

    @UiField
    Button replyButton;

    @UiField
    Button deleteButton;

    @UiField
    Button editButton;

    @UiField
    ElapsedTimeLabel updatedAtField;

    @UiField
    HTMLPanel updatedAtPanel;

    private ReplyToCommentHandler replyHandler = () -> {};

    private EditCommentHandler editHandler = () -> {};

    private DeleteCommentHandler deleteHandler = () -> {};

    private boolean enabled = false;


    @Inject
    public CommentViewImpl() {
        HTMLPanel ui = ourUiBinder.createAndBindUi(this);
        initWidget(ui);
    }

    @UiHandler("replyButton")
    protected void handleReplyButtonPressed(ClickEvent event) {
        replyHandler.handleReplyToComment();
    }

    @UiHandler("editButton")
    protected void handleEditButtonPressed(ClickEvent event) {
        editHandler.handleEditComment();
    }

    @UiHandler("deleteButton")
    protected void handleDeleteButtonPressed(ClickEvent event) {
        deleteHandler.handleDeleteComment();
    }





    @Override
    public void setCreatedBy(UserId createdBy) {
        userIconField.setWidget(UserIcon.get(createdBy));
        userNameField.setText(createdBy.getUserName());
    }

    @Override
    public Optional<UserId> getCreatedBy() {
        if(userNameField.getText().trim().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(UserId.getUserId(userNameField.getText().trim()));
    }

    @Override
    public void setCreatedAt(long timestamp) {
        createdAtField.setBaseTime(timestamp);
    }

    @Override
    public void setUpdatedAt(Optional<Long> updatedAt) {
        if(updatedAt.isPresent()) {
            updatedAtField.setBaseTime(updatedAt.get());
            updatedAtPanel.setVisible(true);
        }
        else {
            updatedAtPanel.setVisible(false);
        }
    }

    @Override
    public void setBody(String body) {
        bodyField.setText(body);
        bodyField.setHTML(new SafeHtmlBuilder().appendHtmlConstant(body).toSafeHtml());
    }

    @Override
    public void setReplyToCommentHandler(ReplyToCommentHandler handler) {
        this.replyHandler = checkNotNull(handler);
    }

    @Override
    public void setEditCommentHandler(EditCommentHandler handler) {
        this.editHandler = checkNotNull(handler);
    }

    @Override
    public void setDeleteCommentHandler(DeleteCommentHandler handler) {
        this.deleteHandler = checkNotNull(handler);
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
     * @param enabled <code>true</code> to enable the widget, <code>false</code>
     *                to disable it
     */
    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        replyButton.setEnabled(enabled);
    }

//    @Override
//    public void setDeleteButtonVisible(boolean visible) {
//        deleteButton.setVisible(visible);
//    }

    @Override
    public void setEditButtonVisible(boolean visible) {
        editButton.setVisible(visible);
    }

    @Override
    public void setReplyButtonVisible(boolean visible) {
        replyButton.setVisible(visible);
    }
}