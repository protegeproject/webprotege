package edu.stanford.bmir.protege.web.server.change.matcher;

import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Mar 2017
 */
public class PropertyFillerExtractor {

    private static final PropertyExtactor PROPERTY_EXTACTOR = new PropertyExtactor();

    private static final FillerExtractor FILLER_EXTRACTOR = new FillerExtractor();

    private final OWLClassExpression classExpression;

    public PropertyFillerExtractor(OWLClassExpression classExpression) {
        this.classExpression = classExpression;
    }

    public Optional<OWLProperty> getProperty() {
        return classExpression.accept(PROPERTY_EXTACTOR);
    }

    public Optional<OWLObject> getFiller() {
        return classExpression.accept(FILLER_EXTRACTOR);
    }

    public boolean isPropertyAndFillerExtracted() {
        return getProperty().isPresent() && getFiller().isPresent();
    }
    
    private static class PropertyExtactor implements OWLClassExpressionVisitorEx<Optional<OWLProperty>> {

        @Nonnull
        @Override
        public Optional<OWLProperty> visit(@Nonnull OWLClass ce) {
            return Optional.empty();
        }

        @Nonnull
        @Override
        public Optional<OWLProperty> visit(@Nonnull OWLObjectIntersectionOf ce) {
            return Optional.empty();
        }

        @Nonnull
        @Override
        public Optional<OWLProperty> visit(@Nonnull OWLObjectUnionOf ce) {
            return Optional.empty();
        }

        @Nonnull
        @Override
        public Optional<OWLProperty> visit(@Nonnull OWLObjectComplementOf ce) {
            return Optional.empty();
        }
        
        @Nonnull
        @Override
        public Optional<OWLProperty> visit(@Nonnull OWLObjectSomeValuesFrom ce) {
            if (!ce.getProperty().isAnonymous()) {
                return Optional.of(ce.getProperty().asOWLObjectProperty());
            }
            else {
                return Optional.empty();
            }
        }

        @Nonnull
        @Override
        public Optional<OWLProperty> visit(@Nonnull OWLObjectAllValuesFrom ce) {
            return Optional.empty();
        }

        @Nonnull
        @Override
        public Optional<OWLProperty> visit(@Nonnull OWLObjectHasValue ce) {
            if (!ce.getProperty().isAnonymous()) {
                return Optional.of(ce.getProperty().asOWLObjectProperty());
            }
            else {
                return Optional.empty();
            }
        }

        @Nonnull
        @Override
        public Optional<OWLProperty> visit(@Nonnull OWLObjectMinCardinality ce) {
            if (!ce.getProperty().isAnonymous()) {
                return Optional.of(ce.getProperty().asOWLObjectProperty());
            }
            else {
                return Optional.empty();
            }
        }

        @Nonnull
        @Override
        public Optional<OWLProperty> visit(@Nonnull OWLObjectExactCardinality ce) {
            if (!ce.getProperty().isAnonymous()) {
                return Optional.of(ce.getProperty().asOWLObjectProperty());
            }
            else {
                return Optional.empty();
            }
        }

        @Nonnull
        @Override
        public Optional<OWLProperty> visit(@Nonnull OWLObjectMaxCardinality ce) {
            if (!ce.getProperty().isAnonymous()) {
                return Optional.of(ce.getProperty().asOWLObjectProperty());
            }
            else {
                return Optional.empty();
            }
        }

        @Nonnull
        @Override
        public Optional<OWLProperty> visit(@Nonnull OWLObjectHasSelf ce) {
            if (!ce.getProperty().isAnonymous()) {
                return Optional.of(ce.getProperty().asOWLObjectProperty());
            }
            else {
                return Optional.empty();
            }
        }

        @Nonnull
        @Override
        public Optional<OWLProperty> visit(@Nonnull OWLObjectOneOf ce) {
            return Optional.empty();
        }

        @Nonnull
        @Override
        public Optional<OWLProperty> visit(@Nonnull OWLDataSomeValuesFrom ce) {
            if (!ce.getProperty().isAnonymous()) {
                return Optional.of(ce.getProperty().asOWLDataProperty());
            }
            else {
                return Optional.empty();
            }
        }

        @Nonnull
        @Override
        public Optional<OWLProperty> visit(@Nonnull OWLDataAllValuesFrom ce) {
            if (!ce.getProperty().isAnonymous()) {
                return Optional.of(ce.getProperty().asOWLDataProperty());
            }
            else {
                return Optional.empty();
            }
        }

        @Nonnull
        @Override
        public Optional<OWLProperty> visit(@Nonnull OWLDataHasValue ce) {
            if (!ce.getProperty().isAnonymous()) {
                return Optional.of(ce.getProperty().asOWLDataProperty());
            }
            else {
                return Optional.empty();
            }
        }

        @Nonnull
        @Override
        public Optional<OWLProperty> visit(@Nonnull OWLDataMinCardinality ce) {
            if (!ce.getProperty().isAnonymous()) {
                return Optional.of(ce.getProperty().asOWLDataProperty());
            }
            else {
                return Optional.empty();
            }
        }

        @Nonnull
        @Override
        public Optional<OWLProperty> visit(@Nonnull OWLDataExactCardinality ce) {
            if (!ce.getProperty().isAnonymous()) {
                return Optional.of(ce.getProperty().asOWLDataProperty());
            }
            else {
                return Optional.empty();
            }
        }

        @Nonnull
        @Override
        public Optional<OWLProperty> visit(@Nonnull OWLDataMaxCardinality ce) {
            if (!ce.getProperty().isAnonymous()) {
                return Optional.of(ce.getProperty().asOWLDataProperty());
            }
            else {
                return Optional.empty();
            }
        }
    }
    
    private static class FillerExtractor implements OWLClassExpressionVisitorEx<Optional<OWLObject>> {

        @Nonnull
        @Override
        public Optional<OWLObject> visit(@Nonnull OWLClass ce) {
            return Optional.empty();
        }

        @Nonnull
        @Override
        public Optional<OWLObject> visit(@Nonnull OWLObjectIntersectionOf ce) {
            return Optional.empty();
        }

        @Nonnull
        @Override
        public Optional<OWLObject> visit(@Nonnull OWLObjectUnionOf ce) {
            return Optional.empty();
        }

        @Nonnull
        @Override
        public Optional<OWLObject> visit(@Nonnull OWLObjectComplementOf ce) {
            return Optional.empty();
        }

        @Nonnull
        @Override
        public Optional<OWLObject> visit(@Nonnull OWLObjectSomeValuesFrom ce) {
            return Optional.of(ce.getFiller());
        }

        @Nonnull
        @Override
        public Optional<OWLObject> visit(@Nonnull OWLObjectAllValuesFrom ce) {
            return Optional.of(ce.getFiller());
        }

        @Nonnull
        @Override
        public Optional<OWLObject> visit(@Nonnull OWLObjectHasValue ce) {
            return Optional.of(ce.getFiller());
        }

        @Nonnull
        @Override
        public Optional<OWLObject> visit(@Nonnull OWLObjectMinCardinality ce) {
            return Optional.of(ce.getFiller());
        }

        @Nonnull
        @Override
        public Optional<OWLObject> visit(@Nonnull OWLObjectExactCardinality ce) {
            return Optional.of(ce.getFiller());
        }

        @Nonnull
        @Override
        public Optional<OWLObject> visit(@Nonnull OWLObjectMaxCardinality ce) {
            return Optional.of(ce.getFiller());
        }

        @Nonnull
        @Override
        public Optional<OWLObject> visit(@Nonnull OWLObjectHasSelf ce) {
            return Optional.empty();
        }

        @Nonnull
        @Override
        public Optional<OWLObject> visit(@Nonnull OWLObjectOneOf ce) {
            return Optional.empty();
        }

        @Nonnull
        @Override
        public Optional<OWLObject> visit(@Nonnull OWLDataSomeValuesFrom ce) {
            return Optional.of(ce.getFiller());
        }

        @Nonnull
        @Override
        public Optional<OWLObject> visit(@Nonnull OWLDataAllValuesFrom ce) {
            return Optional.of(ce.getFiller());
        }

        @Nonnull
        @Override
        public Optional<OWLObject> visit(@Nonnull OWLDataHasValue ce) {
            return Optional.of(ce.getFiller());
        }

        @Nonnull
        @Override
        public Optional<OWLObject> visit(@Nonnull OWLDataMinCardinality ce) {
            return Optional.of(ce.getFiller());
        }

        @Nonnull
        @Override
        public Optional<OWLObject> visit(@Nonnull OWLDataExactCardinality ce) {
            return Optional.of(ce.getFiller());
        }

        @Nonnull
        @Override
        public Optional<OWLObject> visit(@Nonnull OWLDataMaxCardinality ce) {
            return Optional.of(ce.getFiller());
        }
    }
}
