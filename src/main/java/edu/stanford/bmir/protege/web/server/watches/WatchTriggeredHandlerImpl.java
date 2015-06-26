package edu.stanford.bmir.protege.web.server.watches;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import edu.stanford.bmir.protege.web.server.mail.SendMail;
import edu.stanford.bmir.protege.web.server.metaproject.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.BrowserTextProvider;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.net.URLEncoder;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public class WatchTriggeredHandlerImpl implements WatchTriggeredHandler {

    private ExecutorService emailExecutor = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setPriority(Thread.MIN_PRIORITY).build());


    private final ProjectId projectId;

    private final BrowserTextProvider browserTextProvider;

    private final String applicationHost;

    private final SendMail mailManager;

    private final UserDetailsManager userDetailsManager;


    @Inject
    public WatchTriggeredHandlerImpl(ProjectId projectId, BrowserTextProvider browserTextProvider, String applicationHost, SendMail mailManager, UserDetailsManager userDetailsManager) {
        this.projectId = projectId;
        this.browserTextProvider = browserTextProvider;
        this.applicationHost = applicationHost;
        this.mailManager = mailManager;
        this.userDetailsManager = userDetailsManager;
    }

    @Override
    public void handleWatchTriggered(final Watch<?> watch, final UserId userId, final OWLEntity entity) {
        emailExecutor.submit(new Runnable() {
            @Override
            public void run() {
                Optional<UserDetails> userDetailsOptional = userDetailsManager.getUserDetails(userId);
                if(!userDetailsOptional.isPresent()) {
                    return;
                }
                UserDetails userDetails = userDetailsOptional.get();
                final String displayName = "watched project";
                final String emailSubject = String.format("Changes made in %s by %s", displayName, userDetails.getDisplayName());
                String message = "\nChanges were made to " + entity.getEntityType().getName() + " " + browserTextProvider.getOWLEntityBrowserText(entity) + " " + entity.getIRI().toQuotedString();
                message = message + (" on " + new Date() + "\n\n");
                message = message + "You can view this " + entity.getEntityType().getName() + " at the link below:";
                StringBuilder directLinkBuilder = new StringBuilder();
                directLinkBuilder.append("http://");
                directLinkBuilder.append(applicationHost);
                directLinkBuilder.append("#Edit:projectId=");
                directLinkBuilder.append(projectId.getId());
                directLinkBuilder.append(";tab=ClassesTab&id=");
                directLinkBuilder.append(URLEncoder.encode(entity.getIRI().toString()));
                message += "\n" + directLinkBuilder.toString();
                mailManager.sendMail(userDetails.getEmailAddress().or("Not specified"), emailSubject, message);

            }
        });
    }
}
