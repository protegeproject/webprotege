package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.server.app.ApplicationNameSupplier;
import edu.stanford.bmir.protege.web.server.filemanager.FileContents;
import edu.stanford.bmir.protege.web.server.mail.SendMail;
import edu.stanford.bmir.protege.web.server.place.PlaceUrl;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.server.templates.TemplateEngine;
import edu.stanford.bmir.protege.web.server.templates.TemplateObjectsBuilder;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.singletonList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public class WatchTriggeredHandlerImpl implements WatchTriggeredHandler {

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
    public void handleWatchTriggered(final Watch<?> watch, final UserId userId, final OWLEntity entity) {
        Optional<UserDetails> userDetailsOptional = userDetailsManager.getUserDetails(userId);
        userDetailsOptional.ifPresent(userDetails -> {
            userDetails.getEmailAddress().ifPresent(emailAddress -> {
                Map<String, Object> templateObjects =
                        TemplateObjectsBuilder.builder()
                                              .withUserId(userId)
                                              .withEntity(renderingManager.getRendering(entity))
                                              .withProjectDetails(projectDetailsManager.getProjectDetails(projectId))
                                              .withApplicationName(applicationNameSupplier.get())
                                              .withProjectUrl(placeUrl.getProjectUrl(projectId))
                                              .build();

                String displayName = projectDetailsManager.getProjectDetails(projectId).getDisplayName();
                String emailSubject = String.format("[%s] Changes made in by %s" ,
                                                    displayName,
                                                    userDetails.getDisplayName());
                String emailBody = templateEngine.populateTemplate(watchTemplate.getContents(), templateObjects);
                Thread t = new Thread(() -> {
                    sendMail.sendMail(
                            singletonList(emailAddress),
                            emailSubject, emailBody);
                });
                t.setPriority(Thread.MIN_PRIORITY);
                t.start();
            });
        });
    }
}
