package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.collection.CollectionId;
import edu.stanford.bmir.protege.web.shared.collection.CollectionItem;
import edu.stanford.bmir.protege.web.shared.form.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 07/04/16
 */
public class GetFormDescriptorActionHander extends AbstractProjectActionHandler<GetFormDescriptorAction, GetFormDescriptorResult> {

    @Inject
    public GetFormDescriptorActionHander(@Nonnull AccessManager accessManager) {
        super(accessManager);
    }

    @Nonnull
    public GetFormDescriptorResult execute(@Nonnull GetFormDescriptorAction action,
                                           @Nonnull ExecutionContext executionContext) {
        return getDummy(action.getCollectionId(),
                        action.getFormId(),
                        action.getElementId());
    }

    private GetFormDescriptorResult getDummy(CollectionId collectionId,
                                             FormId formId,
                                             CollectionItem elementId) {




//        try {
//
//
//            var collectionItemData = repository.find(collectionId, elementId);
//
//            var formData = collectionItemData.getFormValue().orElse(FormData.empty());
//
//            return new GetFormDescriptorResult(
//                    projectId,
//                    collectionId,
//                    elementId,
//                    formDescriptor.getId(),
//                    formDescriptor,
//                    formData);
//        } catch(IOException e) {
//            throw new RuntimeException(e);
//        }
        throw new RuntimeException();
    }

    @Nonnull
    @Override
    public Class<GetFormDescriptorAction> getActionClass() {
        return GetFormDescriptorAction.class;
    }
}
