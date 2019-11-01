package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.collection.CollectionItemDataRepository;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import edu.stanford.bmir.protege.web.shared.form.SetFormDataAction;
import edu.stanford.bmir.protege.web.shared.form.SetFormDataResult;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.OWLEntityProvider;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Jun 2017
 */
public class SetFormDataActionHandler extends AbstractProjectActionHandler<SetFormDataAction, SetFormDataResult> {

    @Nonnull
    private final CollectionItemDataRepository repository;

    @Nonnull
    private final OWLEntityProvider entityProvider;

    @Nonnull
    private final RenderingManager renderingManager;

    @Inject
    public SetFormDataActionHandler(@Nonnull AccessManager accessManager,
                                    @Nonnull CollectionItemDataRepository repository,
                                    @Nonnull OWLEntityProvider entityProvider,
                                    @Nonnull RenderingManager renderingManager) {
        super(accessManager);
        this.repository = repository;
        this.entityProvider = entityProvider;
        this.renderingManager = renderingManager;
    }

    @Nonnull
    @Override
    public Class<SetFormDataAction> getActionClass() {
        return SetFormDataAction.class;
    }

    @Nonnull
    @Override
    public SetFormDataResult execute(@Nonnull SetFormDataAction action, @Nonnull ExecutionContext executionContext) {


//        CollectionItemData data = null;
//        if (formData.isEmpty()) {
//            data = new CollectionItemData(
//                    action.getCollectionId(),
//                    action.getElementId());
//        }
//        else {
//            data = new CollectionItemData(
//                    action.getCollectionId(),
//                    action.getElementId(),
//                    formData);
//
//        }
//        repository.save(data);
        return new SetFormDataResult();
    }

}
