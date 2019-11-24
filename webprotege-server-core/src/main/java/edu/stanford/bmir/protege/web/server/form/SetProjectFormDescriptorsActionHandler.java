package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.form.SetProjectFormDescriptorsAction;
import edu.stanford.bmir.protege.web.shared.form.SetProjectFormDescriptorsResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-23
 */
public class SetProjectFormDescriptorsActionHandler extends AbstractProjectActionHandler<SetProjectFormDescriptorsAction, SetProjectFormDescriptorsResult> {

    @Nonnull
    private final EntityFormRepository entityFormRepository;

    @Inject
    public SetProjectFormDescriptorsActionHandler(@Nonnull AccessManager accessManager,
                                                  @Nonnull EntityFormRepository entityFormRepository) {
        super(accessManager);
        this.entityFormRepository = checkNotNull(entityFormRepository);
    }

    @Nonnull
    @Override
    public Class<SetProjectFormDescriptorsAction> getActionClass() {
        return SetProjectFormDescriptorsAction.class;
    }

    @Nonnull
    @Override
    public SetProjectFormDescriptorsResult execute(@Nonnull SetProjectFormDescriptorsAction action,
                                                   @Nonnull ExecutionContext executionContext) {
        entityFormRepository.setProjectFormDescriptors(action.getProjectId(),
                                                action.getFormDescriptors());
        return new SetProjectFormDescriptorsResult();
    }
}
