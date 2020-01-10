package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.shared.collection.CollectionId;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
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
import java.util.Optional;

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

    public void store(ProjectId projectId, CollectionId collectionId, FormId formId, OWLEntity entity, FormData formData) {
        Query<FormDataRecord> query = datastore.createQuery(FormDataRecord.class)
                                               .field("projectId").equal(projectId)
                                               .field("collectionId").equal(collectionId)
                                               .field("formId").equal(formId)
                                               .field("subjectId").equal(entity.toString());
        UpdateOperations<FormDataRecord> update = datastore.createUpdateOperations(FormDataRecord.class);
        update.set("data", formData);
        datastore.update(query, update, new UpdateOptions().upsert(true));
    }


    public FormData get(ProjectId projectId,
                        CollectionId collectionId,
                        FormId formId,
                        OWLEntity entity) {

        FormDataRecord record = datastore.find(FormDataRecord.class)
                                         .field("projectId").equal(projectId)
                                         .field("collectionId").equal(collectionId)
                                         .field("formId").equal(formId)
                                         .field("subjectId").equal(entity.toString())
                                         .get();
        if (record == null) {
            return FormData.empty(entity, FormId.generate());
        }
        else {
            return record.getData();
        }

    }
}
