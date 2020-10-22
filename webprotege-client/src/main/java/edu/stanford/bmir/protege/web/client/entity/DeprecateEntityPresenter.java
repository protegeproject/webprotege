package edu.stanford.bmir.protege.web.client.entity;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.form.FormPresenter;
import edu.stanford.bmir.protege.web.client.form.FormStackPresenter;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptorDto;
import edu.stanford.bmir.protege.web.shared.form.GetEntityDeprecationFormsAction;
import edu.stanford.bmir.protege.web.shared.form.GetEntityDeprecationFormsResult;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-21
 */
public class DeprecateEntityPresenter {

    @Nonnull
    private final OWLEntity entity;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final DeprecateEntityView view;

    @Nonnull
    private final FormPresenter formPresenter;

    @AutoFactory
    public DeprecateEntityPresenter(@Nonnull OWLEntity entity,
                                    @Provided @Nonnull ProjectId projectId,
                                    @Provided @Nonnull DispatchServiceManager dispatch,
                                    @Provided @Nonnull DeprecateEntityView view,
                                    @Provided @Nonnull FormPresenter formPresenter) {
        this.entity = checkNotNull(entity);
        this.projectId = projectId;
        this.dispatch = checkNotNull(dispatch);
        this.view = checkNotNull(view);
        this.formPresenter = checkNotNull(formPresenter);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        dispatch.execute(new GetEntityDeprecationFormsAction(projectId, entity),
                         this::displayForm);
        container.setWidget(view);

    }

    private void displayForm(GetEntityDeprecationFormsResult result) {
        ImmutableList<FormDescriptorDto> formDescriptors = result.getFormDescriptors();
        if(formDescriptors.isEmpty()) {
            return;
        }
        FormDescriptorDto firstFormDescriptor = formDescriptors.get(0);
        formPresenter.start(view.getFormContainer());
        formPresenter.setFieldsCollapsible(false);
        FormEntitySubjectDto subjectDto = FormSubjectDto.get(DataFactory.getOWLEntityData(entity,
                                                                                          ImmutableMap.of()));
        FormDataDto formDataDto = FormDataDto.get(subjectDto, firstFormDescriptor, 0);
        formPresenter.displayForm(formDataDto);
    }


}
