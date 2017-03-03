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

    private List<ProjectDetails> details;

    @GwtSerializationConstructor
    private GetAvailableProjectsResult() {
    }

    public GetAvailableProjectsResult(List<ProjectDetails> details) {
        this.details = new ArrayList<>(details);
    }


    public List<ProjectDetails> getDetails() {
        return Collections.unmodifiableList(details);
    }
}
