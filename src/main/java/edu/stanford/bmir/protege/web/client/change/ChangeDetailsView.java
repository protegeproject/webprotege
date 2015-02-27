package edu.stanford.bmir.protege.web.client.change;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/15
 */
public interface ChangeDetailsView extends IsWidget {

    void setHighLevelDescription(String description);

    void setAuthor(UserId author);

    void setDiff(List<DiffElement<String, SafeHtml>> diff);

    void setTimestamp(long timestamp);
}
