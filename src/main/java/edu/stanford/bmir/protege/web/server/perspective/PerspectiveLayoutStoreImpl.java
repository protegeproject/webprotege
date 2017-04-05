package edu.stanford.bmir.protege.web.server.perspective;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveLayout;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.protege.widgetmap.server.node.JsonNodeSerializer;
import edu.stanford.protege.widgetmap.shared.node.Node;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Optional;

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

    @Nonnull
    @Override
    public PerspectiveLayout getPerspectiveLayout(@Nonnull ProjectId projectId, @Nonnull UserId userId, @Nonnull PerspectiveId perspectiveId) {
        File layoutFile = getPerspectiveFile(projectId, userId, perspectiveId);
        return getPerspectiveLayoutFromFile(perspectiveId, layoutFile);
    }

    @Nonnull
    @Override
    public PerspectiveLayout getPerspectiveLayout(@Nonnull ProjectId projectId, @Nonnull PerspectiveId perspectiveId) {
        File layoutFile = getPerspectiveFile(projectId, perspectiveId);
        return getPerspectiveLayoutFromFile(perspectiveId, layoutFile);
    }

    private PerspectiveLayout getPerspectiveLayoutFromFile(PerspectiveId perspectiveId, File layoutFile) {
        try {
            if(layoutFile.exists()) {
                String serialization = Files.toString(layoutFile, Charset.forName("utf-8"));
                Node node = new JsonNodeSerializer().deserialize(serialization);
                return new PerspectiveLayout(perspectiveId, Optional.of(node));
            }
            else {
                return new PerspectiveLayout(perspectiveId, Optional.empty());
            }

        } catch (IOException e) {
            return new PerspectiveLayout(perspectiveId, Optional.empty());
        }
    }

    private File getPerspectiveFile(ProjectId projectId, UserId userId, PerspectiveId perspectiveId) {
        File userFile = perspectiveFileManager.getPerspectiveLayoutForUser(projectId, perspectiveId, userId);
        if(userFile.exists()) {
            return userFile;
        }
        return getPerspectiveFile(projectId, perspectiveId);
    }

    private File getPerspectiveFile(ProjectId projectId, PerspectiveId perspectiveId) {
        File projectFile = perspectiveFileManager.getDefaultPerspectiveLayoutForProject(projectId, perspectiveId);
        if(projectFile.exists()) {
            return projectFile;
        }
        return perspectiveFileManager.getDefaultPerspectiveLayout(perspectiveId);
    }

    @Override
    public void setPerspectiveLayout(@Nonnull ProjectId projectId, @Nonnull UserId userId, @Nonnull PerspectiveLayout layout) {
        File file = perspectiveFileManager.getPerspectiveLayoutForUser(projectId, layout.getPerspectiveId(), userId);
        if(layout.getRootNode().isPresent()) {
            try {
                file.getParentFile().mkdirs();
                JsonNodeSerializer serializer = new JsonNodeSerializer();
                String serialization = serializer.serialize(layout.getRootNode().get());
                Files.write(serialization.getBytes(Charsets.UTF_8), file);
            } catch (IOException e) {
                logger.error(e);
            }
        }
        else {
            file.delete();
        }
    }

    @Override
    public void clearPerspectiveLayout(@Nonnull ProjectId projectId, @Nonnull UserId userId, @Nonnull PerspectiveId perspectiveId) {
        File file = perspectiveFileManager.getPerspectiveLayoutForUser(projectId, perspectiveId, userId);
        if (file.exists()) {
            file.delete();
        }
    }
}
