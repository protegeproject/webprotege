package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.obo.SetOboTermSynonymsAction;
import edu.stanford.bmir.protege.web.shared.obo.SetOboTermSynonymsResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Jun 2017
 */
public class SetOboTermSynonymsActionHandler extends AbstractProjectActionHandler<SetOboTermSynonymsAction, SetOboTermSynonymsResult> {

    @Nonnull
    private final TermSynonymsManager synonymsManager;

    @Inject
    public SetOboTermSynonymsActionHandler(@Nonnull AccessManager accessManager,
                                           @Nonnull TermSynonymsManager synonymsManager) {
        super(accessManager);
        this.synonymsManager = synonymsManager;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(SetOboTermSynonymsAction action) {
        return BuiltInAction.EDIT_ONTOLOGY;
    }

    @Nonnull
    @Override
    public Class<SetOboTermSynonymsAction> getActionClass() {
        return SetOboTermSynonymsAction.class;
    }

    @Nonnull
    @Override
    public SetOboTermSynonymsResult execute(@Nonnull SetOboTermSynonymsAction action, @Nonnull ExecutionContext executionContext) {
        synonymsManager.setSynonyms(executionContext.getUserId(),
                                    action.getEntity(),
                                    action.getSynonyms());
        return new SetOboTermSynonymsResult();
    }
}
