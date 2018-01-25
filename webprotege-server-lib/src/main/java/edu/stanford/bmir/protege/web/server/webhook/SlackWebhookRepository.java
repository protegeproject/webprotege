package edu.stanford.bmir.protege.web.server.webhook;

import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.webhook.SlackWebhook;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2017
 */
public interface SlackWebhookRepository extends Repository {

    List<SlackWebhook> getWebhooks(@Nonnull ProjectId projectId);

    void clearWebhooks(@Nonnull ProjectId projectId);

    void addWebhooks(@Nonnull List<SlackWebhook> webhooks);
}
