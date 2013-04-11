package edu.stanford.bmir.protege.web.client.ui.ontology.revisions;

import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.rpc.data.*;
import edu.stanford.bmir.protege.web.client.ui.bioportal.publish.PublishToBioPortalDialog;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Date;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/10/2012
 */
public class RevisionSummaryPanel extends FlowPanel {

    public static final DateTimeFormat DATE_FORMAT = DateTimeFormat.getFormat("EEEE, d MMMM yyyy 'at' HH:mm:ss (zzzz)");

    private RevisionSummary revisionSummary;
    
    private ProjectId projectId;

//    private UserId userData;

    public RevisionSummaryPanel(ProjectId projectId, RevisionSummary revisionSummary) {
        this.projectId = projectId;
        this.revisionSummary = revisionSummary;
        long revisionNumber = revisionSummary.getRevisionNumber().getValue();
        InlineLabel revisionNumberLabel = new InlineLabel("Revision: " + revisionNumber);
        revisionNumberLabel.addStyleName("webprotege-revision-summary-revision-number-style");
        add(revisionNumberLabel);
        String encodedProjectName = URL.encodeQueryString(projectId.getId());
        add(new Anchor(" [Download]", "webprotege/download?ontology=" + encodedProjectName + "&revision=" + revisionNumber));

        
        Date date = new Date(revisionSummary.getTimestamp());
        add(new Label(DATE_FORMAT.format(date)));


        Label userIdLabel = new InlineLabel(revisionSummary.getUserId().getUserName());
        userIdLabel.addStyleName("webprotege-revision-summary-userid-style");
        add(userIdLabel);



        add(new InlineLabel(" (" + revisionSummary.getChangeCount() + " changes)"));
        addStyleName("webprotege-revision-summary-style");
    }
    


//    private void uploadToBioPortal(ProjectData projectData) {
//        PublishToBioPortalDialog dlg = new PublishToBioPortalDialog(projectData, userData);
//        dlg.show();
//    }
}
