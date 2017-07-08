package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.UpdateOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Jun 2017
 */
@ProjectSingleton
public class FormDataRepository {

    @Nonnull
    private final Datastore datastore;

    @Inject
    public FormDataRepository(@Nonnull Datastore datastore) {
        this.datastore = datastore;
    }

    public void store(ProjectId projectId, FormId formId, OWLEntity entity, FormData formData) {
        Query<FormDataRecord> query = datastore.createQuery(FormDataRecord.class)
                                               .field("projectId").equal(projectId)
                                               .field("formId").equal(formId)
                                               .field("formCollection").equal(entity.toString());
        UpdateOperations<FormDataRecord> update = datastore.createUpdateOperations(FormDataRecord.class);
        update.set("formData", formData);
        datastore.update(query, update, new UpdateOptions().upsert(true));
    }


    public FormData get(ProjectId projectId,
                        FormId formId,
                        OWLEntity entity) {

        FormDataRecord record = datastore.find(FormDataRecord.class)
                                         .field("projectId").equal(projectId)
                                         .field("formId").equal(formId)
                                         .field("formCollection").equal(entity.toString())
                                         .get();
        System.out.println("Record: " + record);
        if (record == null) {
            System.out.println("No form data found");
            return FormData.empty();
        }
        else {
            return record.getFormData();
        }

    }
}
