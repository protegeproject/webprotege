package edu.stanford.bmir.protege.web.server.msg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/02/2013
 */
public class StructuredOWLMessage {

    private String template;

    private List<Object> arguments = new ArrayList<Object>();

    public StructuredOWLMessage(String template, Object... arguments) {
        this(template, Arrays.asList(arguments));
    }

    public StructuredOWLMessage(String template, List<Object> arguments) {
        this.template = template;
        this.arguments = new ArrayList<Object>(arguments);
    }

    public String getTemplate() {
        return template;
    }

    public List<Object> getArguments() {
        return new ArrayList<Object>(arguments);
    }
}
