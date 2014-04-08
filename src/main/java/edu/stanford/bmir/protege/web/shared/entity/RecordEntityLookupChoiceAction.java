package edu.stanford.bmir.protege.web.shared.entity;

import edu.stanford.bmir.protege.web.shared.dispatch.HasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/11/2013
 */
public class RecordEntityLookupChoiceAction implements HasProjectAction<RecordEntityChoiceLookupResult> {

    private EntityLookupChoiceRecord record;

    private RecordEntityLookupChoiceAction() {
    }

    public RecordEntityLookupChoiceAction(EntityLookupChoiceRecord record) {
        this.record = record;
    }

    @Override
    public ProjectId getProjectId() {
        return record.getProjectId();
    }

    public EntityLookupChoiceRecord getRecord() {
        return record;
    }
}
