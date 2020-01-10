package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.collection.CollectionItemDataRepository;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.frame.ClassFrameTranslator;
import edu.stanford.bmir.protege.web.server.hierarchy.ClassHierarchyProvider;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.shared.collection.CollectionId;
import edu.stanford.bmir.protege.web.shared.collection.CollectionItem;
import edu.stanford.bmir.protege.web.shared.collection.CollectionItemData;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

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
