package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.form.CopyFormDescriptorsFromProjectAction;
import edu.stanford.bmir.protege.web.shared.form.CopyFormDescriptorsFromProjectResult;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-14
 */
public class CopyFormDescriptorsFromProjectActionHandler extends AbstractProjectActionHandler<CopyFormDescriptorsFromProjectAction, CopyFormDescriptorsFromProjectResult> {

    @Nonnull
    private final EntityFormRepository entityFormRepository;

    @Inject
    public CopyFormDescriptorsFromProjectActionHandler(@Nonnull AccessManager accessManager,
                                                       @Nonnull EntityFormRepository entityFormRepository) {
        super(accessManager);
        this.entityFormRepository = entityFormRepository;
    }

    @Nonnull
    @Override
    public Class<CopyFormDescriptorsFromProjectAction> getActionClass() {
        return CopyFormDescriptorsFromProjectAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.EDIT_FORMS;
    }

    @Nonnull
    @Override
    public CopyFormDescriptorsFromProjectResult execute(@Nonnull CopyFormDescriptorsFromProjectAction action,
                                                        @Nonnull ExecutionContext executionContext) {
        var copyFromProjectId = action.getProjectIdToCopyFrom();
        var copyToProjectId = action.getProjectId();
        var formsToCopy = action.getFormIdsToCopy();
        var copiedFormDescriptors = ImmutableList.<FormDescriptor>builder();
        formsToCopy.forEach(formId -> {
            var formDescriptor = entityFormRepository.findFormDescriptor(copyFromProjectId, formId);
            formDescriptor.ifPresent(fd -> {
                // Don't overwrite existing ones
                var existingFormDescriptor = entityFormRepository.findFormDescriptor(copyToProjectId, formId);
                if(existingFormDescriptor.isEmpty()) {
                    var freshFormId = FormId.generate();
                    var copiedFormDescriptor = fd.withFormId(freshFormId);
                    entityFormRepository.saveFormDescriptor(copyToProjectId, copiedFormDescriptor);
                    copiedFormDescriptors.add(fd);
                }

            });
        });
        return new CopyFormDescriptorsFromProjectResult(copiedFormDescriptors.build());
    }
}
