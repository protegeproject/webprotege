package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.collection.CollectionElementDataRepositoryImpl;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.hierarchy.ClassHierarchyProvider;
import edu.stanford.bmir.protege.web.shared.collection.CollectionElementData;
import edu.stanford.bmir.protege.web.shared.collection.CollectionElementId;
import edu.stanford.bmir.protege.web.shared.collection.CollectionId;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLDataFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 07/04/16
 */
public class GetFormDescriptorActionHander extends AbstractHasProjectActionHandler<GetFormDescriptorAction, GetFormDescriptorResult> {

    private final ProjectId projectId;

    private final ClassHierarchyProvider classHierarchyProvider;

    private final OWLDataFactory dataFactory;

    private final CollectionElementDataRepositoryImpl repository;

    @Inject
    public GetFormDescriptorActionHander(@Nonnull AccessManager accessManager,
                                         ProjectId projectId,
                                         ClassHierarchyProvider classHierarchyProvider,
                                         OWLDataFactory dataFactory,
                                         CollectionElementDataRepositoryImpl repository) {
        super(accessManager);
        this.projectId = projectId;
        this.classHierarchyProvider = classHierarchyProvider;
        this.dataFactory = dataFactory;
        this.repository = repository;
    }


    @Override
    public Class<GetFormDescriptorAction> getActionClass() {
        return GetFormDescriptorAction.class;
    }

    public GetFormDescriptorResult execute(GetFormDescriptorAction action, ExecutionContext executionContext) {
        return getDummy(action.getCollectionId(),
                        action.getFormId(),
                        action.getElementId());
    }


    private GetFormDescriptorResult getDummy(CollectionId collectionId,
                                             FormId formId,
                                             CollectionElementId elementId) {
        try {
            URL url = GetFormDescriptorActionHander.class.getResource("/amino-acid-form.json");
            System.out.println(url);
            InputStream is = GetFormDescriptorActionHander.class.getResourceAsStream("/amino-acid-form.json");

            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(FormDataValue.class, new FormDataValueDeserializer(dataFactory));
            mapper.registerModule(module);
            mapper.setDefaultPrettyPrinter(new DefaultPrettyPrinter());
            FormDescriptor d = mapper.readerFor(FormDescriptor.class).readValue(new BufferedInputStream(is));

            is.close();

            CollectionElementData formData = repository.find(collectionId, elementId);
            return new GetFormDescriptorResult(
                    projectId,
                    collectionId,
                    elementId,
                    formId,
                    d,
                    formData.getFormData().orElse(FormData.empty())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
