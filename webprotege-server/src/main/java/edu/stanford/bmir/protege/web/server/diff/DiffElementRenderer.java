package edu.stanford.bmir.protege.web.server.diff;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.renderer.HasHtmlBrowserText;
import org.semanticweb.owlapi.change.*;

import java.io.Serializable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/15
 */
public class DiffElementRenderer<S extends Serializable> {

    private HasHtmlBrowserText renderer;

    private OWLOntologyChangeDataVisitor<SafeHtml, RuntimeException> visitor;

    public DiffElementRenderer(HasHtmlBrowserText ren) {
        this.renderer = ren;
        visitor = new OWLOntologyChangeDataVisitor<SafeHtml, RuntimeException>() {
            @Override
            public SafeHtml visit(AddAxiomData data) throws RuntimeException {
                return renderer.getHtmlBrowserText(data.getAxiom());
            }

            @Override
            public SafeHtml visit(RemoveAxiomData data) throws RuntimeException {
                return renderer.getHtmlBrowserText(data.getAxiom());
            }

            @Override
            public SafeHtml visit(AddOntologyAnnotationData data) throws RuntimeException {
                return renderer.getHtmlBrowserText(data.getAnnotation());
            }

            @Override
            public SafeHtml visit(RemoveOntologyAnnotationData data) throws RuntimeException {
                return renderer.getHtmlBrowserText(data.getAnnotation());
            }

            @Override
            public SafeHtml visit(SetOntologyIDData data) throws RuntimeException {
                return new SafeHtmlBuilder().toSafeHtml();
            }

            @Override
            public SafeHtml visit(AddImportData data) throws RuntimeException {
                return renderer.getHtmlBrowserText(data.getDeclaration().getIRI());
            }

            @Override
            public SafeHtml visit(RemoveImportData data) throws RuntimeException {
                return renderer.getHtmlBrowserText(data.getDeclaration().getIRI());
            }
        };
    }

    public DiffElement<S, SafeHtml> render(DiffElement<S, OWLOntologyChangeRecord> element) {
        OWLOntologyChangeRecord lineElement = element.getLineElement();
        return new DiffElement<>(
                element.getDiffOperation(),
                element.getSourceDocument(),
                renderData(lineElement.getData())
        );
    }

    public SafeHtml renderData(OWLOntologyChangeData data) {
        return data.accept(visitor);
    }
}
