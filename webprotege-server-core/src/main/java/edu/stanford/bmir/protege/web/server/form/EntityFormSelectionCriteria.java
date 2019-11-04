package edu.stanford.bmir.protege.web.server.form;

import com.google.auto.factory.AutoFactory;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
@AutoValue
public abstract class EntityFormSelectionCriteria {

    @Nonnull
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract EntityMatchCriteria getCriteria();

    @Nonnull
    public abstract FormId getFormId();
}
