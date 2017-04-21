package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.server.app.ApplicationNameSupplier;
import edu.stanford.bmir.protege.web.server.filemanager.FileContents;
import edu.stanford.bmir.protege.web.server.mail.MessageHeader;
import edu.stanford.bmir.protege.web.server.mail.SendMail;
import edu.stanford.bmir.protege.web.server.place.PlaceUrl;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.server.templates.TemplateEngine;
import edu.stanford.bmir.protege.web.server.templates.TemplateObjectsBuilder;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
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

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

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

    private final TemplateEngine templateEngine;

    private final FileContents watchTemplate;

    @Inject
    public WatchTriggeredHandlerImpl(ProjectId projectId,
                                     RenderingManager renderingManager,
                                     ApplicationNameSupplier applicationNameSupplier,
                                     PlaceUrl placeUrl,
                                     SendMail sendMail,
                                     UserDetailsManager userDetailsManager,
                                     ProjectDetailsManager projectDetailsManager,
                                     TemplateEngine templateEngine,
                                     @WatchNotificationEmailTemplate FileContents watchTemplate) {
        this.projectId = projectId;
        this.renderingManager = renderingManager;
        this.applicationNameSupplier = applicationNameSupplier;
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
        List<String> emailAddresses = usersToNotify.stream()
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
                                      .withProjectDetails(projectDetailsManager.getProjectDetails(projectId))
                                      .withApplicationName(applicationNameSupplier.get())
                                      .withProjectUrl(placeUrl.getProjectUrl(projectId))
                                      .build();

        String displayName = projectDetailsManager.getProjectDetails(projectId).getDisplayName();
        String emailSubject = String.format("[%s] Changes made to %s in %s by %s",
                                            displayName,
                                            modifiedEntityData.getBrowserText(),
                                            displayName,
                                            userDetailsManager.getUserDetails(byUser).map(d -> "by " + d.getDisplayName()).orElse(""));
        String emailBody = templateEngine.populateTemplate(watchTemplate.getContents(), templateObjects);
        logger.info("{} Watch triggered by {} on {}.  Notifying {}", projectId, byUser, modifiedEntity, usersToNotify);
        sendMail.sendMail(emailAddresses, emailSubject, emailBody,
                          MessageHeader.inReplyTo(projectId.getId()),
                          MessageHeader.references(projectId.getId()));
    }
}
