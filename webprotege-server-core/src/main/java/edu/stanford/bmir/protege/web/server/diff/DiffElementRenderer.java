package edu.stanford.bmir.protege.web.server.diff;

import com.google.gwt.safehtml.shared.SafeHtml;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.renderer.HasHtmlBrowserText;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/15
 */
public class DiffElementRenderer<S extends Serializable> {

    private HasHtmlBrowserText renderer;

    private OntologyChangeVisitorEx<SafeHtml> visitor;

    public DiffElementRenderer(HasHtmlBrowserText ren) {
        this.renderer = ren;
        visitor = new OntologyChangeVisitorEx<>() {

            @Override
            public SafeHtml visit(@Nonnull AddAxiomChange change)  {
                return renderer.getHtmlBrowserText(change.getAxiom());
            }

            @Override
            public SafeHtml visit(@Nonnull RemoveAxiomChange change)  {
                return renderer.getHtmlBrowserText(change.getAxiom());
            }

            @Override
            public SafeHtml visit(@Nonnull AddOntologyAnnotationChange change)  {
                return renderer.getHtmlBrowserText(change.getAnnotation());
            }

            @Override
            public SafeHtml visit(@Nonnull RemoveOntologyAnnotationChange change)  {
                return renderer.getHtmlBrowserText(change.getAnnotation());
            }

            @Override
            public SafeHtml visit(@Nonnull AddImportChange change)  {
                return renderer.getHtmlBrowserText(change.getImportsDeclaration().getIRI());
            }

            @Override
            public SafeHtml visit(@Nonnull RemoveImportChange change)  {
                return renderer.getHtmlBrowserText(change.getImportsDeclaration().getIRI());
            }

            @Override
            public SafeHtml getDefaultReturnValue() {
                throw new RuntimeException();
            }
        };
    }

    public DiffElement<S, SafeHtml> render(DiffElement<S, OntologyChange> element) {
        OntologyChange lineElement = element.getLineElement();
        return new DiffElement<>(
                element.getDiffOperation(),
                element.getSourceDocument(),
                renderData(lineElement)
        );
    }

    public SafeHtml renderData(OntologyChange change) {
        return change.accept(visitor);
    }
}
