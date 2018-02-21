package edu.stanford.bmir.protege.web.client.search;

import com.google.gwt.resources.client.DataResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchResult;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;

import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Apr 2017
 */
public class EntitySearchResultRendering {

    private EntitySearchResult result;


    public EntitySearchResultRendering(@Nonnull EntitySearchResult result) {
        this.result = result;
    }

    public SafeHtml getRendering() {
        return new SafeHtmlBuilder().appendHtmlConstant(result.getFieldRendering()).toSafeHtml();
    }

    public DataResource getIcon() {
        return result.getMatchedEntity().getEntity().accept(ICON_VISITOR);
    }


    public static final OWLEntityVisitorEx<DataResource> ICON_VISITOR = new OWLEntityVisitorEx<DataResource>() {
        @Nonnull
        @Override
        public DataResource visit(@Nonnull OWLClass cls) {
            return BUNDLE.svgClassIcon();
        }

        @Nonnull
        @Override
        public DataResource visit(@Nonnull OWLObjectProperty property) {
            return BUNDLE.svgObjectPropertyIcon();
        }

        @Nonnull
        @Override
        public DataResource visit(@Nonnull OWLDataProperty property) {
            return BUNDLE.svgDataPropertyIcon();
        }

        @Nonnull
        @Override
        public DataResource visit(@Nonnull OWLNamedIndividual individual) {
            return BUNDLE.svgIndividualIcon();
        }

        @Nonnull
        @Override
        public DataResource visit(@Nonnull OWLDatatype datatype) {
            return BUNDLE.svgDatatypeIcon();
        }

        @Nonnull
        @Override
        public DataResource visit(@Nonnull OWLAnnotationProperty property) {
            return BUNDLE.svgIndividualIcon();
        }
    };

    public OWLEntityData getEntityData() {
        return result.getMatchedEntity();
    }

    public OWLEntity getEntity() {
        return result.getMatchedEntity().getEntity();
    }
}
