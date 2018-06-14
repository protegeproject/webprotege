package edu.stanford.bmir.protege.web.client.renderer;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
@ApplicationSingleton
public class PrimitiveDataIconProvider {

    private final WebProtegeClientBundle clientBundle;

    @Inject
    public PrimitiveDataIconProvider(@Nonnull WebProtegeClientBundle clientBundle) {
        this.clientBundle = clientBundle;
    }

    private final OWLPrimitiveDataVisitorAdapter<String, RuntimeException> VISITOR = new OWLPrimitiveDataVisitorAdapter<String, RuntimeException>() {
        @Override
        protected String getDefaultReturnValue() {
            return "empty-icon-inset";
        }

        @Override
        public String visit(OWLClassData data) throws RuntimeException {
            return clientBundle.style().classIconInset();
        }

        @Override
        public String visit(OWLObjectPropertyData data) throws RuntimeException {
            return clientBundle.style().objectPropertyIconInset();
        }

        @Override
        public String visit(OWLDataPropertyData data) throws RuntimeException {
            return clientBundle.style().dataPropertyIconInset();
        }

        @Override
        public String visit(OWLAnnotationPropertyData data) throws RuntimeException {
            return clientBundle.style().annotationPropertyIconInset();
        }

        @Override
        public String visit(OWLNamedIndividualData data) throws RuntimeException {
            return clientBundle.style().individualIconInset();
        }

        @Override
        public String visit(OWLDatatypeData data) throws RuntimeException {
            return clientBundle.style().datatypeIconInset();
        }

        @Override
        public String visit(OWLLiteralData data) throws RuntimeException {
            return clientBundle.style().literalIconInset();
        }

        @Override
        public String visit(IRIData data) throws RuntimeException {
            if (data.isHTTPLink()) {
                return clientBundle.style().linkIconInset();
            }
            else {
                return clientBundle.style().iriIconInset();
            }
        }
    };


    @Nonnull
    public String getIconInsetStyleName(@Nonnull OWLPrimitiveData primitiveData) {
        return primitiveData.accept(VISITOR);
    }
}
