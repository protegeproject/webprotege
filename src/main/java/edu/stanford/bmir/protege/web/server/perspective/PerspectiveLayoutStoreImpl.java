package edu.stanford.bmir.protege.web.server.perspective;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.io.Files;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveLayout;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.protege.widgetmap.server.node.JsonNodeSerializer;
import edu.stanford.protege.widgetmap.shared.node.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/16
 */
public class PerspectiveLayoutStoreImpl implements PerspectiveLayoutStore {

    private final PerspectiveFileManager perspectiveFileManager;

    private final WebProtegeLogger logger;

    @Inject
    public PerspectiveLayoutStoreImpl(PerspectiveFileManager perspectiveFileManager, WebProtegeLogger logger) {
        this.perspectiveFileManager = perspectiveFileManager;
        this.logger = logger;
    }

    @Override
    public PerspectiveLayout getPerspectiveLayout(ProjectId projectId, UserId userId, PerspectiveId perspectiveId) {
        File layoutFile = getPerspectiveFile(projectId, userId, perspectiveId);
        try {
            if(layoutFile.exists()) {
                String serialization = Files.toString(layoutFile, Charset.forName("utf-8"));
                Node node = new JsonNodeSerializer().deserialize(serialization);
                return new PerspectiveLayout(perspectiveId, Optional.of(node));
            }
            else {
                return new PerspectiveLayout(perspectiveId, Optional.absent());
            }

        } catch (IOException e) {
            return new PerspectiveLayout(perspectiveId, Optional.absent());
        }
    }

    private File getPerspectiveFile(ProjectId projectId, UserId userId, PerspectiveId perspectiveId) {
        File userFile = perspectiveFileManager.getPerspectiveLayoutForUser(projectId, perspectiveId, userId);
        if(userFile.exists()) {
            return userFile;
        }
        File projectFile = perspectiveFileManager.getDefaultPerspectiveLayoutForProject(projectId, perspectiveId);
        if(projectFile.exists()) {
            return projectFile;
        }
        return perspectiveFileManager.getDefaultPerspectiveLayout(perspectiveId);
    }

    @Override
    public void setPerspectiveLayout(ProjectId projectId, UserId userId, PerspectiveLayout layout) {
        File file = getPerspectiveFile(projectId, userId, layout.getPerspectiveId());
        if(layout.getRootNode().isPresent()) {
            try {
                file.getParentFile().mkdirs();
                JsonNodeSerializer serializer = new JsonNodeSerializer();
                String serialization = serializer.serialize(layout.getRootNode().get());
                Files.write(serialization.getBytes(Charsets.UTF_8), file);
            } catch (IOException e) {
                logger.severe(e);
            }
        }
        else {
            file.delete();
        }
    }

    @Override
    public void clearPerspectiveLayout(ProjectId projectId, UserId userId, PerspectiveId perspectiveId) {
        File file = getPerspectiveFile(projectId, userId, perspectiveId);
        file.delete();
    }
}
