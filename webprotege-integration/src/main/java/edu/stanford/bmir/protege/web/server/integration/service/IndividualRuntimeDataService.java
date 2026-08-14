package edu.stanford.bmir.protege.web.server.integration.service;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.integration.dispatch.ActionDispatch;
import edu.stanford.bmir.protege.web.server.integration.dto.IndividualRuntimeData;
import edu.stanford.bmir.protege.web.server.integration.dto.IndividualRuntimeDataRequest;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetNamedIndividualFrameAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetNamedIndividualFrameResult;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.UpdateNamedIndividualFrameAction;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.frame.PlainNamedIndividualFrame;
import edu.stanford.bmir.protege.web.shared.frame.PlainPropertyValue;
import edu.stanford.bmir.protege.web.shared.individuals.GetIndividualsAction;
import edu.stanford.bmir.protege.web.shared.individuals.GetIndividualsResult;
import edu.stanford.bmir.protege.web.shared.individuals.InstanceRetrievalMode;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Reads and writes named-individual property values from the live project ontology
 * via {@link GetNamedIndividualFrameAction} / {@link UpdateNamedIndividualFrameAction}.
 */
public class IndividualRuntimeDataService {

    private static final int DEFAULT_LIST_PAGE_SIZE = 100;

    @Nonnull
    private final ActionDispatch actionDispatch;

    @Inject
    public IndividualRuntimeDataService(@Nonnull ActionDispatch actionDispatch) {
        this.actionDispatch = Objects.requireNonNull(actionDispatch, "actionDispatch");
    }

    @Nonnull
    public IndividualRuntimeData getRuntimeData(@Nonnull String projectId,
                                                @Nonnull String individualIri,
                                                @Nonnull UserId userId) {
        requireNonBlank(projectId, "projectId");
        requireNonBlank(individualIri, "individualIri");
        Objects.requireNonNull(userId, "userId");

        PlainNamedIndividualFrame frame = getPlainFrame(projectId, individualIri, userId);
        return toRuntimeData(projectId, individualIri, frame, null);
    }

    /**
     * Lists runtime data for every named individual under owl:Thing.
     *
     * <p>Fetches individuals page-by-page via {@link GetIndividualsAction}, advancing until
     * {@code pageNumber} reaches {@link edu.stanford.bmir.protege.web.shared.pagination.Page#getPageCount()}.
     * A single hard-coded page (e.g. size 100) would under-report while REST {@code count}
     * still looked like the full set.</p>
     */
    @Nonnull
    public List<IndividualRuntimeData> listRuntimeData(@Nonnull String projectId,
                                                       @Nonnull UserId userId) {
        requireNonBlank(projectId, "projectId");
        Objects.requireNonNull(userId, "userId");

        ProjectId id = ProjectId.get(projectId);
        List<IndividualRuntimeData> result = new ArrayList<>();
        int pageNumber = 1;
        // Loop until we have walked every page reported by GetIndividualsResult.
        while (true) {
            GetIndividualsAction listAction = new GetIndividualsAction(
                    id,
                    Optional.of(DataFactory.getOWLThing()),
                    "",
                    InstanceRetrievalMode.ALL_INSTANCES,
                    Optional.of(PageRequest.requestPageWithSize(pageNumber, DEFAULT_LIST_PAGE_SIZE))
            );
            GetIndividualsResult listResult = actionDispatch.execute(listAction, userId);
            for (EntityNode node : listResult.getIndividuals()) {
                if (!node.getEntity().isOWLNamedIndividual()) {
                    continue;
                }
                String iri = node.getEntity().getIRI().toString();
                PlainNamedIndividualFrame frame = getPlainFrame(projectId, iri, userId);
                result.add(toRuntimeData(projectId, iri, frame, null));
            }
            int pageCount = listResult.getPaginatedResult().getPageCount();
            // Stop when the last page is reached, or the page is empty (defensive).
            if (pageNumber >= pageCount || listResult.getIndividuals().isEmpty()) {
                break;
            }
            pageNumber++;
        }
        return result;
    }

    @Nonnull
    public IndividualRuntimeData putRuntimeData(@Nonnull String projectId,
                                               @Nonnull String individualIri,
                                               @Nonnull IndividualRuntimeDataRequest request,
                                               @Nonnull UserId userId) {
        requireNonBlank(projectId, "projectId");
        requireNonBlank(individualIri, "individualIri");
        Objects.requireNonNull(request, "request");
        Objects.requireNonNull(userId, "userId");

        PlainNamedIndividualFrame from = getPlainFrame(projectId, individualIri, userId);
        ImmutableSet<PlainPropertyValue> propertyValues =
                IndividualFramePropertyMapper.toPropertyValues(from, request.getProperties());
        PlainNamedIndividualFrame to = PlainNamedIndividualFrame.get(
                from.getSubject(),
                from.getParents(),
                from.getSameIndividuals(),
                propertyValues
        );
        applyUpdate(projectId, from, to, userId);
        return toRuntimeData(projectId, individualIri, to, userId.getUserName());
    }

    @Nonnull
    public IndividualRuntimeData patchRuntimeData(@Nonnull String projectId,
                                                 @Nonnull String individualIri,
                                                 @Nonnull IndividualRuntimeDataRequest request,
                                                 @Nonnull UserId userId) {
        requireNonBlank(projectId, "projectId");
        requireNonBlank(individualIri, "individualIri");
        Objects.requireNonNull(request, "request");
        Objects.requireNonNull(userId, "userId");

        PlainNamedIndividualFrame from = getPlainFrame(projectId, individualIri, userId);
        ImmutableSet<PlainPropertyValue> propertyValues =
                IndividualFramePropertyMapper.mergePropertyValues(from, request.getProperties());
        PlainNamedIndividualFrame to = PlainNamedIndividualFrame.get(
                from.getSubject(),
                from.getParents(),
                from.getSameIndividuals(),
                propertyValues
        );
        applyUpdate(projectId, from, to, userId);
        return toRuntimeData(projectId, individualIri, to, userId.getUserName());
    }

    /**
     * Clears asserted property values on the individual while retaining types and sameAs.
     *
     * @return {@code true} if the update was submitted
     */
    public boolean deleteRuntimeData(@Nonnull String projectId,
                                     @Nonnull String individualIri,
                                     @Nonnull UserId userId) {
        requireNonBlank(projectId, "projectId");
        requireNonBlank(individualIri, "individualIri");
        Objects.requireNonNull(userId, "userId");

        PlainNamedIndividualFrame from = getPlainFrame(projectId, individualIri, userId);
        if (IndividualFramePropertyMapper.toPropertyMap(from).isEmpty()) {
            return false;
        }
        PlainNamedIndividualFrame to = PlainNamedIndividualFrame.get(
                from.getSubject(),
                from.getParents(),
                from.getSameIndividuals(),
                ImmutableSet.of()
        );
        applyUpdate(projectId, from, to, userId);
        return true;
    }

    @Nonnull
    private PlainNamedIndividualFrame getPlainFrame(@Nonnull String projectId,
                                                    @Nonnull String individualIri,
                                                    @Nonnull UserId userId) {
        ProjectId id = ProjectId.get(projectId);
        OWLNamedIndividual individual = DataFactory.getOWLNamedIndividual(individualIri);
        GetNamedIndividualFrameResult result = actionDispatch.execute(
                new GetNamedIndividualFrameAction(id, individual),
                userId
        );
        return result.getFrame().toPlainFrame();
    }

    private void applyUpdate(@Nonnull String projectId,
                             @Nonnull PlainNamedIndividualFrame from,
                             @Nonnull PlainNamedIndividualFrame to,
                             @Nonnull UserId userId) {
        actionDispatch.execute(
                new UpdateNamedIndividualFrameAction(ProjectId.get(projectId), from, to),
                userId
        );
    }

    @Nonnull
    private static IndividualRuntimeData toRuntimeData(@Nonnull String projectId,
                                                       @Nonnull String individualIri,
                                                       @Nonnull PlainNamedIndividualFrame frame,
                                                       String updatedBy) {
        Map<String, String> properties = IndividualFramePropertyMapper.toPropertyMap(frame);
        long updatedAt = updatedBy == null ? 0L : System.currentTimeMillis();
        List<String> types = new ArrayList<>();
        for (OWLClass parent : frame.getParents()) {
            types.add(parent.getIRI().toString());
        }
        return new IndividualRuntimeData(
                projectId,
                individualIri,
                Collections.unmodifiableMap(properties),
                updatedAt,
                updatedBy,
                types
        );
    }

    private static void requireNonBlank(@Nonnull String value, @Nonnull String name) {
        Objects.requireNonNull(value, name);
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
    }
}
