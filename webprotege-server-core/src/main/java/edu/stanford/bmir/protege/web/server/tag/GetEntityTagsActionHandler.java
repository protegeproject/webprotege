package edu.stanford.bmir.protege.web.server.tag;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.tag.GetEntityTagsAction;
import edu.stanford.bmir.protege.web.shared.tag.GetEntityTagsResult;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Mar 2018
 */
public class GetEntityTagsActionHandler extends AbstractProjectActionHandler<GetEntityTagsAction, GetEntityTagsResult> {

    @Nonnull
    private final EntityTagsManager entityTagsManager;

    @Inject
    public GetEntityTagsActionHandler(@Nonnull AccessManager accessManager,
                                      @Nonnull EntityTagsManager entityTagsManager) {
        super(accessManager);
        this.entityTagsManager = checkNotNull(entityTagsManager);
    }

    @Nonnull
    @Override
    public Class<GetEntityTagsAction> getActionClass() {
        return GetEntityTagsAction.class;
    }

    @Nonnull
    @Override
    public GetEntityTagsResult execute(@Nonnull GetEntityTagsAction action, @Nonnull ExecutionContext executionContext) {
        ProjectId projectId = action.getProjectId();
        OWLEntity entity = action.getEntity();
        return new GetEntityTagsResult(entityTagsManager.getTags(entity),
                                       entityTagsManager.getProjectTags());
    }
}
