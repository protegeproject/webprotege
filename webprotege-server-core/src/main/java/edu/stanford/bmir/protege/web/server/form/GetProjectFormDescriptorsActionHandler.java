package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.form.GetProjectFormDescriptorsAction;
import edu.stanford.bmir.protege.web.shared.form.GetProjectFormDescriptorsResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-20
 */
public class GetProjectFormDescriptorsActionHandler extends AbstractProjectActionHandler<GetProjectFormDescriptorsAction, GetProjectFormDescriptorsResult> {


    @Nonnull
    private final EntityFormRepository entityFormRepository;

    @Nonnull
    private final EntityFormSelectorRepository selectorRepository;

    @Inject
    public GetProjectFormDescriptorsActionHandler(@Nonnull AccessManager accessManager,
                                                  @Nonnull EntityFormRepository entityFormRepository,
                                                  @Nonnull EntityFormSelectorRepository selectorRepository) {
        super(accessManager);
        this.entityFormRepository = checkNotNull(entityFormRepository);
        this.selectorRepository = checkNotNull(selectorRepository);
    }

    @Nonnull
    @Override
    public Class<GetProjectFormDescriptorsAction> getActionClass() {
        return GetProjectFormDescriptorsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetProjectFormDescriptorsAction action) {
        return BuiltInAction.EDIT_FORMS;
    }

    @Nonnull
    @Override
    public GetProjectFormDescriptorsResult execute(@Nonnull GetProjectFormDescriptorsAction action,
                                                   @Nonnull ExecutionContext executionContext) {
        ProjectId projectId = action.getProjectId();
        var formDescriptors = entityFormRepository.findFormDescriptors(projectId).collect(toImmutableList());
        var selectors = selectorRepository.findFormSelectors(projectId)
                                          .collect(toImmutableList());

        return GetProjectFormDescriptorsResult.get(projectId, formDescriptors, selectors);
    }
}
