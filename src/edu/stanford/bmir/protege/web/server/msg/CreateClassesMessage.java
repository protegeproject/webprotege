package edu.stanford.bmir.protege.web.server.msg;

import org.semanticweb.owlapi.model.OWLClass;

import java.util.Arrays;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/02/2013
 */

public class CreateClassesMessage {

    private String subclass;

    private OWLClass superClass;

    private String messageTemplate = "Created {0} as a subclass of {1}";

    @DefaultMsg("Created {0} as a subclass of {1}")
    public CreateClassesMessage(String subclass, OWLClass superClass) {
        this.subclass = subclass;
        this.superClass = superClass;
    }

    public List<Object> getArguments() {
        return Arrays.<Object>asList(subclass, superClass);
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }
}
