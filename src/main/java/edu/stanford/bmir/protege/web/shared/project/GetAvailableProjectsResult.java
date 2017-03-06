package edu.stanford.bmir.protege.web.shared.project;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/04/2013
 */
public class GetAvailableProjectsResult implements Result {

    private List<AvailableProject> details;

    @GwtSerializationConstructor
    private GetAvailableProjectsResult() {
    }

    public GetAvailableProjectsResult(List<AvailableProject> details) {
        this.details = new ArrayList<>(details);
    }


    public List<AvailableProject> getDetails() {
        return Collections.unmodifiableList(details);
    }
}
