package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.hierarchy.AssertedClassHierarchyProvider;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataPrimitive;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cern.clhep.Units.s;
import static edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptor.choice;
import static edu.stanford.bmir.protege.web.shared.form.field.ChoiceFieldType.COMBO_BOX;
import static java.util.Arrays.asList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 07/04/16
 */
public class GetFormDescriptorActionHander extends AbstractHasProjectActionHandler<GetFormDescriptorAction, GetFormDescriptorResult> {

    private final ProjectId projectId;

    private final AssertedClassHierarchyProvider classHierarchyProvider;

    private final OWLDataFactory dataFactory;

    private final FormDataRepository formDataRepository;

    @Inject
    public GetFormDescriptorActionHander(@Nonnull AccessManager accessManager,
                                         ProjectId projectId,
                                         AssertedClassHierarchyProvider classHierarchyProvider,
                                         OWLDataFactory dataFactory, FormDataRepository formDataRepository) {
        super(accessManager);
        this.projectId = projectId;
        this.classHierarchyProvider = classHierarchyProvider;
        this.dataFactory = dataFactory;
        this.formDataRepository = formDataRepository;
    }


    @Override
    public Class<GetFormDescriptorAction> getActionClass() {
        return GetFormDescriptorAction.class;
    }

    public GetFormDescriptorResult execute(GetFormDescriptorAction action, ExecutionContext executionContext) {
        return getDummy(action.getFormId(),
                        action.getSubject());
    }


    private GetFormDescriptorResult getDummy(FormId formId, OWLEntity entity) {
        try {
            if (!entity.isOWLClass()) {
                return new GetFormDescriptorResult(projectId, entity, FormDescriptor.empty(), FormData.empty());
            }

            URL url = GetFormDescriptorActionHander.class.getResource("/amino-acid-form.json");
            System.out.println(url);
            InputStream is = GetFormDescriptorActionHander.class.getResourceAsStream("/amino-acid-form.json");

            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(FormDataValue.class, new FormDataValueDeserializer(dataFactory));
            module.addSerializer(new EntitySerializer());
            module.addSerializer(new IRISerializer());
            module.addSerializer(new LiteralSerializer());
            module.addSerializer(new FormElementIdSerializer());
            module.addSerializer(new FormDataSerializer());
            module.addSerializer(new FormDataObjectSerializer());
            mapper.registerModule(module);
            mapper.setDefaultPrettyPrinter(new DefaultPrettyPrinter());
            FormDescriptor d = mapper.readerFor(FormDescriptor.class).readValue(new BufferedInputStream(is));

            is.close();

            FormData formData = formDataRepository.get(projectId, CollectionId.get("Dummy Data"), formId,  entity);
            return new GetFormDescriptorResult(
                    projectId,
                    entity,
                    d,
                    formData
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
