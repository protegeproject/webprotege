package edu.stanford.bmir.protege.web.server.perspective;

import com.google.common.io.Files;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveLayout;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.protege.widgetmap.server.node.JsonNodeSerializer;
import edu.stanford.protege.widgetmap.shared.node.*;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/16
 */
public class PerspectiveLayoutStoreImpl implements PerspectiveLayoutStore {

    private final PerspectiveFileManager perspectiveFileManager;

    @Inject
    public PerspectiveLayoutStoreImpl(PerspectiveFileManager perspectiveFileManager) {
        this.perspectiveFileManager = perspectiveFileManager;
    }

    @Override
    public PerspectiveLayout getPerspectiveLayout(ProjectId projectId, UserId userId, PerspectiveId perspectiveId) {
        File layoutFile = getPerspectiveFile(projectId, userId, perspectiveId);
        try {
            Node node;
            if(layoutFile.exists()) {
                String s = Files.toString(layoutFile, Charset.forName("utf-8"));
                node = new JsonNodeSerializer().deserialize(s);
                return new PerspectiveLayout(perspectiveId, node);
            }
            else {
                node = new ParentNode();
            }
            return new PerspectiveLayout(perspectiveId, node);
        } catch (IOException e) {
            return new PerspectiveLayout(perspectiveId, new ParentNode());
        }
    }

    private File getPerspectiveFile(ProjectId projectId, UserId userId, PerspectiveId perspectiveId) {
        File userFile = perspectiveFileManager.getPerspectiveFileForUser(projectId, perspectiveId, userId);
        if(userFile.exists()) {
            return userFile;
        }
        File projectFile = perspectiveFileManager.getDefaultPerspectiveFileForProject(projectId, perspectiveId);
        if(projectFile.exists()) {
            return projectFile;
        }
        return perspectiveFileManager.getDefaultPerspectiveFile(perspectiveId);
    }

    @Override
    public void setPerspectiveLayout(ProjectId projectId, UserId userId, PerspectiveLayout layout) {

    }

    @Override
    public void clearPerspectiveLayout(ProjectId projectId, UserId userId) {

    }
}
