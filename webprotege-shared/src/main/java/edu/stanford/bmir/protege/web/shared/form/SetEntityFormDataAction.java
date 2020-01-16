package edu.stanford.bmir.protege.web.shared.form;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class SetEntityFormDataAction implements ProjectAction<SetEntityFormDataResult> {

    private ProjectId projectId;

    private OWLEntity entity;

    private FormData pristineFormData;

    private FormData newFormData;

    public SetEntityFormDataAction(ProjectId projectId,
                                   OWLEntity entity,
                                   FormData pristineFormData,
                                   FormData newFormData) {
        this.projectId = checkNotNull(projectId);
        this.entity = checkNotNull(entity);
        this.pristineFormData = checkNotNull(pristineFormData);
        this.newFormData = checkNotNull(newFormData);
    }

    @GwtSerializationConstructor
    private SetEntityFormDataAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public OWLEntity getEntity() {
        return entity;
    }

    public FormData getPristineFormData() {
        return pristineFormData;
    }

    @Nonnull
    public FormData getNewFormData() {
        return newFormData;
    }
}
