package edu.stanford.bmir.protege.web.server.perspective;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.client.perspective.PerspectiveLinkManagerImpl;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/16
 */
public class PerspectivesManagerImpl implements PerspectivesManager {

    private static final Logger logger = LoggerFactory.getLogger(PerspectiveLinkManagerImpl.class);

    private List<PerspectiveId> perspectiveIds = new ArrayList<>(
    );

    private final PerspectiveFileManager perspectiveFileManager;

    @Inject
    public PerspectivesManagerImpl(PerspectiveFileManager perspectiveFileManager) {
        this.perspectiveFileManager = perspectiveFileManager;
    }

    @Override
    public ImmutableList<PerspectiveId> getPerspectives(ProjectId projectId, UserId userId) {
        File userList = perspectiveFileManager.getPerspectiveListForUser(projectId, userId);
        if(userList.exists()) {
            return readPerspectives(userList);
        }
        File projectList = perspectiveFileManager.getDefaultPerspectiveListForProject(projectId);
        if(projectList.exists()) {
            return readPerspectives(projectList);
        }
        File defaultList = perspectiveFileManager.getDefaultPerspectiveList();
        if(defaultList.exists()) {
            return readPerspectives(defaultList);
        }
        return ImmutableList.copyOf(perspectiveIds);
    }

    private ImmutableList<PerspectiveId> readPerspectives(File file) {
        try {
            PerspectiveListSerializer serializer = new PerspectiveListSerializer();
            return ImmutableList.copyOf(new LinkedHashSet<>(serializer.deserializePerspectiveList(file)));
        } catch (IOException e) {
            logger.warn("Error reading perspectives file: {}", file.getAbsoluteFile(), e);
            return ImmutableList.copyOf(perspectiveIds);
        }
    }

    @Override
    public void setPerspectives(ProjectId projectId, UserId userId, List<PerspectiveId> perspectives) {
        File file = perspectiveFileManager.getPerspectiveListForUser(projectId, userId);
        file.getParentFile().mkdirs();
        writePerspectives(file, new ArrayList<>(new LinkedHashSet<>(perspectives)));
    }

    private void writePerspectives(File toFile, List<PerspectiveId> perspectives) {
        try {
            PerspectiveListSerializer serializer = new PerspectiveListSerializer();
            serializer.serializePerspectiveList(perspectives, toFile);
        } catch (IOException e) {
            logger.warn("Error saving perspectives file: {}", toFile.getAbsoluteFile(), e);
        }
    }

}
