package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.form.SetFormDataAction;
import edu.stanford.bmir.protege.web.shared.form.SetFormDataResult;
import edu.stanford.bmir.protege.web.shared.form.field.EntitySerializer;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Jun 2017
 */
public class SetFormDataActionHandler extends AbstractHasProjectActionHandler<SetFormDataAction, SetFormDataResult> {

    private final FormDataRepository repository;

    @Inject
    public SetFormDataActionHandler(@Nonnull AccessManager accessManager,
                                    FormDataRepository repository) {
        super(accessManager);
        this.repository = repository;
    }

    @Override
    public Class<SetFormDataAction> getActionClass() {
        return SetFormDataAction.class;
    }

    @Override
    public SetFormDataResult execute(SetFormDataAction action, ExecutionContext executionContext) {
        repository.store(action.getEntity(), action.getFormData());
        System.out.println("Setting form data: " + action.getFormData());

//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            SimpleModule module = new SimpleModule();
//            module.addSerializer(OWLEntity.class, new EntitySerializer());
//            mapper.registerModule(module);
//            mapper.setDefaultPrettyPrinter(new DefaultPrettyPrinter());
//            String s = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
//                    action.getFormData().getData()
//            );
//            System.out.println(s);
////            List<Object> d = mapper.readerFor(FormElementDescriptor.class).readValues(s).readAll();
////            System.out.println(d);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return new SetFormDataResult();
    }
}
