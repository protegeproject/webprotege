package edu.stanford.bmir.protege.web.client.change;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.diff.DiffLineElementRenderer;
import edu.stanford.bmir.protege.web.client.diff.DiffSourceDocumentRenderer;
import edu.stanford.bmir.protege.web.client.diff.DiffView;
import edu.stanford.bmir.protege.web.client.ui.library.timelabel.ElapsedTimeLabel;
import edu.stanford.bmir.protege.web.client.user.UserIcon;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/15
 */
public class ChangeDetailsViewImpl extends Composite implements ChangeDetailsView {

    interface ChangeDetailsViewImplUiBinder extends UiBinder<HTMLPanel, ChangeDetailsViewImpl> {
    }

    private static ChangeDetailsViewImplUiBinder ourUiBinder = GWT.create(ChangeDetailsViewImplUiBinder.class);

    public ChangeDetailsViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        revisionField.setVisible(false);
        tooManyChangesMessage.setVisible(true);
    }

    @UiField
    protected SimplePanel authorUserIcon;

    @UiField
    protected HTML subjectsField;

    @UiField
    protected Label revisionField;

    @UiField
    protected HasText highLevelDescriptionField;

    @UiField
    protected HasText authorField;

    @UiField
    protected ElapsedTimeLabel timestampField;

    @UiField
    protected DiffView diffView;

    @UiField
    protected HasText changeCountField;

    @UiField
    protected Label tooManyChangesMessage;

    @UiField
    protected Button revertButton;

    private RevertRevisionHandler revertRevisionHandler = new RevertRevisionHandler() {
        @Override
        public void handleRevertRevision() {
        }
    };

    @Override
    public void setSubjects(Collection<OWLEntityData> subjects) {
        StringBuilder sb = new StringBuilder();
        for(OWLEntityData entityData : subjects) {
            sb.append("<span style=\"padding-left: 5px;\">");
            sb.append(entityData.getBrowserText());
            sb.append(",");
            sb.append("</span>");
        }
        subjectsField.setHTML(sb.toString().trim());
    }

    public void setRevertRevisionHandler(RevertRevisionHandler revertRevisionHandler) {
        this.revertRevisionHandler = checkNotNull(revertRevisionHandler);
    }

    @Override
    public void setRevision(RevisionNumber revision) {
        revisionField.setText("R " + Integer.toString(revision.getValueAsInt()));
        revisionField.setVisible(true);
    }

    @Override
    public void setHighLevelDescription(String description) {
        highLevelDescriptionField.setText(checkNotNull(description));
    }

    @Override
    public void setAuthor(UserId author) {
        authorUserIcon.setWidget(UserIcon.get(author));
        authorField.setText(checkNotNull(author).getUserName());
    }

    @Override
    public void setDiff(List<DiffElement<String, SafeHtml>> diff) {
        diffView.setDiff(diff, new DiffLineElementRenderer<SafeHtml>() {
            @Override
            public SafeHtml getRendering(SafeHtml lineElement) {
                return lineElement;
            }
        }, new DiffSourceDocumentRenderer<String>() {
            @Override
            public SafeHtml renderSourceDocument(String document) {
                return new SafeHtmlBuilder().appendHtmlConstant(document).toSafeHtml();
            }
        });
        tooManyChangesMessage.setVisible(false);
    }

    @Override
    public void setDetailsVisible(boolean detailsVisible) {
        diffView.asWidget().setVisible(detailsVisible);
        subjectsField.setVisible(detailsVisible);
    }

    @Override
    public void setChangeCount(int changeCount) {
        changeCountField.setText(Integer.toString(changeCount));
    }

    @Override
    public void setTimestamp(long timestamp) {
        timestampField.setBaseTime(timestamp);
    }

    @Override
    public void setRevertRevisionVisible(boolean visible) {
        revertButton.setVisible(visible);
    }


    @UiHandler("revertButton")
    protected void handleRevertButtonClicked(ClickEvent clickEvent) {
        revertRevisionHandler.handleRevertRevision();
    }
}