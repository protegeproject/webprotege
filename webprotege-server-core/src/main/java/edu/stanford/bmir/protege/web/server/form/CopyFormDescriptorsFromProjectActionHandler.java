package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.form.CopyFormDescriptorsFromProjectAction;
import edu.stanford.bmir.protege.web.shared.form.CopyFormDescriptorsFromProjectResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-14
 */
public class CopyFormDescriptorsFromProjectActionHandler extends AbstractProjectActionHandler<CopyFormDescriptorsFromProjectAction, CopyFormDescriptorsFromProjectResult> {

    @Nonnull
    private final FormsCopierFactory formsCopierFactory;

    @Inject
    public CopyFormDescriptorsFromProjectActionHandler(@Nonnull AccessManager accessManager,
                                                       @Nonnull FormsCopierFactory formsCopierFactory) {
        super(accessManager);
        this.formsCopierFactory = checkNotNull(formsCopierFactory);
    }

    @Nonnull
    @Override
    public Class<CopyFormDescriptorsFromProjectAction> getActionClass() {
        return CopyFormDescriptorsFromProjectAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(CopyFormDescriptorsFromProjectAction action) {
        return BuiltInAction.EDIT_FORMS;
    }

    @Nonnull
    @Override
    public CopyFormDescriptorsFromProjectResult execute(@Nonnull CopyFormDescriptorsFromProjectAction action,
                                                        @Nonnull ExecutionContext executionContext) {
        var copyFromProjectId = action.getProjectIdToCopyFrom();
        var copyToProjectId = action.getProjectId();
        var formsToCopy = action.getFormIdsToCopy();
        var copier = formsCopierFactory.create(copyFromProjectId, copyToProjectId, formsToCopy);
        var copiedFormDescriptors = copier.copyForms();
        return new CopyFormDescriptorsFromProjectResult(copiedFormDescriptors);
    }
}
