package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.form.SetFormDataAction;
import edu.stanford.bmir.protege.web.shared.form.SetFormDataResult;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataObject;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataPrimitive;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.EntitySerializer;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Jun 2017
 */
public class SetFormDataActionHandler extends AbstractHasProjectActionHandler<SetFormDataAction, SetFormDataResult> {

    @Nonnull
    private final FormDataRepository repository;

    @Nonnull
    private final ProjectId projectId;

    @Inject
    public SetFormDataActionHandler(@Nonnull AccessManager accessManager,
                                    @Nonnull FormDataRepository repository,
                                    @Nonnull ProjectId projectId) {
        super(accessManager);
        this.repository = repository;
        this.projectId = projectId;
    }

    @Override
    public Class<SetFormDataAction> getActionClass() {
        return SetFormDataAction.class;
    }

    @Override
    public SetFormDataResult execute(SetFormDataAction action, ExecutionContext executionContext) {
        repository.store(projectId, action.getFormId(), action.getEntity(), action.getFormData());
        System.out.println("Setting form data: " + action.getFormData());

//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            SimpleModule module = new SimpleModule();
//            module.addSerializer(new EntitySerializer());
//            module.addSerializer(new IRISerializer());
//            module.addSerializer(new LiteralSerializer());
//            module.addSerializer(new FormElementIdSerializer());
//            module.addSerializer(new FormDataSerializer());
//            module.addSerializer(new FormDataObjectSerializer());
//            mapper.registerModule(module);
//            mapper.setDefaultPrettyPrinter(new DefaultPrettyPrinter());
//            String s = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
//                    action.getFormData()
//            );
//            System.out.println(s);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return new SetFormDataResult();
    }

    public static void main(String[] args) {
        Map<FormElementId, FormDataValue> map = new HashMap<>();
        map.put(FormElementId.get("FirstName"), FormDataPrimitive.get("Matthew"));
        map.put(FormElementId.get("LastName"), FormDataPrimitive.get("Horridge"));
        map.put(FormElementId.get("FullName"), FormDataPrimitive.get("Matthew Horridge", "en"));
        Map<String, FormDataValue> addressMap = new HashMap<>();
        addressMap.put("street", FormDataPrimitive.get("1788 Oak Creek Drive"));
        addressMap.put("city", FormDataPrimitive.get("Palo Alto"));
        map.put(FormElementId.get("Address"), new FormDataObject(addressMap));
        FormData formData = new FormData(map);
        try {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addSerializer(new EntitySerializer());
            module.addSerializer(new IRISerializer());
            module.addSerializer(new LiteralSerializer());
            module.addSerializer(new FormElementIdSerializer());
            module.addSerializer(new FormDataSerializer());
            module.addSerializer(new FormDataObjectSerializer());
            mapper.registerModule(module);
            mapper.setDefaultPrettyPrinter(new DefaultPrettyPrinter());
            String s = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                    formData
            );
            System.out.println(s);
//            List<Object> d = mapper.readerFor(FormElementDescriptor.class).readValues(s).readAll();
//            System.out.println(d);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
