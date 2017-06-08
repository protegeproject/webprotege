package edu.stanford.bmir.protege.web.server.webhook;

import com.mongodb.DuplicateKeyException;
import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.webhook.SlackWebhook;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.InsertOptions;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.webhook.SlackWebhook.PROJECT_ID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2017
 */
@ApplicationSingleton
public class SlackWebhookRepositoryImpl implements SlackWebhookRepository {

    private static final Logger logger = LoggerFactory.getLogger(SlackWebhookRepositoryImpl.class);

    private final Datastore datastore;

    @Inject
    public SlackWebhookRepositoryImpl(@Nonnull Datastore datastore) {
        this.datastore = checkNotNull(datastore);
    }

    @Override
    public void ensureIndexes() {
        datastore.ensureIndexes(SlackWebhook.class);
    }

    @Override
    public List<SlackWebhook> getWebhooks(@Nonnull ProjectId projectId) {
        return datastore.find(SlackWebhook.class).field(PROJECT_ID).equal(projectId).asList();
    }

    @Override
    public void clearWebhooks(@Nonnull ProjectId projectId) {
        Query<SlackWebhook> query = datastore.createQuery(SlackWebhook.class).field(PROJECT_ID).equal(projectId);
        datastore.delete(query);
    }

    @Override
    public void addWebhooks(@Nonnull List<SlackWebhook> webhooks) {
        try {
            datastore.save(webhooks, new InsertOptions().continueOnError(true));
        } catch (DuplicateKeyException e) {
            logger.debug("Ignored duplicate webhook", e);
        }
    }
}
