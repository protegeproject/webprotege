package edu.stanford.bmir.protege.web.shared.msg;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
public class StructuredMessage {

    private String messageTemplate;

    private List<Object> arguments;

    public StructuredMessage(String plainMessage) {
        this.messageTemplate = plainMessage;
    }

    public StructuredMessage(String messageTemplate, List<Object> arguments) {
        this.messageTemplate = messageTemplate;
        this.arguments = new ArrayList<Object>(arguments);
        checkArgumentCount();
    }

    private void checkArgumentCount() {

    }


    public int getArgumentCount() {
        return arguments.size();
    }

    public String formatMessage() {
        return messageTemplate;
    }
}
