package edu.stanford.bmir.protege.web.shared.merge;

import com.google.gwt.safehtml.shared.SafeHtml;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class ComputeProjectMergeResult implements Result {

    private List<DiffElement<String, SafeHtml>> diff;

    private ComputeProjectMergeResult() {
    }

    public ComputeProjectMergeResult(List<DiffElement<String, SafeHtml>> diff) {
        this.diff = diff;
    }

    public List<DiffElement<String, SafeHtml>> getDiff() {
        return diff;
    }
}
