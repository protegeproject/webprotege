package edu.stanford.bmir.protege.web.client.search;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchFilter;
import edu.stanford.bmir.protege.web.shared.search.GetSearchSettingsAction;
import edu.stanford.bmir.protege.web.shared.search.SetSearchSettingsAction;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-16
 */
public class EntitySearchSettingsService {

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final ProjectId projectId;

    @Inject
    public EntitySearchSettingsService(DispatchServiceManager dispatch, @Nonnull ProjectId projectId) {
        this.dispatch = checkNotNull(dispatch);
        this.projectId = checkNotNull(projectId);
    }

    public void getFilters(Consumer<ImmutableList<EntitySearchFilter>> filters) {
        dispatch.execute(new GetSearchSettingsAction(projectId),
                         result -> filters.accept(result.getFilters()));
    }

    public void setFilters(@Nonnull ImmutableList<EntitySearchFilter> filters) {
        dispatch.execute(new SetSearchSettingsAction(projectId,
                                                     ImmutableList.of(),
                                                     filters),
                         result -> {});
    }
}
