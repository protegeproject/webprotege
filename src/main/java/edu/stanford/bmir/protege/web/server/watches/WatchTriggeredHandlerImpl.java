package edu.stanford.bmir.protege.web.server.watches;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import edu.stanford.bmir.protege.web.client.place.ItemSelection;
import edu.stanford.bmir.protege.web.server.app.ApplicationHostSupplier;
import edu.stanford.bmir.protege.web.server.mail.SendMail;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.BrowserTextProvider;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlaceTokenizer;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Collections.singletonList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public class WatchTriggeredHandlerImpl implements WatchTriggeredHandler {

    private ExecutorService emailExecutor = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setPriority(Thread.MIN_PRIORITY).build());


    private final ProjectId projectId;

    private final BrowserTextProvider browserTextProvider;

    private final ApplicationHostSupplier applicationHostSupplier;

    private final SendMail mailManager;

    private final UserDetailsManager userDetailsManager;


    @Inject
    public WatchTriggeredHandlerImpl(ProjectId projectId,
                                     BrowserTextProvider browserTextProvider,
                                     ApplicationHostSupplier applicationHostSupplier,
                                     SendMail mailManager,
                                     UserDetailsManager userDetailsManager) {
        this.projectId = projectId;
        this.browserTextProvider = browserTextProvider;
        this.applicationHostSupplier = applicationHostSupplier;
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
                final StringBuilder messageBuilder = new StringBuilder();
                messageBuilder.append("\nChanges were made to " )
                  .append(entity.getEntityType().getName())
                  .append(" " )
                  .append(browserTextProvider.getOWLEntityBrowserText(entity))
                  .append(" " )
                  .append(entity.getIRI().toQuotedString());
                messageBuilder.append(" on " ).append(new Date()).append("\n\n" );
                messageBuilder.append("You can view this " )
                  .append(entity.getEntityType().getName())
                  .append(" at the link below:" );
                StringBuilder directLinkBuilder = new StringBuilder();
                directLinkBuilder.append("http://");
                directLinkBuilder.append(applicationHostSupplier.get());
                ItemSelection selection = ItemSelection.builder().addEntity(entity).build();
                ProjectViewPlace place = new ProjectViewPlace(projectId, new PerspectiveId("Classes"), selection);
                ProjectViewPlaceTokenizer tokenizer = new ProjectViewPlaceTokenizer();
                String placeToken = tokenizer.getToken(place);
                directLinkBuilder.append("#ProjectViewPlace:");
                directLinkBuilder.append(placeToken);
                messageBuilder.append("\n" ).append(directLinkBuilder.toString());
                userDetails.getEmailAddress().ifPresent(address -> mailManager.sendMail(singletonList(address), emailSubject, messageBuilder.toString()));
            }
        });
    }
}
