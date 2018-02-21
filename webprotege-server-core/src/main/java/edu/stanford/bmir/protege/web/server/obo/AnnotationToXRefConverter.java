package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.shared.obo.OBOXRef;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Set;

import static edu.stanford.bmir.protege.web.server.obo.OboUtil.getXRefPropertyIRI;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class AnnotationToXRefConverter {

    @Nonnull
    private final XRefConverter oboXRefConverter;

    @Nonnull
    private final OWLDataFactory df;

    @Inject
    public AnnotationToXRefConverter(@Nonnull XRefConverter oboXRefConverter,
                                     @Nonnull OWLDataFactory df) {
        this.oboXRefConverter = oboXRefConverter;
        this.df = df;
    }

    public OWLAnnotation toAnnotation(OBOXRef xref) {
        OWLAnnotationProperty xrefAnnotationProperty = df.getOWLAnnotationProperty(getXRefPropertyIRI());
        String oboId = xref.toOBOId();
        Set<OWLAnnotation> descriptionAnnotations;
        if (xref.getDescription().isEmpty()) {
            descriptionAnnotations = Collections.emptySet();
        }
        else {
            OWLAnnotation descriptionAnnotation = df.getOWLAnnotation(df.getRDFSLabel(), df.getOWLLiteral(xref.getDescription()));
            descriptionAnnotations = Collections.singleton(descriptionAnnotation);
        }
        return df.getOWLAnnotation(xrefAnnotationProperty, df.getOWLLiteral(oboId), descriptionAnnotations);
    }

    public OBOXRef toXRef(@Nonnull final OWLAnnotation annotation,
                                           @Nonnull final Set<OWLAnnotation> annoAnnos) {


        return annotation.getValue().accept(new OWLAnnotationValueVisitorEx<OBOXRef>() {
            @Nonnull
            public OBOXRef visit(@Nonnull org.semanticweb.owlapi.model.IRI iri) {
                String description = "";
                for (OWLAnnotation anno : annoAnnos) {
                    if (anno.getProperty().isLabel()) {
                        description = anno.getValue().accept(new OWLAnnotationValueVisitorEx<String>() {
                            @Nonnull
                            public String visit(@Nonnull org.semanticweb.owlapi.model.IRI iri) {
                                return iri.toString();
                            }

                            @Nonnull
                            public String visit(@Nonnull OWLAnonymousIndividual individual) {
                                return individual.toString();
                            }

                            @Nonnull
                            public String visit(@Nonnull OWLLiteral literal) {
                                return literal.getLiteral();
                            }
                        });
                        break;
                    }
                }
                return oboXRefConverter.toOBOXRef(iri.toString(), description);
            }

            @Nonnull
            public OBOXRef visit(@Nonnull OWLAnonymousIndividual individual) {
                return oboXRefConverter.toOBOXRef(individual.getID().getID(), "");
            }

            @Nonnull
            public OBOXRef visit(@Nonnull OWLLiteral literal) {
                String description = "";
                for (OWLAnnotation anno : annoAnnos) {
                    if (anno.getProperty().isLabel()) {
                        description = anno.getValue().accept(new OWLAnnotationValueVisitorEx<String>() {
                            @Nonnull
                            public String visit(@Nonnull org.semanticweb.owlapi.model.IRI iri) {
                                return iri.toString();
                            }

                            @Nonnull
                            public String visit(@Nonnull OWLAnonymousIndividual individual) {
                                return individual.toString();
                            }

                            @Nonnull
                            public String visit(@Nonnull OWLLiteral literal) {
                                return literal.getLiteral();
                            }
                        });
                        break;
                    }
                }
                return oboXRefConverter.toOBOXRef(literal.getLiteral(), description);
            }
        });
    }
}
