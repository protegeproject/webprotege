package edu.stanford.bmir.protege.web.server.renderer;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import edu.stanford.bmir.protege.web.server.mansyntax.render.*;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryManager;
import edu.stanford.bmir.protege.web.server.shortform.LanguageManager;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.renderer.HasHtmlBrowserText;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxOWLObjectRendererImpl;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/03/2012
 *
 */
@ProjectSingleton
public class RenderingManager implements HasGetRendering, HasHtmlBrowserText {

    private DictionaryManager dictionaryManager;

    private final DeprecatedEntityChecker deprecatedEntityChecker;

    private final ManchesterSyntaxObjectRenderer htmlManchesterSyntaxRenderer;

    private final OWLObjectRenderer owlObjectRenderer = new ManchesterOWLSyntaxOWLObjectRendererImpl();

    @Inject
    public RenderingManager(DictionaryManager dictionaryManager,
                            DeprecatedEntityChecker deprecatedChecker,
                            ManchesterSyntaxObjectRenderer objectRenderer) {
        this.dictionaryManager = dictionaryManager;
        this.htmlManchesterSyntaxRenderer = objectRenderer;
        this.deprecatedEntityChecker = deprecatedChecker;
        owlObjectRenderer.setShortFormProvider(new ShortFormAdapter(dictionaryManager));
    }

    /**
     * Gets the short for for the specified entity.
     * @param entity The entity.
     * @return The entity short form. Not null.
     */
    @Nonnull
    public String getShortForm(OWLEntity entity) {
        return dictionaryManager.getShortForm(entity);
    }

    /**
     * Gets the browser text for a given OWLObject.
     * @param object The object.
     * @return The browser text for the object.
     */
    @Deprecated
    public String getBrowserText(OWLObject object) {
        return owlObjectRenderer.render(object);
    }

    private String getHTMLBrowserText(OWLObject object) {
        return getHTMLBrowserText(object, entity -> false);
    }

    @Override
    public SafeHtml getHtmlBrowserText(OWLObject object) {
        return new SafeHtmlBuilder().appendHtmlConstant(getHTMLBrowserText(object)).toSafeHtml();
    }

    public String getHTMLBrowserText(OWLObject object, final Set<String> highlightedPhrases) {
        return getHTMLBrowserText(object, entity -> highlightedPhrases.contains(getShortForm(entity)));
    }

    private String getHTMLBrowserText(OWLObject object, HighlightedEntityChecker highlightChecker) {
        return htmlManchesterSyntaxRenderer.render(object, highlightChecker, deprecatedEntityChecker);
    }

    public OWLEntityData getRendering(OWLEntity entity) {
        return DataFactory.getOWLEntityData(entity, getShortForm(entity));
    }

    public OWLPrimitiveData getRendering(OWLAnnotationValue value) {
        if(value instanceof IRI) {
            return new IRIData((IRI) value);
        }
        else if(value instanceof OWLLiteral) {
            return new OWLLiteralData((OWLLiteral) value);
        }
        else {
            throw new RuntimeException("Unsupported");
        }
    }

    public OWLClassData getRendering(OWLClass cls) {
        return new OWLClassData(cls, getShortForm(cls));
    }

    public OWLObjectPropertyData getRendering(OWLObjectProperty property) {
        return new OWLObjectPropertyData(property, getShortForm(property));
    }

    public OWLDataPropertyData getRendering(OWLDataProperty property) {
        return new OWLDataPropertyData(property, getShortForm(property));
    }

    public OWLAnnotationPropertyData getRendering(OWLAnnotationProperty property) {
        return new OWLAnnotationPropertyData(property, getShortForm(property));
    }

    public OWLNamedIndividualData getRendering(OWLNamedIndividual individual) {
        return new OWLNamedIndividualData(individual, getShortForm(individual));
    }

    public OWLDatatypeData getRendering(OWLDatatype datatype) {
        return new OWLDatatypeData(datatype, getShortForm(datatype));
    }

    public void dispose() {
    }
}
