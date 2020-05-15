package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.app.ApplicationNameSupplier;
import edu.stanford.bmir.protege.web.server.app.PlaceUrl;
import edu.stanford.bmir.protege.web.server.filemanager.FileContents;
import edu.stanford.bmir.protege.web.server.mail.MessageHeader;
import edu.stanford.bmir.protege.web.server.mail.SendMail;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.server.templates.TemplateEngine;
import edu.stanford.bmir.protege.web.server.templates.TemplateObjectsBuilder;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static edu.stanford.bmir.protege.web.server.access.ProjectResource.forProject;
import static edu.stanford.bmir.protege.web.server.access.Subject.forUser;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public class WatchTriggeredHandlerImpl implements WatchTriggeredHandler {

    private final static Logger logger = LoggerFactory.getLogger(WatchTriggeredHandler.class);

    private final ProjectId projectId;

    private final RenderingManager renderingManager;

    private final ApplicationNameSupplier applicationNameSupplier;

    private final PlaceUrl placeUrl;

    private final SendMail sendMail;

    private final UserDetailsManager userDetailsManager;

    private final ProjectDetailsManager projectDetailsManager;

    private final AccessManager accessManager;

    private final TemplateEngine templateEngine;

    private final FileContents watchTemplate;

    @Inject
    public WatchTriggeredHandlerImpl(ProjectId projectId,
                                     RenderingManager renderingManager,
                                     ApplicationNameSupplier applicationNameSupplier,
                                     AccessManager accessManager,
                                     PlaceUrl placeUrl,
                                     SendMail sendMail,
                                     UserDetailsManager userDetailsManager,
                                     ProjectDetailsManager projectDetailsManager,
                                     TemplateEngine templateEngine,
                                     @WatchNotificationEmailTemplate FileContents watchTemplate) {
        this.projectId = projectId;
        this.renderingManager = renderingManager;
        this.applicationNameSupplier = applicationNameSupplier;
        this.accessManager = accessManager;
        this.placeUrl = placeUrl;
        this.sendMail = sendMail;
        this.userDetailsManager = userDetailsManager;
        this.projectDetailsManager = projectDetailsManager;
        this.templateEngine = templateEngine;
        this.watchTemplate = watchTemplate;
    }

    @Override
    public void handleWatchTriggered(@Nonnull Set<UserId> usersToNotify,
                                     @Nonnull OWLEntity modifiedEntity,
                                     @Nonnull UserId byUser) {
        logger.info("{} [WatchTriggeredHandlerImpl] Handling watch triggered for {} by {}, notifying {}",
                    projectId,
                    modifiedEntity,
                    byUser);
        List<String> emailAddresses = usersToNotify.stream()
                                                   // The user should have view permissions to be notified
                                                   .filter(u -> accessManager.hasPermission(forUser(u),
                                                                                            forProject(projectId),
                                                                                            VIEW_PROJECT))
                                                   .map(userDetailsManager::getEmail)
                                                   .filter(Optional::isPresent)
                                                   .map(Optional::get)
                                                   .distinct()
                                                   .collect(toList());
        OWLEntityData modifiedEntityData = renderingManager.getRendering(modifiedEntity);
        Map<String, Object> templateObjects =
                TemplateObjectsBuilder.builder()
                                      .withUserId(byUser)
                                      .withEntity(modifiedEntityData)
                                      .withEntityUrl(placeUrl.getEntityUrl(projectId, modifiedEntity))
                                      .withProjectDetails(projectDetailsManager.getProjectDetails(projectId))
                                      .withApplicationName(applicationNameSupplier.get())
                                      .withProjectUrl(placeUrl.getProjectUrl(projectId))
                                      .build();

        String displayName = projectDetailsManager.getProjectDetails(projectId).getDisplayName();
        String emailSubject = String.format("[%s] Changes made to %s in %s by %s",
                                            displayName,
                                            renderingManager.getShortForm(modifiedEntity),
                                            displayName,
                                            userDetailsManager.getUserDetails(byUser).map(d -> "by " + d.getDisplayName()).orElse(""));
        String emailBody = templateEngine.populateTemplate(watchTemplate.getContents(), templateObjects);
        logger.info("{} Watch triggered by {} on {}.  Notifying {}", projectId, byUser, modifiedEntity, usersToNotify);
        sendMail.sendMail(emailAddresses, emailSubject, emailBody,
                          MessageHeader.inReplyTo(projectId.getId()),
                          MessageHeader.references(projectId.getId()));
    }
}
