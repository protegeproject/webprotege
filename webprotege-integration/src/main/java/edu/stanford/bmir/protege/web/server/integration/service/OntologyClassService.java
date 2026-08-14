package edu.stanford.bmir.protege.web.server.integration.service;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.integration.dispatch.ActionDispatch;
import edu.stanford.bmir.protege.web.server.integration.dto.OntologyClassData;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetClassFrameAction;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.frame.GetClassFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.PlainClassFrame;
import edu.stanford.bmir.protege.web.shared.match.GetMatchingEntitiesAction;
import edu.stanford.bmir.protege.web.shared.match.GetMatchingEntitiesResult;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityTypeIsOneOfCriteria;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Reads OWL class frames from the live project ontology via {@link GetClassFrameAction}.
 */
public class OntologyClassService {

    private static final int DEFAULT_LIST_PAGE_SIZE = 100;

    @Nonnull
    private final ActionDispatch actionDispatch;

    @Inject
    public OntologyClassService(@Nonnull ActionDispatch actionDispatch) {
        this.actionDispatch = Objects.requireNonNull(actionDispatch, "actionDispatch");
    }

    @Nonnull
    public OntologyClassData getClassData(@Nonnull String projectId,
                                          @Nonnull String classIri,
                                          @Nonnull UserId userId) {
        requireNonBlank(projectId, "projectId");
        requireNonBlank(classIri, "classIri");
        Objects.requireNonNull(userId, "userId");
        return toClassData(projectId, classIri, getPlainFrame(projectId, classIri, userId));
    }

    /**
     * Lists class frames for every OWL class in the project signature.
     *
     * <p>Fetches matches page-by-page via {@link GetMatchingEntitiesAction}, then loads each
     * class frame. A single hard-coded page would under-report while REST {@code count}
     * still looked like the full set.</p>
     */
    @Nonnull
    public List<OntologyClassData> listClassData(@Nonnull String projectId,
                                                 @Nonnull UserId userId) {
        requireNonBlank(projectId, "projectId");
        Objects.requireNonNull(userId, "userId");

        ProjectId id = ProjectId.get(projectId);
        List<OntologyClassData> result = new ArrayList<>();
        int pageNumber = 1;
        while (true) {
            GetMatchingEntitiesAction listAction = GetMatchingEntitiesAction.getMatchingEntities(
                    id,
                    EntityTypeIsOneOfCriteria.get(ImmutableSet.<EntityType<?>>of(EntityType.CLASS)),
                    PageRequest.requestPageWithSize(pageNumber, DEFAULT_LIST_PAGE_SIZE)
            );
            GetMatchingEntitiesResult listResult = actionDispatch.execute(listAction, userId);
            Page<EntityNode> page = listResult.getEntities();
            for (EntityNode node : page.getPageElements()) {
                if (!node.getEntity().isOWLClass()) {
                    continue;
                }
                String iri = node.getEntity().getIRI().toString();
                result.add(toClassData(projectId, iri, getPlainFrame(projectId, iri, userId)));
            }
            if (pageNumber >= page.getPageCount() || page.getPageElements().isEmpty()) {
                break;
            }
            pageNumber++;
        }
        return result;
    }

    @Nonnull
    private PlainClassFrame getPlainFrame(@Nonnull String projectId,
                                          @Nonnull String classIri,
                                          @Nonnull UserId userId) {
        GetClassFrameResult result = actionDispatch.execute(
                new GetClassFrameAction(DataFactory.getOWLClass(classIri), ProjectId.get(projectId)),
                userId
        );
        return result.getFrame().toPlainFrame();
    }

    @Nonnull
    private static OntologyClassData toClassData(@Nonnull String projectId,
                                                 @Nonnull String classIri,
                                                 @Nonnull PlainClassFrame frame) {
        List<String> parents = new ArrayList<>();
        for (OWLClass parent : frame.getParents()) {
            parents.add(parent.getIRI().toString());
        }
        Map<String, String> properties = IndividualFramePropertyMapper.toPropertyMap(frame.getPropertyValues());
        return new OntologyClassData(
                projectId,
                classIri,
                parents,
                Collections.unmodifiableMap(properties)
        );
    }

    private static void requireNonBlank(@Nonnull String value, @Nonnull String name) {
        Objects.requireNonNull(value, name);
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
    }
}
