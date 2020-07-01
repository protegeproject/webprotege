package edu.stanford.bmir.protege.web.shared.form;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class SetEntityFormsDataAction implements ProjectAction<SetEntityFormDataResult> {

    private ProjectId projectId;

    private OWLEntity entity;

    private ImmutableMap<FormId, FormData> pristineFormsData;

    private ImmutableMap<FormId, FormData> editedFormsData;

    public SetEntityFormsDataAction(@Nonnull ProjectId projectId,
                                    @Nonnull OWLEntity entity,
                                    @Nonnull ImmutableMap<FormId, FormData> pristineFormsData,
                                    @Nonnull ImmutableMap<FormId, FormData> editedFormsData) {
        this.projectId = checkNotNull(projectId);
        this.entity = checkNotNull(entity);
        this.pristineFormsData = checkNotNull(pristineFormsData);
        this.editedFormsData = checkNotNull(editedFormsData);
        checkArgument(pristineFormsData.size() == editedFormsData.size(),
                      "Pristine forms data size != edited forms data size");
    }

    @GwtSerializationConstructor
    private SetEntityFormsDataAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public OWLEntity getEntity() {
        return entity;
    }

    @Nonnull
    public ImmutableMap<FormId, FormData> getPristineFormsData() {
        return pristineFormsData;
    }

    @Nonnull
    public ImmutableMap<FormId, FormData> getEditedFormsData() {
        return editedFormsData;
    }
}
