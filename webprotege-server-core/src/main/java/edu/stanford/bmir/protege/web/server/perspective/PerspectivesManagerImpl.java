package edu.stanford.bmir.protege.web.server.perspective;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.perspective.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/16
 */
public class PerspectivesManagerImpl implements PerspectivesManager {

    @Nonnull
    private final ImmutableList<BuiltInPerspective> builtInPerspectives;

    @Nonnull
    private final PerspectiveDescriptorRepository descriptorsRepository;

    @Nonnull
    private final PerspectiveLayoutRepository layoutsRepository;

    @Inject
    public PerspectivesManagerImpl(@Nonnull ImmutableList<BuiltInPerspective> builtInPerspectives,
                                   @Nonnull PerspectiveDescriptorRepository descriptorsRepository,
                                   @Nonnull PerspectiveLayoutRepository layoutsRepository) {
        this.builtInPerspectives = checkNotNull(builtInPerspectives);
        this.descriptorsRepository = checkNotNull(descriptorsRepository);
        this.layoutsRepository = checkNotNull(layoutsRepository);
    }

    @Nonnull
    @Override
    public ImmutableList<PerspectiveDescriptor> getPerspectives(@Nonnull ProjectId projectId, @Nonnull UserId userId) {
        var projectUserPerspectives = descriptorsRepository.findDescriptors(projectId, userId);
        if (projectUserPerspectives.isPresent()) {
            return projectUserPerspectives.get().getPerspectives();
        }
        var projectPerspectives = descriptorsRepository.findDescriptors(projectId);
        if (projectPerspectives.isPresent()) {
            return projectPerspectives.get().getPerspectives();
        }
        var defaultProjectPerspectives = descriptorsRepository.findDescriptors();
        if (defaultProjectPerspectives.isPresent()) {
            return defaultProjectPerspectives.get().getPerspectives();
        }
        return builtInPerspectives.stream()
                                  .map(builtInPerspective -> PerspectiveDescriptor.get(builtInPerspective.getPerspectiveId(),
                                                                                       builtInPerspective.getLabel(),
                                                                                       builtInPerspective.isFavorite()))
                                  .collect(toImmutableList());
    }


    @Override
    public void setPerspectives(@Nonnull ImmutableList<PerspectiveDescriptor> perspectives) {
        var record = PerspectiveDescriptorsRecord.get(perspectives);
        descriptorsRepository.saveDescriptors(record);
    }

    @Override
    public void setPerspectives(@Nonnull ProjectId projectId,
                                @Nonnull ImmutableList<PerspectiveDescriptor> perspectives) {
        var record = PerspectiveDescriptorsRecord.get(projectId, perspectives);
        descriptorsRepository.saveDescriptors(record);
    }


    @Override
    public void setPerspectives(@Nonnull ProjectId projectId,
                                @Nonnull UserId userId,
                                @Nonnull ImmutableList<PerspectiveDescriptor> perspectives) {
        var record = PerspectiveDescriptorsRecord.get(projectId, userId, perspectives);
        descriptorsRepository.saveDescriptors(record);
    }

    @Nonnull
    @Override
    public PerspectiveLayout getPerspectiveLayout(@Nonnull ProjectId projectId,
                                                  @Nonnull UserId userId,
                                                  @Nonnull PerspectiveId perspectiveId) {
        var userProjectLayout = layoutsRepository.findLayout(projectId, userId, perspectiveId);
        if(userProjectLayout.isPresent()) {
            return userProjectLayout.get().toPerspectiveLayout();
        }
        var projectLayout = layoutsRepository.findLayout(projectId, perspectiveId);
        if(projectLayout.isPresent()) {
            return projectLayout.get().toPerspectiveLayout();
        }
        var defaultLayout = layoutsRepository.findLayout(perspectiveId);
        if(defaultLayout.isPresent()) {
            return defaultLayout.get().toPerspectiveLayout();
        }
        var builtInLayout = builtInPerspectives.stream()
                           .filter(builtInPerspective -> builtInPerspective.getPerspectiveId().equals(perspectiveId))
                           .map(BuiltInPerspective::getLayout)
                           .findFirst();
        if(builtInLayout.isPresent()) {
            return PerspectiveLayout.get(perspectiveId, builtInLayout.get());
        }
        return PerspectiveLayout.get(perspectiveId);
    }

    @Override
    public void savePerspectiveLayout(@Nonnull ProjectId projectId,
                                      @Nonnull UserId userId,
                                      @Nonnull PerspectiveLayout layout) {
        var record = PerspectiveLayoutRecord.get(projectId, userId, layout.getPerspectiveId(), layout.getLayout().orElse(null));
        layoutsRepository.saveLayout(record);
    }

    @Override
    public void savePerspectiveLayout(@Nonnull ProjectId projectId, @Nonnull PerspectiveLayout layout) {
        var record = PerspectiveLayoutRecord.get(projectId, null, layout.getPerspectiveId(), layout.getLayout().orElse(null));
        layoutsRepository.saveLayout(record);
    }

    @Override
    public void savePerspectiveLayout(@Nonnull PerspectiveLayout layout) {
        var record = PerspectiveLayoutRecord.get(null, null, layout.getPerspectiveId(), layout.getLayout().orElse(null));
        layoutsRepository.saveLayout(record);
    }

    @Override
    public void resetPerspectiveLayout(@Nonnull ProjectId projectId,
                                       @Nonnull UserId userId,
                                       @Nonnull PerspectiveId perspectiveId) {
        layoutsRepository.dropLayout(projectId, userId, perspectiveId);
    }
}
