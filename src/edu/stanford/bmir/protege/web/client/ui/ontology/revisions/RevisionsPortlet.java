package edu.stanford.bmir.protege.web.client.ui.ontology.revisions;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;

import java.util.Collection;
import java.util.Collections;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/10/2012
 */
public class RevisionsPortlet extends AbstractEntityPortlet {

    public static final int INITIAL_HEIGHT = 400;

    private RevisionsList revisionsList;

    public RevisionsPortlet(Project project) {
        super(project);
    }


    @Override
    public void reload() {
//        revisionsList.reload();
    }

    @Override
    public void initialize() {
        setHeight(INITIAL_HEIGHT);
        GlobalSettings globalSettings = GlobalSettings.getGlobalSettings();
        UserData userData = globalSettings.getUser();
        if(userData == null) {
            userData = new UserData("");
            userData.setEmail("");
        }
        revisionsList = new RevisionsList(getProjectId(), userData);
        add(revisionsList);
        setTitle("Revisions");
    }

    private ProjectId getProjectId() {
        return new ProjectId(projectName());
    }

    private String projectName() {
        return getProject().getProjectName();
    }

    public Collection<EntityData> getSelection() {
        return Collections.emptyList();
    }
}
