package edu.stanford.bmir.protege.web.shared.form;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.collection.CollectionElementId;
import edu.stanford.bmir.protege.web.shared.collection.CollectionId;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Jun 2017
 */
public class SetFormDataAction implements ProjectAction<SetFormDataResult> {

    private ProjectId projectId;

    private CollectionId collectionId;

    private CollectionElementId elementId;

    private FormId formId;

    private FormData formData;

    public SetFormDataAction(ProjectId projectId,
                             CollectionId collectionId,
                             CollectionElementId elementId,
                             FormId formId,
                             FormData formData) {
        this.projectId = projectId;
        this.collectionId = collectionId;
        this.elementId = elementId;
        this.formId = formId;
        this.formData = formData;
    }

    @GwtSerializationConstructor
    private SetFormDataAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public FormId getFormId() {
        return formId;
    }


    public FormData getFormData() {
        return formData;
    }

    public CollectionId getCollectionId() {
        return collectionId;
    }

    public CollectionElementId getElementId() {
        return elementId;
    }

    @Override
    public String toString() {
        return toStringHelper("SetFormDataAction")
                .addValue(projectId)
                .addValue(collectionId)
                .addValue(elementId)
                .addValue(formId)
                .addValue(formData)
                .toString();
    }
}
