package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.collection.CollectionItemDataRepository;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.frame.translator.ClassFrameTranslator;
import edu.stanford.bmir.protege.web.server.hierarchy.ClassHierarchyProvider;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.shared.collection.CollectionId;
import edu.stanford.bmir.protege.web.shared.collection.CollectionItem;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLDataFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 07/04/16
 */
public class GetFormDescriptorActionHander extends AbstractProjectActionHandler<GetFormDescriptorAction, GetFormDescriptorResult> {

    private final ProjectId projectId;

    private final OWLDataFactory dataFactory;

    private final CollectionItemDataRepository repository;

    private final ClassFrameTranslator translator;

    @Inject
    public GetFormDescriptorActionHander(@Nonnull AccessManager accessManager,
                                         ProjectId projectId,
                                         ClassHierarchyProvider classHierarchyProvider,
                                         OWLDataFactory dataFactory,
                                         CollectionItemDataRepository repository,
                                         ProjectOntologiesIndex projectOntologiesIndex,
                                         AnnotationAssertionAxiomsBySubjectIndex index,
                                         ClassFrameTranslator translator) {
        super(accessManager);
        this.projectId = projectId;
        this.dataFactory = dataFactory;
        this.repository = repository;
        this.translator = translator;
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
