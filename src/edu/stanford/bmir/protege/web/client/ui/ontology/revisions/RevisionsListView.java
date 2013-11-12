package edu.stanford.bmir.protege.web.client.ui.ontology.revisions;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AbstractDataProvider;
import edu.stanford.bmir.protege.web.shared.revision.RevisionSummary;
import edu.stanford.bmir.protege.web.shared.HasDispose;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/04/2013
 */
public interface RevisionsListView extends HasDispose {


    void setDataProvider(AbstractDataProvider<RevisionSummary> dataProvider);

    void setDownloadRevisionRequestHandler(DownloadRevisionRequestHandler handler);

    Widget getWidget();

}
