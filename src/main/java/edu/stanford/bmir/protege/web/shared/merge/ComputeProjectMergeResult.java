package edu.stanford.bmir.protege.web.shared.merge;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import edu.stanford.bmir.protege.web.shared.axiom.OWLAxiomData;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.List;
import java.util.Set;

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
