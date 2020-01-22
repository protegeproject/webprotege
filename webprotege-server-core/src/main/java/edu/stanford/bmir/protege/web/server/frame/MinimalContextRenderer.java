package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.server.renderer.ContextRenderer;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-22
 */
public class MinimalContextRenderer extends ContextRenderer {

    private static final String EMPTY_BROWSER_TEXT = "";

    private static final ImmutableMap<DictionaryLanguage, String> EMPTY_SHORT_FORMS = ImmutableMap.of();

    @Inject
    public MinimalContextRenderer(@Nonnull RenderingManager renderingManager) {
        super(renderingManager);
    }

    @Nonnull
    public OWLEntityData getEntityData(@Nonnull OWLEntity entity) {
        return DataFactory.getOWLEntityData(entity,
                                            EMPTY_BROWSER_TEXT,
                                            EMPTY_SHORT_FORMS);
    }

    @Nonnull
    public OWLClassData getClassData(@Nonnull OWLClass cls) {
        return OWLClassData.get(cls,
                                EMPTY_BROWSER_TEXT,
                                EMPTY_SHORT_FORMS);
    }

    @Nonnull
    public OWLObjectPropertyData getObjectPropertyData(@Nonnull OWLObjectProperty property) {
        return OWLObjectPropertyData.get(property,
                                         EMPTY_BROWSER_TEXT,
                                         EMPTY_SHORT_FORMS);
    }

    @Nonnull
    public OWLDataPropertyData getDataPropertyData(@Nonnull OWLDataProperty property) {
        return OWLDataPropertyData.get(property,
                                       EMPTY_BROWSER_TEXT,
                                       EMPTY_SHORT_FORMS);
    }

    @Nonnull
    public OWLAnnotationPropertyData getAnnotationPropertyData(@Nonnull OWLAnnotationProperty property) {
        return OWLAnnotationPropertyData.get(property,
                                             EMPTY_BROWSER_TEXT,
                                             EMPTY_SHORT_FORMS);
    }

    @Nonnull
    public OWLNamedIndividualData getIndividualData(@Nonnull OWLNamedIndividual individual) {
        return OWLNamedIndividualData.get(individual,
                                          EMPTY_BROWSER_TEXT,
                                          EMPTY_SHORT_FORMS);
    }

    @Nonnull
    public OWLDatatypeData getDatatypeData(@Nonnull OWLDatatype datatype) {
        return OWLDatatypeData.get(datatype,
                                   EMPTY_BROWSER_TEXT,
                                   EMPTY_SHORT_FORMS);
    }
}
