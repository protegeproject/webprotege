package edu.stanford.bmir.protege.web.server.metaproject;

import com.google.common.base.Stopwatch;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.impl.MetaProjectImpl;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 02/03/15
 */
public class PPTest {

    public static void main(String[] args) {
        MetaProject metaProject = new MetaProjectImpl(URI.create("file:/data/webprotege/metaproject/metaproject.pprj"));
        ProjectPermissionsManagerImpl projectPermissionsManager = new ProjectPermissionsManagerImpl(metaProject, new
                ProjectDetailsManagerImpl(metaProject), new ProjectExistsFilter() {
            @Override
            public boolean isProjectPresent(ProjectId projectId) {
                return true;
            }
        });
        for (int i = 0; i < 100; i++) {
            Stopwatch stopwatch = Stopwatch.createStarted();
            projectPermissionsManager.getListableReadableProjects(UserId.getUserId("M Horridge"));
            long time = stopwatch.elapsed(TimeUnit.MILLISECONDS);
            System.out.println("Time: " + time + " ms");
        }
    }
}
