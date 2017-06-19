package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.client.project.NewProjectSettings;
import edu.stanford.bmir.protege.web.server.webhook.SlackWebhookRepository;
import edu.stanford.bmir.protege.web.server.webhook.WebhookRepository;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.project.UnknownProjectException;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettings;
import edu.stanford.bmir.protege.web.shared.projectsettings.SlackIntegrationSettings;
import edu.stanford.bmir.protege.web.shared.projectsettings.WebhookSetting;
import edu.stanford.bmir.protege.web.shared.projectsettings.WebhookSettings;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.webhook.ProjectWebhook;
import edu.stanford.bmir.protege.web.shared.webhook.ProjectWebhookEventType;
import edu.stanford.bmir.protege.web.shared.webhook.SlackWebhook;

import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class ProjectDetailsManagerImpl implements ProjectDetailsManager {

    private final ProjectDetailsRepository repository;

    private final SlackWebhookRepository slackWebhookRepository;

    private final WebhookRepository webhookRepository;

    @Inject
    public ProjectDetailsManagerImpl(ProjectDetailsRepository repository,
                                     SlackWebhookRepository slackWebhookRepository,
                                     WebhookRepository webhookRepository) {
        this.repository = checkNotNull(repository);
        this.webhookRepository = checkNotNull(webhookRepository);
        this.slackWebhookRepository = checkNotNull(slackWebhookRepository);
    }

    @Override
    public void registerProject(ProjectId projectId, NewProjectSettings settings) {
        long now = System.currentTimeMillis();
        ProjectDetails record = new ProjectDetails(
                projectId,
                settings.getDisplayName(),
                settings.getProjectDescription(),
                settings.getProjectOwner(),
                false,
                now,
                settings.getProjectOwner(),
                now,
                settings.getProjectOwner());
        repository.save(record);
    }

    @Override
    public ProjectDetails getProjectDetails(ProjectId projectId) throws UnknownProjectException {
        Optional<ProjectDetails> record = repository.findOne(projectId);
        if(!record.isPresent()) {
            throw new UnknownProjectException(projectId);
        }
        return record.get();
    }

    @Override
    public boolean isExistingProject(ProjectId projectId) {
        return repository.containsProject(projectId);
    }

    @Override
    public boolean isProjectOwner(UserId userId, ProjectId projectId) {
        return !userId.isGuest() && repository.containsProjectWithOwner(projectId, userId);
    }

    @Override
    public void setInTrash(ProjectId projectId, boolean b) {
        repository.setInTrash(projectId, b);
    }

    @Override
    public void setProjectSettings(ProjectSettings projectSettings) {
        ProjectId projectId = projectSettings.getProjectId();
        Optional<ProjectDetails> record = repository.findOne(projectId);
        if(!record.isPresent()) {
            return;
        }
        ProjectDetails updatedRecord = record.get().builder()
                .setDisplayName(projectSettings.getProjectDisplayName())
                .setDescription(projectSettings.getProjectDescription())
                .build();
        repository.save(updatedRecord);
        slackWebhookRepository.clearWebhooks(projectId);
        String payloadUrl = projectSettings.getSlackIntegrationSettings().getPayloadUrl();
        if(!payloadUrl.isEmpty()) {
            slackWebhookRepository.addWebhooks(Collections.singletonList(new SlackWebhook(projectId, payloadUrl)));
        }
        webhookRepository.clearProjectWebhooks(projectId);
        List<ProjectWebhook> projectWebhooks = projectSettings.getWebhookSettings().getWebhookSettings().stream()
                                                      .map(s -> new ProjectWebhook(projectId,
                                                                                   s.getPayloadUrl(),
                                                                                   new ArrayList<ProjectWebhookEventType>(
                                                                                           s.getEventTypes())))
                                                      .collect(toList());
        webhookRepository.addProjectWebhooks(projectWebhooks);

    }

    @Override
    public ProjectSettings getProjectSettings(ProjectId projectId) throws UnknownProjectException {
        List<SlackWebhook> projectWebhooks = slackWebhookRepository.getWebhooks(projectId);
        String slackPayloadUrl = projectWebhooks.stream()
                                                .findFirst()
                                                .map(SlackWebhook::getPayloadUrl).orElse("");
        List<WebhookSetting> webhookSettings = webhookRepository.getProjectWebhooks(projectId).stream()
                                                               .map(wh -> new WebhookSetting(wh.getPayloadUrl(), new LinkedHashSet<ProjectWebhookEventType>(wh.getSubscribedToEvents())))
                                                               .collect(toList());
        ProjectDetails projectDetails = getProjectDetails(projectId);
            return new ProjectSettings(projectId,
                    projectDetails.getDisplayName(),
                    projectDetails.getDescription(),
                                       new SlackIntegrationSettings(slackPayloadUrl),
                                       new WebhookSettings(webhookSettings));
    }

}
