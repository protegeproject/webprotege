package edu.stanford.bmir.protege.web.shared.form;

import edu.stanford.bmir.protege.web.shared.collection.CollectionId;
import edu.stanford.bmir.protege.web.shared.collection.CollectionItem;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class GetFormDescriptorResult implements Result {

    private ProjectId projectId;

    private CollectionId collectionId;

    private CollectionItem elementId;

    private FormId formId;

    private FormDescriptor formDescriptor;

    private FormData formData;

    private GetFormDescriptorResult() {
    }

    public GetFormDescriptorResult(ProjectId projectId,
                                   CollectionId collectionId,
                                   CollectionItem elementId,
                                   FormId formId,
                                   FormDescriptor formDescriptor,
                                   FormData formData) {
        this.projectId = projectId;
        this.collectionId = collectionId;
        this.elementId = elementId;
        this.formId = formId;
        this.formDescriptor = formDescriptor;
        this.formData = formData;
    }

    public FormData getFormData() {
        return formData;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public CollectionId getCollectionId() {
        return collectionId;
    }

    public CollectionItem getElementId() {
        return elementId;
    }

    public FormId getFormId() {
        return formId;
    }

    public FormDescriptor getFormDescriptor() {
        return formDescriptor;
    }
}
