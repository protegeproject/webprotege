package edu.stanford.bmir.protege.web.server.integration.service;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.integration.dispatch.ActionDispatch;
import edu.stanford.bmir.protege.web.server.integration.dto.OntologyPropertyData;
import edu.stanford.bmir.protege.web.server.integration.dto.OntologyPropertyKind;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.frame.GetAnnotationPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.GetAnnotationPropertyFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.GetDataPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.GetDataPropertyFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.GetObjectPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.GetObjectPropertyFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.ObjectPropertyCharacteristic;
import edu.stanford.bmir.protege.web.shared.frame.PlainAnnotationPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.PlainDataPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.PlainObjectPropertyFrame;
import edu.stanford.bmir.protege.web.shared.match.GetMatchingEntitiesAction;
import edu.stanford.bmir.protege.web.shared.match.GetMatchingEntitiesResult;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityTypeIsOneOfCriteria;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Reads OWL property frames from the live project ontology.
 */
public class OntologyPropertyService {

    private static final int DEFAULT_LIST_PAGE_SIZE = 100;

    @Nonnull
    private final ActionDispatch actionDispatch;

    @Inject
    public OntologyPropertyService(@Nonnull ActionDispatch actionDispatch) {
        this.actionDispatch = Objects.requireNonNull(actionDispatch, "actionDispatch");
    }

    @Nonnull
    public OntologyPropertyData getPropertyData(@Nonnull String projectId,
                                                @Nonnull String propertyIri,
                                                @Nonnull OntologyPropertyKind kind,
                                                @Nonnull UserId userId) {
        requireNonBlank(projectId, "projectId");
        requireNonBlank(propertyIri, "propertyIri");
        Objects.requireNonNull(kind, "kind");
        Objects.requireNonNull(userId, "userId");
        return loadProperty(projectId, propertyIri, kind, userId);
    }

    /**
     * Lists property frames. When {@code kind} is null, object, data, and annotation
     * properties are all included.
     */
    @Nonnull
    public List<OntologyPropertyData> listPropertyData(@Nonnull String projectId,
                                                       @Nullable OntologyPropertyKind kind,
                                                       @Nonnull UserId userId) {
        requireNonBlank(projectId, "projectId");
        Objects.requireNonNull(userId, "userId");

        ProjectId id = ProjectId.get(projectId);
        ImmutableSet<EntityType<?>> types = kind == null
                ? ImmutableSet.<EntityType<?>>of(
                        EntityType.OBJECT_PROPERTY,
                        EntityType.DATA_PROPERTY,
                        EntityType.ANNOTATION_PROPERTY)
                : ImmutableSet.<EntityType<?>>of(kind.getEntityType());

        List<OntologyPropertyData> result = new ArrayList<>();
        int pageNumber = 1;
        while (true) {
            GetMatchingEntitiesAction listAction = GetMatchingEntitiesAction.getMatchingEntities(
                    id,
                    EntityTypeIsOneOfCriteria.get(types),
                    PageRequest.requestPageWithSize(pageNumber, DEFAULT_LIST_PAGE_SIZE)
            );
            GetMatchingEntitiesResult listResult = actionDispatch.execute(listAction, userId);
            Page<EntityNode> page = listResult.getEntities();
            for (EntityNode node : page.getPageElements()) {
                OWLEntity entity = node.getEntity();
                OntologyPropertyKind nodeKind = kindOf(entity);
                if (nodeKind == null) {
                    continue;
                }
                result.add(loadProperty(projectId, entity.getIRI().toString(), nodeKind, userId));
            }
            if (pageNumber >= page.getPageCount() || page.getPageElements().isEmpty()) {
                break;
            }
            pageNumber++;
        }
        return result;
    }

    @Nonnull
    private OntologyPropertyData loadProperty(@Nonnull String projectId,
                                              @Nonnull String propertyIri,
                                              @Nonnull OntologyPropertyKind kind,
                                              @Nonnull UserId userId) {
        ProjectId id = ProjectId.get(projectId);
        switch (kind) {
            case OBJECT:
                return fromObjectFrame(projectId, propertyIri, getObjectFrame(id, propertyIri, userId));
            case DATA:
                return fromDataFrame(projectId, propertyIri, getDataFrame(id, propertyIri, userId));
            case ANNOTATION:
                return fromAnnotationFrame(projectId, propertyIri, getAnnotationFrame(id, propertyIri, userId));
            default:
                throw new IllegalArgumentException(
                        "Query parameter 'kind' must be OBJECT, DATA, or ANNOTATION");
        }
    }

    @Nonnull
    private PlainObjectPropertyFrame getObjectFrame(@Nonnull ProjectId projectId,
                                                    @Nonnull String propertyIri,
                                                    @Nonnull UserId userId) {
        GetObjectPropertyFrameResult result = actionDispatch.execute(
                new GetObjectPropertyFrameAction(projectId, DataFactory.getOWLObjectProperty(propertyIri)),
                userId
        );
        return result.getFrame().toPlainFrame();
    }

    @Nonnull
    private PlainDataPropertyFrame getDataFrame(@Nonnull ProjectId projectId,
                                                @Nonnull String propertyIri,
                                                @Nonnull UserId userId) {
        GetDataPropertyFrameResult result = actionDispatch.execute(
                new GetDataPropertyFrameAction(projectId, DataFactory.getOWLDataProperty(propertyIri)),
                userId
        );
        return result.getFrame().toPlainFrame();
    }

    @Nonnull
    private PlainAnnotationPropertyFrame getAnnotationFrame(@Nonnull ProjectId projectId,
                                                            @Nonnull String propertyIri,
                                                            @Nonnull UserId userId) {
        GetAnnotationPropertyFrameResult result = actionDispatch.execute(
                new GetAnnotationPropertyFrameAction(DataFactory.getOWLAnnotationProperty(propertyIri), projectId),
                userId
        );
        return result.getFrame().toPlainFrame();
    }

    @Nonnull
    private static OntologyPropertyData fromObjectFrame(@Nonnull String projectId,
                                                        @Nonnull String propertyIri,
                                                        @Nonnull PlainObjectPropertyFrame frame) {
        List<String> domains = classIris(frame.getDomains());
        List<String> ranges = classIris(frame.getRanges());
        List<String> characteristics = new ArrayList<>();
        for (ObjectPropertyCharacteristic characteristic : frame.getCharacteristics()) {
            characteristics.add(jsonName(characteristic));
        }
        List<String> inverses = new ArrayList<>();
        for (OWLObjectProperty inverse : frame.getInverseProperties()) {
            inverses.add(inverse.getIRI().toString());
        }
        return new OntologyPropertyData(
                projectId,
                propertyIri,
                OntologyPropertyKind.OBJECT,
                domains,
                ranges,
                characteristics,
                inverses,
                IndividualFramePropertyMapper.toPropertyMap(frame.getPropertyValues())
        );
    }

    @Nonnull
    private static OntologyPropertyData fromDataFrame(@Nonnull String projectId,
                                                      @Nonnull String propertyIri,
                                                      @Nonnull PlainDataPropertyFrame frame) {
        List<String> ranges = new ArrayList<>();
        for (OWLDatatype datatype : frame.getRanges()) {
            ranges.add(datatype.getIRI().toString());
        }
        List<String> characteristics = frame.isFunctional()
                ? Collections.singletonList("Functional")
                : Collections.emptyList();
        return new OntologyPropertyData(
                projectId,
                propertyIri,
                OntologyPropertyKind.DATA,
                classIris(frame.getDomains()),
                ranges,
                characteristics,
                Collections.emptyList(),
                IndividualFramePropertyMapper.toPropertyMap(frame.getPropertyValues())
        );
    }

    @Nonnull
    private static OntologyPropertyData fromAnnotationFrame(@Nonnull String projectId,
                                                            @Nonnull String propertyIri,
                                                            @Nonnull PlainAnnotationPropertyFrame frame) {
        List<String> domains = new ArrayList<>();
        for (IRI domain : frame.getDomains()) {
            domains.add(domain.toString());
        }
        List<String> ranges = new ArrayList<>();
        for (IRI range : frame.getRanges()) {
            ranges.add(range.toString());
        }
        return new OntologyPropertyData(
                projectId,
                propertyIri,
                OntologyPropertyKind.ANNOTATION,
                domains,
                ranges,
                Collections.emptyList(),
                Collections.emptyList(),
                IndividualFramePropertyMapper.toPropertyMap(frame.getPropertyValues())
        );
    }

    @Nullable
    private static OntologyPropertyKind kindOf(@Nonnull OWLEntity entity) {
        if (entity.isOWLObjectProperty()) {
            return OntologyPropertyKind.OBJECT;
        }
        if (entity.isOWLDataProperty()) {
            return OntologyPropertyKind.DATA;
        }
        if (entity.isOWLAnnotationProperty()) {
            return OntologyPropertyKind.ANNOTATION;
        }
        return null;
    }

    @Nonnull
    private static List<String> classIris(@Nonnull Iterable<OWLClass> classes) {
        List<String> iris = new ArrayList<>();
        for (OWLClass cls : classes) {
            iris.add(cls.getIRI().toString());
        }
        return iris;
    }

    @Nonnull
    private static String jsonName(@Nonnull ObjectPropertyCharacteristic characteristic) {
        switch (characteristic) {
            case FUNCTIONAL:
                return "Functional";
            case INVERSE_FUNCTIONAL:
                return "InverseFunctional";
            case TRANSITIVE:
                return "Transitive";
            case SYMMETRIC:
                return "Symmetric";
            case ASYMMETRIC:
                return "Asymmetric";
            case REFLEXIVE:
                return "Reflexive";
            case IRREFLEXIVE:
                return "Irreflexive";
            default:
                return characteristic.name();
        }
    }

    private static void requireNonBlank(@Nonnull String value, @Nonnull String name) {
        Objects.requireNonNull(value, name);
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
    }
}
