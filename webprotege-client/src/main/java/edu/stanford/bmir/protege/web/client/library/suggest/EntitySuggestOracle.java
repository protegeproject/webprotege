package edu.stanford.bmir.protege.web.client.library.suggest;

import com.google.common.collect.ImmutableList;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.SuggestOracle;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.entity.EntityNodeHtmlRenderer;
import edu.stanford.bmir.protege.web.client.primitive.EntitySuggestOracleSuggestLimit;
import edu.stanford.bmir.protege.web.client.search.SearchResultMatchHighlighter;
import edu.stanford.bmir.protege.web.shared.entity.EntityLookupRequest;
import edu.stanford.bmir.protege.web.shared.entity.EntityLookupResult;
import edu.stanford.bmir.protege.web.shared.entity.LookupEntitiesAction;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.SearchResultMatchPosition;
import edu.stanford.bmir.protege.web.shared.search.SearchType;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2012
 */
public class EntitySuggestOracle extends SuggestOracle {

    public static final int DEFAULT_SUGGEST_LIMIT = 30;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final EntityNodeHtmlRenderer renderer;

    private int suggestLimit;

    final Set<EntityType<?>> entityTypes = new HashSet<>();

    @Nullable
    private CompositeRootCriteria entityMatchCriteria = null;

    @Inject
    public EntitySuggestOracle(@Nonnull ProjectId projectId,
                               @EntitySuggestOracleSuggestLimit int suggestLimit,
                               @Nonnull DispatchServiceManager dispatchServiceManager,
                               @Nonnull EntityNodeHtmlRenderer renderer) {
        this.projectId = projectId;
        this.suggestLimit = suggestLimit;
        this.dispatchServiceManager = dispatchServiceManager;
        this.renderer = renderer;
        renderer.setRenderTags(false);
    }

    public void setCriteria(CompositeRootCriteria entityMatchCriteria) {
        this.entityMatchCriteria = entityMatchCriteria;
    }

    public void setEntityTypes(Set<EntityType<?>> entityTypes) {
        this.entityTypes.clear();
        this.entityTypes.addAll(entityTypes);
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public void requestSuggestions(final Request request, final Callback callback) {
        if(entityTypes.isEmpty()) {
            callback.onSuggestionsReady(request, new Response(Collections.emptyList()));
            return;
        }
        dispatchServiceManager.execute(new LookupEntitiesAction(projectId, new EntityLookupRequest(request.getQuery(), SearchType.getDefault(), suggestLimit, entityTypes, entityMatchCriteria)), result -> {
            List<EntitySuggestion> suggestions = new ArrayList<>();
            for (final EntityLookupResult entity : result.getEntityLookupResults()) {
                ImmutableList<SearchResultMatchPosition> positions = entity.getMatchResult().getPositions();
                renderer.setPrimaryDisplayLanguage(entity.getLanguage());
                if(!positions.isEmpty()) {
                    SearchResultMatchPosition firstPosition = positions.get(0);
                    renderer.setHighlight(firstPosition.getStart(),
                                          firstPosition.getEnd());
                }
                String rendering = renderer.getHtmlRendering(entity.getEntityNode());
                suggestions.add(new EntitySuggestion(entity.getOWLEntityData(),
                                                     rendering));
            }
            callback.onSuggestionsReady(request, new Response(suggestions));
        });
    }

    @Override
    public boolean isDisplayStringHTML() {
        return true;
    }
}
