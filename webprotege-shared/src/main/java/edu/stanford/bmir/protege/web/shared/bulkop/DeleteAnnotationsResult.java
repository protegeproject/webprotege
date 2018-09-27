package edu.stanford.bmir.protege.web.shared.bulkop;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Sep 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class DeleteAnnotationsResult implements Result, HasEventList<ProjectEvent<?>> {

}
