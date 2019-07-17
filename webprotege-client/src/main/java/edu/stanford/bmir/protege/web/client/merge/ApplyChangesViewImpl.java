package edu.stanford.bmir.protege.web.client.merge;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import edu.stanford.bmir.protege.web.client.diff.DiffLineElementRenderer;
import edu.stanford.bmir.protege.web.client.diff.DiffSourceDocumentRenderer;
import edu.stanford.bmir.protege.web.client.diff.DiffViewImpl;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.axiom.OWLAxiomData;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.merge.Diff;
import org.semanticweb.owlapi.model.OWLAnnotation;

import java.util.Collection;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class ApplyChangesViewImpl extends Composite implements ApplyChangesView, HasInitialFocusable {

    interface ApplyChangesViewImplUiBinder extends UiBinder<HTMLPanel, ApplyChangesViewImpl> {
    }

    private static ApplyChangesViewImplUiBinder ourUiBinder = GWT.create(ApplyChangesViewImplUiBinder.class);

    @UiField
    protected TextArea commitMessageField;

    @UiField
    protected DiffViewImpl diffView;

    @UiField
    Label emptyDiffMessage;

    public ApplyChangesViewImpl() {
        MergeClientBundle.BUNDLE.style().ensureInjected();
        initWidget(ourUiBinder.createAndBindUi(this));
        emptyDiffMessage.setVisible(false);
    }

    @Override
    public String getCommitMessage() {
        return commitMessageField.getText().trim();
    }

    @Override
    public void setAnnotationDiff(Diff<OWLAnnotation> annotationDiff) {

    }

    @Override
    public void displayEmptyDiffMessage() {
        emptyDiffMessage.setVisible(true);
    }

    @Override
    public void setDiff(List<DiffElement<String, SafeHtml>> diff) {
        diffView.setDiff(diff, new DiffLineElementRenderer<SafeHtml>() {
            @Override
            public SafeHtml getRendering(SafeHtml lineElement) {
                return lineElement;
            }
        }, new DiffSourceDocumentRenderer<String>() {
            @Override
            public SafeHtml renderSourceDocument(String document) {
                return new SafeHtmlBuilder().appendEscaped(document).toSafeHtml();
            }
        });
    }

    private void renderAxioms(Collection<OWLAxiomData> axioms, StringBuilder rendering, String styleName) {
        for(OWLAxiomData ax : axioms) {
            rendering.append("<div class=\"").append(styleName).append("\">");
            rendering.append(ax.getHtmlRendering());
            rendering.append("</div>");
        }
    }

    @Override
    public java.util.Optional<HasRequestFocus> getInitialFocusable() {
        return java.util.Optional.of(() -> commitMessageField.setFocus(true));
    }
}
