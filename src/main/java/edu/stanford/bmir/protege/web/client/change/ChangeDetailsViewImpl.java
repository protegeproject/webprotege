package edu.stanford.bmir.protege.web.client.change;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.client.diff.DiffLineElementRenderer;
import edu.stanford.bmir.protege.web.client.diff.DiffSourceDocumentRenderer;
import edu.stanford.bmir.protege.web.client.diff.DiffView;
import edu.stanford.bmir.protege.web.client.ui.library.timelabel.ElapsedTimeLabel;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.Date;
import java.util.List;
import java.util.Set;

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
    protected HasText subjectsField;

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

    @Override
    public void setSubjects(Set<OWLEntityData> subjects) {
        StringBuilder sb = new StringBuilder();
        for(OWLEntityData entityData : subjects) {
            sb.append(entityData.getBrowserText());
            sb.append("  ");
        }
        subjectsField.setText(sb.toString().trim());
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
    public void setChangeCount(int changeCount) {
        changeCountField.setText(Integer.toString(changeCount));
    }

    @Override
    public void setTimestamp(long timestamp) {
        timestampField.setBaseTime(timestamp);
    }
}