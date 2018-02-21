package edu.stanford.bmir.protege.web.client.change;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.Collection;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/15
 */
public interface ChangeDetailsView extends IsWidget {

    void setRevision(RevisionNumber revision);

    void setSubjects(Collection<OWLEntityData> subjects);

    void setHighLevelDescription(String description);

    void setAuthor(UserId author);

    void setDiff(List<DiffElement<String, SafeHtml>> diff, int totalChanges);

    void setTimestamp(long timestamp);

    void setChangeCount(int changeCount);

    void setDetailsVisible(boolean detailsVisible);

    void setRevertRevisionVisible(boolean visible);

    void setRevertRevisionHandler(RevertRevisionHandler revertRevisionHandler);

    void setDownloadRevisionVisible(boolean visible);

    void setDownloadRevisionHandler(DownloadRevisionHandler downloadRevisionHandler);
}
