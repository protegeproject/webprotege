package edu.stanford.bmir.protege.web.client.rpc.data;

import org.semanticweb.owlapi.model.IRI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/11/2012
 * <p>
 * Describes a user interface level change.  The descriptor for a change has an Id (IRI) that determines exactly what
 * the change is and a message template which can be used, in combination with arguments, to format a human readable
 * message.
 */
public class ChangeDescriptor implements Serializable {

    private final IRI id;

    private final String messageTemplate;

    private final ArrayList<Object> arguments;

    public ChangeDescriptor(IRI id, String messageTemplate, Serializable ... arguments) {
        this.id = id;
        this.messageTemplate = messageTemplate;
        this.arguments = new ArrayList<Object>(Arrays.asList(arguments));
    }

    public IRI getId() {
        return id;
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }

    public List<Object> getArguments() {
        return new ArrayList<Object>(arguments);
    }
}
