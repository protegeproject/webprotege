package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.collection.CollectionElementDataRepository;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.collection.CollectionElementData;
import edu.stanford.bmir.protege.web.shared.collection.CollectionElementId;
import edu.stanford.bmir.protege.web.shared.collection.CollectionId;
import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.form.SetFormDataAction;
import edu.stanford.bmir.protege.web.shared.form.SetFormDataResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Jun 2017
 */
public class SetFormDataActionHandler extends AbstractHasProjectActionHandler<SetFormDataAction, SetFormDataResult> {

    @Nonnull
    private final CollectionElementDataRepository repository;

    @Inject
    public SetFormDataActionHandler(@Nonnull AccessManager accessManager,
                                    @Nonnull CollectionElementDataRepository repository) {
        super(accessManager);
        this.repository = repository;
    }

    @Override
    public Class<SetFormDataAction> getActionClass() {
        return SetFormDataAction.class;
    }

    @Override
    public SetFormDataResult execute(SetFormDataAction action, ExecutionContext executionContext) {
        FormData formData = action.getFormData();
        CollectionElementData data = null;
        if (formData.isEmpty()) {
            data = new CollectionElementData(
                    action.getCollectionId(),
                    action.getElementId());
        }
        else {
            data = new CollectionElementData(
                    action.getCollectionId(),
                    action.getElementId(),
                    formData);

        }
        repository.save(data);
        return new SetFormDataResult();
    }
}
