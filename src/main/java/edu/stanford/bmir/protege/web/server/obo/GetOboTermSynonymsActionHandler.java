package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.obo.GetOboTermSynonymsAction;
import edu.stanford.bmir.protege.web.shared.obo.GetOboTermSynonymsResult;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermSynonym;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Jun 2017
 */
public class GetOboTermSynonymsActionHandler extends AbstractHasProjectActionHandler<GetOboTermSynonymsAction, GetOboTermSynonymsResult> {

    @Nonnull
    private final TermSynonymsManager synonymsManager;

    @Inject
    public GetOboTermSynonymsActionHandler(@Nonnull AccessManager accessManager,
                                           @Nonnull TermSynonymsManager synonymsManager) {
        super(accessManager);
        this.synonymsManager = synonymsManager;
    }

    @Override
    public Class<GetOboTermSynonymsAction> getActionClass() {
        return GetOboTermSynonymsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Override
    public GetOboTermSynonymsResult execute(GetOboTermSynonymsAction action, ExecutionContext executionContext) {
        Collection<OBOTermSynonym> synonyms = synonymsManager.getSynonyms(action.getEntity());
        return new GetOboTermSynonymsResult(new ArrayList<>(synonyms));
    }
}
