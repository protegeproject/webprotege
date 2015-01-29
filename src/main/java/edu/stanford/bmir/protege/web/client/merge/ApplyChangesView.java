package edu.stanford.bmir.protege.web.client.merge;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.shared.merge.Diff;
import edu.stanford.bmir.protege.web.shared.axiom.OWLAxiomData;
import org.semanticweb.owlapi.model.OWLAnnotation;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public interface ApplyChangesView extends IsWidget, HasInitialFocusable {

    String getCommitMessage();

    void setAnnotationDiff(Diff<OWLAnnotation> annotationDiff);

    void setPreview(SafeHtml preview);
}
