package edu.stanford.bmir.protege.web.server.form;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.EntityFormSelector;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-21
 */
public class FormsCopier {

    @Nonnull
    private final ProjectId fromProjectId;

    @Nonnull
    private final ProjectId toProjectId;

    @Nonnull
    private final ImmutableList<FormId> formsToCopy;

    @Nonnull
    private final EntityFormRepository entityFormRepository;

    @Nonnull
    private final EntityFormSelectorRepository entityFormSelectorRepository;

    @AutoFactory
    public FormsCopier(@Nonnull ProjectId fromProjectId,
                       @Nonnull ProjectId toProjectId,
                       @Nonnull ImmutableList<FormId> formsToCopy,
                       @Provided @Nonnull EntityFormRepository entityFormRepository,
                       @Provided @Nonnull EntityFormSelectorRepository entityFormSelectorRepository) {
        this.fromProjectId = checkNotNull(fromProjectId);
        this.toProjectId = checkNotNull(toProjectId);
        this.formsToCopy = checkNotNull(formsToCopy);
        this.entityFormRepository = checkNotNull(entityFormRepository);
        this.entityFormSelectorRepository = checkNotNull(entityFormSelectorRepository);
    }

    @Nonnull
    public ImmutableList<FormDescriptor> copyForms() {
        var copiedFormDescriptors = ImmutableList.<FormDescriptor>builder();
        formsToCopy.forEach(formId -> {
            var formDescriptor = entityFormRepository.findFormDescriptor(fromProjectId, formId);
            formDescriptor.ifPresent(fd -> {
                // Don't overwrite existing ones
                var existingFormDescriptor = entityFormRepository.findFormDescriptor(toProjectId, formId);
                if (existingFormDescriptor.isEmpty()) {
                    var freshFormId = FormId.generate();
                    var copiedFormDescriptor = fd.withFormId(freshFormId);
                    entityFormRepository.saveFormDescriptor(toProjectId, copiedFormDescriptor);
                    copiedFormDescriptors.add(fd);
                    copyFormSelectorsForForm(formId, freshFormId);
                }
            });
        });
        return copiedFormDescriptors.build();
    }

    private void copyFormSelectorsForForm(@Nonnull FormId fromFormId,
                                          @Nonnull FormId toFormId) {
        entityFormSelectorRepository.findFormSelectors(fromProjectId)
                                    .filter(formSelector -> formSelector.getFormId().equals(fromFormId))
                                    .map(formSelector -> EntityFormSelector.get(toProjectId,
                                                                                formSelector.getCriteria(),
                                                                                toFormId))
                                    .forEach(entityFormSelectorRepository::save);
    }
}
