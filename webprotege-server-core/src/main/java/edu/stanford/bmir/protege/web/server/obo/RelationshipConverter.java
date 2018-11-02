package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import edu.stanford.bmir.protege.web.shared.obo.OBORelationship;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class RelationshipConverter {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final RenderingManager renderingManager;

    @Inject
    public RelationshipConverter(@Nonnull OWLDataFactory dataFactory,
                                 @Nonnull RenderingManager renderingManager) {
        this.dataFactory = dataFactory;
        this.renderingManager = renderingManager;
    }

    @Nonnull
    public OWLObjectSomeValuesFrom toSomeValuesFrom(@Nonnull OBORelationship relationship) {
        OWLObjectProperty property = relationship.getRelation().getEntity();
        OWLClass filler = relationship.getValue().getEntity();
        return dataFactory.getOWLObjectSomeValuesFrom(property, filler);
    }

    @Nonnull
    public OBORelationship toOboRelationship(@Nonnull OWLObjectSomeValuesFrom someValuesFrom) {
        OWLObjectPropertyData property = renderingManager.getObjectPropertyData(someValuesFrom.getProperty().asOWLObjectProperty());
        OWLClassData filler = renderingManager.getClassData(someValuesFrom.getFiller().asOWLClass());
        return new OBORelationship(property, filler);
    }
}
